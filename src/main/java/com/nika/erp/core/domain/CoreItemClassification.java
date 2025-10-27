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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "CORE_ITEM_CLASSIFICATION")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreItemClassification extends AbstractEntity {
	/**
	* The serialVersionUID
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * The displayName
	 */
	@Column(name = "DISPLAY_NAM", nullable = true)
	private String displayName;
	/**
	 * The frenchName
	 */
	@Column(name = "FRENCH_NAME", nullable = true)
	private String frenchName;
	/**
	 * The englishName
	 */
	@Column(name = "ENGLISH_NAME", nullable = true)
	private String englishName;
	/**
	 * The category
	 */
	@Column(name = "CATEGORY", nullable = true)
	private Integer category;
	/**
	 * The description
	 */
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;
	/**
	 * The hierarchy
	 */
	@Column(name = "HIERARCHY", nullable = false)
	private String hierarchy;
	/**
	 * The parent
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_ID", nullable = true)
	private CoreItemClassification parent;

}
