package com.asm3.dto;

import com.asm3.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private int id;
    private String email;
    private String password;
    private String address;
    private String avatar;
    private String phone;
    private boolean isActive;
    private Role role;  
    private String fullName;
	private String code;
	private String rePassword;

}
