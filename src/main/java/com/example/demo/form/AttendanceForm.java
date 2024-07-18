package com.example.demo.form;

import java.util.List;

import lombok.Data;

@Data
public class AttendanceForm {
    private String year;
    private String month;
    private List<String> attendanceDayDto;
//    private List<AttendanceDayDto> attendanceDayDto;
//        private Integer status;
//        private Integer startHour;
//        private Integer startMinute;
//        private Integer endHour;
//        private Integer endMinute;
//        private String remarks;
    }



