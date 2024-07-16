package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.LoginUser;
import com.example.demo.form.UserSerchForm;
import com.example.demo.service.LoginService;
import com.example.demo.service.UserRegistrationService;

@Controller
@RequestMapping("/user")
public class UserRegistrationController {
	
	@Autowired
	private UserRegistrationService userRegistrationService;
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/registration") 
		public String registration(@ModelAttribute UserSerchForm userSerchForm,Model model) {
		
		String userId = userRegistrationService.generateUserID(16);
        model.addAttribute("userId", userId);
        model.addAttribute("userSerchForm", userSerchForm); 
        
        List<LoginUser> allUsers = loginService.findAllUsers();
        System.out.println(userSerchForm);

        for (LoginUser user : allUsers) {
            if (user.getName().equals(userSerchForm.getName())) {
                model.addAttribute("user", user); 
                break; 
            }
        }
        
		return "user/registration";
	}
}
//	@RequestMapping(value = "/userSerch", method = RequestMethod.POST)
//	public String userSerch(@ModelAttribute UserSerchForm userSerchForm, Model model) {
//		
//		LoginUser user = userRegistrationService.serchUsers(userSerchForm.getName());
//        model.addAttribute("user", user);
//        System.out.print(userSerchForm);
//        
//		return "user/registration";
//	}

