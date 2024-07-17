package com.example.demo.form;

import lombok.Data;

@Data
public class UserSerchForm {
	//ユーザー名
	private String name;
	
	private Integer id;
	
	private String password;
	
	private String role;

	private String startDate;

}
