package com.niwe.erp.inventory.repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.inventory.domain.StockMovement;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
	
	List<StockMovement> findByMovementDateBetween(Instant start, Instant end);
}
