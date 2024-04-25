package com.asm3.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {
	private int id;
    private String email;
    private String password;
}
