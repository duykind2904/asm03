package com.asm3.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired private ClinicService clinicService;
	
	@RequestMapping("/")
	public String home(Principal p) {
		return "public/index";
	}
	
	@RequestMapping("/login")
	public String login(Model model) {
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
	public String doctorCreate(Model model) {
		UserDTO userDTO = new UserDTO();
		DoctorDTO doctorDTO = new DoctorDTO();
		
		model.addAttribute("user", userDTO);
		model.addAttribute("doctor", doctorDTO);
		return "public/doctor_create";
	}
	
	@GetMapping("/doctor/edit")
	public String doctorEdit(Model model, @RequestParam(name="id",defaultValue = "0") int id) {
		Doctor doctor = doctorService.findJoinUserById(id);
		model.addAttribute("doctor", doctor);
		return "public/doctor_create";
	}
	
	@GetMapping("/doctor/detail")
	public String doctorDetail(Model model, @RequestParam(name="id",defaultValue = "0") int id) {
		Doctor doctor = doctorService.findJoinUserById(id);
		model.addAttribute("doctor", doctor);
		return "public/doctor_detail";
	}
	
	@RequestMapping("/doctor/list")
	public String doctorList(Model model) {
		UserDTO userDTO = new UserDTO();
		DoctorDTO doctorDTO = new DoctorDTO();
		
		model.addAttribute("user", userDTO);
		model.addAttribute("doctor", doctorDTO);
		return "public/doctor_list";
	}
	
	@RequestMapping("/user/list")
	public String userList(Model model) {
		UserDTO userDTO = new UserDTO();
		DoctorDTO doctorDTO = new DoctorDTO();
		
		model.addAttribute("user", userDTO);
		model.addAttribute("doctor", doctorDTO);
		return "public/user_list";
	}
	
	@GetMapping("/user/edit")
	public String userEdit(Model model, @RequestParam(name="id",defaultValue = "0") int id) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "public/user_edit";
	}
	
	@GetMapping("/user/detail")
	public String userDetail(Model model, @RequestParam(name="id",defaultValue = "0") int id) {
		User user = userService.findById(id);
		model.addAttribute("user", user);
		return "public/user_detail";
	}
	
	@RequestMapping("/schedule/listUserSchedule")
	public String listUserSchedule(Model model, @RequestParam(name="doctorId",defaultValue = "0") int doctorId) {
		model.addAttribute("doctorId", doctorId);
		return "public/doctor_listUserSchedule";
	}
	
	
	
	@RequestMapping("/profile/user")
	public String profileUser(Principal p, Model model) {
		Authentication au = SecurityContextHolder.getContext().getAuthentication();
		if(au != null) {
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	UserDTO userDTO = new UserDTO();
	    	userDTO.setEmail(user.getEmail());
	    	userDTO.setAddress(user.getAddress());
	    	userDTO.setAvatar(user.getAvatar());
	    	userDTO.setPhone(user.getPhone());
	    	userDTO.setFullName(user.getFullName());
	    	
	    	model.addAttribute("user", userDTO);
		}
		
		return "public/profile";
	}
	
	@PostMapping("/doctor/searchGeneral")
    public String searchGeneral(@RequestParam("keySearch") String keySearch, 
    		Model model, Principal p) {
		
		model.addAttribute("keySearchGeneral", keySearch);
        return "public/searchGeneral"; 
    }
	
	@PostMapping("/doctor/searchSpecial")
    public String searchSpecial(@RequestParam("keySearch") String keySearch, 
    		Model model, Principal p) {
		Authentication au = SecurityContextHolder.getContext().getAuthentication();
		int userId = 0;
		if(au != null) {
			CustomUserDetails cu = (CustomUserDetails) au.getPrincipal();
	    	User user = cu.getUser();
	    	userId = user.getId();
		}
		model.addAttribute("userId", userId);
		model.addAttribute("keySearchSpecial", keySearch);
        return "public/searchSpecial";  
    }

}
