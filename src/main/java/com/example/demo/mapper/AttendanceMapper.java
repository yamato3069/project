package com.example.demo.mapper;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.AttendanceDto;

@Mapper
public interface AttendanceMapper {

	List<AttendanceDto> findByYearAndMonth(@Param("year") int year, @Param("month") int month, @Param("userId") int userId);
	
	boolean insertAttendance(@Param("id") Integer id, @Param("userId") Integer userId, 
			@Param("status") Integer status, @Param("date") Date date, @Param("startTime") LocalTime startTime,
			@Param("endTime") LocalTime endTime, @Param("remarks") String remarks);
	
	void deleteAttendance(@Param("id") Integer id);
}
