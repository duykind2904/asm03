package com.asm3.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.asm3.dto.DoctorDTO;
import com.asm3.dto.UserDTO;
import com.asm3.entity.Doctor;
import com.asm3.entity.User;
import com.asm3.jwt.CustomUserDetails;
import com.asm3.service.ClinicService;
import com.asm3.service.DoctorService;
import com.asm3.service.UserService;

@Controller
@RequestMapping("/asm3")
public class ATempLateController {
	
	@Autowired private UserService userService;
	@Autowired private DoctorService doctorService;
	
	@RequestMapping("/")
	public String home(Principal p, Model model) {
		int userId = 0;
		if(p != null) {
			Authentication au = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	userId = user.getId();
		}
		
		model.addAttribute("userId", userId);
		return "public/index";
	}
	
	@RequestMapping("/login")
	public String login(Model model, Principal p) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("user", userDTO);
		return "public/login";
	}
	
	@GetMapping("/resetpassword")
	public String resetPassword(@RequestParam(name = "id", defaultValue = "0") int id, Model model,
			@RequestParam(name="code", defaultValue = "") String code) {
		User user = userService.findById(id);
    	if(user == null) {
    		return "User not exist";
    	} 
    	
    	if(user.getCode() == "" || !user.getCode().equals(code)) {
    		return "Code not exist";
    	}
    	
//    	userService.updateCode(user.getId(), "");
		model.addAttribute("id", id);
		return "public/resetpassword";
	}
	
	@RequestMapping("/doctor/create")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String doctorCreate(Model model) {
		UserDTO userDTO = new UserDTO();
		DoctorDTO doctorDTO = new DoctorDTO();
		
		model.addAttribute("user", userDTO);
		model.addAttribute("doctor", doctorDTO);
		return "public/doctor_create";
	}
	
	@GetMapping("/doctor/edit")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
	public String doctorEdit(Principal p, Model model, 
			@RequestParam(name="id",defaultValue = "0") int id) {
		
		if(p != null && id == 0) {
			Authentication au = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	id = doctorService.findDoctorIdByUserId(user.getId());
		}
		Doctor doctor = doctorService.findJoinAllById(id);
		DoctorDTO doctorDTO = DoctorDTO.convertToDTOBySearch(doctor);
		model.addAttribute("doctor", doctorDTO);
		model.addAttribute("mode", "EDIT");
		return "public/doctor_create";
	}
	
	
	@GetMapping("/doctor/detail")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String doctorDetail(Model model, @RequestParam(name="id",defaultValue = "0") int id) {
		Doctor doctor = doctorService.findJoinAllById(id);
		DoctorDTO doctorDTO = DoctorDTO.convertToDTOBySearch(doctor);
		model.addAttribute("doctor", doctorDTO);
		model.addAttribute("mode", "DETAIL");
		return "public/doctor_detail";
	}
	
	
	@RequestMapping("/doctor/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String doctorList(Model model) {
		UserDTO userDTO = new UserDTO();
		DoctorDTO doctorDTO = new DoctorDTO();
		
		model.addAttribute("user", userDTO);
		model.addAttribute("doctor", doctorDTO);
		return "public/doctor_list";
	}
	
	@RequestMapping("/user/list")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String userList(Principal p, Model model) {
		return "public/user_list";
	}
	
	@GetMapping("/user/edit")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String userEdit(Principal p, Model model) {
		if(p != null) {
			Authentication au = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	UserDTO userDTO = UserDTO.convertToDTO(user);
			
			model.addAttribute("user", userDTO);
			return "public/user_edit";
		}
		
		return "Error";		
	}
	
	@GetMapping("/user/detail")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String userDetail(Principal p, Model model,
			@RequestParam(name="id",defaultValue = "0") int id) {
		
		User user = userService.findById(id);
		if(user == null) {
			return "Error";
		}
		
		UserDTO userDTO = UserDTO.convertToDTO(user);
		
		model.addAttribute("user", userDTO);
		return "public/user_detail";
				
	}
	
	@RequestMapping("/schedule/listUserSchedule")
	@PreAuthorize("hasRole('ROLE_DOCTOR')")
	public String listUserSchedule(Principal p, Model model,
			@RequestParam(name="doctorId",defaultValue = "0") int doctorId) {
		if(p != null && doctorId == 0) {
			Authentication au = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	doctorId = doctorService.findDoctorIdByUserId(user.getId());
		}
		
		model.addAttribute("doctorId", doctorId);
		return "public/doctor_listUserSchedule";
	}
	
	
	@PostMapping("/doctor/searchGeneral")
    public String searchGeneral(@RequestParam("keySearch") String keySearch, 
    		Model model, Principal p) {
		int userId = 0;
		if(p != null) {
			Authentication au = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	userId = user.getId();
		}
		
		model.addAttribute("userId", userId);
		model.addAttribute("keySearchGeneral", keySearch);
        return "public/searchGeneral"; 
    }
	
	@PostMapping("/doctor/searchSpecial")
    public String searchSpecial(@RequestParam("keySearch") String keySearch, 
    		Model model, Principal p) {
		
		int userId = 0;
		if(p != null) {
			Authentication au = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	userId = user.getId();
		}
		model.addAttribute("userId", userId);
		model.addAttribute("keySearchSpecial", keySearch);
        return "public/searchSpecial";  
    }
	
	@GetMapping("/patient/list")
	@PreAuthorize("hasRole('ROLE_DOCTOR')")
	public String patientList(Principal p, Model model, 
			@RequestParam(name="userId",defaultValue = "0") int userId) {
		
		if(p != null && userId == 0) {
			Authentication au = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	userId = user.getId();
		}
		
		User user = userService.findById(userId);
		UserDTO userDTO = UserDTO.convertToDTO(user);
		model.addAttribute("user", userDTO);
		
		return "public/patient_list";
	}

}
