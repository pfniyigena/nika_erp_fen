package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.inventory.domain.StockTransfer;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.StockTransferRepository;
import com.niwe.erp.inventory.repository.WarehouseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StockTransferService {

	private final StockTransferRepository stockTransferRepository;
	private final WarehouseRepository warehouseRepository;
	private final WarehouseStockService warehouseStockService;
	@Transactional
	public Boolean transferStock(String fromWarehouseId, String toWarehouseId, CoreItem item, BigDecimal quantity) {
		Warehouse fromWarehouse = warehouseRepository.getReferenceById(UUID.fromString(fromWarehouseId));
		Warehouse toWarehouse = warehouseRepository.getReferenceById(UUID.fromString(fromWarehouseId));

		
		warehouseStockService.transferProduct(fromWarehouse, toWarehouse, item, quantity);
		StockTransfer movement = new StockTransfer();
		movement.setFromWarehouse(fromWarehouse);
		movement.setToWarehouse(toWarehouse);
		movement.setItem(item);
		movement.setQuantity(quantity);
		movement.setMovementDate(LocalDateTime.now());
		stockTransferRepository.save(movement);
		return false;
	}
}
