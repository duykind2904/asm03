package com.asm3.dto;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.asm3.entity.Clinic;
import com.asm3.entity.Doctor;
import com.asm3.entity.Schedule;
import com.asm3.entity.Specialization;
import com.asm3.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorDTO {
	private int id;
    private String introduction;
    private String trainingProcess;
    private String achievements;
    private User user;
	private List<Schedule> schedules;
    private Clinic clinic;
    private Specialization specialization;  
    
    private Long chooseNumber;
    
    public static DoctorDTO convertToDTO(Doctor doctor) {
    	DoctorDTO doctorDTO = new DoctorDTO();
    	 doctorDTO.setId(doctor.getId());
    	 doctorDTO.setIntroduction(doctor.getIntroduction());
    	 doctorDTO.setTrainingProcess(doctor.getTrainingProcess());
    	 doctorDTO.setAchievements(doctor.getAchievements());
    	 doctorDTO.setUser(doctor.getUser());
    	 doctorDTO.setSchedules(doctor.getSchedules());
    	 doctorDTO.setClinic(doctor.getClinic());
    	 doctorDTO.setSpecialization(doctor.getSpecialization());
    	
    	return doctorDTO;
    }
    
    public static DoctorDTO convertToDTOBySearch(Doctor doctor) {
    	DoctorDTO doctorDTO = new DoctorDTO();
    	if(doctor.getId() > 0) doctorDTO.setId(doctor.getId());
    	if(doctor.getIntroduction() != null) doctorDTO.setIntroduction(doctor.getIntroduction());
    	if(doctor.getTrainingProcess() != null) doctorDTO.setTrainingProcess(doctor.getTrainingProcess());
    	if(doctor.getAchievements() != null) doctorDTO.setAchievements(doctor.getAchievements());
    	if(doctor.getUser().getId() > 0) {
    		doctorDTO.setUser(doctor.getUser());
    		doctorDTO.getUser().setPassword(null);
    		doctorDTO.getUser().setRole(null);
    		doctorDTO.getUser().setSchedules(null);
    	}
    	if(doctor.getClinic() != null && !(doctor.getClinic() instanceof HibernateProxy)) {
    		doctorDTO.setClinic(doctor.getClinic());
    	}
    	if(doctor.getSpecialization() != null && !(doctor.getSpecialization() instanceof HibernateProxy)) {
    		doctorDTO.setSpecialization(doctor.getSpecialization());
    		doctorDTO.getSpecialization().setDoctor(null);
    	}
    	    	
    	return doctorDTO;
    }
    
    
    public static DoctorDTO convertToDTOByTemplatePatientList(Doctor doctor) {
    	DoctorDTO doctorDTO = new DoctorDTO();
    	if(doctor.getId() > 0) doctorDTO.setId(doctor.getId());
    	if(doctor.getClinic() != null && !(doctor.getClinic() instanceof HibernateProxy)) {
    		doctorDTO.setClinic(doctor.getClinic());
    	}
    	if(doctor.getSpecialization() != null && !(doctor.getSpecialization() instanceof HibernateProxy)) {
    		doctorDTO.setSpecialization(doctor.getSpecialization());
    		doctorDTO.getSpecialization().setDoctor(null);
    	}
    	return doctorDTO;
    }

}
