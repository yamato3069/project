package com.example.demo.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.AttendanceDto;
import com.example.demo.dto.MonthlyAttendanceReqDto;
import com.example.demo.entity.AttendanceEntity;

@Mapper
public interface AttendanceMapper {

	List<AttendanceDto> findByYearAndMonth(@Param("year") int year, @Param("month") int month, @Param("userId") int userId);
	
	int insertAttendance(AttendanceEntity attendanceEntity);
	
	void deleteAttendance(@Param("userId") Integer userId, @Param("date") LocalDate date);
	
	List<MonthlyAttendanceReqDto> findAttendanceReq();
	// 『承認申請』ボタン押下
	boolean approval(Integer userId, LocalDate targetYearMonth, LocalDate date);
	// 却下後再度『承認申請』ボタン押下
	boolean approvalAgain(@Param("id") Integer id ,LocalDate targetYearMonth);
	// 『承認』ボタン押下
	boolean permission(Integer selectedUserId, LocalDate targetYearMonth);
	// 『却下』ボタン押下
	boolean dismissal(Integer selectedUserId, LocalDate targetYearMonth);
	//　UM,社員、月次勤怠申請取得
	MonthlyAttendanceReqDto findReqById( LocalDate targetYearMonth,@Param("id") Integer id);

}
