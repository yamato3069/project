package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.LoginUser;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;

@Controller
@RequestMapping("/")
public class LoginController {
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("")
	public String login(@ModelAttribute LoginForm loginForm) {
		return "login/login";
	}
	
	@RequestMapping("login")
	public String loginUser(@ModelAttribute LoginForm loginForm, Model model) {
	    LoginUser user = loginService.getLoginUsers(loginForm.getId());

	    if (user != null && user.getPassword().equals(loginForm.getPassword())) {
	        model.addAttribute("user", user);

	        if ("Admin".equals(user.getRole())) {
	            return "redirect:/user/registration";
	        }

	        return "attendance/regist2";
	        
	    } else {
	        model.addAttribute("error", "ユーザーIDまたはパスワードが正しくありません");
	        return "login/login";
	    }
	}
}
