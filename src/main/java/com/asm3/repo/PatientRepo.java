package com.asm3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asm3.entity.Patient;

public interface PatientRepo extends JpaRepository<Patient, Integer>{

}
