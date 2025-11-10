package com.nika.erp.sale.web.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.nika.erp.Customer;
import com.nika.erp.sale.domain.SaleStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ShelfForm {
	private UUID id;
	private String shelfId;
	private String invoiceNumber;
	private LocalDate invoiceDate;
	private LocalDate dueDate;
	private Customer customer;
	@Builder.Default
	private List<ShelfLineForm> shelfLines = new ArrayList<>();
	private BigDecimal totalAmount;
	private BigDecimal taxAmount;
	@Builder.Default
	private SaleStatus status = SaleStatus.DRAFT;

}
