package com.nika.erp.invoicing.web.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lowagie.text.DocumentException;
import com.nika.erp.invoicing.domain.Invoice;
import com.nika.erp.invoicing.domain.InvoiceLine;
import com.nika.erp.invoicing.service.InvoiceService;
import com.nika.erp.invoicing.web.form.InvoiceForm;
import com.nika.erp.invoicing.web.form.InvoiceLineForm;
import com.nika.erp.invoicing.web.util.NikaErpInvoicingUrlConstants;

import jakarta.servlet.http.HttpServletResponse;
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
		InvoiceForm invoice = new InvoiceForm();
		invoice.setInvoiceLines(List.of(new InvoiceLineForm())); // Start with 1 empty line
		model.addAttribute("invoice", invoice);
		return NikaErpInvoicingUrlConstants.INVOICE_ADD_FORM;
	}
	@PostMapping("/new")
	public String saveDraftInvoice(@ModelAttribute InvoiceForm invoiceForm) {
		
		log.info("saveDraftInvoice:{}",invoiceForm.getInvoiceLines().size());
		
		invoiceService.saveDraftInvoiceForm(invoiceForm);
		
	    //for (InvoiceLineBack line : invoice.getInvoiceLines()) {
	    //    line.setInvoice(invoice);
	   // }

	    //invoiceService.calculateTotals(invoice, new BigDecimal("0.1"));
	    
	   // if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().isEmpty()) {
	   //     invoiceService.generateInvoiceNumber(invoice);
	   // }

	    //invoiceRepository.save(invoice);
	    //return "redirect:/invoice/" + invoice.getId();
	    return NikaErpInvoicingUrlConstants.INVOICES_LIST_REDITECT_URL;
	}
	 // View (print) mode
    @GetMapping("/view/{id}")
    public String viewInvoice(@PathVariable String id, Model model) {
        Invoice invoice = invoiceService.findById(id);
        log.info("viewInvoice:{}",invoice);
        model.addAttribute("invoice", invoice);
        return NikaErpInvoicingUrlConstants.INVOICE_VIEW_FORM;  // ✅ Print view
    }

    // Edit mode
    @GetMapping("/edit/{id}")
    public String editInvoice(@PathVariable String id, Model model) {
    	  InvoiceForm invoice = invoiceService.findInvoiceFormById(id);
          log.info("editInvoice:{}",invoice);
          model.addAttribute("invoice", invoice);

        return NikaErpInvoicingUrlConstants.INVOICE_EDIT_FORM;  // ✅ Edit view
    }
    @GetMapping("/confirm/{id}")
    public String confirmInvoice(@PathVariable String id, Model model) {
        Invoice invoice = invoiceService.confirm(id);
        log.info("confirmInvoice:{}",invoice);
        model.addAttribute("invoice", invoice);
        return NikaErpInvoicingUrlConstants.INVOICE_VIEW_FORM;  // ✅ Print view
    }
    @GetMapping("/print/{id}")
	public void generatePdf(@PathVariable String id, HttpServletResponse response) throws IOException, DocumentException {
		Invoice invoice = invoiceService.findById(id);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=invoice_" + invoice.getInvoiceNumber() + ".pdf");

		com.lowagie.text.Document pdfDoc = new com.lowagie.text.Document();
		com.lowagie.text.pdf.PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();
		pdfDoc.add(new com.lowagie.text.Paragraph("Invoice: " + invoice.getInvoiceNumber()));
		//pdfDoc.add(new com.lowagie.text.Paragraph("Customer: " + invoice.getCustomer().getName()));
		//pdfDoc.add(new com.lowagie.text.Paragraph("Date: " + invoice.getInvoiceDate()));
		//pdfDoc.add(new com.lowagie.text.Paragraph("Due: " + invoice.getDueDate()));
		pdfDoc.add(new com.lowagie.text.Paragraph(" "));

		com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(4);
		table.addCell("Description");
		table.addCell("Quantity");
		table.addCell("Unit Price");
		table.addCell("Total");

		for (InvoiceLine line : invoice.getLines()) {
			table.addCell(line.getItemName());
			table.addCell(String.valueOf(line.getQuantity()));
			table.addCell(line.getUnitPrice().toString());
			table.addCell(line.getTotalAPayer().toString());
		}

		table.addCell("");
		table.addCell("");
		table.addCell("Subtotal");
		table.addCell(invoice.getTotalAPayer().toString());

		table.addCell("");
		table.addCell("");
		table.addCell("Tax");
		table.addCell(invoice.getTotalAPayer().toString());

		table.addCell("");
		table.addCell("");
		table.addCell("Total");
		table.addCell(invoice.getTotalAPayer().add(invoice.getTotalAPayer()).toString());

		pdfDoc.add(table);
		pdfDoc.close();
	}

}
