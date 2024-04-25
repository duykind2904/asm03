package com.asm3.dto;

import com.asm3.entity.Patient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDTO {
	private int id;
	private String date;
	private String time;
	private String description;
	private String userName;
	private int doctorId;
	private int userId;
	private String descriptionDisease;
	private Patient patient;

}
