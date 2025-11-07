package com.nika.erp.inventory.repository;
 

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.inventory.domain.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, UUID> {
    
}

