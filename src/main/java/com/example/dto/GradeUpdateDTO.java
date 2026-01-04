package com.example.dto;

import lombok.Data;

@Data
public class GradeUpdateDTO {
    private Integer gradeValue;
    private String comment;
    private String reasonForChange;
}