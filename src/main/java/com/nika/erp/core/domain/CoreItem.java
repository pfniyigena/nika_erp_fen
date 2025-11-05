package com.nika.erp.core.domain;

import java.math.BigDecimal;

import com.nika.erp.common.domain.AbstractEntity;
import com.nika.erp.invoicing.domain.TaxRate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "CORE_ITEM")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreItem  extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The itemName
	 */
	@Column(name = "ITEM_NAME", nullable = false)
	private String itemName;

	@Column(name = "ITEM_CODE", nullable = true)
	private String itemCode;

	@Column(name = "EXTERNAL_ITEM_CODE", nullable = false, length = 50)
	private String externalItemCode;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false)
	private String internalCode;
	/**
	 * The barcode
	 */
	@Column(name = "BARCODE")
	private String barcode;
	/**
	 * The description
	 */
	@Column(name = "ITEM_DESCRITION", nullable = true)
	private String description;
	/**
	 * The unitPrice
	 */
	@Column(name = "UNIT_PRICE", nullable = true)
	@Builder.Default
	private BigDecimal unitPrice = BigDecimal.ZERO;
	/**
	 * The unitCost
	 */
	@Column(name = "UNIT_COST", nullable = true)
	@Builder.Default
	private BigDecimal unitCost = BigDecimal.ZERO;
	/**
	 * The taxRate
	 */
	@ManyToOne
	@JoinColumn(name = "TAX_RATE_ID")
	private TaxRate tax;
	/**
	 * The 	itemNature
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_NATURE_ID")
	private CoreItemNature nature;
	/**
	 * The classification
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_CLASSIFICATION_ID")
	private CoreItemClassification classification;
	
	/**
	 * The taxpayer
	 */
	@JoinColumn(name = "QUANTITY_UNIT_ID", nullable = false)
	private CoreQuantityUnit unit;
	/**
	 * The irpp
	 */
	@Column(name = "IRPP", nullable = false)
	@Builder.Default
	private Boolean irpp = Boolean.FALSE;
	/**
	 * The taxpayer
	 */
	@JoinColumn(name = "COUNTY_ID", nullable = false)
	private CoreCountry country;
	
	/**
	 * The taxpayer
	 */
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;

}
