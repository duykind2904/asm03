package com.asm3.repo;

import java.util.List;

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
	Long countUserByRole();
	
	@Query("SELECT u FROM User u WHERE u.role.id = 1 ORDER BY u.id DESC ")
	Page<User> findAllUserByRole(Pageable pageable);
	
	@Query("SELECT u FROM User u "
			+ "JOIN FETCH u.schedules s "
			+ "JOIN FETCH s.doctor d "
			+ "JOIN FETCH d.clinic c "
			+ "WHERE u.id = :id")
	User findJoinAllUser(@Param("id") int id);
	
	@Query("SELECT distinct u FROM User u "
			+ "JOIN FETCH u.role r "
			+ "WHERE r.id = 1 "
			+ "ORDER BY u.id DESC")
	List<User> findAllUserByRole();
}
