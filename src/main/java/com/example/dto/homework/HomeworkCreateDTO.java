package com.example.dto.homework;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeworkCreateDTO {
    private String groupId;
    private String teacherId;
    private Integer subjectId;
    private String schoolId;
    private String title;
    private String content;
    private String attachId;
    private LocalDateTime deadline;

}
