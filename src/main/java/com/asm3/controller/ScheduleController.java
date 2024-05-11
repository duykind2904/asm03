package com.asm3.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	@Autowired private UserService userService;
	@Autowired private DoctorService doctorService;
	@Autowired private ScheduleService scheduleService;
	@Autowired private PatientService patientService;
	@Autowired private SpecializationService specializationService;
	
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
	
	@GetMapping(value="/checkScheduleByUserIdAndDoctorId")
	public ResponseEntity<Boolean> checkScheduleByUserIdAndDoctorId(
			@RequestParam(name="userId", defaultValue = "0") int userId,
			@RequestParam(name="doctorId", defaultValue = "0") int doctorId) {
		try {
			if(userId > 0 && doctorId > 0) {
				boolean check = scheduleService.checkScheduleByUserIdAndDoctorId(userId, doctorId);
				return new ResponseEntity<Boolean>(check, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
			
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PostMapping(value="/addSchedule")
	public ResponseEntity<String> addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
		if(scheduleDTO.getUserId() > 0 && scheduleDTO.getDoctorId() > 0) {
			User user = userService.findById(scheduleDTO.getUserId());
			Doctor doctor = doctorService.findJoinAllById(scheduleDTO.getDoctorId());
			
			Schedule schedule = ScheduleDTO.convertToEntity(scheduleDTO);
			schedule.setUser(user);
			schedule.setDoctor(doctor);
			schedule.setSpecialization(doctor.getSpecialization());
			
			scheduleService.save(schedule);
			
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("KO", HttpStatus.OK);
	}
	
	@GetMapping(value="/getSpecialzationOutStanding")
	public ResponseEntity<List<Specialization>> getSpecialzationOutStanding() {
		try {
			List<Object[]> objs = scheduleService.getSpecialzationOutStanding();
			
			List<Specialization> sps = new ArrayList<Specialization>();
			for (Object[] obj : objs) {
				Specialization sp = (Specialization) obj[0];
				sp.setDoctor(null);
				sp.setChooseNumber((Long) obj[1]);
				sps.add(sp);
			}
				
			return new ResponseEntity<>(sps, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/getDoctorOutStanding")
	public ResponseEntity<List<DoctorDTO>> getDoctorOutStanding() {
		try {
			List<Object[]> objs = scheduleService.getDoctorOutStanding();
			
			List<DoctorDTO> doctorDTOs = new ArrayList<DoctorDTO>();
			for (Object[] obj : objs) {
				Doctor doctor = (Doctor) obj[0];
				DoctorDTO doctorDTO = DoctorDTO.convertToDTOBySearch(doctor);
				doctorDTO.setChooseNumber((Long) obj[1]);
				doctorDTOs.add(doctorDTO);
			}
				
			return new ResponseEntity<>(doctorDTOs, HttpStatus.OK);
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
