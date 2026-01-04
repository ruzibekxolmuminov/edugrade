package com.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeacherFeedbackDTO {
    private Long id;
    private String teacherId;
    private Integer rate;
    private String content;
    private Boolean isAnonymous;
    private String studentFullName; // Faqat anonim bo'lmasa to'ldiriladi
    private LocalDateTime createdDate;
}