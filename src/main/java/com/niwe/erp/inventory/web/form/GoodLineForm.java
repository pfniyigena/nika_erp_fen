package com.niwe.erp.inventory.web.form;

import java.math.BigDecimal;

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
public class GoodLineForm {
	private String id;
	private String itemName;
    private String itemCode;
    private String taxCode;
    @Builder.Default
    private BigDecimal taxType=BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal quantity=BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal unitPrice=BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal lineTotal=BigDecimal.ZERO;

    public BigDecimal getLineTotal() {
        return unitPrice != null && quantity != null
                ? unitPrice.multiply(quantity)
                : BigDecimal.ZERO;
    }

}
