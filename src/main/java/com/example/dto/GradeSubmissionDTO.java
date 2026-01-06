package com.example.dto;

import lombok.Data;

@Data
public class GradeSubmissionDTO {
    private Integer submissionId;
    private Integer gradeValue;
    private String feedback;
}