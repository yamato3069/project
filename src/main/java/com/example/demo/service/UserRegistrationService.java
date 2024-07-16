package com.example.demo.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.LoginUser;
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

}
