package com.example.demo.entity;
import java.util.Date;

import lombok.Data;

@Data
public class LoginUser {
	
	private Integer id;
	
	private String password;
	
	private String name;
	
	private String role;
	
	private Date start_date;

}
