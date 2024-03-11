package com.asm3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asm3.entity.User;
import com.asm3.jwt.CustomUserDetails;
import com.asm3.repo.UserRepo;

@Service
public class UserService implements UserDetailsService{
	@Autowired
    private UserRepo userRepo;
	
	@Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }

	public UserDetails loadUserById(Long userId) {
		User user = userRepo.findById(userId);
        if (user == null) {
            throw new UsernameNotFoundException(userId.toString());
        }
        return new CustomUserDetails(user);
	}
	
	public void save(User user) {
        userRepo.save(user);
    }
}
