package com.asm3.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	@Column(name="email")
    private String email;
	
	@Column(name="password")
    private String password;
	
	@Column(name="address")
    private String address;
	
	@Column(name="avatar")
    private String avatar;
	
	@Column(name="phone")
    private String phone;
	
	@Column(name="is_active")
    private boolean isActive;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;  
	
}
