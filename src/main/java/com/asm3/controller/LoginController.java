package com.asm3.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asm3.entity.Role;
import com.asm3.entity.User;
import com.asm3.jwt.CustomUserDetails;
import com.asm3.jwt.JwtTokenProvider;
import com.asm3.mail.EmailDetail;
import com.asm3.mail.EmailService;
import com.asm3.payload.LoginRequest;
import com.asm3.payload.LoginResponse;
import com.asm3.payload.RandomStuff;
import com.asm3.service.RoleService;
import com.asm3.service.UserService;

@RestController
@RequestMapping("/auth")
public class LoginController {
	
	@Autowired AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider tokenProvider;
    
    @Autowired private UserService userService;
    
    @Autowired private PasswordEncoder passwordEncoder;
    
    @Autowired private RoleService roleService;

    @PostMapping(value="/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            User user = userService.findByEmail(loginRequest.getEmail());
            if(!user.isActive()) {
            	return new ResponseEntity<Map<String, String>>(Map.of("status", "USER BLOCKED"), HttpStatus.OK);
            }
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
            System.out.println("jwt = " + jwt);
            Map<String, String> jwtJson = new HashMap<>();
            jwtJson.putIfAbsent("token", jwt);
            return new ResponseEntity<Map<String, String>>(jwtJson, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<Map<String, String>>(Map.of("status", "Login failed!"), HttpStatus.OK);
        }
    }    
    
    @Autowired private EmailService emailService;
    @GetMapping("/sendMailForgotPass.json")
    public ResponseEntity<Map<String,String>> sendMailForgotPass(@RequestParam(name="email", defaultValue = "") String email) throws MessagingException {
    	try {
	    	User user = userService.findByEmail(email);
	    	if(user == null) {
	    		return new ResponseEntity<>(Map.of("data", "Email not exist"), HttpStatus.OK);
	    	} 
	    	
	    	String code = generateRandomString();
	    	String resetLink = "localhost:7070/asm3/resetpassword" +  "?id=" + user.getId() + "&code=" + code;
	    	String subject = "Đặt lại mật khẩu";
	    	String emailContent = "Xin chào, Vui lòng click vào đường link sau để đặt lại mật khẩu:\n"
	    	        +  resetLink;
	//    	String emailContent = "<div> Xin chào, Vui lòng click vào đường link sau để đặt lại mật khẩu: </div>"
	//                + "<a href=\"" + resetLink + "\">Click vào đây</a>";
	    	
	    	userService.updateCode(user.getId(), code);
	    	
	    	emailService.sendMail(email, subject, emailContent);
	    	return new ResponseEntity<>(Map.of("data", "SEND MAIL SUCCESS"), HttpStatus.OK);
    	} catch (Exception e) {
    		return new ResponseEntity<>(Map.of("data", e.getMessage()), HttpStatus.OK);
    	}
    }
    
    @PostMapping(value="/updatePassword.json")
    public ResponseEntity<Map<String,String>> updatePassword(@RequestBody LoginRequest loginRequest) {
    	try {
	    	int id = loginRequest.getId();
	        String password = loginRequest.getPassword();
	        String encoderPasword = passwordEncoder.encode(password);
	        userService.updatePassword(id, encoderPasword);
	        
	    	return new ResponseEntity<>(Map.of("data", "RESET SUCCESS"), HttpStatus.OK);
    	} catch (Exception e) {
    		return new ResponseEntity<>(Map.of("data", e.getMessage()), HttpStatus.OK);
    	}
    }    
    
    @GetMapping("/checkEmailRegister")
    public ResponseEntity<Map<String, Boolean>> checkEmailRegister(@RequestParam(name="email", defaultValue = "") String email) {
    	boolean check = userService.checkEmailExist(email);
        return new ResponseEntity<>(Map.of("data", check), HttpStatus.OK);
    }
    
    @PostMapping(value="/saveUser")
    public ResponseEntity<Map<String,String>> saveUser(@RequestBody User user) {
    	String passwordNoEncoder = user.getPassword();
    	try {
    		int idUserDefauld = 1;
    		Role role = roleService.findById(idUserDefauld);
    		user.setPassword(passwordEncoder.encode(user.getPassword()));
    		user.setRole(role);   		
    		user.setActive(false);
        	userService.save(user);
    	} catch (Exception e) {
    		return new ResponseEntity<>(Map.of("data", e.getMessage()), HttpStatus.OK);
    	}
    	
    	this.loginUser(user.getEmail(),passwordNoEncoder);
    	return new ResponseEntity<>(Map.of("data", "SAVE USER SUCCESS"), HttpStatus.OK);
    }    
    
    private void loginUser(String email,String password) {
    	try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
        }
    }
    
    
    

//    @GetMapping("/random")
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public RandomStuff randomStuff(Principal principal){
//    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    	CustomUserDetails cu = (CustomUserDetails) authentication.getPrincipal();
//    	User user = cu.getUser();
//        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
//    }
    
    private String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 20;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString().toUpperCase();
    }
}
