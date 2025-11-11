package com.niwe.erp.inventory.repository;
 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.domain.WarehouseStock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, UUID> {
    Optional<WarehouseStock> findByWarehouseAndItem(Warehouse warehouse, CoreItem item);

    @Query("SELECT s FROM WarehouseStock s WHERE s.item.id = :itemId")
    List<WarehouseStock> findByItemId(@Param("itemId") UUID itemId);
}

