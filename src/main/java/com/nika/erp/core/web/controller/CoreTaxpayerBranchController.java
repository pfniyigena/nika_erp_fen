package com.nika.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nika.erp.core.domain.CoreTaxpayerBranch;
import com.nika.erp.core.service.CoreTaxpayerBranchService;
import com.nika.erp.core.web.util.NikaErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping(value = NikaErpCoreUrlConstants.TAXPAYER_BRANCHES_URL)
@AllArgsConstructor
public class CoreTaxpayerBranchController {

	private final CoreTaxpayerBranchService coreTaxpayerBranchService;

	@GetMapping(path = "/list")
	public String listCompanies(Model model) {

		List<CoreTaxpayerBranch> list = coreTaxpayerBranchService.findAll();
		log.debug("--------------Calling listCompanies-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpCoreUrlConstants.TAXPAYER_BRANCHES_LIST_URL;
	}
}
