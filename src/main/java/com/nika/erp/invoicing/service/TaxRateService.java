package com.nika.erp.invoicing.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.invoicing.domain.TaxRate;
import com.nika.erp.invoicing.repository.TaxRateRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class TaxRateService {
	
	private TaxRateRepository taxRateRepository;

	public List<TaxRate> findAll() {
		return taxRateRepository.findAll();
	}

	public TaxRate save(TaxRate taxRate) {
		
		if(taxRate.getId()!=null) {
			TaxRate  exist=taxRateRepository.getReferenceById(taxRate.getId());
			exist.setTaxName(taxRate.getTaxName());
			exist.setCode(taxRate.getCode());
			exist.setTaxValue(taxRate.getTaxValue());
			exist.setDescription(taxRate.getDescription());
			exist.setDisplayName(taxRate.getDisplayName());
			return taxRateRepository.save(exist);
		}
		
		return taxRateRepository.save(taxRate);
		
	}

	public TaxRate findById(String id) {
		 
		return taxRateRepository.getReferenceById(UUID.fromString(id));
	}
	
	

}
