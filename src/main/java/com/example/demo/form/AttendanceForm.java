package com.example.demo.form;

import java.time.DayOfWeek;
import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceForm {
	private Integer Id;
	private Integer userId;
	private Integer status;
	private LocalDate date;
	private DayOfWeek dayOfWeek;
	private String startTime;
	private String endTime;
	private String remarks;
	private String formattedDate;
	private String formattedWeek;
}

