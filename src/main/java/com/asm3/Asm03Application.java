package com.asm3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class Asm03Application {

	public static void main(String[] args) {
		SpringApplication.run(Asm03Application.class, args);
	}

}
