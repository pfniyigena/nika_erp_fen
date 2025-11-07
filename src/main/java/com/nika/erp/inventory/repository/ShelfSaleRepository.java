package com.nika.erp.inventory.repository;
 

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.inventory.domain.ShelfSale;

public interface ShelfSaleRepository extends JpaRepository<ShelfSale, UUID> {
    List<ShelfSale> findByBranchId(UUID branchId);
}

