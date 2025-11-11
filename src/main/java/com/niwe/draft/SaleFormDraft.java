package com.niwe.draft;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleFormDraft {
	private Long customerId;
    private LocalDate saleDate;
    private BigDecimal totalAmount;
    public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public LocalDate getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<SaleLineFormDraft> getSaleLines() {
		return saleLines;
	}
	public void setSaleLines(List<SaleLineFormDraft> saleLines) {
		this.saleLines = saleLines;
	}
	private List<SaleLineFormDraft> saleLines = new ArrayList<>();
}
