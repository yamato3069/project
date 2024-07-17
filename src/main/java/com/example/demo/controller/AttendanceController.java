package com.example.demo.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.AttendanceDayDto;
import com.example.demo.dto.AttendanceDto;
import com.example.demo.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@RequestMapping("/regist")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {

		System.out.println("regist");

		return "attendance/regist";
	}

	@RequestMapping("/regist2")
	public String hello2(@RequestParam(value = "name", defaultValue = "World") String name) {

		System.out.println("regist2");
		return "attendance/regist2";
	}

	@RequestMapping("/regist3")
	public String hello3(@RequestParam(value = "name", defaultValue = "World") String name) {

		System.out.println("regist3");
		return "attendance/regist3";
	}

	@Autowired
	private AttendanceService attendanceService;

	@RequestMapping("/show")
	public String getAttendance(@RequestParam(value = "year", required = false) Integer year,
			@RequestParam(value = "month", required = false) Integer month, Model model) {
		
		if (year == null || month == null) {
			System.out.println("未入力");
			//エラーメッセージの表示
			String errorMessage = "未入力の項目があります";
			model.addAttribute("errorMessage", errorMessage);
			return "attendance/regist";			
		} else {
			System.out.println(year);
			System.out.println(month);
			List<AttendanceDayDto> calendar = attendanceService.generateCalendar(year, month);
			System.out.println(calendar);
			model.addAttribute("calendar", calendar);
			List<AttendanceDto> attendanceList = attendanceService.findByYearAndMonth(year, month);
			model.addAttribute("attendanceList", attendanceList);
			// attendanceListの要素をcalendarの要素と比較して値を設定する
			for (AttendanceDayDto day : calendar) {
				for (AttendanceDto attendance : attendanceList) {
					if (day.getDate().equals(attendance.getDate())) {
						day.setStatus(attendance.getStatus());
						day.setStartTime(attendance.getStartTime());
						day.setEndTime(attendance.getEndTime());
						day.setRemarks(attendance.getRemarks());
						break;
					}
				}
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

		    for (AttendanceDayDto day : calendar) {
		        String formattedDate = day.getDate().format(formatter);
		        System.out.println(formattedDate);
		        day.setFormattedDate(formattedDate); // AttendanceDayDtoにformattedDateフィールドを追加してください
		    }

			return "attendance/regist";
		}

	}

}
