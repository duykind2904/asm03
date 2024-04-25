package com.asm3.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clinic")
public class ClinicController {
	
	@PostMapping("/clinic/searchGeneral")
    public String searchGeneral(@RequestParam("keySearch") String keySearch) {
        
		
        return "result"; 
    }
	
	@PostMapping("/clinic/searchSpecial")
    public String searchSpecial(@RequestParam("keySearch") String keySearch) {
        
		
        return "result"; 
    }
}
