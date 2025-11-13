package com.niwe.erp.core.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.niwe.erp.core.service.CoreItemService;
import com.niwe.erp.core.view.CoreItemListView;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = NiweErpCoreUrlConstants.ITEMS_URL)
@AllArgsConstructor
public class AjaxController {
	private final CoreItemService coreItemService;

	@GetMapping("/data")
	public Map<String, Object> getItems(@RequestParam(defaultValue = "3") int draw, @RequestParam(defaultValue = "0") int start,
			@RequestParam(defaultValue = "200") int length,
			@RequestParam(value = "search[value]", required = false) String searchValue,
			@RequestParam(value = "order[0][column]", required = false) Integer orderColumn,
			@RequestParam(value = "order[0][dir]", required = false) String orderDir) {
		log.info("------------getItems-------------------searchValue:{}",searchValue);
		// Determine sorting column
		String[] columns = { "itemName", "itemCode", "unitPrice" };
		String sortBy = (orderColumn != null && orderColumn < columns.length) ? columns[orderColumn] : "itemName";
		Sort.Direction direction = "desc".equalsIgnoreCase(orderDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(start / length, length, Sort.by(direction, sortBy));
		// Fetch data with optional filtering
		Page<CoreItemListView> itemsPage;
        if (searchValue != null && !searchValue.isEmpty()) {
        	itemsPage = coreItemService.getItems(pageable, searchValue, searchValue);
        } else {
        	itemsPage = coreItemService.getItems(pageable);
        }
		

		// Prepare response in DataTables format
		Map<String, Object> response = new HashMap<>();
		response.put("draw", draw);
		response.put("recordsTotal", itemsPage.getTotalPages());
		response.put("recordsFiltered", itemsPage.getTotalElements());
		response.put("data", itemsPage.getContent());

		return response;

	}

}
