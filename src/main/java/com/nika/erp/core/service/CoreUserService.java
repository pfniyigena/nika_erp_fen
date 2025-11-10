package com.nika.erp.core.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nika.erp.core.domain.CorePermission;
import com.nika.erp.core.domain.CoreRole;
import com.nika.erp.core.domain.CoreTaxpayer;
import com.nika.erp.core.domain.CoreUser;
import com.nika.erp.core.repository.CorePermissionRepository;
import com.nika.erp.core.repository.CoreRoleRepository;
import com.nika.erp.core.repository.CoreUserRepository;
import com.nika.erp.security.config.AuthenticationFacade;

import jakarta.transaction.Transactional;

@Service
public class CoreUserService {
	private final CorePermissionRepository corePermissionRepository;
	private final CoreUserRepository coreUserRepository;
	private final CoreRoleRepository coreRoleRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationFacade auth;
	public CoreUserService(CorePermissionRepository corePermissionRepository, CoreUserRepository coreUserRepository,
			CoreRoleRepository coreRoleRepository, PasswordEncoder passwordEncoder,AuthenticationFacade auth) {
		this.corePermissionRepository = corePermissionRepository;
		this.coreUserRepository = coreUserRepository;
		this.coreRoleRepository = coreRoleRepository;
		this.passwordEncoder = passwordEncoder;
		this.auth=auth;
	}

	public Page<CoreUser> listUsers(Pageable p) {
		return coreUserRepository.findAll(p);
	}

	public CoreUser createUser(String username, String rawPassword, String fullname, Set<String> roleNames) {
		CoreUser u = new CoreUser();
		u.setUsername(username);
		u.setPassword(passwordEncoder.encode(rawPassword));
		u.setFullname(fullname);
		for (String rn : roleNames) {
			coreRoleRepository.findByName(rn).ifPresent(r -> u.getRoles().add(r));
		}
		return coreUserRepository.save(u);
	}

	public void assignRole(Long userId, String roleId) {
		CoreUser u = coreUserRepository.getReferenceById(UUID.fromString(roleId));
		CoreRole r = coreRoleRepository.getReferenceById(UUID.fromString(roleId));
		u.getRoles().add(r);
		coreUserRepository.save(u);
	}

	@Transactional
	public void assignRoles(String userId, Set<String> roleIds) {
		CoreUser u = coreUserRepository.getReferenceById(UUID.fromString(userId));
		u.getRoles().clear();
		for (String rid : roleIds) {
			CoreRole r = coreRoleRepository.getReferenceById(UUID.fromString(rid));
			u.getRoles().add(r);
		}
		coreUserRepository.save(u);
	}

	public CoreUser findById(String id) {

		return coreUserRepository.getReferenceById(UUID.fromString(id));
	}
	 @Transactional
	public void initUser(CoreTaxpayer coreTaxpayer) {

		if (coreUserRepository.findByUsername("admin").isEmpty()) {
			initRoles();
			CoreUser u = new CoreUser();
			u.setUsername("admin");
			u.setPassword(new BCryptPasswordEncoder().encode("admin"));
			u.setFullname("System Administrator");
			u.setEnabled(true);
			u.setTaxpayer(coreTaxpayer);
			coreRoleRepository.findByName("ROLE_ADMIN").ifPresent(r -> u.getRoles().add(r));
			coreUserRepository.save(u);
		}

	}
	 public CoreUser getCurrentUserEntity() {
	        String username = auth.getUsername();
	        return username == null ? null : coreUserRepository.findByUsername(username).orElse(null);
	    }
	private void initRoles() {

		if (!coreRoleRepository.findByName("ROLE_ADMIN").isPresent()) {
			coreRoleRepository.save(CoreRole.builder().name("ROLE_ADMIN").description("Administrator").build());
		}
		if (!coreRoleRepository.findByName("ROLE_USER").isPresent()) {
			coreRoleRepository.save(CoreRole.builder().name("ROLE_USER").description("Deafult User").build());
		}

		corePermissionRepository
				.saveAll(List.of(CorePermission.builder().name("USER_MANAGE").description("Manage users").build()));
		CoreRole admin=coreRoleRepository.findByName("ROLE_ADMIN").get();
		corePermissionRepository.findByName("USER_MANAGE").ifPresent(r -> admin.getPermissions().add(r));
		coreRoleRepository.save(admin);
	
	}
}
