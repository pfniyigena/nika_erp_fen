package com.nika.erp.sale.repository;
 

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.sale.domain.Shelf;


public interface ShelfRepository extends JpaRepository<Shelf, UUID> {
    
}

