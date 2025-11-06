package com.nika.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.invoicing.domain.ChargeType;

public interface ChargeTypeRepository  extends JpaRepository<ChargeType, UUID> {
	
	ChargeType  findByChargeCode(String chargeCode);

}
