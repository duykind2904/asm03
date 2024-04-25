package com.asm3.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Role;
import com.asm3.repo.RoleRepo;

@Service
@Transactional
public class RoleService {
	@Autowired private RoleRepo repo;
	
	public Role findById(int id) {
		Optional<Role> r = repo.findById(id);
		return r.get();
	}

}
