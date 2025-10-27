package com.nika.erp.core.service;

import java.util.List;

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
	
	

}
