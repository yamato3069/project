package com.example.demo.form;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AttendanceForm {

	private Integer Id;
	private Integer userId;
	private Short status;
	private LocalDate date;
	private DayOfWeek dayOfWeek;
	private LocalTime startTime;
	private Integer startHour;
	private Integer startMinute;
	private LocalTime endTime;
	private Integer endHour;
	private Integer endMinute;
	private String remarks;
	private String formattedDate; // フォーマット済みの日付
	private String formattedWeek; // フォーマット済みの曜日
}
