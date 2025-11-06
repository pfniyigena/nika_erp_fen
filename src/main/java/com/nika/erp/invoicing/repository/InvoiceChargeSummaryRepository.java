package com.nika.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.invoicing.domain.InvoiceChargeSummary;


public interface InvoiceChargeSummaryRepository  extends JpaRepository<InvoiceChargeSummary, UUID>{

}
