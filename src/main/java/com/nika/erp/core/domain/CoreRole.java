package com.nika.erp.core.domain;

import java.util.HashSet;
import java.util.Set;

import com.nika.erp.common.domain.AbstractEntity;

import jakarta.persistence.*;
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
@Table(name = "CORE_ROLE")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreRole extends AbstractEntity {/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The name
	 */
	@Column(name = "NAME",unique = true, nullable = false)
    private String name; // e.g. "PRODUCT_VIEW", "INVOICE_EDIT"
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION")
    private String description;
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CORE_ROLE_PERMISSION",
        joinColumns = @JoinColumn(name = "ROLE_ID"),
        inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID"))
	@Default
    private Set<CorePermission> permissions = new HashSet<>();

}
