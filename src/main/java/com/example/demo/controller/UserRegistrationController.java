package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

	@GetMapping("/registration")
	public String registration(@ModelAttribute UserSerchForm userSerchForm, Model model) {
		model.addAttribute("userSerchForm", userSerchForm);
		return "user/registration";
	}

	@PostMapping(path = "/registration", params = "search") //検索用メソッド
	public String searchUser(@ModelAttribute UserSerchForm userSerchForm, BindingResult result, Model model) {
		List<LoginUser> allUsers = loginService.findAllUsers();
		userRegistrationService.validateUserSerch(userSerchForm, result);

		if (result.hasErrors()) {
			return "user/registration";
		}

		for (LoginUser user : allUsers) {
			if (user.getName().equals(userSerchForm.getName())) {
				model.addAttribute("user", user);
				break;
			} else {
				String userId = userRegistrationService.generateUserID(5);
				model.addAttribute("userId", userId);
			}

		}
		return "user/registration";
	}

	@PostMapping(path = "/registration", params = "register") //登録＆削除メソッド
	public String registAndDeleteUser(@ModelAttribute UserSerchForm userSerchForm, BindingResult result, Model model)
			throws ParseException {
		userRegistrationService.validateUserRegist(userSerchForm, result);
		if (result.hasErrors()) {
			return "user/registration";
		}

		if ("9999/99/99".equals(userSerchForm.getStartDate().toString())) {
			loginService.deleteUser(userSerchForm.getId());
			model.addAttribute("delete", "ユーザーの削除が完了しました。");
		} else {
			LoginUser existingUser = userRegistrationService.serchUsers(userSerchForm.getName());
			if (existingUser != null) {
				existingUser.setPassword(userSerchForm.getPassword());
				existingUser.setRole(userSerchForm.getRole());

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date startDate = dateFormat.parse(userSerchForm.getStartDate());
				existingUser.setStartDate(startDate);

				userRegistrationService.update(existingUser);
				model.addAttribute("success", "ユーザー情報の更新が完了しました。");
			} else {
				LoginUser newUser = new LoginUser();
				newUser.setId(userSerchForm.getId());
				newUser.setPassword(userSerchForm.getPassword());
				newUser.setName(userSerchForm.getName());
				newUser.setRole(userSerchForm.getRole());

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				Date startDate = dateFormat.parse(userSerchForm.getStartDate());
				newUser.setStartDate(startDate);

				loginService.insertUser(newUser);
				model.addAttribute("success", "ユーザー登録が完了しました。");
			}
		}
		return "user/registration";
	}

}
