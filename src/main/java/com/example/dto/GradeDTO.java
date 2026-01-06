package com.example.dto;

import com.example.enums.GradeType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GradeDTO {
    private String id;
    private String studentId;
    private String teacherId;
    private String groupId;
    private String subjectId;
    private LocalDate date;
    private String scheduleId;
    private String schoolId;
    private Integer grade;
    private GradeType type;
    private String comment;


}
