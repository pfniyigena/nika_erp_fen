package com.nika.erp.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.core.domain.CoreTaxpayerBranch;


public interface CoreTaxpayerBranchRepository extends JpaRepository<CoreTaxpayerBranch, UUID>{
	
	
	CoreTaxpayerBranch  findByBranchCode(String branchCode);

}
