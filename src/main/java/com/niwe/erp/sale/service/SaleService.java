package com.niwe.erp.sale.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.niwe.erp.sale.domain.DailySalesSummary;
import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

	private final SaleRepository saleRepository;

	public List<Sale> findAll() {

		return saleRepository.findAll(Sort.by(Sort.Direction.DESC, "saleDate")
		        .and(Sort.by(Sort.Direction.DESC, "totalAmountToPay")));
	}

	public List<Sale> findByDailySalesSummary(DailySalesSummary summary) {
		return saleRepository.findBySummaryIdOrderBySaleDateDesc(summary.getId());
	}
	 public List<Sale> getSalesByMonth(YearMonth yearMonth) {
	        // Get start and end of the month
	        LocalDate startDate = yearMonth.atDay(1);
	        LocalDate endDate = yearMonth.atEndOfMonth();

	        Instant startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
	        Instant endInstant = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

	        return saleRepository.findBySaleDateBetween(startInstant, endInstant);
	    }
}
