package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SchoolStatsDTO {
    private Long studentCount;
    private Long teacherCount;
    private Double averageGPA;
    private Double attendancePercentage;
}