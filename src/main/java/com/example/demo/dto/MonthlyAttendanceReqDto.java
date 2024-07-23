package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MonthlyAttendanceReqDto {
	
	private Integer Id;
	private Integer userId;
	private LocalDate targetYearMonth;
	private LocalDate date;
	private Short status;
	private String name;


}
