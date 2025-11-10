package com.nika.erp.sale.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nika.erp.common.service.SequenceNumberService;
import com.nika.erp.inventory.domain.Warehouse;
import com.nika.erp.inventory.service.WarehouseService;
import com.nika.erp.sale.domain.Shelf;
import com.nika.erp.sale.service.ShelfService;
import com.nika.erp.sale.web.form.ShelfForm;
import com.nika.erp.sale.web.form.ShelfLineForm;
import com.nika.erp.sale.web.util.NiweErpSaleUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpSaleUrlConstants.SHELVES_URL)
@AllArgsConstructor
public class ShelfController {
	private final ShelfService shelfService;
	private final WarehouseService warehouseService;
	private final SequenceNumberService sequenceNumberService;

	@GetMapping(path = "/list")
	public String listShelves(Model model) {

		List<Shelf> list = shelfService.findAll();
		log.debug("--------------Calling listShelves-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpSaleUrlConstants.SHELVE_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String warehouseForm(Model model) {
		model.addAttribute("shelf", Shelf.builder().internalCode(sequenceNumberService.getNextShelfCode()).build());
		setData(model);

		return NiweErpSaleUrlConstants.SHELVE_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveShelf(Shelf shelf, RedirectAttributes redirectAttrs, BindingResult bindingResult, Model model) {

		log.debug(String.format("------calling saveShelf:{%s}", shelf));
		shelfService.save(shelf);
		return NiweErpSaleUrlConstants.SHELVES_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/open/{id}")
	public String newInvoice(@PathVariable String id, Model model) {
		Shelf shelf =shelfService.findById(id);
		ShelfForm shelfForm = new ShelfForm();
		shelfForm.setShelfId(shelf.getId().toString());
		shelfForm.setShelfLines(List.of(new ShelfLineForm()));
		model.addAttribute("shelfForm", shelfForm);
		return NiweErpSaleUrlConstants.SHELVE_POS_FORM_PAGE;
	}

	private void setData(Model model) {
		List<Warehouse> warehouses = warehouseService.findAll();
		model.addAttribute("warehouses", warehouses);

	}
}
