package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.domain.CoreTaxpayerBranch;
import com.niwe.erp.core.repository.CoreTaxpayerBranchRepository;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.inventory.domain.EStockOperation;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.domain.StockMovement;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.domain.WarehouseStock;
import com.niwe.erp.inventory.repository.StockMovementRepository;
import com.niwe.erp.inventory.repository.WarehouseRepository;
import com.niwe.erp.inventory.repository.WarehouseStockRepository;
import com.niwe.erp.inventory.web.dto.ProductStockAgingDto;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.dto.WarehouseStockDetailDto;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.repository.ShelfRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class WarehouseStockService {

	private final CoreTaxpayerBranchRepository coreTaxpayerBranchRepository;
	private final SequenceNumberService sequenceNumberService;
	private final WarehouseRepository warehouseRepository;
	private final WarehouseStockRepository warehouseStockRepository;
	private final ShelfRepository shelfRepository;
	private final StockMovementRepository stockMovementRepository;
	private final CoreItemService coreItemService;

	public void initiateInventory(CoreTaxpayer taxpayer) {
		try {

			List<CoreTaxpayerBranch> branches = coreTaxpayerBranchRepository.findByTaxpayerId(taxpayer.getId());

			if (!branches.isEmpty() && branches.size() > 1) {
				return;
			}

			Warehouse warehouse = warehouseRepository.save(
					Warehouse.builder().warehouseName("MAIN").internalCode(sequenceNumberService.getNextWarehouseCode())
							.branch(branches.get(0)).isMain(true).build());
			shelfRepository.save(Shelf.builder().internalCode(sequenceNumberService.getNextShelfCode())
					.warehouse(warehouse).name("MAIN POS").description("MAIN POS").build());

			coreItemService.initItems();

		} catch (Exception e) {
			log.error("InitiateInventory:{}", e);
		}

	}

	public void updateProductQuantity(Warehouse warehouse, CoreItem product, BigDecimal quantityChange,
			MovementType movementType, String reference, EStockOperation eStockOperation) {
		try {

			log.debug("-------updateProductQuantity");
			WarehouseStock stock = warehouseStockRepository.findByWarehouseAndItem(warehouse, product).orElseGet(() -> {
				WarehouseStock newStock = new WarehouseStock();
				newStock.setWarehouse(warehouse);
				newStock.setItem(product);
				newStock.setQuantity(new BigDecimal("0.00"));
				return newStock;
			});
			BigDecimal previousQuantiry = stock.getQuantity();
			BigDecimal newQuantity = new BigDecimal("0.00");
			switch (eStockOperation) {
			case IN: {

				newQuantity = previousQuantiry.add(quantityChange);
				break;
			}
			case OUT: {
				newQuantity = previousQuantiry.subtract(quantityChange);
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + eStockOperation);
			}

			switch (movementType) {
			case PURCHASE: {
				stock.setReceivedDate(Instant.now());
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + movementType);

			}
			/*
			 * if (newQuantity.compareTo(new BigDecimal("0.00")) < 0) { throw new
			 * IllegalStateException( "Not enough stock for product " +
			 * product.getItemName() + " in warehouse " + warehouse.getWarehouseName()); }
			 */
			stock.setQuantity(newQuantity);
			warehouseStockRepository.save(stock);
			// Record movement
			StockMovement movement = StockMovement.builder().warehouse(warehouse).item(product)
					.movementType(movementType).reference(reference).previousQuantity(previousQuantiry)
					.currentQuantity(newQuantity).quantity(quantityChange).stockOperation(eStockOperation)
					.movementDate(Instant.now())

					.build();

			stockMovementRepository.save(movement);
		} catch (Exception e) {
			log.error("-------updateProductQuantity:{}", e);
		}
	}

	public void transferProduct(Warehouse fromWarehouse, Warehouse toWarehouse, CoreItem product, BigDecimal quantity) {
		updateProductQuantity(fromWarehouse, product, quantity, MovementType.ADJUSTMENT,
				"TRANSFER-OUT-" + fromWarehouse.getWarehouseName(), EStockOperation.OUT);
		updateProductQuantity(toWarehouse, product, quantity, MovementType.ADJUSTMENT,
				"TRANSFER-IN-" + toWarehouse.getWarehouseName(), EStockOperation.IN);
	}

	public List<WarehouseStock> findAll() {
		return warehouseStockRepository.findAll();
	}

	public List<WarehouseStock> getStockByProduct(UUID itemId) {
		return warehouseStockRepository.findByItemId(itemId);
	}

	public List<ProductStockSummaryDto> getStockSummary() {
		List<WarehouseStock> stocks = warehouseStockRepository.findAll();

		Map<CoreItem, BigDecimal> totalByProduct = stocks.stream()
				.collect(Collectors.groupingBy(WarehouseStock::getItem,
						Collectors.reducing(BigDecimal.ZERO, s -> s.getQuantity(), // getter returning BigDecimal
								BigDecimal::add)));

		return totalByProduct.entrySet().stream().map(e -> new ProductStockSummaryDto(e.getKey().getId(),
				e.getKey().getItemCode(), e.getKey().getItemName(), e.getValue())).toList();
	}

	public List<WarehouseStockDetailDto> getWarehouseStockDetails(String productId) {
		List<WarehouseStock> stocks = warehouseStockRepository.findByItemId(UUID.fromString(productId));
		return stocks.stream().map(s -> new WarehouseStockDetailDto(s.getWarehouse().getWarehouseName(),
				s.getQuantity(), s.getModifiedAt())).toList();
	}

	public List<ProductStockAgingDto> getStockSummaryWithAging() {
		List<WarehouseStock> stocks = warehouseStockRepository.findAll();

		// Group by product
		Map<CoreItem, List<WarehouseStock>> grouped = stocks.stream()
				.collect(Collectors.groupingBy(WarehouseStock::getItem));

		return grouped.entrySet().stream().map(entry -> {
			CoreItem item = entry.getKey();
			List<WarehouseStock> stockList = entry.getValue();

			// total quantity across all warehouses
			BigDecimal totalQty = stockList.stream().map(WarehouseStock::getQuantity).reduce(BigDecimal.ZERO,
					BigDecimal::add);

			// compute aging buckets
			Map<String, BigDecimal> agingBuckets = new HashMap<>();
			agingBuckets.put("0-30", BigDecimal.ZERO);
			agingBuckets.put("31-60", BigDecimal.ZERO);
			agingBuckets.put("61-90", BigDecimal.ZERO);
			agingBuckets.put("91-180", BigDecimal.ZERO);
			agingBuckets.put("181-365", BigDecimal.ZERO);
			agingBuckets.put("365+", BigDecimal.ZERO);

			
			for (WarehouseStock ws : stockList) {
				Instant today=Instant.now();
				Instant date = ws.getReceivedDate() != null ? ws.getReceivedDate() : Instant.now();
				long days = ChronoUnit.DAYS.between(date, today);
				BigDecimal qty = ws.getQuantity();

				if (days <= 30)
					agingBuckets.put("0-30", agingBuckets.get("0-30").add(qty));
				else if (days <= 60)
					agingBuckets.put("31-60", agingBuckets.get("31-60").add(qty));
				else if (days <= 90)
					agingBuckets.put("61-90", agingBuckets.get("61-90").add(qty));
				else if (days <= 180)
					agingBuckets.put("91-180", agingBuckets.get("91-180").add(qty));
				else if (days <= 365)
					agingBuckets.put("181-365", agingBuckets.get("181-365").add(qty));
				else
					agingBuckets.put("365+", agingBuckets.get("365+").add(qty));
			}

			ProductStockAgingDto dto = new ProductStockAgingDto();
			dto.setItemId(item.getId());
			dto.setProductCode(item.getItemCode());
			dto.setProductName(item.getItemName());
			dto.setTotalQuantity(totalQty);
			dto.setAgingBuckets(agingBuckets);
			return dto;
		}).toList();
	}

}
