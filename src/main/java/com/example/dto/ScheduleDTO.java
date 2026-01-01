package com.example.dto;

import com.example.enums.WeekDay;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ScheduleDTO {
    private String id;
    private String roomNumber;
    private WeekDay weekDay;
    private Integer pairNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean visible;
    private LocalDateTime createdDate;
    private String groupId;
    private Integer subjectId;
    private String teacherId;
    private String schoolId;
}
