package com.nika.erp.ebm.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.ebm.domain.SdcInformation;

public interface SdcInformationRepository extends JpaRepository<SdcInformation, UUID> {

	SdcInformation findBySdcNumber(String sdcNumber);

}
