package com.niwe.erp.sale.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.sale.domain.Sale;
import com.niwe.erp.sale.service.SaleService;
import com.niwe.erp.sale.web.util.NiweErpSaleUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.SALES_URL)
@AllArgsConstructor
@Slf4j
public class SaleController {
	private final SaleService saleService;


	@GetMapping(path = "/list")
	public String listSales(Model model) {

		List<Sale> list = saleService.findAll();
		log.debug("--------------Calling listSales-------------------:{}", list.size());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.SALES_LIST_PAGE;
	}
	
}
