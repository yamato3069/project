package com.example.demo.form;

import lombok.Data;

@Data
public class AttendanceForm {
	private String year;
	private String formattedDate;
	private Integer userId;
	private Integer status;
	private Integer startHour;
	private Integer startMinute;
	private Integer endHour;
	private Integer endMinute;
	private String remarks;

}
