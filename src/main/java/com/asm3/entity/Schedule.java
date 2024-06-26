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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "schedules")
@Getter
@Setter
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name="date")
	private String date;
	
	@Column(name="time")
	private String time;
	
	@Column(name="description")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", referencedColumnName = "id")
	private Doctor doctor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToOne(mappedBy = "schedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Patient patient;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "specializations_id", referencedColumnName = "id")
    private Specialization specialization;
	
	
	
}
