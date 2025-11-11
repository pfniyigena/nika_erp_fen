package com.niwe.erp.inventory.repository;
 

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.inventory.domain.StockTransfer;

public interface StockTransferRepository extends JpaRepository<StockTransfer, UUID> {
    
}

