package com.nika.erp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private InvoiceRepository invoiceRepository;
	@GetMapping
	public String listInvoices(
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        @RequestParam(value = "sort", defaultValue = "invoiceDate") String sort,
	        @RequestParam(value = "dir", defaultValue = "desc") String dir,
	        @RequestParam(value = "status", required = false) String status,
	        @RequestParam(value = "q", required = false) String query,
	        Model model) {

	    Sort.Direction direction = dir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));

	    Page<Invoice> invoicePage;

	    if ((query != null && !query.isEmpty()) && (status != null && !status.isEmpty())) {
	        invoicePage = invoiceRepository
	                .findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCaseAndStatus(
	                        query, query, InvoiceStatus.valueOf(status), pageable);
	    } else if (query != null && !query.isEmpty()) {
	        invoicePage = invoiceRepository
	                .findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(query, query, pageable);
	    } else if (status != null && !status.isEmpty()) {
	        invoicePage = invoiceRepository.findByStatus(InvoiceStatus.valueOf(status), pageable);
	    } else {
	        invoicePage = invoiceRepository.findAll(pageable);
	    }

	    model.addAttribute("invoicePage", invoicePage);
	    model.addAttribute("query", query);
	    model.addAttribute("status", status);
	    model.addAttribute("sort", sort);
	    model.addAttribute("dir", dir);

	    return "invoice-list";
	}

	@GetMapping("/{id}")
	public String viewInvoice(@PathVariable Long id, Model model) {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow();
		invoice.setTotalAmount(invoiceService.calculateTotal(invoice));
		invoice.setTaxAmount(invoiceService.calculateTax(invoice, new BigDecimal("0.1"))); // 10% tax
		model.addAttribute("invoice", invoice);
		return "invoice-template"; // Thymeleaf template
	}

	@GetMapping("/{id}/pdf")
	public void generatePdf(@PathVariable Long id, HttpServletResponse response) throws IOException, DocumentException {
		Invoice invoice = invoiceRepository.findById(id).orElseThrow();
		invoiceService.calculateTotals(invoice, new BigDecimal("0.1"));

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=invoice_" + invoice.getInvoiceNumber() + ".pdf");

		com.lowagie.text.Document pdfDoc = new com.lowagie.text.Document();
		com.lowagie.text.pdf.PdfWriter.getInstance(pdfDoc, response.getOutputStream());
		pdfDoc.open();
		pdfDoc.add(new com.lowagie.text.Paragraph("Invoice: " + invoice.getInvoiceNumber()));
		pdfDoc.add(new com.lowagie.text.Paragraph("Customer: " + invoice.getCustomer().getName()));
		pdfDoc.add(new com.lowagie.text.Paragraph("Date: " + invoice.getInvoiceDate()));
		pdfDoc.add(new com.lowagie.text.Paragraph("Due: " + invoice.getDueDate()));
		pdfDoc.add(new com.lowagie.text.Paragraph(" "));

		com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(4);
		table.addCell("Description");
		table.addCell("Quantity");
		table.addCell("Unit Price");
		table.addCell("Total");

		for (InvoiceLine line : invoice.getInvoiceLines()) {
			table.addCell(line.getDescription());
			table.addCell(String.valueOf(line.getQuantity()));
			table.addCell(line.getUnitPrice().toString());
			table.addCell(line.getLineTotal().toString());
		}

		table.addCell("");
		table.addCell("");
		table.addCell("Subtotal");
		table.addCell(invoice.getTotalAmount().toString());

		table.addCell("");
		table.addCell("");
		table.addCell("Tax");
		table.addCell(invoice.getTaxAmount().toString());

		table.addCell("");
		table.addCell("");
		table.addCell("Total");
		table.addCell(invoice.getTotalAmount().add(invoice.getTaxAmount()).toString());

		pdfDoc.add(table);
		pdfDoc.close();
	}

	// Display empty form
	@GetMapping("/new")
	public String showInvoiceForm(Model model) {
		Invoice invoice = new Invoice();
		invoice.setInvoiceLines(List.of(new InvoiceLine())); // Start with 1 empty line
		model.addAttribute("invoice", invoice);
		model.addAttribute("customers", customerRepository.findAll());
		return "invoice-form";
	}

	
	@PostMapping("/save")
	public String saveInvoice(@ModelAttribute Invoice invoice) {
	    for (InvoiceLine line : invoice.getInvoiceLines()) {
	        line.setInvoice(invoice);
	    }

	    invoiceService.calculateTotals(invoice, new BigDecimal("0.1"));
	    
	    if (invoice.getInvoiceNumber() == null || invoice.getInvoiceNumber().isEmpty()) {
	        invoiceService.generateInvoiceNumber(invoice);
	    }

	    invoiceRepository.save(invoice);
	    return "redirect:/invoice/" + invoice.getId();
	}

	@PostMapping("/{id}/mark-paid")
	public String markPaid(@PathVariable Long id) {
	    Invoice invoice = invoiceRepository.findById(id).orElseThrow();
	    invoice.setStatus(InvoiceStatus.PAID);
	    invoiceRepository.save(invoice);
	    return "redirect:/invoice/" + id;
	}
}
