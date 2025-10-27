package com.nika.erp.invoicing.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nika.erp.invoicing.domain.Invoice;

 

public interface InvoiceRepository extends JpaRepository<Invoice, UUID>  {

}
