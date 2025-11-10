package com.nika.erp.inventory.domain;

import java.math.BigDecimal;

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
@Table(name = "INVENTORY_RECEIVED_ITEM")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReceivedItem extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The itemName
	 */
	@Column(name = "ITEM_NAME")
	private String itemName;
	/**
	 * The quantity
	 */
	@Column(name = "QUANTITY")
	private BigDecimal quantity;
	/**
	 * The purchasePrice
	 */
	@Column(name = "PURCHASE_PRICE")
	private BigDecimal purchasePrice;
	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private CoreItem item;

	/**
	 * The receivedGood
	 */
	@ManyToOne
	@JoinColumn(name = "RECEIVED_GOOD_ID")
	private ReceivedGood receivedGood;
}
