package com.asm3.dto;

import org.hibernate.proxy.HibernateProxy;

import com.asm3.entity.Doctor;
import com.asm3.entity.Patient;
import com.asm3.entity.Schedule;

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
	private Doctor doctor;
	private int doctorId;
	private int userId;
	private String descriptionDisease;
	private Patient patient;
	
	public static ScheduleDTO convertToDTO(Schedule s) {
		ScheduleDTO sDTO = new ScheduleDTO();
		if(s.getId() > 0) sDTO.setId(s.getId());
		if(s.getDate() != null) sDTO.setDate(s.getDate());
		if(s.getTime() != null) sDTO.setTime(s.getTime());
		if(s.getDescription() != null) sDTO.setDescription(s.getDescription());
		if(s.getUser() != null && !(s.getUser() instanceof HibernateProxy)) {
			sDTO.setUserId(s.getUser().getId());
			sDTO.setUserName(s.getUser().getFullName());
		}
		if(s.getPatient() != null && !(s.getPatient() instanceof HibernateProxy)) {
			sDTO.setPatient(s.getPatient());
			sDTO.getPatient().setSchedule(null);
		}
		if(s.getDoctor() != null && !(s.getDoctor() instanceof HibernateProxy)) {
			sDTO.setDoctorId(s.getDoctor().getId());
		}
		
		
		return sDTO;
	}
	
	public static Schedule convertToEntity(ScheduleDTO sDTO) {
		Schedule s = new Schedule();
		if(sDTO.getId() > 0) s.setId(sDTO.getId());
		if(sDTO.getDate() != null) s.setDate(sDTO.getDate());
		if(sDTO.getTime() != null) s.setTime(sDTO.getTime());
		if(sDTO.getDescription() != null) s.setDescription(sDTO.getDescription());
		
		return s;
	}
	
	public static ScheduleDTO convertToDTOTemplatePatient(Schedule s) {
		ScheduleDTO sDTO = new ScheduleDTO();
		if(s.getId() > 0) sDTO.setId(s.getId());
		if(s.getDate() != null) sDTO.setDate(s.getDate());
		if(s.getTime() != null) sDTO.setTime(s.getTime());
		if(s.getDescription() != null) sDTO.setDescription(s.getDescription());
		
		if(s.getPatient() != null && !(s.getPatient() instanceof HibernateProxy)) {
			sDTO.setPatient(s.getPatient());
			sDTO.getPatient().setSchedule(null);
		}
		
		if(s.getDoctor() != null && !(s.getDoctor() instanceof HibernateProxy)) {
			sDTO.setDoctor(s.getDoctor());
			sDTO.getDoctor().setSchedules(null);
			sDTO.getDoctor().setSpecialization(null);
			sDTO.getDoctor().setUser(null);
		}
		
		return sDTO;
	}

}
