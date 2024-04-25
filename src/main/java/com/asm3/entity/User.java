package com.asm3.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;  
	
	@Column(name="fullname")
	private String fullName;
	
	@Column(name="code")
	private String code;
	
	@Column(name="description_lock")
	private String descriptionLock;
	
	@Transient
	private String rePassword;
	
	@OneToMany(mappedBy="user")
	private List<Schedule> schedules;
	
}
