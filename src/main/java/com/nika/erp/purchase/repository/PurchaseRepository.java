package com.nika.erp.purchase.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.purchase.domain.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
}
