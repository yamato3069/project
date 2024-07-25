package com.example.demo.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.entity.LoginUser;
import com.example.demo.form.LoginForm;
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
	
    public LoginUser getLoginUsers(String string) {   	
    	
    	return loginMapper.getLoginUsers(string);
    }

    public boolean insertUser(LoginUser loginUser) {
    	int result = sqlSessionTemplate.insert("insertUser", loginUser);
        return result > 0;
    }
    
    public void deleteUser(Integer id) {
    	loginMapper.deleteUser(id);
    }
    
    public void checkLoginUser(LoginForm loginForm, BindingResult result) {
    	if(loginForm.getId().isEmpty()) {
    		result.addError(new FieldError("loginForm", "id", "ユーザーIDを入力してください"));
    	}
    	if(loginForm.getPassword().isEmpty()) {
    		result.addError(new FieldError("loginForm", "password", "パスワードを入力してください"));
    	}
    }
}
