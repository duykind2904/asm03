package com.asm3.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asm3.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{
	
}
