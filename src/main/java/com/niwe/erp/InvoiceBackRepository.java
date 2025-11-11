package com.niwe.erp;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceBackRepository extends JpaRepository<InvoiceBack, Long> {
	List<InvoiceBack> findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(String invoiceNumber,
			String customerName);
	  Page<InvoiceBack> findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(
	            String invoiceNumber, String customerName, Pageable pageable);

	    Page<InvoiceBack> findByInvoiceNumberContainingIgnoreCaseOrCustomerNameContainingIgnoreCaseAndStatus(
	            String invoiceNumber, String customerName, InvoiceStatusBack status, Pageable pageable);

	    Page<InvoiceBack> findByStatus(InvoiceStatusBack status, Pageable pageable);
}
