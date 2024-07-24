package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.example.demo.dto.MonthlyAttendanceReqDto;
import com.example.demo.entity.AttendanceEntity;
import com.example.demo.entity.LoginUser;
import com.example.demo.form.AttendanceForm;
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
			@RequestParam String show, @ModelAttribute AttendanceFormList attendanceFormList, Model model,
			HttpSession session) {

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
		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);

		if (year == null || month == null) {
			System.out.println("未入力");
			//エラーメッセージの表示
			String errorMessage = "未入力の項目があります";
			model.addAttribute("errorMessage", errorMessage);
			return "attendance/regist";

		} else {

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

			AttendanceFormList formList = new AttendanceFormList();
			List<AttendanceForm> form = new ArrayList<AttendanceForm>();
			formList.setAttendanceForm(form);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");
			for (AttendanceDayDto day2 : calendar) {
				AttendanceForm attendanceForm = new AttendanceForm();
				attendanceForm.setId(day2.getId());
				attendanceForm.setUserId(user.getId());
				attendanceForm.setStatus(day2.getStatus());
				attendanceForm.setDate(day2.getDate());
				attendanceForm.setDayOfWeek(day2.getDayOfWeek());
				attendanceForm.setStartTime(day2.getStartTime());
				attendanceForm.setEndTime(day2.getEndTime());
				attendanceForm.setRemarks(day2.getRemarks());
				String formattedDate = day2.getDate().format(formatter);
				attendanceForm.setFormattedDate(formattedDate);
				attendanceForm.setFormattedWeek(day2.getFormattedWeek());
				form.add(attendanceForm);
			}
			formList.setAttendanceForm(form);
			model.addAttribute("formList", formList);
			return "attendance/regist";
		}

	}

	// 『登録』ボタン押下
	@PostMapping(path = "/show", params = "register")
	public String submitAttendance(Integer year, Integer month, @ModelAttribute AttendanceFormList formList,
			@RequestParam String register, Model model, HttpSession session) {

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

		attendanceService.deleteAttendance(userId);

		for (AttendanceForm attendanceForm : formList.getAttendanceForm()) {
			AttendanceEntity newAttendance = new AttendanceEntity();
			newAttendance.setUserId(user.getId());
			newAttendance.setStatus(attendanceForm.getStatus());
			newAttendance.setDate(attendanceForm.getDate());
			newAttendance.setStartTime(attendanceForm.getStartTime());
			newAttendance.setEndTime(attendanceForm.getEndTime());
			newAttendance.setRemarks(attendanceForm.getRemarks());
			attendanceService.insertAttendance(newAttendance);
		}
		model.addAttribute("success", "勤怠登録が完了しました。");

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
