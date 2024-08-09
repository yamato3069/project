package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MonthlyAttendanceReqDto {
	
	private Integer Id;
	private Integer selectedUserId;
	private LocalDate targetYearMonth;
	private LocalDate date;
	private Short status;
	private String name;
	private String rejectionReason;
	
// コンストラクタ
	public MonthlyAttendanceReqDto(){

		setStatus((short) 0);
	};

}
