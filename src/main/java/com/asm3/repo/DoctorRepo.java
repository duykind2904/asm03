package com.asm3.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm3.entity.Doctor;

public interface DoctorRepo extends JpaRepository<Doctor, Integer>{

//	@Query("SELECT d, u FROM Doctor d LEFT JOIN d.user u ORDER BY d.id DESC LIMIT :pageSize OFFSET :pageNumber * :pageSize")
//    List<Doctor> findAllDoctorsWithUser(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize);
	
	@Query("SELECT d FROM Doctor d "
			+ "JOIN FETCH d.user "
			+ "JOIN FETCH d.clinic c "
			+ "JOIN FETCH d.specialization "
			+ "WHERE d.id = :id")
	Doctor findJoinAllById(@Param("id") int id);
	
	@Query("SELECT d.id FROM Doctor d JOIN d.user WHERE d.user.id = :userId")
	int findDoctorIdByUserId(@Param("userId") int userId);
	
}
