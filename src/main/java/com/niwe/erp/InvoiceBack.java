package com.niwe.erp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceBack {
	@Id
	@GeneratedValue
	private Long id;

	private String invoiceNumber;

	private LocalDate invoiceDate;
	private LocalDate dueDate;

	@ManyToOne
	private Customer customer;

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
	private List<InvoiceLineBack> invoiceLines = new ArrayList<>();

	private BigDecimal totalAmount;
	private BigDecimal taxAmount;

	@Enumerated(EnumType.STRING)
	private InvoiceStatusBack status = InvoiceStatusBack.DRAFT; // Default status
}
