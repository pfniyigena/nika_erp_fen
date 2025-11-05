package com.nika.erp.common.service;

import org.springframework.stereotype.Service;

import com.nika.erp.common.domain.ESequenceType;
import com.nika.erp.common.domain.SequenceNumber;
import com.nika.erp.common.repository.SequenceNumberRepository;

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
		return "COMP000" + StringUtils.leftPad(sequence, 7, "0");
	}

	/**
	 * @return
	 */
	public String getNextTaxpayerBranchCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.BRANCH_TAXPAYER);
		return "BRA000" + StringUtils.leftPad(sequence, 8, "0");
	}
	public String getNextItemCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.ITEM);
		return "ITEM" + StringUtils.leftPad(sequence, 9, "0");
	}
	public String getNextInvoiceCode() {

		String sequence = this.getNextSequenceNumber(ESequenceType.INVOICE);
		return "INV" + StringUtils.leftPad(sequence, 9, "0");
	}
}
