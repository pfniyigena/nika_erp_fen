package com.nika.erp.invoicing.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.invoicing.domain.InvoiceTaxSummary;

 

public interface InvoiceTaxSummaryRepository extends JpaRepository<InvoiceTaxSummary, UUID>{

	List<InvoiceTaxSummary>  findByInvoiceIdOrderByTaxDisplayLevelAsc(UUID id);
}
