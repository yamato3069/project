package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AttendanceEntity {

	//ユーザーID
	private Integer userId;
	//勤務状況
	private Integer status;
	//日付
	private LocalDate date;
	//勤務開始時刻
	private LocalTime startTime;
	//勤務終了時刻
	private LocalTime endTime;
	//備考
	private String remarks;

}
