package com.nika.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nika.erp.core.domain.CoreTaxpayer;
import com.nika.erp.core.service.CoreTaxpayerService;
import com.nika.erp.core.web.util.NikaErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping(value = NikaErpCoreUrlConstants.TAXPAYERS_URL)
@AllArgsConstructor
public class CoreTaxpayerController {

	private final CoreTaxpayerService coreTaxpayerService;

	@GetMapping(path = "/list")
	public String listCompanies(Model model) {

		List<CoreTaxpayer> list = coreTaxpayerService.findAll();
		log.debug("--------------Calling listCompanies-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpCoreUrlConstants.TAXPAYERS_LIST_PAGE;
	}
}
