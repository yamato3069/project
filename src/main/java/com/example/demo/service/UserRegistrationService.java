package com.example.demo.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.entity.LoginUser;
import com.example.demo.form.UserSerchForm;
import com.example.demo.mapper.LoginMapper;

@Service
public class UserRegistrationService {
	@Autowired
	private LoginMapper loginMapper;

	private final SecureRandom random = new SecureRandom();

	public String generateUserID(int length) {
		StringBuilder id = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int digit = random.nextInt(10); // 0から9までのランダムな数値を生成
			id.append(digit);
		}
		return id.toString();
	}

	public LoginUser serchUsers(String name) {
		return loginMapper.serchUsers(name);
	}

	public void validateUserSerch(UserSerchForm userSerchForm, BindingResult result) {
		if(userSerchForm.getName().isEmpty()) {
			result.addError(new FieldError("userSerchForm", "name", "ユーザー名を入力してください。"));
		}else if(userSerchForm.getName().length() > 20 ) {
			result.addError(new FieldError("userSerchForm", "name", "ユーザー名は20文字以内で入力してください。"));
		}
	}
	
	public void update(LoginUser loginUser) {
        loginMapper.update(loginUser);
    }
	
	public void validateUserRegist(UserSerchForm userSerchForm, BindingResult result) {
		if ("9999/99/99".equals(userSerchForm.getStartDate())) {
	        return;
	    }
		if(userSerchForm.getName().isEmpty()) {
			result.addError(new FieldError("userSerchForm", "name", "ユーザー名を入力してください。"));
		}else if(userSerchForm.getName().length() > 20 ) {
			result.addError(new FieldError("userSerchForm", "name", "ユーザー名は20文字以内で入力してください。"));
		}else if (userSerchForm.getName().matches(".*[\\p{InBasicLatin}].*")) {
		    result.addError(new FieldError("userSerchForm", "name", "ユーザー名は全角文字で入力してください。"));
		}
		if(userSerchForm.getId() == null) {
			result.addError(new FieldError("userSerchForm", "id", "ユーザーIDは必須です。"));
		}
		if(userSerchForm.getPassword().isEmpty() || userSerchForm.getPassword().length() > 16 || !userSerchForm.getPassword().matches("[a-zA-Z0-9]*")) {
			result.addError(new FieldError("userSerchForm", "password", "パスワードは半角英数字16文字以内で入力してください。"));
		}
		if(userSerchForm.getRole().isEmpty()) {
			result.addError(new FieldError("userSerchForm", "role", "権限を選択してください。"));
		}
		if(userSerchForm.getStartDate().isEmpty()) {
			result.addError(new FieldError("userSerchForm", "startDate", "利用開始日を入力してください。"));
		}else if(!userSerchForm.getStartDate().matches("\\d{4}/\\d{2}/\\d{2}")) {	
			result.addError(new FieldError("userSerchForm", "startDate", "日付は'yyyy/MM/dd'の形式で入力してください。"));
		}
		try {
	        LocalDate.parse(userSerchForm.getStartDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	    } catch (DateTimeParseException e) {
	        result.addError(new FieldError("userSerchForm", "startDate", "存在しない日付です。"));
	    }
		
	}
	
}
