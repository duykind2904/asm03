package com.asm3.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asm3.entity.Doctor;
import com.asm3.entity.User;
import com.asm3.jwt.CustomUserDetails;
import com.asm3.repo.UserRepo;

@Service
@Transactional
public class UserService implements UserDetailsService{
	@Autowired
    private UserRepo repo;
	@PersistenceContext private EntityManager entityManager;
	
	@Override
    public UserDetails loadUserByUsername(String email) {
        User user = repo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new CustomUserDetails(user);
    }

	public UserDetails loadUserById(Long userId) {
		User user = repo.findById(userId);
        if (user == null) {
            throw new UsernameNotFoundException(userId.toString());
        }
        return new CustomUserDetails(user);
	}
	
	public User save(User user) {
        return repo.save(user);
    }
	
	public User findByEmail(String email) {
		return repo.findByEmail(email);
	}

	public void updateCode(int id , String code) {
		User userToUpdate = entityManager.find(User.class, id);
		if (userToUpdate != null) {
            userToUpdate.setCode(code);
            entityManager.merge(userToUpdate);
        }
	}
	
	public User findById(int id) {
		Optional<User> u = repo.findById(id);
		return u.get();
	}
	
	public void updatePassword(int id , String encoderPassword) {
		User userToUpdate = entityManager.find(User.class, id);
		if (userToUpdate != null) {
            userToUpdate.setPassword(encoderPassword);
            entityManager.merge(userToUpdate);
        }
	}
	
	public boolean checkEmailExist(String email) {
		return repo.existsByEmail(email);
	}
	
//	public User findByDoctor(int doctorId) {
//		return repo.findByDoctor(doctorId);
//	}
	
	public void updateLock(int id,boolean isLock, String descriptionLock) {		
		User userToUpdate = entityManager.find(User.class, id);
		if (userToUpdate != null) {
            userToUpdate.setActive(isLock);
            userToUpdate.setDescriptionLock(descriptionLock);
            entityManager.merge(userToUpdate);
        }
	}
	
	public long countUser() {
		return repo.countUser();
	}
	
	public List<User> findAllUser(int pageNumber, int pageSize) {
		PageRequest pageable = PageRequest.of(pageNumber, pageSize);
		return repo.findAllUser(pageable).getContent();
	}
	
	public void deleteUser(int id) {
		repo.deleteById(id);
	}
	
	
}
