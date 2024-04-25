package com.asm3.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.asm3.entity.Doctor;
import com.asm3.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{	
	User findById(Long id);
	
	User findByEmail(String email);
	
	boolean existsByEmail(String email);
	
//	@Query("SELECT u FROM User u WHERE u.doctor_id = :doctorId")
//	User findByDoctor(@Param("doctorId") int doctorId);
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.role.id = 1")
	Long countUser();
	
	@Query("SELECT u FROM User u WHERE u.role.id = 1")
	Page<User> findAllUser(Pageable pageable);
}
