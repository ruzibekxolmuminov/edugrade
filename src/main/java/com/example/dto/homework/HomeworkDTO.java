package com.example.dto.homework;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeworkDTO {
    private String id;
    private String teacherId;
    private String groupId;
    private Integer subjectId;
    private String schoolId;
    private String title;
    private String content;
    private String attachId;
    private LocalDateTime deadlineDate;
    private LocalDateTime createDate;
}
