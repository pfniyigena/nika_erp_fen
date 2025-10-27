package com.nika.erp.ebm.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.ebm.domain.SdcDailyReport;
import com.nika.erp.ebm.domain.SdcInformation;

public interface SdcDailyReportRespository extends JpaRepository<SdcDailyReport, UUID> {

	SdcDailyReport  findBySdcInformationAndReportDate(SdcInformation sdcInformation,LocalDate reportDate);
}
