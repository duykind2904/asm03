package com.asm3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asm3.entity.User;
import com.asm3.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired private UserService userService;
	
	@GetMapping(value="countUser")
	public ResponseEntity<Long> countUser() {
		
		long count = userService.countUser();
		
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}
	
	@GetMapping(value="/getAllUser")
	public ResponseEntity<List<User>> getAllUser(@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
													 @RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		pageNumber = pageNumber - 1;
		List<User> users = userService.findAllUser(pageNumber, pageSize);
		
		for(User user: users) {
			user.setRole(null);
		}
		
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@GetMapping(value="/updateLock")
	public ResponseEntity<String> updateLock(@RequestParam(name="userId",defaultValue = "0") int userId,
			@RequestParam(name="isLock", defaultValue="") boolean isLock,
			@RequestParam(name="descriptionLock", defaultValue="") String descriptionLock) {
		
		userService.updateLock(userId, isLock, descriptionLock);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@GetMapping(value="/deleteUser")
	public ResponseEntity<String> deleteUser(@RequestParam(name="id",defaultValue = "0") int id) {
		
		userService.deleteUser(id);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

}
