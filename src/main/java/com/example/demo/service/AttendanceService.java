package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.example.demo.dto.AttendanceDayDto;
import com.example.demo.dto.AttendanceDto;
import com.example.demo.dto.MonthlyAttendanceReqDto;
import com.example.demo.entity.AttendanceEntity;
import com.example.demo.form.AttendanceForm;
import com.example.demo.form.AttendanceFormList;
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

	public List<MonthlyAttendanceReqDto> findAttendanceReq() {

		List<MonthlyAttendanceReqDto> attendanceReqList = attendanceMapper.findAttendanceReq();

		return attendanceReqList;
	}

	public boolean insertAttendance(AttendanceEntity attendanceEntity) {

		int result = attendanceMapper.insertAttendance(attendanceEntity);

		return result > 0;
	}

	public void deleteAttendance(Integer userId, LocalDate date) {

		attendanceMapper.deleteAttendance(userId, date);
	}

	public boolean approval(Integer userId, LocalDate targetYearMonth, LocalDate date) {

		boolean attendance = attendanceMapper.approval(userId, targetYearMonth, date);

		System.out.println("申請");

		return attendance;

	}

	public boolean permission(Integer userId, LocalDate targetYearMonth) {

		boolean permission = attendanceMapper.permission(userId, targetYearMonth);

		return permission;
	};

	public boolean dismissal(Integer userId, LocalDate targetYearMonth) {

		boolean dismissal = attendanceMapper.permission(userId, targetYearMonth);

		return dismissal;
	};

	public void validateAttendance(AttendanceFormList attendanceFormList, BindingResult result) {
		for (int i = 0; i < attendanceFormList.getAttendanceForm().size(); i++) {
			AttendanceForm form = attendanceFormList.getAttendanceForm().get(i);

			if (form.getStartTime() != null && !form.getStartTime().isEmpty()
					&& !form.getStartTime().matches("\\d{2}:\\d{2}")) {
				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].startTime",
						"出勤時間は'hh:mm'の形式で入力してください。"));
			}
			if (form.getEndTime() != null && !form.getEndTime().isEmpty()
					&& !form.getEndTime().matches("\\d{2}:\\d{2}")) {
				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].endTime",
						"退勤時間は'hh:mm'の形式で入力してください。"));
			}
			if (form.getRemarks().matches(".*[\\p{InBasicLatin}].*")) {
				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].remarks",
						"備考は全角文字で入力してください。"));
			} else if (form.getRemarks().length() > 20)
				result.addError(new FieldError("attendanceFormList", "attendanceForm[" + i + "].remarks",
						"備考は20文字以内で入力してください。"));
		}
	}

}
