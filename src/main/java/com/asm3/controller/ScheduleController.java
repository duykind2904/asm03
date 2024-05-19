package com.asm3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asm3.dto.DoctorDTO;
import com.asm3.dto.ScheduleDTO;
import com.asm3.entity.Doctor;
import com.asm3.entity.Patient;
import com.asm3.entity.Schedule;
import com.asm3.entity.Specialization;
import com.asm3.entity.User;
import com.asm3.service.DoctorService;
import com.asm3.service.PatientService;
import com.asm3.service.ScheduleService;
import com.asm3.service.SpecializationService;
import com.asm3.service.UserService;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
	@Autowired private ScheduleService scheduleService;
	
	@GetMapping(value="/countUserSchedule")
	public ResponseEntity<Long> countUserSchedule(@RequestParam(name="doctorId", defaultValue = "0") int doctorId) {
		try {
			long count = scheduleService.countUserSchedule(doctorId);
			
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Long>(-1L, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/getUserScheduleByDoctorId")
	public ResponseEntity<List<ScheduleDTO>> getUserScheduleByDoctorId(@RequestParam(name="doctorId", defaultValue = "0") int doctorId,
																@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
																@RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		try {
			pageNumber = pageNumber - 1;
			List<Schedule> schedules = scheduleService.getUserScheduleByDoctorId(doctorId, pageNumber, pageSize);
			
			List<ScheduleDTO> scheduleDTOs = new ArrayList<ScheduleDTO>();
			for(Schedule s : schedules) {
				ScheduleDTO sDTO = ScheduleDTO.convertToDTO(s);
				scheduleDTOs.add(sDTO);
			}
			
			return new ResponseEntity<List<ScheduleDTO>>(scheduleDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/countScheduleByUser")
	public ResponseEntity<Long> countScheduleByUser(@RequestParam(name="userId", defaultValue = "0") int userId) {
		if(userId == 0) {
			return new ResponseEntity<Long>(-1L, HttpStatus.OK);
		}
		
		long count = scheduleService.countScheduleByUser(userId);
		
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}
	
	@GetMapping(value="/getAllScheduleByUser")
	public ResponseEntity<List<ScheduleDTO>> getAllScheduleByUser(
			@RequestParam(name="userId", defaultValue = "0") int userId,
			@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		try {
			pageNumber = pageNumber - 1;
			List<Schedule> schedules = scheduleService.getAllScheduleByUser(userId, pageNumber, pageSize);
			
			List<ScheduleDTO> scheduleDTOs = new ArrayList<ScheduleDTO>();
			for(Schedule s : schedules) {
				ScheduleDTO sDTO = ScheduleDTO.convertToDTOTemplatePatient(s);
				scheduleDTOs.add(sDTO);
			}
			
			return new ResponseEntity<List<ScheduleDTO>>(scheduleDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/deleteSchedule")
	public ResponseEntity<String> deleteSchedule(@RequestParam(name="scheduleId", defaultValue = "0") int scheduleId) {
		try {
			if(scheduleId == 0) {
				return new ResponseEntity<String>("KO", HttpStatus.OK);
			}		
			scheduleService.delete(scheduleId);
			
			return new ResponseEntity<>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	

}
