package com.nika.erp.ebm.service;

import org.springframework.stereotype.Service;

import com.nika.erp.ebm.domain.SdcCounter;
import com.nika.erp.ebm.repository.SdcCounterRepository;
import com.nika.erp.invoicing.domain.EInvoiceType;
import com.nika.erp.invoicing.domain.Invoice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SdcCounterService {

	private final SdcCounterRepository sdcCounterRepository;

	public SdcCounter increaseCounters(SdcCounter sdcCounter, Invoice invoice) {
		try {
			SdcCounter newSdcCounter = sdcCounterRepository.getReferenceById(sdcCounter.getId());
			this.increaseCounter(newSdcCounter, invoice);

			return sdcCounterRepository.save(newSdcCounter);
		} catch (Exception e) {
			log.debug("{}", e);
			return null;
		}
	}

	private SdcCounter increaseCounter(SdcCounter sdcCounter, Invoice invoice) {
		SdcCounter toBeIncreased = null;
		EInvoiceType invoiceType = invoice.getInvoiceType();
		switch (invoiceType) {
		case INVOICE_TYPE_NORMAL:
			// Increase Normal Counters
			toBeIncreased = increaseNormalCounters(sdcCounter, invoice);
			break;
		case INVOICE_TYPE_TRAINING:
			// Increase Training Counters
			toBeIncreased = increaseTrainingCounters(sdcCounter, invoice);
			break;
		case INVOICE_TYPE_COPY:
			// Increase Copy Counters
			toBeIncreased = increaseCopyCounters(sdcCounter, invoice);
			break;
		case INVOICE_TYPE_PROFORMA:
			// Increase Proforma Counters
			toBeIncreased = increaseProformaCounters(sdcCounter, invoice);
			break;
		default:
			break;
		}
		// Increase Global Counters
		if (toBeIncreased != null) {
			increaseGlobalCounters(toBeIncreased, invoice);
		}
		return toBeIncreased;

	}

	private SdcCounter increaseNormalCounters(SdcCounter sdcCounter, Invoice invoice) {

		sdcCounter.setTotalNormalInvoiceNumber(sdcCounter.getTotalNormalInvoiceNumber() + 1);
		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:
			sdcCounter.setNormalSaleAmountIncludingTaxA(
					sdcCounter.getNormalSaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setNormalSaleAmountIncludingTaxB1(
					sdcCounter.getNormalSaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setNormalSaleAmountIncludingTaxB2(
					sdcCounter.getNormalSaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			sdcCounter.setNormalSaleAmountIncludingTaxC(
					sdcCounter.getNormalSaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setNormalSaleAmountIncludingTaxD(
					sdcCounter.getNormalSaleAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			sdcCounter.setTotalNormalSaleTaxAmount(
					sdcCounter.getTotalNormalSaleTaxAmount().add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1())
							.add(invoice.getTotalTaxB2()).add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));
			sdcCounter.setTotalNormalSaleAmountIncludingTax(
					sdcCounter.getTotalNormalSaleAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));
			break;
		case TRANSACTION_TYPE_REFUND:
			sdcCounter.setNormalReturnAmountIncludingTaxA(
					sdcCounter.getNormalReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setNormalReturnAmountIncludingTaxB1(
					sdcCounter.getNormalReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setNormalReturnAmountIncludingTaxB2(
					sdcCounter.getNormalReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			sdcCounter.setNormalReturnAmountIncludingTaxC(
					sdcCounter.getNormalReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setNormalReturnAmountIncludingTaxD(
					sdcCounter.getNormalReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));
			sdcCounter.setTotalNormalReturnTaxAmount(
					sdcCounter.getTotalNormalReturnTaxAmount().add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1())
							.add(invoice.getTotalTaxB2()).add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));

			sdcCounter.setTotalNormalReturnAmountIncludingTax(
					sdcCounter.getTotalNormalReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));

			break;
		default:
			break;
		}
		return sdcCounter;
	}

	private SdcCounter increaseTrainingCounters(SdcCounter sdcCounter, Invoice invoice) {

		sdcCounter.setTotalTrainingInvoiceNumber(sdcCounter.getTotalTrainingInvoiceNumber() + 1);
		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:
			sdcCounter.setTrainingSaleAmountIncludingTaxA(
					sdcCounter.getTrainingSaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setTrainingSaleAmountIncludingTaxB1(
					sdcCounter.getTrainingSaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setTrainingSaleAmountIncludingTaxB2(
					sdcCounter.getTrainingSaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			sdcCounter.setTrainingSaleAmountIncludingTaxC(
					sdcCounter.getTrainingSaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setTrainingSaleAmountIncludingTaxD(
					sdcCounter.getTrainingSaleAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));
			sdcCounter.setTotalTrainingSaleTaxAmount(
					sdcCounter.getTotalTrainingSaleTaxAmount().add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1())
							.add(invoice.getTotalTaxB2()).add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));

			sdcCounter.setTotalTrainingSaleAmountIncludingTax(
					sdcCounter.getTotalTrainingSaleAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));

			break;
		case TRANSACTION_TYPE_REFUND:
			sdcCounter.setTrainingReturnAmountIncludingTaxA(
					sdcCounter.getTrainingReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setTrainingReturnAmountIncludingTaxB1(
					sdcCounter.getTrainingReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setTrainingReturnAmountIncludingTaxB2(
					sdcCounter.getTrainingReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));
			sdcCounter.setTrainingReturnAmountIncludingTaxC(
					sdcCounter.getTrainingReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setTrainingReturnAmountIncludingTaxD(
					sdcCounter.getTrainingReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));
			sdcCounter.setTotalTrainingReturnTaxAmount(sdcCounter.getTotalTrainingReturnTaxAmount()
					.add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1()).add(invoice.getTotalTaxB2())
					.add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));

			sdcCounter.setTotalTrainingReturnAmountIncludingTax(
					sdcCounter.getTotalTrainingReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));

			break;
		default:
			break;
		}
		return sdcCounter;

	}

	private SdcCounter increaseCopyCounters(SdcCounter sdcCounter, Invoice invoice) {

		sdcCounter.setTotalCopyInvoiceNumber(sdcCounter.getTotalCopyInvoiceNumber() + 1);
		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:
			sdcCounter.setCopySaleAmountIncludingTaxA(
					sdcCounter.getCopySaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setCopySaleAmountIncludingTaxB1(
					sdcCounter.getCopySaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setCopySaleAmountIncludingTaxB2(
					sdcCounter.getCopySaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));
			sdcCounter.setCopySaleAmountIncludingTaxC(
					sdcCounter.getCopySaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setCopySaleAmountIncludingTaxD(
					sdcCounter.getCopySaleAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			sdcCounter.setTotalCopySaleTaxAmount(
					sdcCounter.getTotalCopySaleTaxAmount().add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1())
							.add(invoice.getTotalTaxB2()).add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));

			sdcCounter.setTotalCopySaleAmountIncludingTax(
					sdcCounter.getTotalCopySaleAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));

			break;
		case TRANSACTION_TYPE_REFUND:
			sdcCounter.setCopyReturnAmountIncludingTaxA(
					sdcCounter.getCopyReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setCopyReturnAmountIncludingTaxB1(
					sdcCounter.getCopyReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setCopyReturnAmountIncludingTaxB2(
					sdcCounter.getCopyReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));

			sdcCounter.setCopyReturnAmountIncludingTaxC(
					sdcCounter.getCopyReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setCopyReturnAmountIncludingTaxD(
					sdcCounter.getCopyReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			sdcCounter.setTotalCopyReturnTaxAmount(
					sdcCounter.getTotalCopyReturnTaxAmount().add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1())
							.add(invoice.getTotalTaxB2()).add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));

			sdcCounter.setTotalCopyReturnAmountIncludingTax(
					sdcCounter.getTotalCopyReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));

			break;
		default:
			break;
		}
		return sdcCounter;

	}

	private SdcCounter increaseProformaCounters(SdcCounter sdcCounter, Invoice invoice) {

		sdcCounter.setTotalProformaInvoiceNumber(sdcCounter.getTotalProformaInvoiceNumber() + 1);
		switch (invoice.getTransactionType()) {
		case TRANSACTION_TYPE_SALE:
			sdcCounter.setProformaSaleAmountIncludingTaxA(
					sdcCounter.getProformaSaleAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setProformaSaleAmountIncludingTaxB1(
					sdcCounter.getProformaSaleAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setProformaSaleAmountIncludingTaxB2(
					sdcCounter.getProformaSaleAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));
			sdcCounter.setProformaSaleAmountIncludingTaxC(
					sdcCounter.getProformaSaleAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setProformaSaleAmountIncludingTaxD(
					sdcCounter.getProformaSaleAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			sdcCounter.setTotalProformaSaleTaxAmount(
					sdcCounter.getTotalProformaSaleTaxAmount().add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1())
							.add(invoice.getTotalTaxB2()).add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));

			sdcCounter.setTotalProformaSaleAmountIncludingTax(
					sdcCounter.getTotalProformaSaleAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));

			break;
		case TRANSACTION_TYPE_REFUND:
			sdcCounter.setProformaReturnAmountIncludingTaxA(
					sdcCounter.getProformaReturnAmountIncludingTaxA().add(invoice.getTotalAmountIncludingTaxA()));
			sdcCounter.setProformaReturnAmountIncludingTaxB1(
					sdcCounter.getProformaReturnAmountIncludingTaxB1().add(invoice.getTotalAmountIncludingTaxB1()));
			sdcCounter.setProformaReturnAmountIncludingTaxB2(
					sdcCounter.getProformaReturnAmountIncludingTaxB2().add(invoice.getTotalAmountIncludingTaxB2()));
			sdcCounter.setProformaReturnAmountIncludingTaxC(
					sdcCounter.getProformaReturnAmountIncludingTaxC().add(invoice.getTotalAmountIncludingTaxC()));
			sdcCounter.setProformaReturnAmountIncludingTaxD(
					sdcCounter.getProformaReturnAmountIncludingTaxD().add(invoice.getTotalAmountIncludingTaxD()));

			sdcCounter.setTotalProformaReturnTaxAmount(sdcCounter.getTotalProformaReturnTaxAmount()
					.add(invoice.getTotalTaxA()).add(invoice.getTotalTaxB1()).add(invoice.getTotalTaxB2())
					.add(invoice.getTotalTaxC()).add(invoice.getTotalTaxD()));

			sdcCounter.setTotalProformaReturnAmountIncludingTax(
					sdcCounter.getTotalProformaReturnAmountIncludingTax().add(invoice.getTotalAmountIncludingTaxA())
							.add(invoice.getTotalAmountIncludingTaxB1()).add(invoice.getTotalAmountIncludingTaxB2())
							.add(invoice.getTotalAmountIncludingTaxC()).add(invoice.getTotalAmountIncludingTaxD()));

			break;
		default:
			break;
		}
		return sdcCounter;

	}

	private SdcCounter increaseGlobalCounters(SdcCounter sdcCounter, Invoice invoice) {
		sdcCounter.setTotalInvoiceNumber(sdcCounter.getTotalInvoiceNumber() + 1);

		return sdcCounter;
		
	}

}
