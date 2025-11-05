package com.nika.erp.invoicing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.core.domain.EItemNature;
import com.nika.erp.core.service.CoreItemService;
import com.nika.erp.ebm.domain.SdcDailyReport;
import com.nika.erp.ebm.service.SdcDailyReportService;
import com.nika.erp.invoicing.domain.ETransactionType;
import com.nika.erp.invoicing.domain.Invoice;
import com.nika.erp.invoicing.domain.InvoiceLine;
import com.nika.erp.invoicing.domain.TaxRate;
import com.nika.erp.invoicing.mapper.InvoiceManualMapper;
import com.nika.erp.invoicing.repository.InvoiceRepository;
import com.nika.erp.invoicing.web.form.InvoiceForm;
import com.nika.erp.invoicing.web.form.InvoiceLineForm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class InvoiceService {
	private final InvoiceRepository invoiceRepository;
	private final SdcDailyReportService sdcDailyReportService;
	private final CoreItemService coreItemService;
	private final SequenceNumberService sequenceNumberService;
	private final TaxRateService taxRateService;

	/**
	 * @param invoice
	 * @return Invoice
	 */
	public Invoice saveInvoiceAndGenerateDailyReport(final Invoice invoice) {
		try {
			SdcDailyReport updatedateReport = sdcDailyReportService.updateDailyReport(invoice.getSdcInformation(),
					LocalDate.now(), invoice);
			if (updatedateReport != null && updatedateReport.getId() == null) {

				updatedateReport.setSdcInformation(invoice.getSdcInformation());
			}
			sdcDailyReportService.createSdcDailyReport(updatedateReport);
			if (updatedateReport != null) {
				invoice.setSdcDailyReport(updatedateReport);
			}
			return saveInvoice(invoice);
		} catch (Exception e) {
			return null;
		}
	}

	public Invoice saveInvoice(Invoice invoice) {

		return invoiceRepository.save(invoice);
	}

	public List<Invoice> findAll() {

		return invoiceRepository.findAll();
	}

	public Invoice findById(String id) {

		return invoiceRepository.getReferenceById(UUID.fromString(id));
	}

	public void saveDraftInvoiceForm(InvoiceForm invoiceForm) {
		Invoice invoice = mapToInvoice(invoiceForm);
		invoice.setTaxPayerName("1");
		invoice.setSdcNumber("1");
		invoice.setRegisteredTinNumber("1");
		invoice.setMachineRegistrationCode("1");
		invoice.setCisVersion("1");
		invoice.setInvoiceNumber(1L);
		invoice.setInternalCode(sequenceNumberService.getNextItemCode());
		// List<InvoiceLine> lines =
		// invoiceForm.getInvoiceLines().stream().map(this::mapToInvoiceLine).toList();
		AtomicInteger counter = new AtomicInteger(1);
		List<InvoiceLine> lines = invoiceForm.getInvoiceLines().stream().map(formLine -> {
			InvoiceLine line = mapToInvoiceLine(formLine);
			line.setItemSeq(counter.getAndIncrement());
			return line;
		}).toList();
		lines.forEach((n) -> n.setInvoice(invoice));
		invoice.setLines(lines);
		invoice.setItemNumber(lines.size());
		invoiceRepository.save(invoice);
	}

	private Invoice mapToInvoice(InvoiceForm invoiceForm) {

		return Invoice.builder().grossAmount(invoiceForm.getTotalAmount()).totalTaxAmount(invoiceForm.getTaxAmount())
				.build();
	}

	private InvoiceLine mapToInvoiceLine(InvoiceLineForm invoiceLineForm) {
		CoreItem coreItem = coreItemService.findByInternalCode(invoiceLineForm.getItemCode());
		TaxRate taxRate = taxRateService.findByCode(invoiceLineForm.getTaxCode());
		return InvoiceLine.builder().item(coreItem).itemName(invoiceLineForm.getItemName())
				.itemNature(EItemNature.valueOf(coreItem.getNature().getCode())).quantity(invoiceLineForm.getQuantity())
				.unitPrice(invoiceLineForm.getUnitPrice()).taxCode(invoiceLineForm.getTaxCode())
				.taxName(taxRate.getTaxName()).taxRate(invoiceLineForm.getTaxRate())
				.transactionType(ETransactionType.TRANSACTION_TYPE_SALE).grossAmount(invoiceLineForm.getLineTotal())
				.build();
	}

	public List<InvoiceForm> getAllInvoicesForm() {
		List<Invoice> invoices = invoiceRepository.findAll();
		return InvoiceManualMapper.toDtoList(invoices);
	}

	public InvoiceForm findInvoiceFormById(String id) {
		Invoice invoice = findById(id);
		return InvoiceManualMapper.toDto(invoice);
	}
}
