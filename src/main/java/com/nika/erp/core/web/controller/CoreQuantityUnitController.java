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

import com.nika.erp.core.domain.CoreQuantityUnit;
import com.nika.erp.core.service.CoreQuantityUnitService;
import com.nika.erp.core.web.util.NikaErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping(value = NikaErpCoreUrlConstants.QUANTITYUNITS_URL)
@AllArgsConstructor
public class CoreQuantityUnitController {

	private final CoreQuantityUnitService coreQuantityUnitService;

	@GetMapping(path = "/list")
	public String listUnits(Model model) {

		List<CoreQuantityUnit> list = coreQuantityUnitService.findAll();
		log.debug("--------------Calling listUnits-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpCoreUrlConstants.QUANTITYUNITS_LIST_PAGE;
	}
	@GetMapping(path = "/new")
	public String newCountry(Model model) {
		 model.addAttribute("quantityUnit", CoreQuantityUnit.builder().build());
		return NikaErpCoreUrlConstants.QUANTITYUNITS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveUnit(CoreQuantityUnit quantityUnit, RedirectAttributes redirectAttrs, BindingResult bindingResult,
			Model model) {

		log.debug(String.format("------calling saveUnit:{%s}", quantityUnit));
		coreQuantityUnitService.save(quantityUnit);
		return NikaErpCoreUrlConstants.QUANTITYUNITS_LIST_REDITECT_URL;
	}
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreQuantityUnit quantityUnit = coreQuantityUnitService.findById(id);

		model.addAttribute("quantityUnit", quantityUnit);

		return NikaErpCoreUrlConstants.QUANTITYUNITS_ADD_FORM_PAGE;
	}

}
