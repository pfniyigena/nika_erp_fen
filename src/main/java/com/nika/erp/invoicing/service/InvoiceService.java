package com.nika.erp.invoicing.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nika.erp.ebm.domain.SdcDailyReport;
import com.nika.erp.ebm.service.SdcDailyReportService;
import com.nika.erp.invoicing.domain.Invoice;
import com.nika.erp.invoicing.repository.InvoiceRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class InvoiceService {
	private final InvoiceRepository invoiceRepository;

	private final SdcDailyReportService sdcDailyReportService;

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

}
