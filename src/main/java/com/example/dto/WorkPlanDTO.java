package com.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkPlanDTO {
    private String id;
    private Integer subjectId;
    private String teacherId;
    private String schoolId;
    private String title;
    private Integer orderNumber;
    private LocalDateTime createdDate;
}