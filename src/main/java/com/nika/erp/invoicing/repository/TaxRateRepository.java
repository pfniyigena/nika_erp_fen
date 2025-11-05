package com.nika.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.invoicing.domain.TaxRate;

public interface TaxRateRepository  extends JpaRepository<TaxRate, UUID> {
	
	TaxRate  findByCode(String code);

}
