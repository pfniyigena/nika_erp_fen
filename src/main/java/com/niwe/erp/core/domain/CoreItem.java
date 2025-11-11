package com.niwe.erp.core.domain;

import java.math.BigDecimal;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.invoicing.domain.TaxType;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
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

	@Column(name = "EXTERNAL_ITEM_CODE", nullable = true, length = 50)
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
	 * The sku
	 */
	@Column(name = "SKU")
	private String sku;
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
	 * The tax
	 */
	@ManyToOne
	@JoinColumn(name = "TAX_TYPE_ID")
	private TaxType tax;
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
	@JoinColumn(name = "QUANTITY_UNIT_ID", nullable = true)
	private CoreQuantityUnit unit;
	/**
	 * The irpp
	 */
	@Column(name = "IRPP", nullable = true)
	@Builder.Default
	private Boolean irpp = Boolean.FALSE;
	/**
	 * The taxpayer
	 */
	@JoinColumn(name = "COUNTY_ID", nullable = true)
	private CoreCountry country;
	
	/**
	 * The taxpayer
	 */
	@JoinColumn(name = "TAXPAYER_ID", nullable = true)
	private CoreTaxpayer taxpayer;
	
	// Copy constructor
    public CoreItem(CoreItem copy) {
        this.itemName = copy.getItemName();
        this.externalItemCode=copy.getExternalItemCode();
        this.classification=copy.getClassification();
        this.unitPrice=copy.getUnitPrice();
        this.unitCost=copy.getUnitCost();
        this.nature=copy.getNature();
        this.irpp=copy.getIrpp();
        this.unit=copy.getUnit();
        this.country=copy.getCountry();
        this.taxpayer=copy.getTaxpayer();
        this.tax=copy.getTax();
        
    }

}
