package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendanceStatsDTO {
    private Double attendanceRate; // 95%
    private String status;         // "Good", "Average", "Poor"
    private Long excusedAbsences;  // 3 excused absences
    private Long totalAbsences;    // Jami qoldirilgan darslar
}