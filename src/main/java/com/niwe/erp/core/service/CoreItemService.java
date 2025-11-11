package com.niwe.erp.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.niwe.erp.common.exception.ResourceNotFoundException;
import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.domain.CoreItemNature;
import com.niwe.erp.core.domain.EItemNature;
import com.niwe.erp.core.repository.CoreItemRepository;
import com.niwe.erp.invoicing.web.form.ItemForm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreItemService {

	private final CoreItemRepository coreItemRepository;
	private final SequenceNumberService sequenceNumberService;
	private final CoreItemNatureService coreItemNatureService;

	List<ItemForm> findAllAsForm() {
		return coreItemRepository.findAllAsForm();
	}

	public List<ItemForm> findAllAsFormByItemNameContainingIgnoreCase(String itemName) {
		return coreItemRepository.findAllAsFormByItemNameContainingIgnoreCase(itemName);
	}

	public CoreItem saveNew(CoreItem coreItem) {
		String code = sequenceNumberService.getNextItemCode();
		coreItem.setInternalCode(code);
		if (coreItem.getBarcode() == null || coreItem.getBarcode().isEmpty())
			coreItem.setBarcode(code);
		if (coreItem.getExternalItemCode() == null || coreItem.getExternalItemCode().isEmpty())
			coreItem.setExternalItemCode(code);
		if (coreItem.getItemCode() == null || coreItem.getItemCode().isEmpty())
			coreItem.setItemCode(code);
		return coreItemRepository.save(coreItem);

	}

	public List<CoreItem> findAll() {

		return coreItemRepository.findAll();
	}

	public CoreItem save(CoreItem item) {
		if (item.getId() != null) {
			CoreItem exist = coreItemRepository.getReferenceById(item.getId());
			exist.setItemName(item.getItemName());
			exist.setItemCode(item.getItemCode());
			exist.setUnitPrice(item.getUnitPrice());
			exist.setUnitCost(item.getUnitCost());
			exist.setCountry(item.getCountry());
			exist.setClassification(item.getClassification());
			exist.setUnit(item.getUnit());
			exist.setNature(item.getNature());
			exist.setTax(item.getTax());
			exist.setTaxpayer(item.getTaxpayer());
			return coreItemRepository.save(exist);
		}
		return coreItemRepository.save(item);

	}

	public CoreItem findById(String id) {
		return coreItemRepository.getReferenceById(UUID.fromString(id));
	}

	public CoreItem findByInternalCode(String internalCode) {
		return coreItemRepository.findByInternalCode(internalCode).orElseThrow(
				() -> new ResourceNotFoundException("Product not found with internalCode: " + internalCode));
		 
	}

	public void initItems() {

		if (coreItemNatureService.findAll().isEmpty()) {
			List<CoreItemNature> natures = new ArrayList<>();
			for (EItemNature nature : EItemNature.values()) {
				natures.add(CoreItemNature.builder().code(nature.name()).name(nature.name()).build());

			}
			coreItemNatureService.saveAll(natures);
		}

	}

	public CoreItem duplicate(String id) {

		CoreItem original = findById(id);

		CoreItem copy = new CoreItem(original);
		log.info("original:{} and Copy:{}", original, copy);

		return saveNew(copy);
	}

}
