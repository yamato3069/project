package com.example.demo.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AttendanceDayDto {
	private Integer Id;
	private Integer userId;
	private Short status;
	private LocalDate date;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private LocalTime endTime;
	private String remarks;
	private String formattedDate; // フォーマット済みの日付
	private String formattedDay;

	// コンストラクタ
	public AttendanceDayDto(LocalDate date) {
		this.date = date;
		this.dayOfWeek = date.getDayOfWeek();

	}



}
