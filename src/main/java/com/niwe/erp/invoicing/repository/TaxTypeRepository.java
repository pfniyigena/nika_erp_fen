package com.niwe.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.invoicing.domain.TaxType;

public interface TaxTypeRepository  extends JpaRepository<TaxType, UUID> {
	
	TaxType  findByTaxCode(String taxCode);

}
