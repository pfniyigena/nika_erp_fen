package com.niwe.erp.inventory.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.inventory.domain.StockTransfer;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.StockTransferService;
import com.niwe.erp.inventory.service.WarehouseService;
import com.niwe.erp.inventory.web.form.StockTransferForm;
import com.niwe.erp.inventory.web.form.StockTransferLineForm;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.STOCK_TRANSFERS_URL)
@AllArgsConstructor
public class StockTransferController {
	private final StockTransferService stockTransferService;
	private final WarehouseService warehouseService;

	@GetMapping(path = "/list")
	public String listStockTransfers(Model model) {

		List<StockTransfer> list = stockTransferService.findAll();
		log.debug("--------------Calling listStockTransfers-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.STOCK_TRANSFERS_LIST_PAGE;
	}
	@GetMapping("/new/{fromWarehouseId}")
	public String newStockTransfer(@PathVariable String fromWarehouseId, Model model) {
		Warehouse fromWarehouse = warehouseService.findById(fromWarehouseId);
		StockTransferForm stockTransferForm = StockTransferForm.builder().fromWarehouseId(fromWarehouseId)
				.fromWarehouseCode(fromWarehouse.getInternalCode()).fromWarehouseName(fromWarehouse.getWarehouseName())
				.build();
		stockTransferForm.setLines(List.of(new StockTransferLineForm()));
		model.addAttribute("stockTransferForm", stockTransferForm);
		setData(model,UUID.fromString(fromWarehouseId));
		return NikaErpInventoryUrlConstants.STOCK_TRANSFERS_ADD_FORM;
	}
	@PostMapping("/new")
	public String makeStockTransfer(@ModelAttribute StockTransferForm stockTransferForm , Model model) {
		stockTransferService.makeStockTransfer(stockTransferForm);
		 return NikaErpInventoryUrlConstants.STOCK_TRANSFERS_LIST_REDITECT_URL;
	}
	private void setData(Model model, UUID fromWarehouseId) {
		List<Warehouse> warehouses = warehouseService.findByIdNotIn(List.of(fromWarehouseId));
		model.addAttribute("warehouses", warehouses);
	}
}
