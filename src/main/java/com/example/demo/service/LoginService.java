package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.LoginUser;
import com.example.demo.mapper.LoginMapper;

@Service
public class LoginService {
	@Autowired
	private LoginMapper loginMapper;
	
    public LoginUser getLoginUsers(Integer id) {   	
    	
    	return loginMapper.getLoginUsers(id);
    }

}
