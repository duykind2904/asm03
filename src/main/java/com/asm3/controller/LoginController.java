package com.asm3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

//    @PostMapping("/login")
//    public LoginResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
//
//        // Xác thực thông tin người dùng Request lên
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getUsername(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        // Nếu không xảy ra exception tức là thông tin hợp lệ
//        // Set thông tin authentication vào Security Context
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Trả về jwt cho người dùng.
//        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
//        return new LoginResponse(jwt);
//    }
    
    @GetMapping("/login")
    public LoginResponse authenticateUser() {
    	LoginRequest loginRequest = new LoginRequest();
    	loginRequest.setUsername("loda");
    	loginRequest.setPassword("loda");

        // Xác thực thông tin người dùng Request lên
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return new LoginResponse(jwt);
    }

    @GetMapping("/random")
    public RandomStuff randomStuff(){
        return new RandomStuff("JWT Hợp lệ mới có thể thấy được message này");
    }
}
