package com.nika.erp.inventory.repository;
 

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.inventory.domain.Warehouse;

import java.util.List;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {
    List<Warehouse> findByBranchId(UUID branchId);
}

