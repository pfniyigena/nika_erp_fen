package com.nika.erp.common.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4476780039321218901L;

	@JsonIgnore
	@Id
	@UuidGenerator
	@Column(name = "id", nullable = false, unique = true, updatable = false)

	private UUID id;

	/**
	 * This field is used for auditory and logging purposes. It is populated by the
	 * system when an entity instance is created.
	 */
	@JsonIgnore
	@Column(name = "created_at")
	@CreatedDate
	protected LocalDateTime createdAt;
	@JsonIgnore
	@Column(name = "modified_at")
	@LastModifiedDate
	protected LocalDateTime modifiedAt;

	@CreatedBy
	@Basic(optional = true)
	@Column(name = "created_by")
	private String createdBy = "";
	@LastModifiedBy
	@Basic(optional = true)
	@Column(name = "updated_by")
	private String updatedBy = "";
	@JsonIgnore
	@Version
	public int version;

	/**
	 * This constructor is required by JPA. All subclasses of this class will
	 * inherit this constructor.
	 */
	protected AbstractEntity() {
		createdAt = LocalDateTime.now();
		modifiedAt = LocalDateTime.now();
	}

}
