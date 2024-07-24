package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AttendanceDayDto;
import com.example.demo.dto.AttendanceDto;
import com.example.demo.dto.MonthlyAttendanceReqDto;
import com.example.demo.mapper.AttendanceMapper;

@Service

public class AttendanceService {
	
	@Autowired
	private AttendanceMapper attendanceMapper;
	
	public List<AttendanceDayDto> generateCalendar(int year, int month) {
		
		// 指定年月から月始めと月末の日付を取得
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

		// 空のインスタンスを生成
		List<AttendanceDayDto> calendar = new ArrayList<>();
		
		for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
			
			// コンストラクタで日付と曜日をセット（それ以外は全部空）
			AttendanceDayDto attendanceDayDto = new AttendanceDayDto(date);
			
			calendar.add(attendanceDayDto);
			
		}

		return calendar;
	}

	public List<AttendanceDto> findByYearAndMonth(int year, int month, int userId) {

		List<AttendanceDto> attendanceDtoList = attendanceMapper.findByYearAndMonth(year, month, userId);		
				
//		System.out.println(attendanceDtoList);

		return attendanceDtoList;

	}
	
	public List<MonthlyAttendanceReqDto> findAttendanceReq() {
		
		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceMapper.findAttendanceReq();
//		
//		System.out.println(attendanceReqList);
		
		return attendanceReqList;
	}

}
