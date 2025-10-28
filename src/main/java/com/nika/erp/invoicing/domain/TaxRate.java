package com.nika.erp.invoicing.domain;

import java.math.BigDecimal;

import com.nika.erp.common.domain.AbstractEntity;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "INVOICING_TAX_RATE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TaxRate extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The taxName
	 */
	@Column(name = "TAX_NAME", nullable = false,unique = true)
	private String taxName;
	
	/**
	 * The displayName
	 */
	@Column(name = "DISPLAY_NAME", nullable = true)
	private String displayName;
	
	/**
	 * The code
	 */
	@Column(name = "CODE", nullable = false,unique = true)
	private String code;
	/**
	 * The taxValue
	 */
	@Column(name = "TAX_VALUE", nullable = true)
	@Builder.Default
	private BigDecimal taxValue = BigDecimal.ZERO;
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION", nullable = false)
	private String description;



}
