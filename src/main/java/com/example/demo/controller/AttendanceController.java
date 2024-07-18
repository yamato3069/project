package com.example.demo.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.AttendanceDayDto;
import com.example.demo.dto.AttendanceDto;
import com.example.demo.form.AttendanceForm;
import com.example.demo.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@RequestMapping("/regist")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {

		System.out.println("regist");

		return "attendance/regist";
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
			model.addAttribute("calendar", calendar);
			List<AttendanceDto> attendanceList = attendanceService.findByYearAndMonth(year, month);
			model.addAttribute("attendanceList", attendanceList);
			// attendanceListの要素をcalendarの要素と比較して値を設定する
			for (AttendanceDayDto day : calendar) {
				for (AttendanceDto attendance : attendanceList) {
					if (day.getDate().equals(attendance.getDate())) {
						day.setUserId(attendance.getUserId());
						day.setId(attendance.getId());
						day.setStatus(attendance.getStatus());
						day.setStartTime(attendance.getStartTime());
						day.setEndTime(attendance.getEndTime());
						day.setRemarks(attendance.getRemarks());
						break;
					}
				}
			}

			//表示年月のフォーマットを整える
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");
			for (AttendanceDayDto day : calendar) {
				String formattedDate = day.getDate().format(formatter);
				System.out.println(formattedDate);
				day.setFormattedDate(formattedDate); // AttendanceDayDtoにformattedDateフィールドを追加してください
			}

//			//開始時刻と終了時刻を時間と分に分ける
//			for (AttendanceDayDto startTime : calendar) {
//
//				String sTimeStr = startTime.toString();
//
//				String[] sParts = sTimeStr.split(":");
//				Integer sHour = Integer.parseInt(sParts[0]);
//				Integer sMinute = Integer.parseInt(sParts[1]);
//				System.out.println(sHour + ":" + sMinute);
//			}

			return "attendance/regist";
		}

	}
	
    @PostMapping("/submit")
    public String submitAttendance(@ModelAttribute AttendanceForm form) {
        System.out.println("登録: " + form);
        for (AttendanceForm.AttendanceRecord record : form.getAttendances()) {
            System.out.println("出勤状況: " + record.getStatus());
            System.out.println("出勤時間: " + record.getStartHour() + ":" + record.getStartMinute());
            System.out.println("退勤時間: " + record.getEndHour() + ":" + record.getEndMinute());
            System.out.println("備考: " + record.getRemarks());
        }
        return "attendance/regist";
    }

}
