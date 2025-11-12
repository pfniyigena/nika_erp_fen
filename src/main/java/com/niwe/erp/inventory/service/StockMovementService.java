package com.niwe.erp.inventory.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.inventory.domain.EStockOperation;
import com.niwe.erp.inventory.domain.StockMovement;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.repository.StockMovementRepository;
import com.niwe.erp.inventory.web.dto.WarehouseStockSummaryDto;

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

	public Map<Warehouse, List<StockMovement>> getStockMovementsGroupedByWarehouse(Instant start, Instant end) {
		List<StockMovement> movements = stockMovementRepository.findByMovementDateBetween(start, end);
		return movements.stream().collect(Collectors.groupingBy(StockMovement::getWarehouse));
	}
	
	public Map<Warehouse, WarehouseStockSummaryDto> getWarehouseStockSummary(Instant start, Instant end) {
        List<StockMovement> movements = stockMovementRepository.findByMovementDateBetween(start, end);

        Map<Warehouse, List<StockMovement>> grouped = movements.stream()
                .collect(Collectors.groupingBy(StockMovement::getWarehouse));

        Map<Warehouse, WarehouseStockSummaryDto> summaryMap = new LinkedHashMap<>();

        grouped.forEach((warehouse, movementList) -> {

            BigDecimal totalIn = movementList.stream()
                    .filter(m -> m.getStockOperation() == EStockOperation.IN)
                    .map(StockMovement::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalOut = movementList.stream()
                    .filter(m -> m.getStockOperation() == EStockOperation.OUT)
                    .map(StockMovement::getQuantity)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal balance = totalIn.subtract(totalOut);

            // Calculate total value = Σ(balance per product × unitPrice)
            Map<CoreItem, BigDecimal> productBalances = new HashMap<>();
            movementList.forEach(m -> {
                BigDecimal current = productBalances.getOrDefault(m.getItem(), BigDecimal.ZERO);
                BigDecimal qty = m.getStockOperation() == EStockOperation.IN
                        ? current.add(m.getQuantity())
                        : current.subtract(m.getQuantity());
                productBalances.put(m.getItem(), qty);
            });

            BigDecimal totalValue = productBalances.entrySet().stream()
                    .map(e -> e.getKey().getUnitPrice().multiply(e.getValue()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            summaryMap.put(warehouse,
                    new WarehouseStockSummaryDto(warehouse, totalIn, totalOut, totalValue,balance));
        });

        return summaryMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().getTotalValue().compareTo(a.getValue().getTotalValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldV, newV) -> oldV,
                        LinkedHashMap::new
                ));
    }
}
