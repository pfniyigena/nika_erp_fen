package com.nika.erp.inventory.domain;

import java.math.BigDecimal;
import java.time.Instant;

import com.nika.erp.common.domain.AbstractEntity;
import com.nika.erp.core.domain.CoreItem;

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
@Table(name = "INVENTORY_STOCK_MOVEMENT")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockMovement extends AbstractEntity {

	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The reference
	 */
	@Column(name = "REFERENCE")
	private String reference;
	/**
	 * The previousQuantity
	 */
	@Column(name = "PREVIOUS_QUANTITY")
	private BigDecimal previousQuantity;
	/**
	 * The quantity
	 */
	@Column(name = "QUANTITY")
	private BigDecimal quantity;

	/**
	 * The currentQuantity
	 */
	@Column(name = "CURRENT_QUANTITY")
	private BigDecimal currentQuantity;
	/**
	 * The movementType
	 */
	@Column(name = "MOVEMENT_TYPE")
	@lombok.ToString.Include
	private MovementType movementType;
	/**
	 * The stockOperation
	 */
	@Column(name = "STOCK_OPERATION")
	@lombok.ToString.Include
	private EStockOperation stockOperation;
	
	
	/**
	 * The movementDate
	 */
	@Column(name = "MOUVEMENT_DATE")
	@Builder.Default
	private Instant movementDate=Instant.now();

	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID",nullable = false)
	private CoreItem item;

	/**
	 * The warehouse
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID",nullable = false)
	private Warehouse warehouse;

}
