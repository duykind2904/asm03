package com.asm3.controller;

import java.io.File;

import javax.mail.MessagingException;

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

import com.asm3.entity.Clinic;
import com.asm3.entity.Patient;
import com.asm3.entity.Schedule;
import com.asm3.entity.User;
import com.asm3.mail.EmailService;
import com.asm3.pdf.PdfGenerator;
import com.asm3.service.PatientService;
import com.asm3.service.ScheduleService;

@RestController
@RequestMapping("/patient")
public class PatientController {
	@Autowired private PatientService patientService;
	@Autowired private ScheduleService scheduleService;
	
	@PostMapping(value="/create")
	public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
		try {
			Schedule schedule = scheduleService.findById(patient.getScheduleId());
			patient.setSchedule(schedule);
			
			patient = patientService.save(patient);
			patient.setSchedule(null);
			
			return new ResponseEntity<>(patient, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}
	
	@GetMapping(value="/updateExamination")
	public ResponseEntity<String> updateExamination(
			@RequestParam(name="scheduleId", defaultValue = "0") int scheduleId,
			@RequestParam(name="patientId", defaultValue = "0") int patientId,
			@RequestParam(name="decExam", defaultValue = "") String decExam,
			@RequestParam(name="examStatus", defaultValue = "false") boolean examStatus) throws MessagingException {
		try {
			patientService.updateExam(patientId, examStatus, decExam);
			
			Schedule schedule = scheduleService.findJoinAllById(scheduleId);
			
			String checkMail = this.sendMailExamed(schedule);
			if(checkMail == "File null") {
				return new ResponseEntity<String>("Error create pdf", HttpStatus.OK);
			}
			
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
		}
	}
	
	@Autowired private EmailService emailService;
    private String sendMailExamed(Schedule schedule) throws MessagingException{   
    	User user = schedule.getUser();
    	Clinic clinic = schedule.getDoctor().getClinic();
    	String subject = "Thông tin bệnh án";
    	String emailContent = "Xin chào " + user.getFullName() + ",\n" 
    						+ clinic.getName() + " gửi file thông tin bệnh án cho bạn trong lần khám vừa qua \n"
    						+ "Trân trọng!";
    	
    	File file = PdfGenerator.createPDF(schedule); 
    	
    	if(file == null) {
    		return "File null";
    	}
    	emailService.sendMailWithFile(user.getEmail(), subject, emailContent, file);
    	return "OK";
    }

}
