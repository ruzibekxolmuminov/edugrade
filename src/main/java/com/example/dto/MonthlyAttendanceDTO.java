package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyAttendanceDTO {
    private String month;      // Masalan: "Sep", "Oct"
    private Double percentage; // Masalan: 75.0
}