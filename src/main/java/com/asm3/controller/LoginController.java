package com.asm3.controller;

import java.security.Principal;

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

import com.asm3.entity.User;
import com.asm3.jwt.CustomUserDetails;
import com.asm3.jwt.JwtTokenProvider;
import com.asm3.payload.LoginRequest;
import com.asm3.payload.LoginResponse;
import com.asm3.payload.RandomStuff;
import com.asm3.service.UserService;

@RestController
@RequestMapping("/auth")
public class LoginController {
	
	@Autowired AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider tokenProvider;
    
    @Autowired private UserService userService;

    @PostMapping(value="/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
            return new ResponseEntity<String>("LOGIN SUCCESS", HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<String>("LOGIN FAIL", HttpStatus.OK);
        }
    }    


    @GetMapping("/random")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RandomStuff randomStuff(Principal principal){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	CustomUserDetails cu = (CustomUserDetails) authentication.getPrincipal();
    	User user = cu.getUser();
        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
    }
}
