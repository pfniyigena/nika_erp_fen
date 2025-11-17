package com.niwe.erp.sale.repository;
 

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.sale.domain.Sale;


public interface SaleRepository extends JpaRepository<Sale, UUID> {
	
	Optional<Sale> findByExternalCode(String externalCode);

	List<Sale> findBySummaryIdOrderBySaleDateDesc(UUID summaryId);
	List<Sale> findBySaleDateBetween(Instant start, Instant end);
    
}

