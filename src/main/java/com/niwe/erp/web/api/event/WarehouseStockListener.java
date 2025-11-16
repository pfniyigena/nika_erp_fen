package com.niwe.erp.web.api.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.niwe.erp.inventory.domain.EStockOperation;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.service.WarehouseStockService;
import com.niwe.erp.sale.domain.TransactionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseStockListener {
	private final WarehouseStockService warehouseStockService;
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSaleCreated(SaleCreatedEvent event) {
		log.debug("------------onSaleCreated:{}",event.getSale());
		event.getSale().getItems().forEach((n) -> {
			EStockOperation stockOperation = EStockOperation.OUT;
			MovementType movementType = MovementType.SALE;
			if (!event.getSale().getTransactionType().equals(TransactionType.SALE)) {
				stockOperation = EStockOperation.IN;
				movementType = MovementType.SALE_RETURN;

			}
			warehouseStockService.updateProductQuantity(event.getSale().getShelf().getWarehouse(), n.getItem(), n.getQuantity(),
					movementType, event.getSale().getExternalCode(), stockOperation);
		});
    }
}
