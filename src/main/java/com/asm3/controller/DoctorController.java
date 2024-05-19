package com.asm3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asm3.entity.Clinic;
import com.asm3.entity.Doctor;
import com.asm3.dto.DoctorDTO;
import com.asm3.entity.Role;
import com.asm3.entity.Specialization;
import com.asm3.entity.User;
import com.asm3.service.ClinicService;
import com.asm3.service.DoctorService;
import com.asm3.service.RoleService;
import com.asm3.service.SpecializationService;
import com.asm3.service.UserService;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
	@Autowired private UserService userService;
	@Autowired private DoctorService doctorService;
	@Autowired private RoleService roleService;
	@Autowired private ClinicService clinicService;
	@Autowired private SpecializationService specializationService;
	
	@Autowired private PasswordEncoder passwordEncoder;
	
	@PostMapping(value="/saveDoctor")
    public ResponseEntity<String> saveUser(@RequestBody Doctor doctor) {
    	try {
    		
    		User user = doctor.getUser();
    		if(doctor.getId() == 0) { //create new doctor
    			Role role = roleService.findById(2);
        		user = doctor.getUser();
        		user.setRole(role);
        		user.setActive(true);
        		user.setPassword(passwordEncoder.encode(user.getPassword()));
        		user = userService.save(user);
    		}
    		
    		if(doctor.getId() > 0) {
    			User userDB = userService.findById(doctor.getUser().getId());
    			userDB.setFullName(user.getFullName());
    			userDB.setAddress(user.getAddress());
    			userDB.setPhone(user.getPhone());
    			if(user.getPassword() != null) {
        			user.setPassword(passwordEncoder.encode(user.getPassword()));
        		}
    			user = userService.save(userDB);
    		}
    		
    		Clinic clinic = doctor.getClinic();
    		clinic = clinicService.save(clinic);
    		
    		Specialization spe = specializationService.findById(doctor.getSpecialization().getId());
    		
    		doctor.setUser(user);
    		doctor.setClinic(clinic);
    		doctor.setSpecialization(spe);
    		doctorService.save(doctor);
    		
    	} catch (Exception e) {
    		return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
    	}
           
    	return new ResponseEntity<String>("SAVE DOCTOR SUCCESS", HttpStatus.OK);
    }    
	
	@GetMapping(value="countDoctor")
	public ResponseEntity<Long> countDoctor() {
		
		long count = doctorService.countDoctor();
		
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}
	
	@GetMapping(value="/getAllDoctor")
	public ResponseEntity<List<DoctorDTO>> getAllDoctor(@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
													 @RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		try {
			pageNumber = pageNumber - 1;
			List<Doctor> doctors = doctorService.findAllDoctorsWithUser(pageNumber, pageSize);
			
			List<DoctorDTO> dsDTO = new ArrayList<DoctorDTO>();
			for(Doctor doctor : doctors) {
				DoctorDTO doctorDTO = DoctorDTO.convertToDTOBySearch(doctor);
				dsDTO.add(doctorDTO);
			}
			
			return new ResponseEntity<>(dsDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/updateLock")
	public ResponseEntity<String> updateLock(@RequestParam(name="userId",defaultValue = "0") int userId,
			@RequestParam(name="isLock", defaultValue="") boolean isLock,
			@RequestParam(name="descriptionLock", defaultValue="") String descriptionLock) {
		
		try {
			userService.updateLock(userId, isLock, descriptionLock);
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

	@GetMapping(value="/deleteDoctor")
	public ResponseEntity<String> deleteDoctor(@RequestParam(name="doctorId",defaultValue = "0") int doctorId) {
		try {
			doctorService.deleteDoctor(doctorId);
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}
	
}
