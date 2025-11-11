package com.niwe.erp.sale.repository;
 

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.sale.domain.Sale;


public interface SaleRepository extends JpaRepository<Sale, UUID> {
    
}

