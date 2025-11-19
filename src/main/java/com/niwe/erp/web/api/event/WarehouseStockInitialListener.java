package com.niwe.erp.web.api.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.niwe.erp.inventory.domain.EStockOperation;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.inventory.service.WarehouseStockService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseStockInitialListener {
	private final WarehouseStockService warehouseStockService;
	private final WarehouseService warehouseService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onSaleCreated(ItemCreatedEvent event) {
		Warehouse defaultWarehouse = warehouseService.findMain();
		event.getItems().forEach((n) -> {
			EStockOperation stockOperation = EStockOperation.IN;

			warehouseStockService.updateProductQuantity(defaultWarehouse, n, n.getQuanityInitial(),
					MovementType.STOCK_INITIAL, n.getInternalCode(), stockOperation);
		});
	}
}
