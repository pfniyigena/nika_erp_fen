package com.niwe.erp.core.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.niwe.erp.core.repository.CorePermissionRepository;
import com.niwe.erp.core.web.util.NiweErpCoreUrlConstants;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value = NiweErpCoreUrlConstants.PERMISSIONS_URL)
@AllArgsConstructor
public class PermissionController {
	private final CorePermissionRepository corePermissionRepository;

    @GetMapping(path = "/list")
    public String listPermissions(Model model) {
        model.addAttribute("lists", corePermissionRepository.findAll());
        return NiweErpCoreUrlConstants.PERMISSIONS_LIST_PAGE; 
         
    }

}
