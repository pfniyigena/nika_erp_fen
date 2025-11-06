package com.nika.erp.core.domain;

import com.nika.erp.common.domain.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CORE_TAXPAYER_BRANCH")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreTaxpayerBranch extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	
	/**
	 * The branchName
	 */
	@Column(name = "BRANCH_NAME", nullable = false)
	private String branchName;
	
	/**
	 * The branchCode
	 */
	@Column(name = "BRANCH_CODE", nullable = false)
	private String branchCode;
	
	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = false)
	private CoreTaxpayer taxpayer;

}
