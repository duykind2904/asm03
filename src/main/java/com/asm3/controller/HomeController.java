package com.asm3.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
import com.asm3.entity.Schedule;
import com.asm3.entity.Specialization;
import com.asm3.entity.User;
import com.asm3.service.DoctorService;
import com.asm3.service.ScheduleService;
import com.asm3.service.UserService;

@RestController
@RequestMapping("/asm3")
public class HomeController {

	@Autowired private UserService userService;
	@Autowired private DoctorService doctorService;
	@Autowired private ScheduleService scheduleService;
	
	@GetMapping(value="/schedule/checkScheduleByUserIdAndDoctorId")
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
	
	@PostMapping(value="/schedule/addSchedule")
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
	
	@GetMapping(value="/schedule/getSpecialzationOutStanding")
	public ResponseEntity<List<Specialization>> getSpecialzationOutStanding() {
		try {
			List<Object[]> objs = scheduleService.getSpecialzationOutStanding();
			
			List<Specialization> sps = new ArrayList<Specialization>();
			for (Object[] obj : objs) {
				Specialization sp = new Specialization();
				sp.setId((int) obj[0]);
				sp.setName((String) obj[1]);
				BigInteger bigint = (BigInteger) obj[2];
				sp.setChooseNumber(bigint.longValue());
								
				sps.add(sp);
			}
				
			return new ResponseEntity<>(sps, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/schedule/getDoctorOutStanding")
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
	
	@GetMapping(value="/doctor/countDoctorBySearchSpecial")
	public ResponseEntity<Long> countDoctorBySearchSpecial(@RequestParam(name="keySearch", defaultValue = "") String keySearch) {
		try {
			long count = doctorService.countDoctorBySearchSpecial(keySearch);
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(-1L, HttpStatus.OK);
		}
		
	}
	
	@GetMapping(value="/doctor/getAllDoctorBySearchSpecial")
	public ResponseEntity<List<DoctorDTO>> getAllDoctorBySearchSpecial(@RequestParam(name="keySearch", defaultValue = "") String keySearch,
				@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
				@RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		try {
			pageNumber = pageNumber - 1;
			List<Doctor> doctors = doctorService.findAllDoctorsWithUserBySearchSpecial(keySearch, pageNumber, pageSize);
			
			List<DoctorDTO> dsDTO = new ArrayList<DoctorDTO>();
			for(Doctor doctor : doctors) {
				DoctorDTO doctorDTO = DoctorDTO.convertToDTOBySearch(doctor);
				dsDTO.add(doctorDTO);
			}
			
			return new ResponseEntity<List<DoctorDTO>>(dsDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/doctor/countDoctorBySearchGeneral")
	public ResponseEntity<Long> countDoctorBySearchGeneral(@RequestParam(name="keySearch", defaultValue = "") String keySearch) {
		try {
			long count = doctorService.countDoctorBySearchGeneral(keySearch);
			return new ResponseEntity<Long>(count, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(-1L, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/doctor/getAllDoctorBySearchGeneral")
	public ResponseEntity<List<DoctorDTO>> getAllDoctorBySearchGeneral(@RequestParam(name="keySearch", defaultValue = "") String keySearch,
				@RequestParam(name="pageNumber", defaultValue = "1") int pageNumber,
				@RequestParam(name="pageSize", defaultValue = "0") int pageSize) {
		try {
			pageNumber = pageNumber - 1;
			List<Doctor> doctors = doctorService.findAllDoctorsWithUserBySearchGeneral(keySearch, pageNumber, pageSize);
			
			List<DoctorDTO> dsDTO = new ArrayList<DoctorDTO>();
			for(Doctor doctor : doctors) {
				DoctorDTO doctorDTO = DoctorDTO.convertToDTOBySearch(doctor);
				dsDTO.add(doctorDTO);
			}
			
			return new ResponseEntity<List<DoctorDTO>>(dsDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
}
