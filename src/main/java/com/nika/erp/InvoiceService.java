package com.nika.erp;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceRepository invoiceRepository;

	public BigDecimal calculateTotal(Invoice invoice) {
		return invoice.getInvoiceLines().stream().map(InvoiceLine::getLineTotal).reduce(BigDecimal.ZERO,
				BigDecimal::add);
	}

	public BigDecimal calculateTax(Invoice invoice, BigDecimal taxRate) {
		return calculateTotal(invoice).multiply(taxRate);
	}

	public Invoice calculateTotals(Invoice invoice, BigDecimal taxRate) {
		BigDecimal total = invoice.getInvoiceLines().stream().map(InvoiceLine::getLineTotal).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		invoice.setTotalAmount(total);
		invoice.setTaxAmount(total.multiply(taxRate));
		return invoice;
	}

	public void generateInvoiceNumber(Invoice invoice) {
		long count = invoiceRepository.count() + 1;
		String number = String.format("INV%05d", count);
		invoice.setInvoiceNumber(number);
	}
}
