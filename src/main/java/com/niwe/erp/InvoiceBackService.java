package com.niwe.erp;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceBackService {
	@Autowired
	private InvoiceBackRepository invoiceRepository;

	public BigDecimal calculateTotal(InvoiceBack invoice) {
		return invoice.getInvoiceLines().stream().map(InvoiceLineBack::getLineTotal).reduce(BigDecimal.ZERO,
				BigDecimal::add);
	}

	public BigDecimal calculateTax(InvoiceBack invoice, BigDecimal taxRate) {
		return calculateTotal(invoice).multiply(taxRate);
	}

	public InvoiceBack calculateTotals(InvoiceBack invoice, BigDecimal taxRate) {
		BigDecimal total = invoice.getInvoiceLines().stream().map(InvoiceLineBack::getLineTotal).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		invoice.setTotalAmount(total);
		invoice.setTaxAmount(total.multiply(taxRate));
		return invoice;
	}

	public void generateInvoiceNumber(InvoiceBack invoice) {
		long count = invoiceRepository.count() + 1;
		String number = String.format("INV%05d", count);
		invoice.setInvoiceNumber(number);
	}
}
