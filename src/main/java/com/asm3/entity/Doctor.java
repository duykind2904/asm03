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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doctor")
@Getter
@Setter
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	@Column(name="introduction")
    private String introduction;
	
	@Column(name="training_process")
    private String trainingProcess;
	
	@Column(name="achievements")
    private String achievements;
	
	@Column(name="specialty")
    private String specialty;
	
	@Column(name="is_active")
    private boolean isActive;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;  
	
	@OneToMany(mappedBy="doctor")
	private List<Schedule> schedules;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "clinics_id", referencedColumnName = "id")
    private Clinic clinic;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "specializations_id", referencedColumnName = "id")
    private Specialization specialization;  
	
}
