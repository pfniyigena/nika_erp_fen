package com.nika.erp.inventory.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.nika.erp.common.domain.AbstractEntity;
import com.nika.erp.purchase.domain.Purchase;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "INVENTORY_RECEIVED_GOOD")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReceivedGood extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	/**
	 * The receivedDate
	 */
	@Column(name = "RECEIVED_DATE")
	@Default
	private Instant receivedDate = Instant.now();
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
	 * The items
	 */
	@OneToMany(mappedBy = "receivedGood", cascade = CascadeType.ALL)
	@Builder.Default
	private List<ReceivedItem> items = new ArrayList<>();
	/**
	 * The warehouse
	 */
	@ManyToOne
	@JoinColumn(name = "WAREHOUSE_ID", nullable = false)
	private Warehouse warehouse;
	/**
	 * The purchase
	 */
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PURCHASE_ID", referencedColumnName = "ID")
	private Purchase purchase;
	
	/**
	 * The stockOperation
	 */
	@Column(name = "STOCK_STATUS")
	@lombok.ToString.Include
	@Builder.Default
	private EStockReceivedStatus status=EStockReceivedStatus.WAITING;
}
