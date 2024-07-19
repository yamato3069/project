package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@GetMapping("/registration")
	public String registration(@ModelAttribute UserSerchForm userSerchForm, Model model) {
	    model.addAttribute("userSerchForm", userSerchForm);
	    return "user/registration";
	}

	@PostMapping("/registration")
	public String handleFormSubmit(@ModelAttribute UserSerchForm userSerchForm,
	                               @RequestParam String action,
	                               Model model) {
	    if ("search".equals(action)) {
	        List<LoginUser> allUsers = loginService.findAllUsers();
	        for (LoginUser user : allUsers) {
	            if (user.getName().equals(userSerchForm.getName())) {
	                model.addAttribute("user", user);
	                break;
	            }
	        }
	    } else if ("register".equals(action)) {
	        if ("9999/99/99".equals(userSerchForm.getStartDate().toString())) {
	            loginService.deleteUser(userSerchForm.getId());
	            model.addAttribute("delete", "ユーザーの削除が完了しました。");
	        } else {
	            List<LoginUser> allUsers = loginService.findAllUsers();
	            boolean alreadyRegist = false;
	            for (LoginUser user : allUsers) {
	                if (user.getName().equals(userSerchForm.getName())) {
	                    alreadyRegist = true;
	                    break;
	                }
	            }
	            if (alreadyRegist) {
	                model.addAttribute("error", "既に登録済みのユーザーです。");
	            } else {
	                LoginUser newUser = new LoginUser();
	                newUser.setId(userSerchForm.getId());
	                newUser.setPassword(userSerchForm.getPassword());
	                newUser.setName(userSerchForm.getName());
	                newUser.setRole(userSerchForm.getRole());

	                try {
	                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	                    Date startDate = dateFormat.parse(userSerchForm.getStartDate());
	                    newUser.setStartDate(startDate);
	                } catch (ParseException e) {
	                    model.addAttribute("error", "日付の形式が正しくありません。");
	                    return "user/registration";
	                }

	                loginService.insertUser(newUser);
	                model.addAttribute("success", "ユーザー登録が完了しました。");
	            }
	        }
	    }

	    if ("search".equals(action)) {
	        String userId = userRegistrationService.generateUserID(5);
	        model.addAttribute("userId", userId);
	    }

	    model.addAttribute("userSerchForm", userSerchForm);

	    return "user/registration";
	}
}
