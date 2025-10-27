package com.nika.erp.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.common.util.NikaErpConstants;
import com.nika.erp.core.domain.CoreTaxpayer;
import com.nika.erp.core.domain.CoreTaxpayerBranch;
import com.nika.erp.core.repository.CoreTaxpayerRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreTaxpayerService {

	private final CoreTaxpayerRepository coreTaxpayerRepository;

	private final SequenceNumberService sequenceNumberService;

	private final CoreTaxpayerBranchService coreTaxpayerBranchService;

	public void initTaxpayer() {

		if (coreTaxpayerRepository.findAll().size() < 1) {

			CoreTaxpayer coreTaxpayer = saveNew(CoreTaxpayer.builder()

					.tinNumber(NikaErpConstants.NIKA_DEFAULT_TIN_NUMBER)
					.taxpayerName(NikaErpConstants.NIKA_DEFAULT_TAXPAYER_NAME)
					.taxpayerPhoneNumber(NikaErpConstants.NIKA_DEFAULT_TAXPAYER_PHONE_NUMBER)
					.taxpayerEmail(NikaErpConstants.NIKA_DEFAULT_TAXPAYER_EMAIL)
					.taxpayerAddress(NikaErpConstants.NIKA_DEFAULT_TAXPAYER_ADDRESS)
					.allowedToExport(NikaErpConstants.NIKA_DEFAULT_ALLOWED_TO_EXPORT)
					.communalTaxRegistered(NikaErpConstants.NIKA_DEFAULT_COMMUNAL_TAX_REGISTERED)
					.deboursRegistered(NikaErpConstants.NIKA_DEFAULT_DEBOURS_REGISTERED)
					.vatRegistered(NikaErpConstants.NIKA_DEFAULT_VAT_REGISTERED).build());

			coreTaxpayerBranchService.saveNew(CoreTaxpayerBranch.builder().taxpayer(coreTaxpayer)
					.branchCode(NikaErpConstants.NIKA_DEFAULT_TAXPAYER_BRANCH_NAME)
					.branchName(NikaErpConstants.NIKA_DEFAULT_TAXPAYER_BRANCH_NAME).build());
		}

	}

	public CoreTaxpayer saveNew(CoreTaxpayer coreTaxpayer) {
		coreTaxpayer.setInternalCode(sequenceNumberService.getNextTaxpayerCode());
		return coreTaxpayerRepository.save(coreTaxpayer);

	}

	public CoreTaxpayer update(CoreTaxpayer coreTaxpayer) {

		return coreTaxpayerRepository.save(coreTaxpayer);

	}

	public List<CoreTaxpayer> findAll() {

		return coreTaxpayerRepository.findAll();

	}
}
