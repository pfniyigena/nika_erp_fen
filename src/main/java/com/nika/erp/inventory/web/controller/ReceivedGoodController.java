package com.nika.erp.inventory.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nika.erp.inventory.domain.ReceivedGood;
import com.nika.erp.inventory.service.ReceivedGoodService;
import com.nika.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.STOCKS_RECEIVED_URL)
@AllArgsConstructor
public class ReceivedGoodController {
	
	private final ReceivedGoodService receivedGoodService;
	
	@GetMapping(path = "/list")
	public String listReceivedGoods(Model model) {

		List<ReceivedGood> list = receivedGoodService.findAll();
		log.debug("--------------Calling listReceivedGoods-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.STOCKS_RECEIVED_LIST_PAGE;
	}


}
