package com.nika.erp.core.web.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {
@GetMapping("/login")
public String login() { return "login"; }


@GetMapping("/v2")
public String index() { return "index-2"; }
}