package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.LoginUser;
import com.example.demo.form.LoginForm;
import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpSession;

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
	public String loginUser(@ModelAttribute LoginForm loginForm, BindingResult result, Model model,
			HttpSession session) {
		LoginUser user = loginService.getLoginUsers(loginForm.getId());
		loginService.checkLoginUser(loginForm, result);
		if (result.hasErrors()) {
			return "login/login";
		}

		if (user != null && user.getPassword().equals(loginForm.getPassword())) {
			session.setAttribute("user", user);

			if ("Admin".equals(user.getRole())) {
				return "redirect:/user/registration";
			}

			return "redirect:/attendance/regist";

		} else {
			model.addAttribute("error", "ユーザーIDまたはパスワードが正しくありません");
			return "login/login";
		}
	}
}
