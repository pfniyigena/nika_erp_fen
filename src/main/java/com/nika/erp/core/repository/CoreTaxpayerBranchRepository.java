package com.nika.erp.core.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.core.domain.CoreTaxpayerBranch;


public interface CoreTaxpayerBranchRepository extends JpaRepository<CoreTaxpayerBranch, UUID>{
	
	
	CoreTaxpayerBranch  findByInternalCode(String internalCode);
	List<CoreTaxpayerBranch> findByTaxpayerId(UUID id);

}
