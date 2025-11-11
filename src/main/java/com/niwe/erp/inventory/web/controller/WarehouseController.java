package com.niwe.erp.inventory.web.controller;

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
import com.niwe.erp.core.domain.CoreTaxpayerBranch;
import com.niwe.erp.core.service.CoreTaxpayerBranchService;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.WAREHOUSES_URL)
@AllArgsConstructor
public class WarehouseController {

	private final WarehouseService warehouseService;
	private final CoreTaxpayerBranchService coreTaxpayerBranchService;
	private final SequenceNumberService sequenceNumberService;

	@GetMapping(path = "/list")
	public String listWarehouses(Model model) {

		List<Warehouse> list = warehouseService.findAll();
		log.debug("--------------Calling listWarehouses-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSES_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String warehouseForm(Model model) {
		model.addAttribute("warehouse",
				Warehouse.builder().internalCode(sequenceNumberService.getNextWarehouseCode()).build());
		setData(model);

		return NikaErpInventoryUrlConstants.WAREHOUSES_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveWarehouse(Warehouse warehouse, RedirectAttributes redirectAttrs, BindingResult bindingResult,
			Model model) {

		log.debug(String.format("------calling saveWarehouse:{%s}", warehouse));
		warehouseService.save(warehouse);
		return NikaErpInventoryUrlConstants.WAREHOUSES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		Warehouse warehouse = warehouseService.findById(id);
		model.addAttribute("warehouse", warehouse);
		setData(model);
		return NikaErpInventoryUrlConstants.WAREHOUSES_ADD_FORM_PAGE;
	}

	private void setData(Model model) {
		List<CoreTaxpayerBranch> branches = coreTaxpayerBranchService.findAll();
		model.addAttribute("branches", branches);

	}

}
