package com.nika.erp.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.core.domain.CoreItemNature;
public interface CoreItemNatureRepository  extends JpaRepository<CoreItemNature, UUID>{

}
