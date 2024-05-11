package com.asm3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.asm3.entity.Specialization;
import com.asm3.service.SpecializationService;

@RestController
@RequestMapping("/specialization")
public class SpecializationController {
	@Autowired private SpecializationService specializationService;
	
	@GetMapping(value="getAllSpecialization")
	public ResponseEntity<List<Specialization>> getAllSpecialization() {
		List<Specialization> spes = specializationService.findAll();
		
		spes.forEach(spe -> spe.setDoctor(null));
		
		return new ResponseEntity<>(spes, HttpStatus.OK);
	}

}
