package com.nika.erp.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.common.util.NikaErpConstants;
import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.core.domain.CoreItemNature;
import com.nika.erp.core.domain.CoreTaxpayer;
import com.nika.erp.core.domain.CoreTaxpayerBranch;
import com.nika.erp.core.domain.EItemNature;
import com.nika.erp.core.repository.CoreTaxpayerRepository;
import com.nika.erp.invoicing.service.TaxTypeService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreTaxpayerService {

	private final CoreTaxpayerRepository coreTaxpayerRepository;
	private final SequenceNumberService sequenceNumberService;
	private final CoreTaxpayerBranchService coreTaxpayerBranchService;
	private final CoreItemService coreItemService;
	private final CoreItemNatureService coreItemNatureService;
	private final TaxTypeService taxTypeService;

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

			List<CoreItemNature> natures = new ArrayList<>();
			for (EItemNature nature : EItemNature.values()) {
				natures.add(CoreItemNature.builder().code(nature.name()).name(nature.name()).build());

			}

			List<CoreItemNature> natureList = coreItemNatureService.saveAll(natures);
			coreItemService.saveNew(CoreItem.builder().taxpayer(coreTaxpayer)
					.itemName(NikaErpConstants.NIKA_DEFAULT_ITEM_NAME).nature(natureList.get(0)).build());

		}

	}

	public CoreTaxpayer saveNew(CoreTaxpayer coreTaxpayer) {
		taxTypeService.initializeTaxes(coreTaxpayer.getFiscalYear());
		coreTaxpayer.setInternalCode(sequenceNumberService.getNextTaxpayerCode());
		return coreTaxpayerRepository.save(coreTaxpayer);

	}

	public CoreTaxpayer update(CoreTaxpayer coreTaxpayer) {
		taxTypeService.initializeTaxes(coreTaxpayer.getFiscalYear());
		return coreTaxpayerRepository.save(coreTaxpayer);

	}

	public List<CoreTaxpayer> findAll() {

		return coreTaxpayerRepository.findAll();

	}

	public CoreTaxpayer findById(String id) {
		return coreTaxpayerRepository.getReferenceById(UUID.fromString(id));
	}

	public void save(CoreTaxpayer taxpayer) {

		if (taxpayer.getId() != null) {

			CoreTaxpayer exist = coreTaxpayerRepository.getReferenceById(taxpayer.getId());
			exist.setTaxpayerName(taxpayer.getTaxpayerName());
			exist.setTinNumber(taxpayer.getTinNumber());
			exist.setTaxpayerEmail(taxpayer.getTaxpayerEmail());
			exist.setTaxpayerPhoneNumber(taxpayer.getTaxpayerPhoneNumber());
			exist.setTaxpayerAddress(taxpayer.getTaxpayerAddress());
			exist.setFiscalYear(taxpayer.getFiscalYear());
			coreTaxpayerRepository.save(exist);
			taxTypeService.initializeTaxes(exist.getFiscalYear());
		}

	}

}
