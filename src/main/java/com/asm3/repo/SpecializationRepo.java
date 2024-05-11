package com.asm3.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm3.entity.Specialization;

public interface SpecializationRepo extends JpaRepository<Specialization, Integer>{
	
	@Query("SELECT s FROM Specialization s LEFT JOIN s.doctor d WHERE d.id = :doctorId")
	Specialization findByDoctorId(@Param("doctorId") int doctorId);

}
