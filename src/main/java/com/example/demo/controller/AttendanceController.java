package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.AttendanceDayDto;
import com.example.demo.dto.AttendanceDto;
import com.example.demo.dto.MonthlyAttendanceReqDto;
import com.example.demo.entity.LoginUser;
import com.example.demo.form.AttendanceFormList;
import com.example.demo.service.AttendanceService;
import com.example.demo.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private LoginService loginService;

	// 初期表示
	@RequestMapping("/regist")
	public String hello(@RequestParam(value = "Name", defaultValue = "World") String name, HttpSession session,
			Model model) {
		LoginUser user = (LoginUser) session.getAttribute("user");
		AttendanceFormList attendanceFormList = new AttendanceFormList();
		model.addAttribute("attendanceFormList", attendanceFormList);
		model.addAttribute("user", user);

		// マネージャーのみ。承認申請者の取得
		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceService.findAttendanceReq();
		model.addAttribute("attendanceReqList", attendanceReqList);
		List<LoginUser> usersList = loginService.findAllUsers();
		for (MonthlyAttendanceReqDto req : attendanceReqList) {
			for (LoginUser users : usersList) {
				if (req.getUserId().equals(users.getId())) {
					req.setName(users.getName());
				}
			}
		}

		return "attendance/regist";
	}

	// 『表示』ボタン押下
	@RequestMapping(path = "/show", params = "show")
	public String getAttendance(Integer year, Integer month,
			@RequestParam String show, AttendanceFormList attendanceFormList, Model model, HttpSession session) {

		// マネージャーのみ。承認申請者の取得
		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceService.findAttendanceReq();
		model.addAttribute("attendanceReqList", attendanceReqList);
		List<LoginUser> usersList = loginService.findAllUsers();
		for (MonthlyAttendanceReqDto req : attendanceReqList) {
			for (LoginUser users : usersList) {
				if (req.getUserId().equals(users.getId())) {
					req.setName(users.getName());
				}
			}
		}

		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);
		int userId = user.getId();

		model.addAttribute("attendanceFormList", attendanceFormList);
		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);

		// 年か月が未入力の場合
		if (year == null || month == null) {

			//エラーメッセージの表示
			String errorMessage = "未入力の項目があります";
			model.addAttribute("errorMessage", errorMessage);

			return "attendance/regist";

		} else {

			List<AttendanceDayDto> calendar = attendanceService.generateCalendar(year, month);
			model.addAttribute("calendar", calendar);

			List<AttendanceDto> attendanceList = attendanceService.findByYearAndMonth(year, month, userId);

			//表示年月のフォーマットを整える
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

			// attendanceListの要素をcalendarの要素と比較して値を設定する

			for (AttendanceDayDto day : calendar) {

				String formattedDate = day.getDate().format(formatter);
				day.setFormattedDate(formattedDate);

				for (AttendanceDto attendance : attendanceList) {

					// AttendanceDayDtoリストの表示させる項目にデータの値をセット

					if (day.getDate().equals(attendance.getDate())) {

						day.setUserId(attendance.getUserId());

						day.setId(attendance.getId());

						day.setStatus(attendance.getStatus());

						day.setStartTime(attendance.getStartTime());

						day.setStartHour(attendance.getStartTime().getHour());

						day.setStartMinute(attendance.getStartTime().getMinute());

						day.setEndTime(attendance.getEndTime());

						day.setEndHour(attendance.getEndTime().getHour());

						day.setEndMinute(attendance.getEndTime().getMinute());

						day.setRemarks(attendance.getRemarks());

					}
				}
			}
			return "attendance/regist";
		}

	}

	
	// 『登録』ボタン押下
	@RequestMapping(path = "/show", params = "register")
	public String submitAttendance(Integer year, Integer month,
			@RequestParam String register, Model model, HttpSession session,
			@ModelAttribute AttendanceFormList attendanceFormList) {

		// マネージャーのみ。承認申請者の取得
		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceService.findAttendanceReq();
		model.addAttribute("attendanceReqList", attendanceReqList);
		List<LoginUser> usersList = loginService.findAllUsers();
		for (MonthlyAttendanceReqDto req : attendanceReqList) {
			for (LoginUser users : usersList) {
				if (req.getUserId().equals(users.getId())) {
					req.setName(users.getName());
				}
			}
		}

		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);
		int userId = user.getId();
		model.addAttribute("user", user);
		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);
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
			day.setFormattedDate(formattedDate);
		}

		return "attendance/regist";
	}

	@RequestMapping(path = "/test")
	public String test(Model model, HttpSession session) {
		System.out.println("コントローラー呼び出し");
		// "Property or field 'name' cannot be found on null"を回避
		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);

		return "attendance/regist";
	}

	@RequestMapping(path = "/request")
	public String request(@RequestParam("targetYearMonth") String targetYearMonthStr,
			@RequestParam("userId") String userIdStr, Model model, HttpSession session) {
		LocalDate targetYearMonth = LocalDate.parse(targetYearMonthStr);
		Integer userId = Integer.parseInt(userIdStr);
		Integer targetYear = targetYearMonth.getYear();
		Integer targetMonth = targetYearMonth.getMonthValue();
		System.out.println("リンク押下");
		System.out.println(targetYearMonth);
		System.out.println(targetYear);
		System.out.println(targetMonth);
		System.out.println(userId);
		// "Property or field 'name' cannot be found on null"を回避
		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);
		
		return "attendance/regist";
	};

}
