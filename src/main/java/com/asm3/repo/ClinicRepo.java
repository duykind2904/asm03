package com.asm3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asm3.entity.Clinic;

public interface ClinicRepo extends JpaRepository<Clinic, Integer>{

}
