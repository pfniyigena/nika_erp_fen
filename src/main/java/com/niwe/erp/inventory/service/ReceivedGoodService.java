package com.niwe.erp.inventory.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreUserService;
import com.niwe.erp.inventory.domain.EStockOperation;
import com.niwe.erp.inventory.domain.EStockReceivedStatus;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.domain.ReceivedGood;
import com.niwe.erp.inventory.domain.ReceivedItem;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.ReceivedGoodRepository;
import com.niwe.erp.inventory.web.form.GoodForm;
import com.niwe.erp.inventory.web.form.GoodLineForm;
import com.niwe.erp.purchase.domain.Purchase;
import com.niwe.erp.purchase.domain.PurchaseItem;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class ReceivedGoodService {
	private final ReceivedGoodRepository receivedGoodRepository;
	private final WarehouseService warehouseService;
	private final WarehouseStockService warehouseStockService;
	private final SequenceNumberService sequenceNumberService;
	private final CoreItemService coreItemService;
	private final CoreUserService coreUserService;
	

	public Boolean receiveGoodFromPurchase(Purchase purchase, String receivedBy, String warehouseId) {
		Warehouse warehouse = warehouseService.findById(warehouseId);
		ReceivedGood received = new ReceivedGood();
		received.setInternalCode(sequenceNumberService.getNextGoodReceivedCode());
		received.setSupplierName(purchase.getSupplierName());
		received.setReceivedDate(Instant.now());
		received.setPurchase(purchase);
		received.setReceivedBy(receivedBy);
		received.setWarehouse(warehouse);
		received.setStatus(EStockReceivedStatus.RECEIVED);
		received.setItemNumber(purchase.getItems().size());
		int seq=1;
		for (PurchaseItem purchaseItem : purchase.getItems()) {
			ReceivedItem ri = new ReceivedItem();
			ri.setItemName(purchaseItem.getItemName());
			ri.setItem(purchaseItem.getItem());
			ri.setQuantity(purchaseItem.getQuantity());
			ri.setPurchasePrice(purchaseItem.getPurchasePrice());
			ri.setReceivedGood(received);
			ri.setItemSeq(seq);
			received.getItems().add(ri);
			seq++;
		}
		receivedGoodRepository.save(received);
		updateProductQuantities(received);
		return true;
	}

	private void updateProductQuantities(ReceivedGood received) {

		received.getItems().forEach((n) -> {
			warehouseStockService.updateProductQuantity(received.getWarehouse(), n.getItem(), n.getQuantity(),
					MovementType.PURCHASE, "p", EStockOperation.IN);

		});

	}

	public List<ReceivedGood> findAll() {

		return receivedGoodRepository.findAll();
	}

	public ReceivedGood findById(String id) {
		return receivedGoodRepository.getReferenceById(UUID.fromString(id));
	}

	public ReceivedGood saveDraftReceivedGood(GoodForm goodForm) {
		ReceivedGood good = new ReceivedGood();
		good.setStatus(EStockReceivedStatus.WAITING);
		good.setInternalCode(sequenceNumberService.getNextGoodReceivedCode());
		AtomicInteger counter = new AtomicInteger(1);
		List<ReceivedItem> lines = goodForm.getGoodLines().stream().map(formLine -> {

			ReceivedItem line = mapToPurchaseLine(formLine);
			line.setItemSeq(counter.getAndIncrement());
			return line;
		}).toList();
		lines.forEach((n) -> n.setReceivedGood(good));
		good.setItems(lines);
		good.setItemNumber(lines.size());

		receivedGoodRepository.save(good);

		return good;

	}

	private ReceivedItem mapToPurchaseLine(GoodLineForm goodLineForm) {
		CoreItem coreItem = coreItemService.findByInternalCode(goodLineForm.getInternalCode());

		return ReceivedItem.builder().item(coreItem).itemName(coreItem.getItemName())
				.quantity(goodLineForm.getQuantity()).purchasePrice(goodLineForm.getUnitPrice()).build();

	}

	public ReceivedGood confirmAndReceive(String id, String warehouseId) {
		Warehouse warehouse = warehouseService.findById(warehouseId);
		String username = coreUserService.getCurrentUserEntity().getUsername();
		ReceivedGood good = receivedGoodRepository.getReferenceById(UUID.fromString(id));
		good.setStatus(EStockReceivedStatus.RECEIVED);
		good.setReceivedBy(username);
		good.setReceivedDate(Instant.now());
		good.setWarehouse(warehouse);
		ReceivedGood confirmed = receivedGoodRepository.save(good);
		updateProductQuantities(confirmed);
		return confirmed;
	}
}
