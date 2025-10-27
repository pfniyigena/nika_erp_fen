package com.nika.erp.invoicing.web.form;

import java.math.BigDecimal;

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
public class ItemForm {
	private String name;
	private String code;
	private BigDecimal price;

}
