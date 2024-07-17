package com.example.demo.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.LoginUser;
import com.example.demo.mapper.LoginMapper;

@Service
public class LoginService {
	@Autowired
	private LoginMapper loginMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
	
	public List<LoginUser> findAllUsers() {
		return loginMapper.findAllUsers();
	}
	
    public LoginUser getLoginUsers(Integer id) {   	
    	
    	return loginMapper.getLoginUsers(id);
    }

    public boolean insertUser(LoginUser loginUser) {
    	int result = sqlSessionTemplate.insert("insertUser", loginUser);
        return result > 0;
    }
}
