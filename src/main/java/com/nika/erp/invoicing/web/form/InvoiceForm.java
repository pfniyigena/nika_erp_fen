package com.nika.erp.invoicing.web.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoiceForm {
	
	private String internalCode;

}
