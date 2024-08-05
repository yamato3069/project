package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
	public String hello(HttpSession session,
			Model model) {
		LoginUser user = (LoginUser) session.getAttribute("user");
		AttendanceFormList attendanceFormList = new AttendanceFormList();
		model.addAttribute("attendanceFormList", attendanceFormList);
		model.addAttribute("user", user);

		// MG側　月次勤怠申請リスト取得
		if (user != null && "Manager".equals(user.getRole())) {
			List<MonthlyAttendanceReqDto> attendanceReqList = attendanceService.findAttendanceReq();
			model.addAttribute("attendanceReqList", attendanceReqList);
			List<LoginUser> usersList = loginService.findAllUsers();
			for (MonthlyAttendanceReqDto req : attendanceReqList) {
				for (LoginUser users : usersList) {
					if (req.getSelectedUserId().equals(users.getId())) {
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

		LoginUser user = (LoginUser) session.getAttribute("user");
		model.addAttribute("user", user);
		int userId = user.getId();
		model.addAttribute("selectedYear", year);
		model.addAttribute("selectedMonth", month);

		if (year == null || month == null) {
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
			// 社員、UM側　月次勤怠取得
			if (user != null && !("Manager".equals(user.getRole()))) {
				// 年と月を組み合わせてLocalDate型を作る
				String targetYearMonthStr = year + "-" + month + "-" + "1";
				LocalDate targetYearMonth = LocalDate.parse(targetYearMonthStr,
						DateTimeFormatter.ofPattern("yyyy-M-d"));

				MonthlyAttendanceReqDto myRequest = attendanceService.findReqById(targetYearMonth, user.getId());
				// 参照できるデータがなかった場合、コンストラクタで 0 をセットする
				if (myRequest == null) {
					myRequest = new MonthlyAttendanceReqDto();
				}

				model.addAttribute("myRequest", myRequest);
			}
			formList.setAttendanceForm(form);
			model.addAttribute("formList", formList);

			// 繰り返し
			//			formList.setAttendanceForm(form);
			//			model.addAttribute("formList", formList);

			AttendanceFormList checkFormList = (AttendanceFormList) model.getAttribute("formList");
			List<AttendanceForm> checkForms = checkFormList.getAttendanceForm();

			boolean flg = true;

			for (AttendanceForm attendanceForm : checkForms) {
				if (attendanceForm.getStatus() == null || attendanceForm.getStartTime().isEmpty()
						|| attendanceForm.getEndTime().isEmpty() || attendanceForm.getRemarks().isEmpty()) {
					flg = false;
					break;
				}
			}
			model.addAttribute("attendanceFlg", flg);

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

		boolean hasErrors = false;


		for (AttendanceForm form : attendanceFormList.getAttendanceForm()) {
		    if (form.getStartTime() != null && !form.getStartTime().isEmpty()) {
		        if (!form.getStartTime().matches("\\d{2}:\\d{2}")) {
		            model.addAttribute("errorStartTime", "出勤時間は'hh:mm'の形式で入力してください。");
		            hasErrors = true;
		        } else {
		            try {
		                LocalTime.parse(form.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
		            } catch (DateTimeParseException e) {
		                model.addAttribute("startTime", "出勤時間は存在しない時間です。");
		                hasErrors = true;
		            }
		        }
		    }

		    if (form.getEndTime() != null && !form.getEndTime().isEmpty()) {
		        if (!form.getEndTime().matches("\\d{2}:\\d{2}")) {
		            model.addAttribute("errorEndTime", "退勤時間は'hh:mm'の形式で入力してください。");
		            hasErrors = true;
		        } else {
		            try {
		                LocalTime.parse(form.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
		            } catch (DateTimeParseException e) {
		                model.addAttribute("endTime", "退勤時間は存在しない時間です。");
		                hasErrors = true;
		            }
		        }
		    }

		    if (form.getRemarks() != null && form.getRemarks().matches(".*[\\p{InBasicLatin}].*")) {
		        model.addAttribute("errorRemarks", "備考は全角文字で入力してください。");
		        hasErrors = true;
		    } else if (form.getRemarks() != null && form.getRemarks().length() > 20) {
		        model.addAttribute("errorRemarks2", "備考は20文字以内で入力してください。");
		        hasErrors = true;
		    }
		    
//		    if(form.getStatus() != 99 && form.getStartTime() || form.getEndTime() || form.getRemarks()) {
//		    	
//		    }
		    
		}

		if (hasErrors) {
		    return getAttendance(year, month, attendanceFormList, model, session);
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
				if (req.getSelectedUserId().equals(users.getId())) {
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

		// 申請の時にステータスを再所得
		MonthlyAttendanceReqDto myRequest = attendanceService.findReqById(targetYearMonth, user.getId());

		// 参照できるデータがなかった場合、コンストラクタで 0 をセットする
		if (myRequest == null) {
			myRequest = new MonthlyAttendanceReqDto();
		}

		// 	却下済みの勤怠にもう一度申請をかけるとき
		if (myRequest.getStatus() == 3) {

			attendanceService.approvalAgain(user.getId(), targetYearMonth);

		} else {
			LocalDate today = LocalDate.now();
			attendanceService.approval(userId, targetYearMonth, today);
		}
		model.addAttribute("approvalSuccess", "申請が完了しました。");
		return "attendance/regist";
	};

	// リンク押下
	@RequestMapping(path = "/request")
	//	@PostMapping(path = "/request")
	public String request(@RequestParam("targetYearMonth") LocalDate targetYearMonth,
			@RequestParam("selectedUserId") Integer selectedUserId,
			@ModelAttribute AttendanceFormList attendanceFormList,
			Model model, HttpSession session) {

		// 年を抽出
		Integer year = targetYearMonth.getYear();
		model.addAttribute("linkedYear", year);
		// 月を抽出
		Integer month = targetYearMonth.getMonthValue();
		model.addAttribute("linkedMonth", month);
		// ログイン情報を再取得
		LoginUser user = (LoginUser) session.getAttribute("user");
				model.addAttribute("user", user);

		List<AttendanceDayDto> calendar = attendanceService.generateCalendar(year, month);

		List<AttendanceDto> attendanceList = attendanceService.findByYearAndMonth(year, month, selectedUserId);

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
		model.addAttribute("targetYearMonth", targetYearMonth);
		model.addAttribute("selectedUserId", selectedUserId);

		List<LoginUser> usersList = loginService.findAllUsers();
		for (MonthlyAttendanceReqDto req : attendanceReqList) {
			for (LoginUser users : usersList) {
				if (req.getSelectedUserId().equals(users.getId())) {
					req.setName(users.getName());
				}
			}
		}

		// リンク押下でエラーが出るのでモデル追加
		MonthlyAttendanceReqDto myRequest = new MonthlyAttendanceReqDto();

		model.addAttribute("myRequest", myRequest);

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

	// 『承認』ボタン押下
	@PostMapping(path = "/show", params = "permission")
	public String permission(@RequestParam("targetYearMonth") LocalDate targetYearMonth,
			@RequestParam("userId") Integer userId, @RequestParam("selectedUserId") Integer selectedUserId, Model model,
			HttpSession session) {

		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceService.findAttendanceReq();
		model.addAttribute("attendanceReqList", attendanceReqList);
		List<LoginUser> usersList = loginService.findAllUsers();
		for (MonthlyAttendanceReqDto req : attendanceReqList) {
			for (LoginUser users : usersList) {
				if (req.getSelectedUserId().equals(users.getId())) {
					req.setName(users.getName());
				}
			}
		}
		// ログイン情報を再取得
		//		LoginUser user = (LoginUser) session.getAttribute("user");
		//		model.addAttribute("user", user);

		attendanceService.permission(selectedUserId, targetYearMonth);
		model.addAttribute("permission", "承認しました。");

		return hello(session, model);

	};

	// 『却下』ボタン押下
	@PostMapping(path = "/show", params = "dismissal")
	public String dismissal(@RequestParam("targetYearMonth") LocalDate targetYearMonth,
			@RequestParam("userId") Integer userId, @RequestParam("selectedUserId") Integer selectedUserId, Model model,
			HttpSession session) {
		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceService.findAttendanceReq();
		model.addAttribute("attendanceReqList", attendanceReqList);
		List<LoginUser> usersList = loginService.findAllUsers();
		for (MonthlyAttendanceReqDto req : attendanceReqList) {
			for (LoginUser users : usersList) {
				if (req.getSelectedUserId().equals(users.getId())) {
					req.setName(users.getName());
				}
			}
		}
		// ログイン情報を再取得
		//		LoginUser user = (LoginUser) session.getAttribute("user");
		//		model.addAttribute("user", user);

		attendanceService.dismissal(
				selectedUserId, targetYearMonth);

		model.addAttribute("dismissal", "却下しました。");

		return hello(session, model);
	};

}
