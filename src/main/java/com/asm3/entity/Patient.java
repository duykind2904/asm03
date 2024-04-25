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
@Table(name = "patients")
@Getter
@Setter
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	@Column(name="status")
    private Boolean status;
	
	@Column(name="description_disease")
	private String descriptionDisease;
	
	@Column(name="exam_status")
	private boolean examStatus;
	
	@Column(name="dec_exam")
	private String decExam;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "schedules_id", referencedColumnName = "id")
    private Schedule schedule;  
}
