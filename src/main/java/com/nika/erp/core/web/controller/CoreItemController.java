package com.nika.erp.core.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nika.erp.core.service.CoreItemService;
import com.nika.erp.core.web.util.NikaErpCoreUrlConstants;
import com.nika.erp.invoicing.web.form.ItemForm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpCoreUrlConstants.ITEMS_URL)
@AllArgsConstructor
public class CoreItemController {
	private final CoreItemService coreItemService;
	
	@ResponseBody
    @GetMapping("/search")
    public List<ItemForm> searchItems(@RequestParam String query) {
		log.info("searchItems:{}",query);
        return coreItemService.findAllAsFormByItemNameContainingIgnoreCase(query);
    }

}
