package com.nika.erp.inventory.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nika.erp.common.domain.AbstractEntity;
import com.nika.erp.core.domain.CoreItem;

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
@Table(name = "INVENTORY_STOCK_MOVEMENT")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StockMovement  extends AbstractEntity {
    
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The quantity
	 */
	@Column(name = "QUANTITY")
    private BigDecimal quantity;
	/**
	 * The movementDate
	 */
	@Column(name = "MOUVEMENT_DATE")
    private LocalDateTime movementDate;
	/**
	 * The fromWarehouse
	 */
	@ManyToOne
	@JoinColumn(name = "FROM_WAREHOUSE_ID")
    private Warehouse fromWarehouse;
	/**
	 * The toWarehouse
	 */
	@ManyToOne
	@JoinColumn(name = "TO_WAREHOUSE_ID")
    private Warehouse toWarehouse;
	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID")
    private CoreItem item;

}

