package com.nika.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.core.repository.CoreItemRepository;
import com.nika.erp.invoicing.web.form.ItemForm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreItemService {

	private final CoreItemRepository coreItemRepository;
	private final SequenceNumberService sequenceNumberService;

	List<ItemForm> findAllAsForm() {
		return coreItemRepository.findAllAsForm();
	}

	public List<ItemForm> findAllAsFormByItemNameContainingIgnoreCase(String itemName) {
		return coreItemRepository.findAllAsFormByItemNameContainingIgnoreCase(itemName);
	}

	public CoreItem saveNew(CoreItem coreItem) {
		coreItem.setInternalCode(sequenceNumberService.getNextItemCode());
		coreItem.setBarcode(sequenceNumberService.getNextItemCode());
		coreItem.setExternalItemCode(sequenceNumberService.getNextItemCode());
		coreItem.setItemCode(sequenceNumberService.getNextItemCode());
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

}
