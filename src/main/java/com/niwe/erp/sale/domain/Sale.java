package com.niwe.erp.sale.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.niwe.erp.common.domain.AbstractEntity;
import com.niwe.erp.common.domain.PaymentStatus;
import com.niwe.erp.core.domain.CoreTaxpayer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "SALE_SALE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Sale extends AbstractEntity {
	/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The itemNumber
	 */
	@Column(name = "ITEM_NUMBER", nullable = false)
	private Integer itemNumber;
	/**
	 * The internalCode
	 */
	@Column(name = "INTERNAL_CODE", nullable = false, unique = true)
	private String internalCode;
	/**
	 * The customerName
	 */
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	/**
	 * The confirmedBy
	 */
	@Column(name = "CONFIRMED_BY")
	private String confirmedBy;
	/**
	 * The saleDate
	 */
	@Column(name = "SALE_DATE")
	private LocalDateTime saleDate;
	/**
	 * The items
	 */
	@OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
	@Builder.Default
	@ToString.Exclude
	private List<SaleItem> items = new ArrayList<>();
	/**
	 * The status
	 */
	@Column(name = "SALE_STATUS", nullable = true)
	@Builder.Default
	private SaleStatus status = SaleStatus.DRAFT;
	/**
	 * The paymentStatus
	 */
	@Column(name = "PAYMENT_STATUS", nullable = true)
	@Builder.Default
	private PaymentStatus paymentStatus = PaymentStatus.PENDING;
	/**
	 * The shelf
	 */
	@ManyToOne
	@JoinColumn(name = "SHELF_ID", nullable = true)
	private Shelf shelf;

	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = true)
	private CoreTaxpayer taxpayer;
}
