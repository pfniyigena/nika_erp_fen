package com.niwe.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.service.CoreCountryService;
import com.niwe.erp.core.service.CoreItemClassificationService;
import com.niwe.erp.core.service.CoreItemNatureService;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreQuantityUnitService;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;
import com.niwe.erp.invoicing.service.TaxTypeService;
import com.niwe.erp.invoicing.web.form.ItemForm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.ITEMS_URL)
@AllArgsConstructor
public class CoreItemController {
	private final CoreItemService coreItemService;
	private final CoreCountryService coreCountryService;
	private final CoreItemClassificationService coreItemClassificationService;
	private final CoreItemNatureService coreItemNatureService;
	private final CoreQuantityUnitService coreQuantityUnitService;
	private final TaxTypeService taxTypeService;
	private final CoreTaxpayerService coreTaxpayerService;
	@GetMapping(path = "/list")
	public String listItems(Model model) {

		List<CoreItem> list = coreItemService.findAll();
		log.debug("--------------Calling listItemNatures-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.ITEMS_LIST_PAGE;
	}

	@GetMapping(path = "/new")
	public String newItem(Model model) {
		model.addAttribute("item", CoreItem.builder().build());
		setData(model);
		return NiweErpCoreUrlConstants.ITEMS_ADD_FORM_PAGE;
	}

	@PostMapping(path = "/new")
	public String saveItem(CoreItem item, RedirectAttributes redirectAttrs,
			BindingResult bindingResult, Model model) {

		log.debug(String.format("------calling saveItem:{%s}", item));
		coreItemService.save(item);
		return NiweErpCoreUrlConstants.ITEMS_LIST_REDITECT_URL;
	}

	@GetMapping(path = "/update/{id}")
	public String findById(@PathVariable String id, Model model) {
		CoreItem item = coreItemService.findById(id);

		model.addAttribute("item", item);
		setData(model);
		return NiweErpCoreUrlConstants.ITEMS_ADD_FORM_PAGE;
	}
	@GetMapping(path = "/duplicate/{id}")
	public String duplicate(@PathVariable String id, Model model) {
		CoreItem item = coreItemService.duplicate(id);
		model.addAttribute("item", item);
		setData(model);
		return NiweErpCoreUrlConstants.ITEMS_ADD_FORM_PAGE;
	}

	@ResponseBody
    @GetMapping("/search")
    public List<ItemForm> searchItems(@RequestParam String query) {
		log.info("searchItems:{}",query);
        return coreItemService.findAllAsFormByItemNameContainingIgnoreCase(query);
    }

	public void setData( Model model) {
		model.addAttribute("countries", coreCountryService.findAll());
		model.addAttribute("classifications", coreItemClassificationService.findAll());
		model.addAttribute("natures", coreItemNatureService.findAll());
		model.addAttribute("units", coreQuantityUnitService.findAll());
		model.addAttribute("taxes", taxTypeService.findAll());
		model.addAttribute("taxpayers", coreTaxpayerService.findAll());
		
	}
}
