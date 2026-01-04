package com.example.dto;

import lombok.Data;

@Data
public class GradeSubmissionDTO {
    private Integer submissionId;
    private Integer gradeValue; // Grade moduliga yuborish uchun
    private String feedback;
}