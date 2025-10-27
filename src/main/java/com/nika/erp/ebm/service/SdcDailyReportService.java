package com.nika.erp.ebm.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.nika.erp.ebm.domain.SdcDailyReport;
import com.nika.erp.ebm.domain.SdcInformation;
import com.nika.erp.ebm.repository.SdcDailyReportRespository;
import com.nika.erp.invoicing.domain.EInvoiceType;
import com.nika.erp.invoicing.domain.Invoice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SdcDailyReportService {

	private SdcDailyReportRespository sdcDailyReportRespository;

	public SdcDailyReport findBySdcInformationAndReportDate(SdcInformation sdcInformation, LocalDate reportDate) {
		return sdcDailyReportRespository.findBySdcInformationAndReportDate(sdcInformation, reportDate);
	}

	public SdcDailyReport updateDailyReport(SdcInformation sdcInformation, LocalDate reportDate,
			final Invoice invoice) {

		SdcDailyReport dailyReport = findBySdcInformationAndReportDate(sdcInformation, reportDate);
		SdcDailyReport toBeIncreased = null;
		EInvoiceType invoiceType = invoice.getInvoiceType();
		switch (invoiceType) {
		case INVOICE_TYPE_NORMAL:
			// Increase Normal Counters
			toBeIncreased = updateNormalDailyReport(dailyReport, invoice);
			break;
		case INVOICE_TYPE_TRAINING:
			// Increase Training Counters
			toBeIncreased = updateTrainingDaily(dailyReport, invoice);
			break;
		case INVOICE_TYPE_COPY:
			// Increase Copy Counters
			toBeIncreased = updateCopyDailyReport(dailyReport, invoice);
			break;
		case INVOICE_TYPE_PROFORMA:
			// Increase Proforma Counters
			toBeIncreased = updateProformaDailyReport(dailyReport, invoice);
			break;
		default:
			break;
		}
		// Increase Global Daily report
		if (toBeIncreased != null) {
			updateGlobalDailyReport(toBeIncreased, invoice);
		}
		return toBeIncreased;

	}

	private SdcDailyReport updateNormalDailyReport(SdcDailyReport dailyReport, Invoice invoice) {

		dailyReport.setTotalNormalInvoiceNumber(dailyReport.getTotalNormalInvoiceNumber() + 1);
		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:
			dailyReport.setNormalSaleAmountIncludingTaxA(
					dailyReport.getNormalSaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			// apply discount
//			dailyReport.setNormalSaleAmountIncludingTaxA(
//					invoice.getTotalAmountIncludingTaxA().compareTo(BigDecimal.ZERO) == 0
//							? dailyReport.getNormalSaleAmountIncludingTaxA()
//							: dailyReport.getNormalSaleAmountIncludingTaxA()
//									.subtract(invoice.getTotalDiscountAmount()));

			dailyReport.setNormalSaleAmountIncludingTaxB1(
					dailyReport.getNormalSaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));

			dailyReport.setNormalSaleAmountIncludingTaxB2(
					dailyReport.getNormalSaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			dailyReport.setNormalSaleAmountIncludingTaxC(
					dailyReport.getNormalSaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));

			dailyReport.setNormalSaleAmountIncludingTaxD(
					dailyReport.getNormalSaleAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));
			break;
		case TRANSACTION_TYPE_REFUND:
			dailyReport.setNormalReturnAmountIncludingTaxA(
					dailyReport.getNormalReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			dailyReport.setNormalReturnAmountIncludingTaxB1(
					dailyReport.getNormalReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			dailyReport.setNormalReturnAmountIncludingTaxB2(
					dailyReport.getNormalReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			dailyReport.setNormalReturnAmountIncludingTaxC(
					dailyReport.getNormalReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			dailyReport.setNormalReturnAmountIncludingTaxD(
					dailyReport.getNormalReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			dailyReport.setTotalReturnAmountIncludingTax(
					dailyReport.getTotalReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTax()));
			break;
		default:
			break;
		}
		return dailyReport;
	}

	private SdcDailyReport updateTrainingDaily(SdcDailyReport sdcDailyReport, Invoice invoice) {

		sdcDailyReport.setTotalTrainingInvoiceNumber(sdcDailyReport.getTotalTrainingInvoiceNumber() + 1);

		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:

			sdcDailyReport.setTrainingSaleAmountIncludingTaxA(
					sdcDailyReport.getTrainingSaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));

			sdcDailyReport.setTrainingSaleAmountIncludingTaxB1(
					sdcDailyReport.getTrainingSaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));

			sdcDailyReport.setTrainingSaleAmountIncludingTaxB2(
					sdcDailyReport.getTrainingSaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			sdcDailyReport.setTrainingSaleAmountIncludingTaxC(
					sdcDailyReport.getTrainingSaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			break;
		case TRANSACTION_TYPE_REFUND:
			sdcDailyReport.setTrainingReturnAmountIncludingTaxA(
					sdcDailyReport.getTrainingReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcDailyReport.setTrainingReturnAmountIncludingTaxB1(
					sdcDailyReport.getTrainingReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcDailyReport.setTrainingReturnAmountIncludingTaxB2(
					sdcDailyReport.getTrainingReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			sdcDailyReport.setTrainingReturnAmountIncludingTaxC(
					sdcDailyReport.getTrainingReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcDailyReport.setTrainingReturnAmountIncludingTaxD(
					sdcDailyReport.getTrainingReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			sdcDailyReport.setTotalReturnAmountIncludingTax(
					sdcDailyReport.getTotalReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTax()));

			break;
		default:
			break;
		}
		return sdcDailyReport;

	}

	/**
	 * @param sdcCounter
	 * @param invoice
	 */
	private SdcDailyReport updateCopyDailyReport(final SdcDailyReport dailyReport, final Invoice invoice) {

		dailyReport.setTotalCopyInvoiceNumber(dailyReport.getTotalCopyInvoiceNumber() + 1);
		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:
			dailyReport.setCopySaleAmountIncludingTaxA(
					dailyReport.getCopySaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));

			dailyReport.setCopySaleAmountIncludingTaxB1(
					dailyReport.getCopySaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));

			dailyReport.setCopySaleAmountIncludingTaxB2(
					dailyReport.getCopySaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			dailyReport.setCopySaleAmountIncludingTaxC(
					dailyReport.getCopySaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));

			dailyReport.setCopySaleAmountIncludingTaxD(
					dailyReport.getCopySaleAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			break;
		case TRANSACTION_TYPE_REFUND:
			dailyReport.setCopyReturnAmountIncludingTaxA(
					dailyReport.getCopyReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			dailyReport.setCopyReturnAmountIncludingTaxB1(
					dailyReport.getCopyReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			dailyReport.setCopyReturnAmountIncludingTaxB2(
					dailyReport.getCopyReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			dailyReport.setCopyReturnAmountIncludingTaxC(
					dailyReport.getCopyReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			dailyReport.setCopyReturnAmountIncludingTaxD(
					dailyReport.getCopyReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			dailyReport.setTotalReturnAmountIncludingTax(
					dailyReport.getTotalReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTax()));

			break;
		default:
			break;
		}
		return dailyReport;
	}

	private SdcDailyReport updateProformaDailyReport(final SdcDailyReport dailReport, final Invoice invoice) {
		dailReport.setTotalProformaInvoiceNumber(dailReport.getTotalProformaInvoiceNumber() + 1);
		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:
			dailReport.setProformaSaleAmountIncludingTaxA(
					dailReport.getProformaSaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));

			dailReport.setProformaSaleAmountIncludingTaxB1(
					dailReport.getProformaSaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));

			dailReport.setProformaSaleAmountIncludingTaxB2(
					dailReport.getProformaSaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			dailReport.setProformaSaleAmountIncludingTaxC(
					dailReport.getProformaSaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));

			dailReport.setProformaSaleAmountIncludingTaxD(
					dailReport.getProformaSaleAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			break;
		case TRANSACTION_TYPE_REFUND:
			dailReport.setProformaReturnAmountIncludingTaxA(
					dailReport.getProformaReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			dailReport.setProformaReturnAmountIncludingTaxB1(
					dailReport.getProformaReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			dailReport.setProformaReturnAmountIncludingTaxB2(
					dailReport.getProformaReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			dailReport.setProformaReturnAmountIncludingTaxC(
					dailReport.getProformaReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			dailReport.setProformaReturnAmountIncludingTaxD(
					dailReport.getProformaReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			dailReport.setTotalReturnAmountIncludingTax(
					dailReport.getTotalReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTax()));

			break;
		default:
			break;
		}
		return dailReport;
	}

	private SdcDailyReport updateGlobalDailyReport(final SdcDailyReport sdcCounter, final Invoice invoice) {
		sdcCounter.setTotalInvoiceNumber(sdcCounter.getTotalInvoiceNumber() + 1);
		sdcCounter.setTotalSaleAmountIncludingTax(
				invoice.getTotalAmountIncludingTax().add(sdcCounter.getTotalSaleAmountIncludingTax()));
		return sdcCounter;
	}

	public SdcDailyReport createSdcDailyReport(SdcDailyReport sdcDailyReport) {
		return sdcDailyReportRespository.save(sdcDailyReport);

	}
}
