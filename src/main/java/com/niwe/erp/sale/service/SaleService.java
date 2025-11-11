package com.niwe.erp.sale.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.repository.SaleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleService {

	private final SaleRepository saleRepository;

	public List<Sale> findAll() {

		return saleRepository.findAll();
	}

}
