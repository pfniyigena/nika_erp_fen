package com.nika.erp.inventory.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.inventory.domain.ReceivedGood;

public interface ReceivedGoodRepository extends JpaRepository<ReceivedGood, UUID> {}
