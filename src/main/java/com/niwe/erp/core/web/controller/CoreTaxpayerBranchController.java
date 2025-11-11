package com.niwe.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.common.service.SequenceNumberService;
import com.niwe.erp.core.domain.CoreTaxpayer;
import com.niwe.erp.core.domain.CoreTaxpayerBranch;
import com.niwe.erp.core.service.CoreTaxpayerBranchService;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.TAXPAYER_BRANCHES_URL)
@AllArgsConstructor
public class CoreTaxpayerBranchController {

	private final CoreTaxpayerService coreTaxpayerService;
	private final CoreTaxpayerBranchService coreTaxpayerBranchService;
	private final SequenceNumberService sequenceNumberService;

	@GetMapping(path = "/list")
	public String listCompanies(Model model) {

		List<CoreTaxpayerBranch> list = coreTaxpayerBranchService.findAll();
		log.debug("--------------Calling listCompanies-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.TAXPAYER_BRANCHES_LIST_URL;
	}
	@GetMapping(path = "/new")
	public String newTaxpayerBranch(Model model) {
		model.addAttribute("branch",
				CoreTaxpayerBranch.builder().internalCode(sequenceNumberService.getNextTaxpayerBranchCode()).build());
		setData(model);

		return NiweErpCoreUrlConstants.TAXPAYER_BRANCHES_ADD_FORM_PAGE;
	}
	@PostMapping(path = "/new")
	public String saveTaxpayerBranch(CoreTaxpayerBranch branch, RedirectAttributes redirectAttrs,
			BindingResult bindingResult, Model model) {

		log.debug(String.format("------calling saveTaxpayerBranch:{%s}", branch));
		coreTaxpayerBranchService.save(branch);
		return NiweErpCoreUrlConstants.TAXPAYER_BRANCHES_LIST_REDITECT_URL;
	}
	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreTaxpayerBranch branch = coreTaxpayerBranchService.findById(id);

		model.addAttribute("branch", branch);
		setData(model);
		return NiweErpCoreUrlConstants.TAXPAYER_BRANCHES_ADD_FORM_PAGE;
	}

	private void setData(Model model) {
		List<CoreTaxpayer> taxpayers = coreTaxpayerService.findAll();
		model.addAttribute("taxpayers", taxpayers);

	}
}
