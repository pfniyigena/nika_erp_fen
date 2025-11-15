package com.niwe.erp.common.service;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.domain.ESequenceType;
import com.niwe.erp.common.domain.SequenceNumber;
import com.niwe.erp.common.repository.SequenceNumberRepository;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class SequenceNumberService {

	/**
	 * The sequenceNumberRepository
	 */
	private final SequenceNumberRepository sequenceNumberRepository;

	/**
	 * @param type
	 * @return
	 */
	private String getNextSequenceNumber(final ESequenceType type) {

		SequenceNumber result = sequenceNumberRepository.findByType(type);

		if (result == null) {
			// That means we do not have any sequence, we need to create one
			SequenceNumber sequence = new SequenceNumber();
			sequence.setType(type);
			sequence.setSequence("1");
			// create the sequence
			sequenceNumberRepository.save(sequence);
			return sequence.getSequence();
		}

		Long nextSequence = Long.parseLong(result.getSequence()) + 1;
		result.setSequence(nextSequence.toString());
		sequenceNumberRepository.save(result);
		return result.getSequence();
	}

	/**
	 * @return
	 */
	public String getNextTaxpayerCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.TAXPAYER);
		return "COMP" + StringUtils.leftPad(sequence, 3, "0");
	}

	/**
	 * @return
	 */
	public String getNextTaxpayerBranchCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.BRANCH_TAXPAYER);
		return "BRA" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextItemCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.ITEM);
		return "ITEM" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextInvoiceCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.INVOICE);
		return "INV" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextChargeCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.CHARGE);
		return "CHG" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextWarehouseCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.WAREHOUSE);
		return "WH" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextShelfCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.SHELF);
		return "POS" + StringUtils.leftPad(sequence, 3, "0");
	}
	public String getNextPurchaseCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.PURCHASE);
		return "PO" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextGoodReceivedCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.RECEIVED_GOOD);
		return "GR" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextPaymentMethodCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.PAYMENT_METHOD);
		return "PM" + StringUtils.leftPad(sequence, 2, "0");
	}
}
