package com.nika.erp.ebm.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.nika.erp.ebm.domain.SdcCounter;
import com.nika.erp.ebm.domain.SdcInformation;
import com.nika.erp.ebm.repository.SdcCounterRepository;
import com.nika.erp.ebm.repository.SdcInformationRepository;
import com.nika.erp.ebm.util.AesEnc;
import com.nika.erp.ebm.util.Base32ToHex;
import com.nika.erp.ebm.util.EbmUtil;
import com.nika.erp.invoicing.domain.EInvoiceType;
import com.nika.erp.invoicing.domain.ETransactionType;
import com.nika.erp.invoicing.domain.Invoice;

import jakarta.xml.bind.DatatypeConverter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class EbmGenericService {

	private final SdcInformationRepository sdcInformationRepository;

	private final SdcCounterRepository sdcCounterRepository;
	
	private final SdcCounterService sdcCounterService;

	/**
	 * @param invoice
	 * @return
	 */
	public String generateSdcData(final Invoice invoice) {
		try {
			StringBuilder sdcData = new StringBuilder();
			SdcInformation sdcInformation = sdcInformationRepository.findBySdcNumber(invoice.getSdcNumber());
			invoice.setSdcInformation(sdcInformation);
			SdcCounter sdcCounter = sdcCounterRepository.findBySdcInformation(sdcInformation);
			// update signature staff
			String signature = " ";
			String internalData = " ";
			if (invoice.getInvoiceType().equals(EInvoiceType.INVOICE_TYPE_NORMAL)) {
				String signatureInputString = getSignatureInputString(invoice);
				String internalDataInputString = getInternalDataInputString(invoice, sdcCounter);
				signature = getSignature(signatureInputString, sdcInformation.getSignatureKey());
				internalData = getInternalData(internalDataInputString, sdcInformation.getInternalDataKey());

			}

			LocalDateTime sdcDate = LocalDateTime.now();
			SdcCounter newSdcCounter = sdcCounterService.increaseCounters(sdcCounter, invoice);
			sdcData.append(sdcInformation.getTaxpayer().getTinNumber());
			sdcData.append(",");
			sdcData.append(sdcInformation.getSdcNumber());
			sdcData.append(",");
			sdcData.append(invoice.getMachineRegistrationCode());
			sdcData.append(",");
			sdcData.append(getInvoiceNumberPerType(newSdcCounter, invoice));
			sdcData.append(",");
			sdcData.append(newSdcCounter.getTotalInvoiceNumber());
			sdcData.append(",");
			sdcData.append(invoice.getTransactionTypeCode());
			sdcData.append(invoice.getInvoiceTypeCode());
			sdcData.append(",");
			sdcData.append(invoice.getCisDate());
			sdcData.append(",");
			sdcData.append(sdcDate);
			sdcData.append(",");
			sdcData.append(invoice.getTotalTaxAmount());
			sdcData.append(",");
			sdcData.append(invoice.getTotalTaxAmount());
			sdcData.append(",");
			sdcData.append(formateString(signature));
			sdcData.append(",");
			sdcData.append(formateString(internalData));
			log.debug("SDC DATA:{}", sdcData.toString());
			return sdcData.toString();
		} catch (Exception e) {
			return null;
		}
	}

	private String formateString(String d) {
		if (d != null && !d.isEmpty()) {
			int j = 4;
			StringBuilder bld = new StringBuilder();
			for (int i = 0; i < d.length(); i++) {
				if (j >= d.length()) {
					bld.append(d.substring(i, d.length()));
				} else {
					bld.append(d.substring(i, j)).append("-");
				}
				i = j - 1;
				j = j + 4;
			}
			return bld.toString();
		} else {
			return "";

		}

	}

	private String getSignatureInputString(final Invoice invoice) {
		return EbmUtil.getSignatureInputString(invoice.getRegisteredTinNumber(), invoice.getSdcNumber(),
				invoice.getMachineRegistrationCode(), invoice.getInvoiceNumber(), invoice.getCustomerTinNumber(),
				invoice.getInvoiceTypeCode(), invoice.getTotalInvoicePerTypeCounter(), invoice.getTotalInvoiceCounter(),
				invoice.getTransactionTypeCode(), invoice.getTaxRateA(), invoice.getTaxRateB1(), invoice.getTaxRateB2(),
				invoice.getTaxRateC(), invoice.getTaxRateD(), invoice.getTotalAmountIncludingTaxA(),
				invoice.getTotalAmountIncludingTaxB1(), invoice.getTotalAmountIncludingTaxB2(),
				invoice.getTotalAmountIncludingTaxC(), invoice.getTotalAmountIncludingTaxD(), invoice.getTotalTaxA(),
				invoice.getTotalTaxB1(), invoice.getTotalTaxB2(), invoice.getTotalTaxC(), invoice.getTotalTaxD(),
				invoice.getCisDate(), invoice.getSdcDate());
	}

	private String getInternalDataInputString(Invoice invoice, SdcCounter sdcCounter) {

		BigDecimal totalSaleTaxAmount = BigDecimal.ZERO;
		BigDecimal totalReturnTaxAmount = BigDecimal.ZERO;
		Integer numberOfCopyInvoice = 0;
		Integer totalInvoiceCounter = 0;
		if (invoice.getTransactionType().equals(ETransactionType.TRANSACTION_TYPE_SALE)) {
			totalSaleTaxAmount = sdcCounter.getTotalNormalSaleTaxAmount().add(invoice.getTotalTaxAmount());
			totalReturnTaxAmount = sdcCounter.getTotalNormalReturnTaxAmount();
		}
		if (invoice.getTransactionType().equals(ETransactionType.TRANSACTION_TYPE_REFUND)) {
			totalSaleTaxAmount = sdcCounter.getTotalNormalSaleTaxAmount();
			totalReturnTaxAmount = sdcCounter.getTotalNormalReturnTaxAmount().add(invoice.getTotalTaxAmount());
		}
		if (invoice.getInvoiceType().equals(EInvoiceType.INVOICE_TYPE_COPY)) {
			numberOfCopyInvoice = sdcCounter.getTotalCopyInvoiceNumber() + 1;
		} else {
			numberOfCopyInvoice = sdcCounter.getTotalCopyInvoiceNumber();
		}
		totalInvoiceCounter = sdcCounter.getTotalInvoiceNumber() + 1;
		byte[] getInternalDataInputByteArray = EbmUtil.getInternalDataInput(totalSaleTaxAmount, totalReturnTaxAmount,
				numberOfCopyInvoice, numberOfCopyInvoice, totalInvoiceCounter);

		return DatatypeConverter.printHexBinary(getInternalDataInputByteArray);

	}

	private String getSignature(final String signatureInputString, final String signKey) throws Exception {

		Base32ToHex b = new Base32ToHex();
		return b.hmacSha1(signatureInputString, signKey);
	}

	private String getInternalData(String internalDataInputString, String internalKey) throws Exception {

		Base32ToHex base32 = new Base32ToHex();
		AesEnc internal = new AesEnc();
		String hexKey = base32.bytesToHex(base32.decode(internalKey)).toUpperCase();
		return internal.encrypt(internalDataInputString, hexKey);

	}

	private Integer getInvoiceNumberPerType(SdcCounter sdcCounter, Invoice invoice) {
		EInvoiceType invoiceType = invoice.getInvoiceType();
		Integer invoiceNumberPerType = 0;

		switch (invoiceType) {
		case INVOICE_TYPE_NORMAL:

			invoiceNumberPerType = sdcCounter.getTotalNormalInvoiceNumber();
			break;
		case INVOICE_TYPE_TRAINING:

			invoiceNumberPerType = sdcCounter.getTotalTrainingInvoiceNumber();
			break;
		case INVOICE_TYPE_COPY:

			invoiceNumberPerType = sdcCounter.getTotalCopyInvoiceNumber();
			break;
		case INVOICE_TYPE_PROFORMA:

			invoiceNumberPerType = sdcCounter.getTotalProformaInvoiceNumber();
			break;
		default:
			break;
		}

		return invoiceNumberPerType;
	}
}
