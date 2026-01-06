package com.example.dto;

import com.example.enums.GradeType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GradeCreateDTO {
    private String studentId;
    private Integer subjectId;
    private String scheduleId;
    private LocalDate lessonDate;
    private String groupId;
    private String schoolId;
    private Integer gradeValue;
    private GradeType type;
    private String comment;
}