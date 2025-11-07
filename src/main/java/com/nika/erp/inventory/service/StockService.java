package com.nika.erp.inventory.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.core.domain.CoreTaxpayer;
import com.nika.erp.core.domain.CoreTaxpayerBranch;
import com.nika.erp.core.repository.CoreTaxpayerBranchRepository;
import com.nika.erp.inventory.domain.Shelf;
import com.nika.erp.inventory.domain.StockItem;
import com.nika.erp.inventory.domain.Warehouse;
import com.nika.erp.inventory.repository.ShelfRepository;
import com.nika.erp.inventory.repository.StockItemRepository;
import com.nika.erp.inventory.repository.WarehouseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class StockService {

	private final StockItemRepository stockItemRepository;
	private final CoreTaxpayerBranchRepository coreTaxpayerBranchRepository;
	private final SequenceNumberService sequenceNumberService;
	private final WarehouseRepository warehouseRepository;
	private final ShelfRepository shelfRepository;

	@Transactional
	public void initiateInventory(CoreTaxpayer taxpayer) {
		try {

			List<CoreTaxpayerBranch> branches = coreTaxpayerBranchRepository.findByTaxpayerId(taxpayer.getId());

			if (!branches.isEmpty() && branches.size() > 1) {
				return;
			}

			Warehouse warehouse = warehouseRepository
					.save(Warehouse.builder().name("MAIN").internalCode(sequenceNumberService.getNextWarehouseCode())
							.branch(branches.get(0)).isMain(true).build());
			shelfRepository.save(Shelf.builder().internalCode(sequenceNumberService.getNextShelfCode())
					.warehouse(warehouse).branch(branches.get(0)).build());

		} catch (Exception e) {
			log.error("InitiateInventory:{}", e);
		}

	}

	@Transactional
	public void addStock(Warehouse warehouse, CoreItem item, BigDecimal quantity) {
		Optional<StockItem> itemOpt = stockItemRepository.findByWarehouseAndItem(warehouse, item);
		StockItem stockItem;
		if (itemOpt.isPresent()) {
			stockItem = itemOpt.get();
			stockItem.setQuantity(stockItem.getQuantity().add(quantity));
		} else {
			stockItem = new StockItem();
			stockItem.setWarehouse(warehouse);
			stockItem.setItem(item);
			stockItem.setQuantity(quantity);
		}
		stockItemRepository.save(stockItem);
	}

	@Transactional
	public boolean deductStock(Warehouse warehouse, CoreItem item, BigDecimal quantity) {
		Optional<StockItem> itemOpt = stockItemRepository.findByWarehouseAndItem(warehouse, item);
		if (itemOpt.isPresent() && itemOpt.get().getQuantity().compareTo(quantity) >= 0) {
			StockItem stockItem = itemOpt.get();
			stockItem.setQuantity(stockItem.getQuantity().subtract(quantity));
			stockItemRepository.save(stockItem);
			return true;
		}
		return false;
	}

}
