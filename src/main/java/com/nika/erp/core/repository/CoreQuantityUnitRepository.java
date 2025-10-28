package com.nika.erp.core.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nika.erp.core.domain.CoreQuantityUnit;

public interface CoreQuantityUnitRepository  extends JpaRepository<CoreQuantityUnit, UUID>{

}
