package com.asm3.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.proxy.HibernateProxy;

import com.asm3.entity.Role;
import com.asm3.entity.Schedule;
import com.asm3.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	private int id;
    private String email;
    private String password;
    private String address;
    private String avatar;
    private String phone;
    private boolean isActive;
    private Role role;  
    private String fullName;
	private String code;
	private String rePassword;
	private List<ScheduleDTO> schedules;
	
	public static UserDTO convertToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		if(user.getId() > 0) userDTO.setId(user.getId());
		if(user.getEmail() != null) userDTO.setEmail(user.getEmail());
		if(user.getAddress() != null) userDTO.setAddress(user.getAddress());
		if(user.getAvatar() != null) userDTO.setAvatar(user.getAvatar());
		if(user.getPhone() != null) userDTO.setPhone(user.getPhone());
		userDTO.setActive(user.isActive());
		if(user.getFullName() != null) userDTO.setFullName(user.getFullName());
		
		return userDTO;
	}
	
	public static UserDTO convertToDTOTemplatePatientList(User user) {
		UserDTO userDTO = new UserDTO();
		if(user.getId() > 0) userDTO.setId(user.getId());
		if(user.getEmail() != null) userDTO.setEmail(user.getEmail());
		if(user.getAddress() != null) userDTO.setAddress(user.getAddress());
		if(user.getAvatar() != null) userDTO.setAvatar(user.getAvatar());
		if(user.getPhone() != null) userDTO.setPhone(user.getPhone());
		userDTO.setActive(user.isActive());
		if(user.getFullName() != null) userDTO.setFullName(user.getFullName());
		
		List<Schedule> spes = user.getSchedules();
		List<ScheduleDTO> speDTOs = new ArrayList<ScheduleDTO>();
		spes.forEach(spe -> {
			ScheduleDTO speDTO = ScheduleDTO.convertToDTOTemplatePatient(spe);
			
			speDTOs.add(speDTO);
		});
		
		
		userDTO.setSchedules(speDTOs);
		
		
		return userDTO;
	}

}
