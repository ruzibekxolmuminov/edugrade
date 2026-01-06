package com.example.dto;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class GradebookRowDTO {
    private String studentId;
    private String studentName;
    // Sanalar va imtihon turlari bo'yicha baholar: "2026-01-12" -> "9", "quiz1" -> "18"
    private Map<String, String> grades = new LinkedHashMap<>();
    
    private Double totalPercentage;
}