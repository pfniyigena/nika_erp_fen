package com.niwe.erp.core.web.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.niwe.erp.core.domain.CorePermission;
import com.niwe.erp.core.domain.CoreRole;
import com.niwe.erp.core.repository.CorePermissionRepository;
import com.niwe.erp.core.repository.CoreRoleRepository;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.ROLES_URL)
@AllArgsConstructor
public class RoleController {
	private final CoreRoleRepository coreRoleRepository;
	private final CorePermissionRepository corePermissionRepository;

	@GetMapping(path = "/list")
	public String listPermissions(Model model) {
		model.addAttribute("lists", coreRoleRepository.findAll());
		return NiweErpCoreUrlConstants.ROLES_LIST_PAGE;

	}

	@GetMapping("/edit/{id}")
	public String editEditRole(@PathVariable String id, Model model) {
		CoreRole role = coreRoleRepository.findById(UUID.fromString(id)).orElse(new CoreRole());
		model.addAttribute("role", role);
		model.addAttribute("permissions", corePermissionRepository.findAll());
		Map<String, List<CorePermission>> groupedPermissions = corePermissionRepository.findAll().stream()
				.collect(Collectors.groupingBy(p -> p.getName().split("_")[0]));

		model.addAttribute("groups", groupedPermissions);

		return NiweErpCoreUrlConstants.ROLE_ADD_FORM_PAGE;
	}

	@PostMapping
	public String saveRole(@ModelAttribute CoreRole role,
			@RequestParam(required = false, name = "permIds") List<String> permIds) {
		Set<CorePermission> perms = permIds != null
				? new HashSet<>(corePermissionRepository.findAllById(listOfStringUuids(permIds)))
				: new HashSet<>();

		role.setPermissions(perms);
		coreRoleRepository.save(role);

		return "redirect:/admin/roles";
	}

	private List<UUID> listOfStringUuids(List<String> listOfStringUuids) {
		List<UUID> listOfUuids = listOfStringUuids.stream().map(s -> {
			try {
				return UUID.fromString(s);
			} catch (IllegalArgumentException e) {
				log.error("Invalid UUID string:{} ", e.getMessage());
				return null; // Handle invalid UUID strings, e.g., return null or throw a custom exception
			}
		}).filter(java.util.Objects::nonNull) // Filter out any nulls resulting from invalid UUID strings
				.collect(Collectors.toList());
		return listOfUuids;
	}

}
