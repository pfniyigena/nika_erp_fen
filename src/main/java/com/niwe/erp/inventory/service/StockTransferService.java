package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.inventory.domain.StockTransfer;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.StockTransferRepository;
import com.niwe.erp.inventory.repository.WarehouseRepository;
import com.niwe.erp.inventory.web.form.StockTransferForm;
import com.niwe.erp.inventory.web.form.StockTransferLineForm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StockTransferService {

	private final StockTransferRepository stockTransferRepository;
	private final WarehouseRepository warehouseRepository;
	private final WarehouseStockService warehouseStockService;
	private final CoreItemService coreItemService;
	private final SequenceNumberService sequenceNumberService;

	@Transactional
	public void transferStock(String fromWarehouseId, String toWarehouseId, CoreItem item, BigDecimal quantity) {
		Warehouse fromWarehouse = warehouseRepository.getReferenceById(UUID.fromString(fromWarehouseId));
		Warehouse toWarehouse = warehouseRepository.getReferenceById(UUID.fromString(toWarehouseId));
		warehouseStockService.transferProduct(fromWarehouse, toWarehouse, item, quantity);
		StockTransfer movement = new StockTransfer();
		movement.setInternalCode(sequenceNumberService.getNextStockTransferCode());
		movement.setFromWarehouse(fromWarehouse);
		movement.setToWarehouse(toWarehouse);
		movement.setItem(item);
		movement.setQuantity(quantity);
		movement.setTransferDate(LocalDateTime.now());
		stockTransferRepository.save(movement);

	}

	public List<StockTransfer> findAll() {
		return stockTransferRepository.findAll();
	}

	public void makeStockTransfer(StockTransferForm stockTransferForm) {
		log.info("---------makeStockTransfer from:{}, to:{}",stockTransferForm.getFromWarehouseId(),stockTransferForm.getToWarehouseId());

		for (StockTransferLineForm line : stockTransferForm.getLines()) {
			CoreItem item = coreItemService.findByInternalCode(line.getInternalCode());
			transferStock(stockTransferForm.getFromWarehouseId(), stockTransferForm.getToWarehouseId(), item,
					line.getQuantity());
		}

	}
}
