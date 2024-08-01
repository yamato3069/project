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
import com.example.demo.entity.AttendanceEntity;
import com.example.demo.mapper.AttendanceMapper;

@Service

public class AttendanceService {

	@Autowired
	private AttendanceMapper attendanceMapper;

	public List<AttendanceDayDto> generateCalendar(int year, int month) {

		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
		List<AttendanceDayDto> calendar = new ArrayList<>();

		for (LocalDate date = firstDayOfMonth; !date.isAfter(lastDayOfMonth); date = date.plusDays(1)) {
			// 初期値として空のフィールドでAttendanceDayDyoを作成
			AttendanceDayDto attendanceDayDto = new AttendanceDayDto(date);
			calendar.add(attendanceDayDto);
		}

		return calendar;
	}

	public List<AttendanceDto> findByYearAndMonth(int year, int month, int userId) {

		List<AttendanceDto> attendanceDtoList = attendanceMapper.findByYearAndMonth(year, month, userId);

		return attendanceDtoList;

	}
	
	// MG側　月次勤怠申請リスト取得
	public List<MonthlyAttendanceReqDto> findAttendanceReq() {

		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceMapper.findAttendanceReq();

		return attendanceReqList;
	}
	
	// 社員、UM側　月次勤怠取得
	public MonthlyAttendanceReqDto findReqById(LocalDate targetYearMonth,Integer id) {

		MonthlyAttendanceReqDto myRequest = attendanceMapper.findReqById(targetYearMonth,id);

		return myRequest;
	}

	public boolean insertAttendance(AttendanceEntity attendanceEntity) {

		int result = attendanceMapper.insertAttendance(attendanceEntity);

		return result > 0;
	}

	public void deleteAttendance(Integer userId, LocalDate date) {

		attendanceMapper.deleteAttendance(userId, date);
	}

	// 『承認申請』ボタン押下
	public boolean approval(Integer userId, LocalDate targetYearMonth, LocalDate date) {

		boolean attendance = attendanceMapper.approval(userId, targetYearMonth, date);

		return attendance;

	}
	
	// 却下後再度『承認申請』ボタン押下
	public boolean approvalAgain(Integer id, LocalDate targetYearMonth) {
		
		boolean again = attendanceMapper.approvalAgain(id, targetYearMonth);
		
		return again;
	};

	public boolean permission(Integer selectedUserId, LocalDate targetYearMonth) {

		boolean permission = attendanceMapper.permission(selectedUserId, targetYearMonth);

		return permission;
	};

	public boolean dismissal(Integer selectedUserId, LocalDate targetYearMonth) {

		boolean dismissal = attendanceMapper.dismissal(selectedUserId, targetYearMonth);

		return dismissal;
	};

	//以下は暇なとき要検証
//	public void validateAttendance(AttendanceFormList attendanceFormList, BindingResult result) {
//		for (int i = 0; i < attendanceFormList.getAttendanceForm().size(); i++) {
//			AttendanceForm form = attendanceFormList.getAttendanceForm().get(i);
//
//			if (form.getStartTime() != null && !form.getStartTime().isEmpty()
//					&& !form.getStartTime().matches("\\d{2}:\\d{2}")) {
//				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].startTime",
//						"出勤時間は'hh:mm'の形式で入力してください。"));
//			}
//			if (form.getEndTime() != null && !form.getEndTime().isEmpty()
//					&& !form.getEndTime().matches("\\d{2}:\\d{2}")) {
//				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].endTime",
//						"退勤時間は'hh:mm'の形式で入力してください。"));
//			}
//			if (form.getRemarks().matches(".*[\\p{InBasicLatin}].*")) {
//				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].remarks",
//						"備考は全角文字で入力してください。"));
//			} else if (form.getRemarks().length() > 20)
//				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].remarks",
//						"備考は20文字以内で入力してください。"));
//		}
//	}

}
