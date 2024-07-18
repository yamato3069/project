package com.example.demo.form;

import java.util.List;

import lombok.Data;

@Data
public class AttendanceForm {
    private String year;
    private String month;
    private List<AttendanceRecord> attendances;

    @Data
    public static class AttendanceRecord {
        private Integer status;
        private Integer startHour;
        private Integer startMinute;
        private Integer endHour;
        private Integer endMinute;
        private String remarks;
    }

    @Override
    public String toString() {
        return "AttendanceForm{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", attendances=" + attendances +
                '}';
    }
}
