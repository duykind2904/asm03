package com.asm3.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm3.entity.Schedule;

public interface ScheduleRepo extends JpaRepository<Schedule, Integer>{
	@Query("SELECT COUNT(s) FROM Schedule s WHERE s.doctor.id = :doctorId")
	Long countUserSchedule(@Param("doctorId") int doctorId);
	
	@Query("SELECT COUNT(s) FROM Schedule s WHERE s.user.id = :userId AND s.doctor.id = :doctorId")
	Long checkScheduleByUserIdAndDoctorId(@Param("userId") int userId, @Param("doctorId") int doctorId);
	
}
