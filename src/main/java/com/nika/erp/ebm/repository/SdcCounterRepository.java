package com.nika.erp.ebm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.ebm.domain.SdcCounter;
import com.nika.erp.ebm.domain.SdcInformation;

public interface SdcCounterRepository extends JpaRepository<SdcCounter, UUID> {

	SdcCounter findBySdcInformation(SdcInformation sdcInformation);

}
