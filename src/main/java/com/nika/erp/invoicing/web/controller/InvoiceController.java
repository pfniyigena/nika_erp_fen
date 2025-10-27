package com.nika.erp.invoicing.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nika.erp.invoicing.domain.Invoice;
import com.nika.erp.invoicing.service.InvoiceService;
import com.nika.erp.invoicing.web.form.InvoiceLineForm;
import com.nika.erp.invoicing.web.util.NikaErpInvoicingUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping(value = NikaErpInvoicingUrlConstants.INVOICES_URL)
@AllArgsConstructor
public class InvoiceController {
	
	private final InvoiceService invoiceService;

	@GetMapping(path = "/list")
	public String listInvoices(Model model) {

		List<Invoice> list = invoiceService.findAll();
		log.debug("--------------Calling listInvoices-------------------" + list.size());
		model.addAttribute("lists", list);
		return NikaErpInvoicingUrlConstants.INVOICE_LIST_PAGE;
	}
	@GetMapping(path = "/new")
	public String newInvoice(Model model) {
		 model.addAttribute("invoiceLineForms", List.of(new InvoiceLineForm()));
		return NikaErpInvoicingUrlConstants.INVOICE_ADD_FORM;
	}

}
