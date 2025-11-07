package com.nika.erp.inventory.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nika.erp.common.domain.AbstractEntity;
import com.nika.erp.core.domain.CoreItem;
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
@Table(name = "INVENTORY_SHELF_SALE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ShelfSale extends AbstractEntity{
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
	 * The totalPrice
	 */
	@Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;
	/**
	 * The saleDate
	 */
	@Column(name = "SALE_DATE")
    private LocalDateTime saleDate;
	/**
	 * The item
	 */
	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false)
	private CoreItem item; 
	/**
	 * The branch
	 */
	@ManyToOne
	@JoinColumn(name = "BRANCH_ID", nullable = false)
	private CoreTaxpayerBranch branch; 

}
