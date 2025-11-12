package com.niwe.erp.inventory.web.controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.niwe.erp.inventory.domain.StockMovement;
import com.niwe.erp.inventory.domain.Warehouse;
import com.niwe.erp.inventory.service.StockMovementService;
import com.niwe.erp.inventory.web.util.NikaErpInventoryUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NikaErpInventoryUrlConstants.STOCK_MOVEMENTS_URL)
@AllArgsConstructor
public class StockMovementController {

	private final StockMovementService stockMovementService;
	@GetMapping(path = "/list")
	public String listStockMovements(Model model) {

		List<StockMovement> list = stockMovementService.findAll();
		log.debug("--------------Calling listStockMovements-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInventoryUrlConstants.STOCK_MOVEMENTS_LIST_PAGE;
	}
	@GetMapping("/movement-by-warehouse")
    public String stockMovementByWarehouse(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Instant end,
            Model model) {

        Map<Warehouse, List<StockMovement>> groupedMovements =
        		stockMovementService.getStockMovementsGroupedByWarehouse(start, end);

        model.addAttribute("groupedMovements", groupedMovements);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "stock-movements/movement-by-warehouse";
    }
}
