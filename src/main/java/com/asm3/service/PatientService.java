package com.asm3.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Patient;
import com.asm3.repo.PatientRepo;

@Service
@Transactional
public class PatientService {
	@Autowired private PatientRepo repo;
	@PersistenceContext private EntityManager entityManager;
	
	public void updateConfirm(int patientId, boolean status, String descriptionDisease) {
		Patient patientUpdate = entityManager.find(Patient.class, patientId);
		if( patientUpdate != null) {
			patientUpdate.setStatus(status);
			patientUpdate.setDescriptionDisease(descriptionDisease);
			entityManager.merge(patientUpdate);
		}
		
	}
	
	public void updateExam(int patientId, boolean examStatus, String desExam) {
		Patient patientUpdate = entityManager.find(Patient.class, patientId);
		if( patientUpdate != null) {
			patientUpdate.setExamStatus(examStatus);
			patientUpdate.setDecExam(desExam);
			entityManager.merge(patientUpdate);
		}
		
	}
	
}
