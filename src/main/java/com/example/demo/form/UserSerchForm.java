package com.example.demo.form;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class UserSerchForm {
	//ユーザー名
	private String name;
	
	private Integer id;
	
	private String password;
	
	private String role;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;

}
