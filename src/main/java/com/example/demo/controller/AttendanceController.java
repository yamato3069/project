package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

		if (user != null && "Manager".equals(user.getRole())) {
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
		}

		return "attendance/regist";
	}

	// 『表示』ボタン押下
	@RequestMapping(path = "/show", params = "show")
	public String getAttendance(Integer year, Integer month,
			@ModelAttribute AttendanceFormList attendanceFormList, Model model,
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
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			for (AttendanceDayDto day2 : calendar) {
				AttendanceForm attendanceForm = new AttendanceForm();
				attendanceForm.setId(day2.getId());
				attendanceForm.setUserId(user.getId());
				attendanceForm.setStatus(day2.getStatus());
				attendanceForm.setDate(day2.getDate());
				attendanceForm.setDayOfWeek(day2.getDayOfWeek());
				String formattedStartTime = day2.getStartTime() != null ? day2.getStartTime().format(timeFormatter)
						: "";
				String formattedEndTime = day2.getEndTime() != null ? day2.getEndTime().format(timeFormatter) : "";
				attendanceForm.setStartTime(formattedStartTime);
				attendanceForm.setEndTime(formattedEndTime);
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
	public String submitAttendance(Integer year, Integer month, @ModelAttribute AttendanceFormList attendanceFormList,
			@ModelAttribute AttendanceForm attendanceForm, BindingResult result,
			Model model, HttpSession session) {
		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);
		int userId = user.getId();
		model.addAttribute("user", user);
		attendanceService.validateAttendance(attendanceFormList, result);

		if (result.hasErrors()) {
			return "attendance/regist";
		}

		for (AttendanceForm attendanceForm1 : attendanceFormList.getAttendanceForm()) {

			attendanceService.deleteAttendance(userId, attendanceForm1.getDate());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

		for (AttendanceForm attendanceForm1 : attendanceFormList.getAttendanceForm()) {
			AttendanceEntity newAttendance = new AttendanceEntity();
			newAttendance.setUserId(user.getId());
			newAttendance.setStatus(attendanceForm1.getStatus());
			newAttendance.setDate(attendanceForm1.getDate());

			LocalTime startTime = !attendanceForm1.getStartTime().isEmpty()
					? LocalTime.parse(attendanceForm1.getStartTime(), formatter)
					: null;
			LocalTime endTime = !attendanceForm1.getEndTime().isEmpty()
					? LocalTime.parse(attendanceForm1.getEndTime(), formatter)
					: null;

			newAttendance.setStartTime(startTime);
			newAttendance.setEndTime(endTime);
			newAttendance.setRemarks(attendanceForm1.getRemarks());
			attendanceService.insertAttendance(newAttendance);
		}
		model.addAttribute("success", "勤怠登録が完了しました。");
		return getAttendance(year, month, attendanceFormList, model, session);
	}

	// 『承認申請』ボタン押下
	@PostMapping(path = "/show", params = "approval")
	public String approval(Integer year, Integer month, Model model, HttpSession session) {
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
		// ログイン情報を再取得
		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);
		int userId = user.getId();

		String targetYearMonthStr = year + "-" + month + "-" + "1";
		LocalDate targetYearMonth = LocalDate.parse(targetYearMonthStr, DateTimeFormatter.ofPattern("yyyy-M-d"));

		LocalDate today = LocalDate.now();
		attendanceService.approval(userId, targetYearMonth, today);
		model.addAttribute("approvalSuccess", "申請が完了しました。");
		return "attendance/regist";
	};

	// リンク押下
	@RequestMapping(path = "/request")
	public String request(@RequestParam("targetYearMonth") LocalDate targetYearMonth,
			@RequestParam("userId") Integer userId, @ModelAttribute AttendanceFormList attendanceFormList,
			Model model, HttpSession session) {

		// 年を抽出
		Integer year = targetYearMonth.getYear();
		// 月を抽出
		Integer month = targetYearMonth.getMonthValue();
		// ログイン情報を再取得
		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);
		// 

		List<AttendanceDayDto> calendar = attendanceService.generateCalendar(year, month);

		List<AttendanceDto> attendanceList = attendanceService.findByYearAndMonth(year, month, userId);

		// attendanceListの要素をcalendarの要素と比較して値を設定する
		for (AttendanceDayDto day : calendar) {
			for (AttendanceDto attendance : attendanceList) {
				if (day.getDate().equals(attendance.getDate())) {
					day.setId(attendance.getId());
					day.setStatus(attendance.getStatus());
					day.setStartTime(attendance.getStartTime());
					day.setEndTime(attendance.getEndTime());
					day.setRemarks(attendance.getRemarks());
					break;
				}
			}
		}

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

		return getAttendance(year, month, attendanceFormList, model, session);

	}

	// 『承認』ボタン押下
	@PostMapping(path = "/show", params = "permission")
	public String permission(@RequestParam("targetYearMonth") LocalDate targetYearMonth,
			@RequestParam("userId") Integer userId, Model model, HttpSession session) {
		// ログイン情報を再取得
		LoginUser user = (LoginUser) session.getAttribute("user");
		attendanceService.permission(userId, targetYearMonth);
		model.addAttribute("user", user);
		model.addAttribute("permission", "承認しました。");
		return "attendance/regist";
	};

	// 『却下』ボタン押下
	@PostMapping(path = "/show", params = "dismissal")
	public String dismissal(@RequestParam("targetYearMonth") LocalDate targetYearMonth,
			@RequestParam("userId") Integer userId, Model model, HttpSession session) {
		// ログイン情報を再取得
		LoginUser user = (LoginUser) session.getAttribute("user");
		attendanceService.dismissal(userId, targetYearMonth);
		model.addAttribute("user", user);
		model.addAttribute("dismissal", "却下しました。");
		return "attendance/regist";
	};

}
