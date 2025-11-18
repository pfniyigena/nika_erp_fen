package com.niwe.erp.inventory.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.niwe.erp.inventory.domain.ReceivedGood;

public interface ReceivedGoodRepository extends JpaRepository<ReceivedGood, UUID> {
	
}
