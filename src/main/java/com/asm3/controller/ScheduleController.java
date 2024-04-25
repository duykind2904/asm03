package com.asm3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asm3.dto.ScheduleDTO;
import com.asm3.entity.Doctor;
import com.asm3.entity.Schedule;
import com.asm3.service.PatientService;
import com.asm3.service.ScheduleService;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
	@Autowired private ScheduleService scheduleService;
	@Autowired private PatientService patientService;
	
	@GetMapping(value="countUserSchedule")
	public ResponseEntity<Long> countUserSchedule(@RequestParam(name="doctorId", defaultValue = "0") int doctorId) {
		
		long count = scheduleService.countUserSchedule(doctorId);
		
		return new ResponseEntity<Long>(count, HttpStatus.OK);
	}
	
	@GetMapping(value="/getUserScheduleByDoctorId")
	public ResponseEntity<List<ScheduleDTO>> getUserScheduleByDoctorId(@RequestParam(name="doctorId", defaultValue = "0") int doctorId,
																@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
																@RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		pageNumber = pageNumber - 1;
		List<Schedule> schedules = scheduleService.getUserScheduleByDoctorId(doctorId, pageNumber, pageSize);
		
		List<ScheduleDTO> scheduleDTOs = new ArrayList<ScheduleDTO>();
		for(Schedule s : schedules) {
			ScheduleDTO sDTO = new ScheduleDTO();
			sDTO.setId(s.getId());
			sDTO.setDate(s.getDate());
			sDTO.setTime(s.getTime());
			sDTO.setDescription(s.getDescription());
			sDTO.setUserName(s.getUser().getFullName());
			sDTO.setDoctorId(s.getDoctor().getId());
			sDTO.setUserId(s.getUser().getId());
			s.getPatient().setSchedule(null);
			sDTO.setPatient(s.getPatient());
			scheduleDTOs.add(sDTO);
		}
		
		return new ResponseEntity<List<ScheduleDTO>>(scheduleDTOs, HttpStatus.OK);
	}
	
	@GetMapping(value="/updateConfirm")
	public ResponseEntity<String> updateConfirm(@RequestParam(name="patientId", defaultValue = "0") int patientId,
																@RequestParam(name="descriptionDisease", defaultValue = "") String descriptionDisease,
																@RequestParam(name="status", defaultValue = "false") boolean status) {
		
		patientService.updateConfirm(patientId, status, descriptionDisease);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	@GetMapping(value="/updateExamination")
	public ResponseEntity<String> updateExamination(@RequestParam(name="patientId", defaultValue = "0") int patientId,
																@RequestParam(name="decExam", defaultValue = "") String decExam,
																@RequestParam(name="examStatus", defaultValue = "false") boolean examStatus) {
		
		patientService.updateExam(patientId, examStatus, decExam);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	

}
