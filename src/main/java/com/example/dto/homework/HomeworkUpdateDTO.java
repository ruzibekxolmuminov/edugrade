package com.example.dto.homework;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HomeworkUpdateDTO {
    private String title;
    private String content;
    private LocalDateTime deadline;
    private String attachId;
}
