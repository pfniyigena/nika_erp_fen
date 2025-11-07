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
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "INVENTORY_GOODS_RECEIPT")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class GoodsReceipt extends AbstractEntity {
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
	 * The receivedDate
	 */
	@Column(name = "RECEIVED_DATE")
	@Default
    private LocalDateTime receivedDate = LocalDateTime.now();
	/**
	 * The receivedBy
	 */
	@Column(name = "RECEIVED_BY")
    private String receivedBy;
	/**
	 * The receivedBy
	 */
	@Column(name = "SUPPLIER_NAME")
    private String supplierName;
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
	@JoinColumn(name = "WAREHOUSE_ID", nullable = false)
	private CoreTaxpayerBranch branch;
    private Warehouse warehouse;


}
