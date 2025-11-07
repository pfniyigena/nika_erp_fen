package com.nika.erp.inventory.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.inventory.domain.StockMovement;
import com.nika.erp.inventory.domain.Warehouse;
import com.nika.erp.inventory.repository.StockMovementRepository;
import com.nika.erp.inventory.repository.WarehouseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StockMovementService {

	private final StockMovementRepository stockMovementRepository;
	private final WarehouseRepository warehouseRepository;
	private final StockService stockService;
	@Transactional
	public Boolean transferStock(String fromWarehouseId, String toWarehouseId, CoreItem item, BigDecimal quantity) {
		Warehouse fromWarehouse = warehouseRepository.getReferenceById(UUID.fromString(fromWarehouseId));
		Warehouse toWarehouse = warehouseRepository.getReferenceById(UUID.fromString(fromWarehouseId));

		boolean deducted = stockService.deductStock(fromWarehouse, item, quantity);
		if (!deducted)
			return false;

		stockService.addStock(toWarehouse, item, quantity);

		StockMovement movement = new StockMovement();
		movement.setFromWarehouse(fromWarehouse);
		movement.setToWarehouse(toWarehouse);
		movement.setItem(item);
		movement.setQuantity(quantity);
		movement.setMovementDate(LocalDateTime.now());
		stockMovementRepository.save(movement);
		return false;
	}
}
