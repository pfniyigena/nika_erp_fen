package com.niwe.erp.sale.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "REPORT_DAILY_SALES_SUMMARY")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DailySalesSummary extends AbstractEntity{
	
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The totalSalesTaxesInclusive
	 */
	@Column(name = "TOTAL_SALES_TAXES_INCLISIVE")
	@Builder.Default
	private BigDecimal totalSalesTaxesInclusive=BigDecimal.ZERO;
	/**
	 * The totalSalesTaxesExclusive
	 */
	@Column(name = "TOTAL_SALES_TAXES_EXCLUSIVE")
	@Builder.Default
	private BigDecimal totalSalesTaxesExclusive=BigDecimal.ZERO;
	
	/**
	 * The totalCost
	 */
	@Column(name = "TOTAL_COST")
	@Builder.Default
	private BigDecimal totalCost=BigDecimal.ZERO;
	
	/**
	 * The grossProfit
	 */
	@Column(name = "GROSS_PROFIT")
	@Builder.Default
	private BigDecimal grossProfit=BigDecimal.ZERO;
	/**
	 * The numberOfReceipts
	 */
	@Column(name = "NUMBER_OF_RECEIPTS")
	@Builder.Default
	private Integer numberOfReceipts=0;
	
	/**
	 * The summaryDate
	 */
	@Column(name = "SUMMARY_DATE")
	private LocalDate summaryDate;



}
