package com.nika.erp.purchase.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.core.service.CoreItemService;
import com.nika.erp.core.service.CoreUserService;
import com.nika.erp.inventory.service.ReceivedGoodService;
import com.nika.erp.purchase.domain.Purchase;
import com.nika.erp.purchase.domain.PurchaseItem;
import com.nika.erp.purchase.domain.PurchaseStatus;
import com.nika.erp.purchase.repository.PurchaseRepository;
import com.nika.erp.purchase.web.form.PurchaseForm;
import com.nika.erp.purchase.web.form.PurchaseLineForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {

	private final PurchaseRepository purchaseRepository;
	private final CoreItemService coreItemService;
	private final SequenceNumberService sequenceNumberService;
	private final CoreUserService coreUserService;
	private final ReceivedGoodService receivedGoodService;

	public List<Purchase> findAll() {

		return purchaseRepository.findAll();
	}

	public void saveDraftPurchaseForm(PurchaseForm purchaseForm) {

		Purchase purchase = new Purchase();
		purchase.setPurchaseDate(LocalDateTime.now());
		purchase.setInternalCode(sequenceNumberService.getNextPurchaseCode());
		AtomicInteger counter = new AtomicInteger(1);
		List<PurchaseItem> lines = purchaseForm.getPurchaseLines().stream().map(formLine -> {

			PurchaseItem line = mapToPurchaseLine(formLine);
			line.setItemSeq(counter.getAndIncrement());
			return line;
		}).toList();
		lines.forEach((n) -> n.setPurchase(purchase));
		purchase.setItems(lines);
		purchase.setItemNumber(lines.size());

		purchaseRepository.save(purchase);

	}

	private PurchaseItem mapToPurchaseLine(PurchaseLineForm purchaseLineForm) {
		CoreItem coreItem = coreItemService.findByInternalCode(purchaseLineForm.getItemCode());

		return PurchaseItem.builder().item(coreItem).itemName(coreItem.getItemName())
				.quantity(purchaseLineForm.getQuantity()).purchasePrice(purchaseLineForm.getUnitPrice()).build();

	}

	public Purchase confirm(String id) {
		Purchase purchase = purchaseRepository.getReferenceById(UUID.fromString(id));
		purchase.setStatus(PurchaseStatus.DONE);
		purchase.setConfirmedBy(coreUserService.getCurrentUserEntity().getUsername());
		purchase.setConfirmedAt(LocalDateTime.now());
		return purchaseRepository.save(purchase);
	}

	public Purchase confirmAndReceive(String id,String warehouseId) {
		String username = coreUserService.getCurrentUserEntity().getUsername();
		Purchase purchase = purchaseRepository.getReferenceById(UUID.fromString(id));
		purchase.setStatus(PurchaseStatus.DONE);
		purchase.setConfirmedBy(username);
		purchase.setConfirmedAt(LocalDateTime.now());
		Purchase confirmed = purchaseRepository.save(purchase);
		Boolean received = receivedGoodService.receiveGoodFromPurchase(confirmed, username,warehouseId);
		purchase.setReceived(received);
		return purchaseRepository.save(purchase);
	}

	public Purchase findById(String id) {
		 
		return purchaseRepository.getReferenceById(UUID.fromString(id));
	}

}
