package com.niwe.erp.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.core.repository.CoreUserRepository;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.USERS_URL)
@AllArgsConstructor
public class UserController {
	private final CoreUserRepository coreUserRepository;

	@GetMapping(path = "/list")
	public String listPermissions(Model model) {
		model.addAttribute("lists", coreUserRepository.findAll());
		return NiweErpCoreUrlConstants.USERS_LIST_PAGE;

	}

}
