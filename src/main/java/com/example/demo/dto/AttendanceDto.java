package com.example.demo.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AttendanceDto {

	
	//勤怠ID
	private Integer Id;
	//ユーザーID
	private Integer userId;
	//勤務状況
	private Short status;
	//日付
	private LocalDate date;
	//曜日
	private DayOfWeek dayOfWeek;
	//勤務開始時刻
	private LocalTime startTime;
	//勤務終了時刻
	private LocalTime endTime;
	//備考
	private String remarks;
	

}
