package com.nika.erp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	List<Invoice> findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(String invoiceNumber,
			String customerName);
	  Page<Invoice> findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(
	            String invoiceNumber, String customerName, Pageable pageable);

	    Page<Invoice> findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCaseAndStatus(
	            String invoiceNumber, String customerName, InvoiceStatus status, Pageable pageable);

	    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);
}
