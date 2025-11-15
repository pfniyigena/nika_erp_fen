package com.niwe.erp.core.web.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.common.util.NiweErpCommonConstants;
import com.niwe.erp.core.domain.CoreItem;
import com.niwe.erp.core.form.CoreItemForm;
import com.niwe.erp.core.service.CoreCountryService;
import com.niwe.erp.core.service.CoreItemClassificationService;
import com.niwe.erp.core.service.CoreItemNatureService;
import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.service.CoreQuantityUnitService;
import com.niwe.erp.core.service.CoreTaxpayerService;
import com.niwe.erp.core.uti.annotation.CanManageItems;
import com.niwe.erp.core.view.CoreItemListView;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;
import com.niwe.erp.invoicing.service.TaxTypeService;

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

		Page<CoreItemForm> list = coreItemService.findAllAsForm(0, NiweErpCommonConstants.NIKA_DEFAULT_PAGE_SIZE);
		log.debug("--------------Calling listItems-------------------" + list.getTotalElements());
		model.addAttribute("lists", list.getContent());
		return NiweErpCoreUrlConstants.ITEMS_LIST_PAGE;
	}

	@GetMapping(path = "/list/v2")
	public String listItemsV2(Model model) {

		List<CoreItem> list = coreItemService.findAll();
		log.debug("--------------Calling listItemNatures-------------------" + list.size());
		model.addAttribute("lists", list);
		return NiweErpCoreUrlConstants.ITEMS_LIST_PAGE;
	}

	@GetMapping(path = "/list/v3")
	public String listItemsV3(Model model) {

		int page = 0;
		int size = NiweErpCommonConstants.NIKA_DEFAULT_PAGE_SIZE;
		Page<CoreItem> list = coreItemService.findAll(page, size);
		model.addAttribute("lists", list.getContent());
		return NiweErpCoreUrlConstants.ITEMS_LIST_PAGE;
	}

	@CanManageItems
	@GetMapping(path = "/new")
	public String newItem(Model model) {
		model.addAttribute("item", CoreItem.builder().build());
		setData(model);
		return NiweErpCoreUrlConstants.ITEMS_ADD_FORM_PAGE;
	}

	@PreAuthorize("hasAnyAuthority('COREITEM_CREATE', 'COREITEM_UPDATE')")
	@PostMapping(path = "/new")
	public String saveItem(CoreItem item, RedirectAttributes redirectAttrs, BindingResult bindingResult, Model model) {

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

	@GetMapping(path = "/view/{id}")
	public String viewItemInfo(@PathVariable String id, Model model) {
		CoreItem item = coreItemService.findById(id);

		model.addAttribute("item", item);
		setData(model);
		return NiweErpCoreUrlConstants.ITEMS_VIEW_FORM_PAGE;
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
	public List<CoreItemForm> searchItems(@RequestParam String query) {
		log.info("searchItems:{}", query);
		return coreItemService.findAllAsFormByItemNameContainingIgnoreCase(query);
	}

	@GetMapping("/filter")
	public String listItems(@RequestParam(defaultValue = "") String searchTerm,
			@RequestParam(defaultValue = "0") int page, Model model) {

		Page<CoreItem> itemPage = coreItemService.listItems(searchTerm, page,
				NiweErpCommonConstants.NIKA_DEFAULT_PAGE_SIZE);

		model.addAttribute("lists", itemPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", itemPage.getTotalPages());
		model.addAttribute("searchTerm", searchTerm);

		return "items/list";
	}

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
		String contentType = file.getContentType();
		if (!Objects.equals(contentType, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			model.addAttribute("error", "Please upload an Excel file (.xlsx)");
			return NiweErpCoreUrlConstants.ITEMS_ADD_FORM_PAGE;
		}

		coreItemService.impotExcelFile(file);
		return NiweErpCoreUrlConstants.ITEMS_LIST_REDITECT_URL;
	}

	@PostMapping("/updatePrice")
	public String updateItemValue(@RequestParam String id, @RequestParam String type, @RequestParam BigDecimal value) {
		coreItemService.updateUnitPriceOrUnitCost(id, type, value);
		return NiweErpCoreUrlConstants.ITEMS_LIST_REDITECT_URL;
	}

	@GetMapping("/page")
	public String listItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "itemName") String sortBy, @RequestParam(defaultValue = "ASC") String sortDir,
			@RequestParam(required = false) String name, @RequestParam(required = false) String code, Model model) {

		Page<CoreItemListView> itemsPage = coreItemService.getItems(page, size, sortBy, sortDir, name, code);
		model.addAttribute("itemsPage", itemsPage);
		model.addAttribute("name", name);
		model.addAttribute("code", code);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortDir", sortDir);

		return "items/page";
	}

	public void setData(Model model) {
		model.addAttribute("countries", coreCountryService.findAll());
		model.addAttribute("classifications", coreItemClassificationService.findAll());
		model.addAttribute("natures", coreItemNatureService.findAll());
		model.addAttribute("units", coreQuantityUnitService.findAll());
		model.addAttribute("taxes", taxTypeService.findAll());
		model.addAttribute("taxpayers", coreTaxpayerService.findAll());

	}
}
