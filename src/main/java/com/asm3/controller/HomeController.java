package com.asm3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/asm3")
public class HomeController {
	@RequestMapping("/")
	public String home() {
		return "public/index";
	}
	
	@RequestMapping("/login")
	public String login() {
		return "public/login";
	}

}
