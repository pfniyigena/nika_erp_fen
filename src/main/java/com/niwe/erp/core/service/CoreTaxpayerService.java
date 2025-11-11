package com.niwe.erp.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.common.util.NiweErpConstants;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.domain.CoreItemNature;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.domain.CoreTaxpayerBranch;
import com.niwe.erp.core.domain.EItemNature;
import com.niwe.erp.core.repository.CoreTaxpayerBranchRepository;
import com.niwe.erp.core.repository.CoreTaxpayerRepository;
import com.niwe.erp.inventory.service.WarehouseStockService;
import com.niwe.erp.invoicing.service.TaxTypeService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreTaxpayerService {

	private final CoreTaxpayerRepository coreTaxpayerRepository;
	private final SequenceNumberService sequenceNumberService;
	private final CoreTaxpayerBranchRepository coreTaxpayerBranchRepository;
	private final CoreItemService coreItemService;
	private final CoreItemNatureService coreItemNatureService;
	private final TaxTypeService taxTypeService;
	private final WarehouseStockService stockService;

	public CoreTaxpayer initTaxpayer() {
		CoreTaxpayer coreTaxpayer = null;
		if (coreTaxpayerRepository.findAll().size() < 1) {

			coreTaxpayer = save(CoreTaxpayer.builder()

					.tinNumber(NiweErpConstants.NIKA_DEFAULT_TIN_NUMBER)
					.taxpayerName(NiweErpConstants.NIKA_DEFAULT_TAXPAYER_NAME)
					.taxpayerPhoneNumber(NiweErpConstants.NIKA_DEFAULT_TAXPAYER_PHONE_NUMBER)
					.taxpayerEmail(NiweErpConstants.NIKA_DEFAULT_TAXPAYER_EMAIL)
					.taxpayerAddress(NiweErpConstants.NIKA_DEFAULT_TAXPAYER_ADDRESS)
					.allowedToExport(NiweErpConstants.NIKA_DEFAULT_ALLOWED_TO_EXPORT)
					.communalTaxRegistered(NiweErpConstants.NIKA_DEFAULT_COMMUNAL_TAX_REGISTERED)
					.deboursRegistered(NiweErpConstants.NIKA_DEFAULT_DEBOURS_REGISTERED)
					.vatRegistered(NiweErpConstants.NIKA_DEFAULT_VAT_REGISTERED).build());

			coreTaxpayerBranchRepository.save(CoreTaxpayerBranch.builder().taxpayer(coreTaxpayer)
					.internalCode(sequenceNumberService.getNextTaxpayerBranchCode())
					.branchName(NiweErpConstants.NIKA_DEFAULT_TAXPAYER_BRANCH_NAME).build());

			List<CoreItemNature> natures = new ArrayList<>();
			for (EItemNature nature : EItemNature.values()) {
				natures.add(CoreItemNature.builder().code(nature.name()).name(nature.name()).build());

			}

			List<CoreItemNature> natureList = coreItemNatureService.saveAll(natures);
			coreItemService.saveNew(CoreItem.builder().taxpayer(coreTaxpayer)
					.itemName(NiweErpConstants.NIKA_DEFAULT_ITEM_NAME).nature(natureList.get(0)).build());

		}
		return coreTaxpayer;
	}

	public CoreTaxpayer save(CoreTaxpayer taxpayer) {
		CoreTaxpayer savedTaxpayer = null;
		String code = taxpayer.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextTaxpayerCode();
		}

		if (taxpayer.getId() != null) {
			CoreTaxpayer exist = coreTaxpayerRepository.getReferenceById(taxpayer.getId());
			exist.setTaxpayerName(taxpayer.getTaxpayerName());
			exist.setTinNumber(taxpayer.getTinNumber());
			exist.setTaxpayerEmail(taxpayer.getTaxpayerEmail());
			exist.setTaxpayerPhoneNumber(taxpayer.getTaxpayerPhoneNumber());
			exist.setTaxpayerAddress(taxpayer.getTaxpayerAddress());
			exist.setFiscalYear(taxpayer.getFiscalYear());
			exist.setInternalCode(code);
			exist.setUseInventory(taxpayer.getUseInventory());
			savedTaxpayer = coreTaxpayerRepository.save(exist);

		} else {
			taxpayer.setInternalCode(code);
			savedTaxpayer = coreTaxpayerRepository.save(taxpayer);
		}
		taxTypeService.initializeTaxes(savedTaxpayer.getFiscalYear());
		if (savedTaxpayer.getUseInventory()) {
			stockService.initiateInventory(savedTaxpayer);
		}
		return taxpayer;

	}

	public List<CoreTaxpayer> findAll() {

		return coreTaxpayerRepository.findAll();

	}

	public CoreTaxpayer findById(String id) {
		return coreTaxpayerRepository.getReferenceById(UUID.fromString(id));
	}

}
