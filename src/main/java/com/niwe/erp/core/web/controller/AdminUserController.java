package com.niwe.erp.core.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.niwe.erp.core.domain.CoreUser;
import com.niwe.erp.core.repository.CoreRoleRepository;
import com.niwe.erp.core.service.CoreUserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
	private final CoreUserService coreUserService;
	private final CoreRoleRepository coreRoleRepository;

	public AdminUserController(CoreUserService coreUserService, CoreRoleRepository coreRoleRepository) {
		this.coreUserService = coreUserService;
		this.coreRoleRepository = coreRoleRepository;
	}

	@GetMapping
	public String list(Model m, Pageable p) {
		Page<CoreUser> page = coreUserService.listUsers(p);
		m.addAttribute("users", page);
		return "admin/users/list";
	}

	@GetMapping("/create")
	public String createForm(Model m) {
		m.addAttribute("allRoles", coreRoleRepository.findAll());
		m.addAttribute("userForm", new UserForm());
		return "admin/users/form";
	}

	@PostMapping("/create")
	public String createSubmit(@ModelAttribute UserForm form, RedirectAttributes ra) {
		coreUserService.createUser(form.getUsername(), form.getPassword(), form.getFullname(), form.getRoleNames());
		ra.addFlashAttribute("success", "User created");
		return "redirect:/admin/users";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable String id, Model m) {
		CoreUser u=coreUserService.findById(id);
		m.addAttribute("user", u);
		m.addAttribute("allRoles", coreRoleRepository.findAll());
		return "admin/users/edit";
	}

	@PostMapping("/assignRoles/{id}")
	public String assignRoles(@PathVariable String id, @RequestParam(required = false) Set<String> roleIds,
			RedirectAttributes ra) {
		coreUserService.assignRoles(id, roleIds == null ? new HashSet<>() : roleIds);
		ra.addFlashAttribute("success", "Roles updated");
		return "redirect:/admin/users";
	}

// DTO inner-class for form binding
	public static class UserForm {
		private String username;
		private String password;
		private String fullname;
		private Set<String> roleNames = new HashSet<>();

// getters/setters
		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getFullname() {
			return fullname;
		}

		public void setFullname(String fullname) {
			this.fullname = fullname;
		}

		public Set<String> getRoleNames() {
			return roleNames;
		}

		public void setRoleNames(Set<String> roleNames) {
			this.roleNames = roleNames;
		}
	}
}