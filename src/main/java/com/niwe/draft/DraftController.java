package com.niwe.draft;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DraftController {
	@GetMapping("draft/pos_sale")
	public String showSaleFormOdoo(Model model) {
	    model.addAttribute("saleForm", new SaleFormDraft());
	    //model.addAttribute("customers", customerService.findAll());
	    //model.addAttribute("products", productService.findAll());
	    return "pos_sale";
	}
}
