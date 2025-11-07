package com.nika.erp.inventory.repository;
 

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.core.domain.CoreItem;
import com.nika.erp.inventory.domain.StockItem;
import com.nika.erp.inventory.domain.Warehouse;

import java.util.Optional;
import java.util.UUID;

public interface StockItemRepository extends JpaRepository<StockItem, UUID> {
    Optional<StockItem> findByWarehouseAndItem(Warehouse warehouse, CoreItem item);
}

