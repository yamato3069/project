package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.dto.AttendanceDto;

@Mapper
public interface AttendanceMapper {

	List<AttendanceDto> findByYearAndMonth(@Param("year") int year, @Param("month") int month);
	
}
