package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.service.UserRegistrationService;

@Controller
@RequestMapping("/user")
public class UserRegistrationController {
	
	@Autowired
	private UserRegistrationService userRegistrationService;
	
	@RequestMapping("/registration") 
		public String registration(Model model) {
		
		String userId = userRegistrationService.generateUserID(16);
        model.addAttribute("userId", userId);
				
		return "user/registration";
	}

}
