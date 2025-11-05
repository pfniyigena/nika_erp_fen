package com.nika.erp.invoicing.web.form;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class InvoiceLineForm {
	private String id;
	private String itemName;
    private String itemCode;
    private String taxCode;
    @Default
    private BigDecimal taxRate=BigDecimal.ZERO;
    @Default
    private BigDecimal quantity=BigDecimal.ZERO;
    @Default
    private BigDecimal unitPrice=BigDecimal.ZERO;
    @Default
    private BigDecimal lineTotal=BigDecimal.ZERO;

    public BigDecimal getLineTotal() {
        return unitPrice != null && quantity != null
                ? unitPrice.multiply(quantity)
                : BigDecimal.ZERO;
    }

}
