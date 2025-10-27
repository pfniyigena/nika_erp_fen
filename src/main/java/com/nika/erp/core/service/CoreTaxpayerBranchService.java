package com.nika.erp.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.core.domain.CoreTaxpayerBranch;
import com.nika.erp.core.repository.CoreTaxpayerBranchRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreTaxpayerBranchService {

	private final CoreTaxpayerBranchRepository coreTaxpayerBranchRepository;
	private final SequenceNumberService sequenceNumberService;

	CoreTaxpayerBranch saveNew(CoreTaxpayerBranch coreTaxpayerBranch) {
		coreTaxpayerBranch.setInternalCode(sequenceNumberService.getNextTaxpayerBranchCode());
		return coreTaxpayerBranchRepository.save(coreTaxpayerBranch);

	}

	public List<CoreTaxpayerBranch> findAll() {

		return coreTaxpayerBranchRepository.findAll();
	}
}
