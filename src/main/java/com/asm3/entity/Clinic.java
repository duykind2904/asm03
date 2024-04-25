package com.asm3.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clinics")
@Getter
@Setter
public class Clinic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	@Column(name="name")
    private String name;
	
	@Column(name="address")
    private String address;
	
	@Column(name="phone")
    private String phone;
	
	@Column(name="description")
    private String description;
	
	
}
