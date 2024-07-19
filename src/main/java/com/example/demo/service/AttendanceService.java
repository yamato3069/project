package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AttendanceDayDto;
import com.example.demo.dto.AttendanceDto;
import com.example.demo.mapper.AttendanceMapper;

@Service

public class AttendanceService {

	public List<AttendanceDayDto> generateCalendar(int year, int month) {
		List<AttendanceDayDto> calendar = new ArrayList<>();
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
			// 初期値として空のフィールドでAttendanceDayDyoを作成
			AttendanceDayDto attendanceDayDto = new AttendanceDayDto(date);
			calendar.add(attendanceDayDto);
		}

		return calendar;
	}

	@Autowired
	private AttendanceMapper attendanceMapper;

	public List<AttendanceDto> findByYearAndMonth(int year, int month, int userId) {

		System.out.println("findByYearAndMonth");

		List<AttendanceDto> attendanceDtoList = attendanceMapper.findByYearAndMonth(year, month, userId);
		
//		for(AttendanceDto attendancedto :) {
//			
//		}
		
		System.out.println(attendanceDtoList);

		return attendanceDtoList;

	}

}
