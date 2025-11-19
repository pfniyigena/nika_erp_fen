package com.niwe.erp.sale.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.sale.domain.Customer;
import com.niwe.erp.sale.service.CustomerService;
import com.niwe.erp.sale.web.util.NiweErpSaleUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.CUSTOMERS_URL)
@AllArgsConstructor
@Slf4j
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping(path = "/list")
	public String listCustomers(Model model) {

		List<Customer> list = customerService.findAll();
		log.debug("--------------Calling listCustomers-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.CUSTOMERS_LIST_PAGE;
	}
}
