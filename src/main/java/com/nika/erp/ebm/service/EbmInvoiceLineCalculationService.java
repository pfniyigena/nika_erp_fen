package com.nika.erp.ebm.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class EbmInvoiceLineCalculationService {
	private final BigDecimal quantity;
	private final BigDecimal unitPrice;
	private final BigDecimal taxRate;
	private final BigDecimal irrpRate;
	private final BigDecimal discountRate;

	public EbmInvoiceLineCalculationService(BigDecimal quantity, BigDecimal unitPrice, BigDecimal taxRate, BigDecimal irrpRate,
			BigDecimal discountRate) {
		this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null");
		this.unitPrice = Objects.requireNonNull(unitPrice, "Unit price cannot be null");
		this.taxRate = Objects.requireNonNull(taxRate, "Tax rate cannot be null");
		this.irrpRate = Objects.requireNonNull(irrpRate, "IRPP Rate cannot be null");
		this.discountRate = Objects.requireNonNull(discountRate, "Discount Rate cannot be null");
	}

	public BigDecimal getGrossAmount() {

		return quantity.multiply(unitPrice);

	}

	public BigDecimal getDiscountAmount() {

		BigDecimal discountPercentage = discountRate.divide(BigDecimal.valueOf(100), 14, RoundingMode.HALF_UP);

		return getGrossAmount().multiply(discountPercentage);

	}

	public BigDecimal getAmountAfterDiscount() {

		return getGrossAmount().subtract(getDiscountAmount());

	}

	public BigDecimal getTaxAmount() {

		BigDecimal taxPercentage = taxRate.divide(BigDecimal.valueOf(100).add(taxRate), 14, RoundingMode.HALF_UP);

		return getAmountAfterDiscount().multiply(taxPercentage);

	}

	public BigDecimal getAmountHorsTax() {

		return getAmountAfterDiscount().subtract(getTaxAmount());

	}

	public BigDecimal getIrppAmount() {

		return getAmountHorsTax().multiply(irrpRate).divide(BigDecimal.valueOf(100), 14, RoundingMode.HALF_UP);

	}

	public BigDecimal getAmountToPay() {

		return getAmountAfterDiscount().add(getIrppAmount());

	}
}
