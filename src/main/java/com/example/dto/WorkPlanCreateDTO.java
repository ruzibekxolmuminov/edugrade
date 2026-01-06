package com.example.dto;

import lombok.Data;

@Data
public class WorkPlanCreateDTO {
    private Integer subjectId;
    private String schoolId;
    private String title;
    private Integer orderNumber;
}