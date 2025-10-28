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

import com.nika.erp.core.domain.CoreCountry;
import com.nika.erp.core.service.CoreCountryService;
import com.nika.erp.core.web.util.NikaErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping(value = NikaErpCoreUrlConstants.COUNTRIES_URL)
@AllArgsConstructor
public class CoreCountryController {

	private final CoreCountryService coreCountryService;

	@GetMapping(path = "/list")
	public String listCountries(Model model) {

		List<CoreCountry> list = coreCountryService.findAll();
		log.debug("--------------Calling listCountries-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpCoreUrlConstants.COUNTRIES_LIST_PAGE;
	}
	@GetMapping(path = "/new")
	public String newCountry(Model model) {
		 model.addAttribute("country", CoreCountry.builder().build());
		return NikaErpCoreUrlConstants.COUNTRIES_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveCountry(CoreCountry country, RedirectAttributes redirectAttrs, BindingResult bindingResult,
			Model model) {

		log.debug(String.format("------calling saveCountry:{%s}", country));
		coreCountryService.save(country);
		return NikaErpCoreUrlConstants.COUNTRIES_LIST_REDITECT_URL;
	}
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreCountry country = coreCountryService.findById(id);

		model.addAttribute("country", country);

		return NikaErpCoreUrlConstants.COUNTRIES_ADD_FORM_PAGE;
	}

}
