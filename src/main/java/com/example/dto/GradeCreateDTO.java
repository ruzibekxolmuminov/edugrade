package com.example.dto;

import lombok.Data;

@Data
public class GradeCreateDTO {
    private String studentId;
    private Integer subjectId;
    private String scheduleId;
    private String schoolId;
    private Integer gradeValue;
    private Integer weight;
    private String comment;
}