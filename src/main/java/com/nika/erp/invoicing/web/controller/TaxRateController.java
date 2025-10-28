package com.nika.erp.invoicing.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nika.erp.invoicing.domain.TaxRate;
import com.nika.erp.invoicing.service.TaxRateService;
import com.nika.erp.invoicing.web.util.NikaErpInvoicingUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping(value = NikaErpInvoicingUrlConstants.TAXARATES_URL)
@AllArgsConstructor
public class TaxRateController {

	private final TaxRateService taxRateService;

	@GetMapping(path = "/list")
	public String listTaxRates(Model model) {

		List<TaxRate> list = taxRateService.findAll();
		log.debug("--------------Calling listTaxRates-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInvoicingUrlConstants.TAXARATES_LIST_PAGE;
	}
	@GetMapping(path = "/new")
	public String newTaxRate(Model model) {
		 model.addAttribute("taxRate", TaxRate.builder().build());
		return NikaErpInvoicingUrlConstants.TAXRATE_ADD_FORM;
	}

	@PostMapping(path = "/new")
	public String saveTaxRate(TaxRate taxRate, RedirectAttributes redirectAttrs, BindingResult bindingResult,
			Model model) {

		log.debug(String.format("------calling saveTaxRate:{%s}", taxRate));
		taxRateService.save(taxRate);
		return NikaErpInvoicingUrlConstants.TAXRATES_LIST_REDITECT_URL;
	}
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		TaxRate taxRate = taxRateService.findById(id);

		model.addAttribute("taxRate", taxRate);

		return NikaErpInvoicingUrlConstants.TAXRATE_ADD_FORM;
	}

}
