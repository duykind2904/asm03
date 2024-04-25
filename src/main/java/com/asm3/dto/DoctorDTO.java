package com.asm3.dto;

import java.util.List;

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

}
