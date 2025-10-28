package com.nika.erp.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.core.domain.CoreItemClassification;

public interface CoreItemClassificationRepository  extends JpaRepository<CoreItemClassification, UUID>{

}
