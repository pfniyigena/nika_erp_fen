package com.niwe.erp.inventory.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.inventory.domain.WarehouseStock;
import com.niwe.erp.inventory.service.WarehouseStockService;
import com.niwe.erp.inventory.web.dto.ProductStockSummaryDto;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_URL)
@AllArgsConstructor
public class WarehouseStockController {

	private final WarehouseStockService warehouseStockService;

	@GetMapping(path = "/list")
	public String listWarehouseStocks(Model model) {

		List<WarehouseStock> list = warehouseStockService.findAll();
		log.debug("--------------Calling listWarehouseStocks-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_LIST_PAGE;
	}

	@GetMapping(path = "/summary")
	public String listWarehouseStockByProduct(Model model) {

		List<ProductStockSummaryDto> list = warehouseStockService.getStockSummary();
		log.debug("--------------Calling listWarehouseStockByProduct-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_SUMMARY_PAGE;
	}

	@GetMapping("/details/{id}")
	public String viewWarehouseStockByProduct(@PathVariable String id, Model model) {

		List<WarehouseStock> details = warehouseStockService.getStockByProduct(UUID.fromString(id));
		log.info("viewWarehouseStockByProduct details:{}", details);
		model.addAttribute("details", details);
		return NikaErpInventoryUrlConstants.WAREHOUSE_STOCKS_VIEW_FORM;
	}

}
