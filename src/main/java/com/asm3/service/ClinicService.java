package com.asm3.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Clinic;
import com.asm3.repo.ClinicRepo;

@Service
@Transactional
public class ClinicService {
	@Autowired private ClinicRepo repo;
	@PersistenceContext private EntityManager entityManager;
	
	public Clinic save(Clinic clinic) {
		return repo.save(clinic);
	}
	
}
