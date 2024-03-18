package com.asm3.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asm3.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{	
	User findById(Long id);
	
	User findByEmail(String email);
	
	
}
