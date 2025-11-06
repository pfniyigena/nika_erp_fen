package com.nika.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nika.erp.core.domain.CoreTaxpayer;
import com.nika.erp.core.domain.EFiscalYear;
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
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreTaxpayer taxpayer = coreTaxpayerService.findById(id);

		model.addAttribute("taxpayer", taxpayer);
		setData(model); 
		return NikaErpCoreUrlConstants.TAXPAYERS_ADD_FORM_PAGE;
	}
	
	@PostMapping(path = "/new")
	public String saveTaxpayer(CoreTaxpayer taxpayer, RedirectAttributes redirectAttrs,
			BindingResult bindingResult, Model model) {

		log.debug(String.format("------calling saveTaxpayer:{%s}", taxpayer));
		coreTaxpayerService.save(taxpayer);
		return NikaErpCoreUrlConstants.TAXPAYER_LIST_REDITECT_URL;
	}
	private void setData(Model model) {
		model.addAttribute("fiscalYears", EFiscalYear.values());
		
	}
	
	 
}
