package com.nika.erp.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nika.erp.inventory.domain.StockMovement;
import com.nika.erp.inventory.repository.StockMovementRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class StockMovementService {

	private final StockMovementRepository stockMovementRepository;

	public List<StockMovement> findAll() {
		return stockMovementRepository.findAll();

	}

}
