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
	private Long itemId;
    private String itemName;
    @Default
    private BigDecimal quantity=BigDecimal.ZERO;
    @Default
    private BigDecimal price=BigDecimal.ZERO;
    @Default
    private BigDecimal lineTotal=BigDecimal.ZERO;

    public BigDecimal getLineTotal() {
        return price != null && quantity != null
                ? price.multiply(quantity)
                : BigDecimal.ZERO;
    }

}
