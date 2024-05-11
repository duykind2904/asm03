package com.asm3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Specialization;
import com.asm3.repo.SpecializationRepo;

@Service
@Transactional
public class SpecializationService {
	@Autowired private SpecializationRepo repo;
	
	public List<Specialization> findAll() {
		return repo.findAll();
	}
	
	public Specialization findById(int id) {
		return repo.findById(id).get();
	}
	
	public Specialization findByDoctorId(int doctorId) {
		return repo.findByDoctorId(doctorId);
	}

}
