package com.nika.erp.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.common.domain.ESequenceType;
import com.nika.erp.common.domain.SequenceNumber;

public interface SequenceNumberRepository extends JpaRepository<SequenceNumber, UUID> {

	SequenceNumber findByType(ESequenceType type);

}
