package com.example.demo.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
		
	    private  final SecureRandom random = new SecureRandom();

	    public  String generateUserID(int length) {
	        StringBuilder id = new StringBuilder(length);
	        for (int i = 0; i < length; i++) {
	            int digit = random.nextInt(10); // 0から9までのランダムな数値を生成
	            id.append(digit);
	        }
	        return id.toString();
	    }
	    
	

	}

