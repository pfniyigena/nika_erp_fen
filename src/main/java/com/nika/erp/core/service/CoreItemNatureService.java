package com.nika.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.core.domain.CoreItemNature;
import com.nika.erp.core.repository.CoreItemNatureRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreItemNatureService {
	
	private final CoreItemNatureRepository coreItemNatureRepository;

	public List<CoreItemNature> findAll() {
		return coreItemNatureRepository.findAll();
	}

	public CoreItemNature  findById(String id) {
		return coreItemNatureRepository.getReferenceById(UUID.fromString(id));
	}

	public CoreItemNature save(CoreItemNature coreItemNature) {
		if(coreItemNature.getId()!=null) {
			CoreItemNature  exist=coreItemNatureRepository.getReferenceById(coreItemNature.getId());
			exist.setCode(coreItemNature.getCode());
			exist.setName(coreItemNature.getName());
			return coreItemNatureRepository.save(exist);
		}
		
		return coreItemNatureRepository.save(coreItemNature);
		
	}

}
