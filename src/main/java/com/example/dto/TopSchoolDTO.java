package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopSchoolDTO {
    private String schoolId;
    private String schoolName;
    private Double averageGrade;
    private Double attendancePercentage;
}