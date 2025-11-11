package com.niwe.erp.core.domain;

import java.util.HashSet;
import java.util.Set;

import com.niwe.erp.common.domain.AbstractEntity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.SuperBuilder; 

@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "CORE_USER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CoreUser extends AbstractEntity {/**
	 * The serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The username
	 */
	@Column(name="USERNAME",unique=true, nullable=false)
    private String username;
	/**
	 * The email
	 */
	@Column(name="EMAIL")
    private String email;
    /**
     * The password
     */
    @Column(name="PASSWORD",nullable=false)
    private String password; // encoded
    
    /**
     * The fullname
     */
    @Column(name="FULLNAME")
    private String fullname;
    /**
     * The enabled
     */
    @Column(name="ENABLED",nullable=false)
    @Default
    private boolean enabled = true;

    /**
     * The roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "CORE_USER_ROLE",
        joinColumns = @JoinColumn(name = "USER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
    @Default
    private Set<CoreRole> roles = new HashSet<>();
    
	
	/**
	 * The Taxpayer
	 */
	@ManyToOne
	@JoinColumn(name = "TAXPAYER_ID", nullable = true)
	private CoreTaxpayer taxpayer;

}
