package com.nika.erp;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceLineBack {
    @Id @GeneratedValue
    private Long id;
    private String description;
    private int quantity;
    private BigDecimal unitPrice;

    @ManyToOne
    private InvoiceBack invoice;

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}