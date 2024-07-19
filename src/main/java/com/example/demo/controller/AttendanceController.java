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
import com.example.demo.entity.LoginUser;
import com.example.demo.form.AttendanceForm;
import com.example.demo.form.AttendanceFormList;
import com.example.demo.service.AttendanceService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@RequestMapping("/regist")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name, HttpSession session, Model model) {
		LoginUser user = (LoginUser)session.getAttribute("user");
		model.addAttribute("user",user);
		System.out.println("regist");

		return "attendance/regist";
	}


	@Autowired
	private AttendanceService attendanceService;

	@RequestMapping(path = "/show",params ="show")
	public String getAttendance(Integer year, Integer month,@ModelAttribute AttendanceFormList attendanceFormList,@RequestParam String show, Model model, HttpSession session) {

		LoginUser user = (LoginUser)session.getAttribute("user");
		model.addAttribute("user",user);
		int userId = user.getId();
		if (year == null || month == null) {
			System.out.println("未入力");
			//エラーメッセージの表示
			String errorMessage = "未入力の項目があります";
			model.addAttribute("errorMessage", errorMessage);
			return "attendance/regist";
		} else {
//			System.out.println(year);
//			System.out.println(month);
			List<AttendanceDayDto> calendar = attendanceService.generateCalendar(year, month);
			model.addAttribute("calendar", calendar);
			List<AttendanceDto> attendanceList = attendanceService.findByYearAndMonth(year, month, userId);
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
//				System.out.println(formattedDate);
				day.setFormattedDate(formattedDate); // AttendanceDayDtoにformattedDateフィールドを追加してください
			}
			
			return "attendance/regist";
		}

	}
	
    @PostMapping(path = "/show", params = "register")
    public String submitAttendance(@ModelAttribute AttendanceFormList attendanceFormList,@RequestParam String register ,Model model, HttpSession session, @ModelAttribute AttendanceForm attendanceForm) {
		LoginUser user = (LoginUser)session.getAttribute("user");
		model.addAttribute("user",user);
    	System.out.println("登録");
    	System.out.print(attendanceFormList);
    	System.out.print(attendanceForm);
//    	System.out.println(attendanceForm.getAttendanceDayDto());
//    	System.out.println(((AttendanceDayDto) attendanceForm.getAttendanceDayDto()).getStatus());
    	
        return "attendance/regist";
    }

}
