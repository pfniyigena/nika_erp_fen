package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.Instant;
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
			MovementType type, String reference, EStockOperation eStockOperation) {
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

		/*
		 * if (newQuantity.compareTo(new BigDecimal("0.00")) < 0) { throw new
		 * IllegalStateException( "Not enough stock for product " +
		 * product.getItemName() + " in warehouse " + warehouse.getWarehouseName()); }
		 */
		stock.setQuantity(newQuantity);
		warehouseStockRepository.save(stock);

		// Record movement
		StockMovement movement = StockMovement.builder().warehouse(warehouse).item(product).movementType(type)
				.reference(reference).previousQuantity(previousQuantiry).currentQuantity(newQuantity)
				.quantity(quantityChange).stockOperation(eStockOperation).movementDate(Instant.now())

				.build();

		stockMovementRepository.save(movement);
		}catch (Exception e) {
			log.error("-------updateProductQuantity:{}",e);
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

}
