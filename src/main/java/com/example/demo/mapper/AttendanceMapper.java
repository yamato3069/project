package com.example.demo.mapper;

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
	
	void deleteAttendance(@Param("userId") Integer userId);
	
	List<MonthlyAttendanceReqDto> findAttendanceReq();
}
