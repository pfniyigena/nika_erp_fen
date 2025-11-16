package com.niwe.erp.sale.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niwe.erp.common.api.dto.SaleItemRequest;
import com.niwe.erp.common.api.dto.SaleRequest;
import com.niwe.erp.common.domain.EChannel;
import com.niwe.erp.common.domain.PaymentStatus;
import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.common.util.DataParserUtil;
import com.niwe.erp.common.util.NiweErpCommonConstants;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreUserService;
import com.niwe.erp.invoicing.domain.EPaymentMethod;
import com.niwe.erp.sale.domain.DailySalesSummary;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.domain.SaleItem;
import com.niwe.erp.sale.domain.SaleStatus;
import com.niwe.erp.sale.domain.Shelf;
import com.niwe.erp.sale.domain.TransactionType;
import com.niwe.erp.sale.repository.SaleRepository;
import com.niwe.erp.sale.repository.ShelfRepository;
import com.niwe.erp.sale.web.form.ShelfForm;
import com.niwe.erp.sale.web.form.ShelfLineForm;
import com.niwe.erp.web.api.event.SaleCreatedEvent;

import lombok.RequiredArgsConstructor;
import wys.ebm.core.invoice.util.InvoiceCalculationService;

@Service
@RequiredArgsConstructor
public class ShelfService {
	private final SaleRepository saleRepository;
	private final ShelfRepository shelfRepository;
	private final SequenceNumberService sequenceNumberService;
	private final CoreItemService coreItemService;
	private final CoreUserService coreUserService;
	private final PaymentMethodService paymentMethodService;
	private final DailySalesSummaryService dailySalesSummaryService;
	private final ApplicationEventPublisher eventPublisher;

	public List<Shelf> findAll() {

		return shelfRepository.findAll().stream().map(shelf -> {
			shelf.setNumberOfProductsNotSynchronized(
					coreItemService
							.findByLastUpdatedAfter(shelf.getLastSyn(),
									PageRequest.of(0, NiweErpCommonConstants.NIKA_DEFAULT_PAGE_SIZE))
							.getTotalElements());
			return shelf;
		}).toList();

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

	@Transactional
	public void savePost(ShelfForm shelfForm) {
		Sale sale = new Sale();
		sale.setSourceChannel(EChannel.WEB);
		sale.setTransactionType(TransactionType.SALE);
		Shelf shelf = findById(shelfForm.getShelfId());
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
		sale.setPaymentMethod(paymentMethodService.save(EPaymentMethod.CASH.name()));
		saleRepository.save(sale);
		dailySalesSummaryService.save(sale);
		eventPublisher.publishEvent(new SaleCreatedEvent(this, sale));

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

	@Transactional
	public void receiveSaleFromExternalShelf(SaleRequest request) {

		if (saleRepository.findByExternalCode(request.externalReference()).isPresent()) {
			return;
		}
		Shelf shelf = findByInternalCode(request.niweHeaderRequest().shelfCode());

		Sale sale = new Sale();
		sale.setSourceChannel(EChannel.API);
		TransactionType transactionType = TransactionType.SALE;
		if (!request.transactionType().equals("S")) {
			transactionType = TransactionType.REFUND;
		}
		sale.setTransactionType(transactionType);
		sale.setSaleDate(DataParserUtil.instantFromDateString(request.saleDate()));
		sale.setExternalCode(request.externalReference());
		sale.setShelf(shelf);
		sale.setPaymentMethod(paymentMethodService.save(request.paymentMethod()));
		sale.setConfirmedBy(request.confirmedBy());
		sale.setInternalCode(sequenceNumberService.getNextShelfCode());
		List<SaleItem> lines = request.items().stream().map(itemRequest -> {
			InvoiceCalculationService invoiceCalculationService = new InvoiceCalculationService(itemRequest.quantity(),
					itemRequest.unitPrice(), itemRequest.taxRate(), new BigDecimal("0.00"), new BigDecimal("0.00"));

			sale.setTotalAmountToPay(sale.getTotalAmountToPay().add(invoiceCalculationService.getAmountToPay()));

			SaleItem line = mapToSaleLine(itemRequest);
			return line;

		}).toList();
		lines.forEach((n) -> n.setSale(sale));
		sale.setItems(lines);
		sale.setItemNumber(lines.size());
		sale.setStatus(SaleStatus.DONE);
		sale.setPaymentStatus(PaymentStatus.PAID);
		DailySalesSummary summary = dailySalesSummaryService.save(sale);
		sale.setSummary(summary);
		saleRepository.save(sale);
		eventPublisher.publishEvent(new SaleCreatedEvent(this, sale));
	}

	private SaleItem mapToSaleLine(SaleItemRequest saleItemRequest) {
		CoreItem coreItem = coreItemService.findByInternalCode(saleItemRequest.itemCode());
		return SaleItem.builder().item(coreItem).itemName(coreItem.getItemName()).quantity(saleItemRequest.quantity())
				.salePrice(saleItemRequest.unitPrice()).itemSeq(saleItemRequest.itemSequence()).build();

	}

}
