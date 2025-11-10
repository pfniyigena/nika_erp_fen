package com.nika.erp.inventory.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.nika.erp.inventory.domain.EStockOperation;
import com.nika.erp.inventory.domain.MovementType;
import com.nika.erp.inventory.domain.ReceivedGood;
import com.nika.erp.inventory.domain.ReceivedItem;
import com.nika.erp.inventory.domain.Warehouse;
import com.nika.erp.inventory.repository.ReceivedGoodRepository;
import com.nika.erp.purchase.domain.Purchase;
import com.nika.erp.purchase.domain.PurchaseItem;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ReceivedGoodService {
	private final ReceivedGoodRepository receivedGoodRepository;
	private final WarehouseService warehouseService;
	private final WarehouseStockService warehouseStockService;

	public Boolean receiveGoodFromPurchase(Purchase purchase,String receivedBy,String warehouseId) {
        Warehouse  warehouse=warehouseService.findById(warehouseId);
		ReceivedGood received = new ReceivedGood();
		received.setSupplierName(purchase.getSupplierName());
		received.setReceivedDate(LocalDateTime.now());
		received.setPurchase(purchase);
		received.setReceivedBy(receivedBy);
		received.setWarehouse(warehouse);
		for (PurchaseItem purchaseItem : purchase.getItems()) {
			ReceivedItem ri = new ReceivedItem();
			ri.setItemName(purchaseItem.getItemName());
			ri.setItem(purchaseItem.getItem());
			ri.setQuantity(purchaseItem.getQuantity());
			ri.setPurchasePrice(purchaseItem.getPurchasePrice());
			ri.setReceivedGood(received);
			received.getItems().add(ri);
		}
		receivedGoodRepository.save(received);
		updateProductQuantities(received);
		return true;
	}

	private void updateProductQuantities(ReceivedGood received ) {
		
		received.getItems().forEach((n) -> {
			warehouseStockService.updateProductQuantity(received.getWarehouse(), n.getItem(), n.getQuantity(), MovementType.PURCHASE, "p", EStockOperation.IN);
	
		});
		
		
		
	}
}
