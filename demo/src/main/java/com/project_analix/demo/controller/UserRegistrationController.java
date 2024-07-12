package com.project_analix.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserRegistrationController {
	
	@RequestMapping("/registration") 
		public String registration() {
		return "user/registration";
	}

}
