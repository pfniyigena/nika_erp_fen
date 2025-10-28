package com.nika.erp.core.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.core.domain.CoreCountry;
import com.nika.erp.core.repository.CoreCountryRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CoreCountryService {
	
	private final CoreCountryRepository coreCountryRepository;

	public List<CoreCountry> findAll() {
		return coreCountryRepository.findAll();
	}

	public CoreCountry findById(String id) {
		return coreCountryRepository.getReferenceById(UUID.fromString(id));
	}

	public CoreCountry save(CoreCountry country) {
		if(country.getId()!=null) {
			CoreCountry  exist=coreCountryRepository.getReferenceById(country.getId());
			exist.setCode(country.getCode());
			exist.setDisplayName(country.getDisplayName());
			exist.setEnglishName(country.getEnglishName());
			exist.setFrenchName(country.getFrenchName());
			return coreCountryRepository.save(exist);
		}
		
		return coreCountryRepository.save(country);
		
	}

}
