package com.nika.erp.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.core.domain.CoreTaxpayer;


public interface CoreTaxpayerRepository extends JpaRepository<CoreTaxpayer, UUID>{
	CoreTaxpayer  findByTinNumber(String tinNumber);
}
