package com.niwe.erp.sale.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.domain.PaymentStatus;
import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreUserService;
import com.niwe.erp.inventory.domain.EStockOperation;
import com.niwe.erp.inventory.domain.MovementType;
import com.niwe.erp.inventory.service.WarehouseStockService;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.domain.SaleItem;
import com.niwe.erp.sale.domain.SaleStatus;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.repository.SaleRepository;
import com.niwe.erp.sale.repository.ShelfRepository;
import com.niwe.erp.sale.web.form.ShelfForm;
import com.niwe.erp.sale.web.form.ShelfLineForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShelfService {
	private final SaleRepository saleRepository;
	private final ShelfRepository shelfRepository;
	private final SequenceNumberService sequenceNumberService;
	private final CoreItemService coreItemService;
	private final CoreUserService coreUserService;
	private final WarehouseStockService warehouseStockService;

	public List<Shelf> findAll() {

		return shelfRepository.findAll();
	}

	public Shelf save(Shelf shelf) {
		Shelf savedShelf = null;
		String code = shelf.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextShelfCode();
		}

		if (shelf.getId() != null) {

			Shelf exist = shelfRepository.getReferenceById(shelf.getId());
			exist.setInternalCode(code);
			exist.setName(shelf.getName());
			exist.setWarehouse(shelf.getWarehouse());

			savedShelf = shelfRepository.save(exist);
		} else {
			shelf.setInternalCode(code);
			savedShelf = shelfRepository.save(shelf);
		}

		return savedShelf;

	}

	public Shelf findById(String id) {

		return shelfRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ResourceNotFoundException("Shelf not found with id " + id));

	}

	public void savePost(ShelfForm shelfForm) {

		Shelf shelf = findById(shelfForm.getShelfId());
		Sale sale = new Sale();
		sale.setShelf(shelf);
		sale.setConfirmedBy(coreUserService.getCurrentUserEntity().getUsername());
		sale.setSaleDate(Instant.now());
		sale.setInternalCode(sequenceNumberService.getNextShelfCode());
		AtomicInteger counter = new AtomicInteger(1);
		List<SaleItem> lines = shelfForm.getShelfLines().stream().map(formLine -> {

			SaleItem line = mapToSaleLine(formLine);
			line.setItemSeq(counter.getAndIncrement());
			return line;
		}).toList();
		lines.forEach((n) -> n.setSale(sale));
		sale.setItems(lines);
		sale.setItemNumber(lines.size());
		sale.setStatus(SaleStatus.DONE);
		sale.setPaymentStatus(PaymentStatus.PAID);
		saleRepository.save(sale);
		sale.getItems().forEach((n) -> {
			warehouseStockService.updateProductQuantity(sale.getShelf().getWarehouse(), n.getItem(), n.getQuantity(),
					MovementType.SALE, "S", EStockOperation.OUT);

		});

	}

	private SaleItem mapToSaleLine(ShelfLineForm shelfLineForm) {
		CoreItem coreItem = coreItemService.findByInternalCode(shelfLineForm.getInternalCode());

		return SaleItem.builder().item(coreItem).itemName(coreItem.getItemName()).quantity(shelfLineForm.getQuantity())
				.salePrice(shelfLineForm.getUnitPrice()).build();

	}

	public Shelf findByInternalCode(String internalCode) {
		return shelfRepository.findByInternalCode(internalCode)
				.orElseThrow(() -> new ResourceNotFoundException("Shelf not found with internalCode: " + internalCode));
	}

}
