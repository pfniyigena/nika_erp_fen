package com.nika.erp.core.service;

import java.util.List;
import java.util.UUID;

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

	public CoreTaxpayerBranch save(CoreTaxpayerBranch coreTaxpayerBranch) {
		CoreTaxpayerBranch savedTaxpayerBranch = null;
		String code = coreTaxpayerBranch.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextTaxpayerBranchCode();
		}

		if (coreTaxpayerBranch.getId() != null) {

			CoreTaxpayerBranch exist = coreTaxpayerBranchRepository.getReferenceById(coreTaxpayerBranch.getId());
			exist.setBranchAddress(coreTaxpayerBranch.getBranchAddress());
			exist.setInternalCode(code);
			exist.setBranchName(coreTaxpayerBranch.getBranchName());
			exist.setTaxpayer(coreTaxpayerBranch.getTaxpayer());
			savedTaxpayerBranch=coreTaxpayerBranchRepository.save(exist);
		} else {
			coreTaxpayerBranch.setInternalCode(code);
			savedTaxpayerBranch = coreTaxpayerBranchRepository.save(coreTaxpayerBranch);
		}

		return savedTaxpayerBranch;

	}

	public List<CoreTaxpayerBranch> findAll() {

		return coreTaxpayerBranchRepository.findAll();
	}

	public CoreTaxpayerBranch findById(String id) {

		return coreTaxpayerBranchRepository.getReferenceById(UUID.fromString(id));
	}
}
