package com.nika.erp.inventory.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.inventory.domain.Warehouse;
import com.nika.erp.inventory.repository.WarehouseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class WarehouseService {

	private final WarehouseRepository warehouseRepository;
	private final SequenceNumberService sequenceNumberService;

	public List<Warehouse> findAll() {
		return warehouseRepository.findAll();
	}

	public Warehouse findById(String id) {

		return warehouseRepository.getReferenceById(UUID.fromString(id));
	}

	public Warehouse save(Warehouse warehouse) {
		Warehouse savedWarehouse = null;
		String code = warehouse.getInternalCode();
		if (code == null || code.isEmpty()) {
			code = sequenceNumberService.getNextTaxpayerBranchCode();
		}

		if (warehouse.getId() != null) {

			Warehouse exist = warehouseRepository.getReferenceById(warehouse.getId());
			exist.setInternalCode(code);
			exist.setWarehouseName(warehouse.getWarehouseName());
			exist.setBranch(warehouse.getBranch());
			savedWarehouse = warehouseRepository.save(exist);
		} else {
			warehouse.setInternalCode(code);
			savedWarehouse = warehouseRepository.save(warehouse);
		}

		return savedWarehouse;

	}

 
}
