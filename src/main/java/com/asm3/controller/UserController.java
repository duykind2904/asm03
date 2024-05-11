package com.asm3.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asm3.dto.UserDTO;
import com.asm3.entity.User;
import com.asm3.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired private UserService userService;
	
	@Autowired private PasswordEncoder passwordEncoder;
	
	@GetMapping(value="countUser")
	public ResponseEntity<Long> countUser() {
		try { 
			long count = userService.countUserByRole();
			
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Long>(-1L, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/getAllUser")
	public ResponseEntity<List<UserDTO>> getAllUser(@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
													 @RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		try {
			pageNumber = pageNumber - 1;
			List<User> users = userService.findAllUserByRole(pageNumber, pageSize);
			
			List<UserDTO> userDTOs = new ArrayList<>();
			for(User user: users) {
				UserDTO userDTO = UserDTO.convertToDTO(user);
				userDTOs.add(userDTO);
			}
			
			return new ResponseEntity<List<UserDTO>>(userDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/updateLock")
	public ResponseEntity<String> updateLock(@RequestParam(name="userId",defaultValue = "0") int userId,
			@RequestParam(name="isLock", defaultValue="") boolean isLock,
			@RequestParam(name="descriptionLock", defaultValue="") String descriptionLock) {
		try {
			userService.updateLock(userId, isLock, descriptionLock);
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@GetMapping(value="/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam(name="id",defaultValue = "0") int id) {
		try {
			userService.deleteUser(id);
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/checkEmailRegister")
	public ResponseEntity<Boolean> checkEmailRegister(@RequestParam(name="email",defaultValue = "") String email) {
		if(email.equals("")) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
		
		boolean check = userService.checkEmailExist(email);
		return new ResponseEntity<>(check, HttpStatus.OK);
	}
	
	@PostMapping(value="/saveUser")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		try {
			if(user != null) {
				User userDB = userService.findById(user.getId());
				userDB.setFullName(user.getFullName());
				userDB.setAddress(user.getAddress());
				userDB.setPhone(user.getPhone());
				if(user.getPassword() != null) {
	    			user.setPassword(passwordEncoder.encode(user.getPassword()));
	    		}
				user = userService.save(userDB);
				
				return new ResponseEntity<>("OK", HttpStatus.OK);
			}
			
			return new ResponseEntity<>("KO", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

}
