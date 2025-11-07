package com.nika.erp.inventory.domain;

import com.nika.erp.common.domain.AbstractEntity;
import com.nika.erp.core.domain.CoreTaxpayerBranch;

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
@Table(name = "INVENTORY_SHELF")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Shelf extends AbstractEntity {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false)
	private String internalCode;
	/**
	 * The branch
	 */
	@ManyToOne
	@JoinColumn(name = "BRANCH_ID")
	private CoreTaxpayerBranch branch;
	/**
	 * The warehouse
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID")
	private Warehouse warehouse;
}
