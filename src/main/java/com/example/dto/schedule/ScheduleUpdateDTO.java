package com.example.dto.schedule;

import com.example.enums.WeekDay;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ScheduleUpdateDTO {
    private String roomNumber;
    private WeekDay weekDay;
    private Integer pairNumber;
    private LocalTime startTime;
    private LocalTime endTime;
    private String teacherId;
    private Integer subjectId;
    private String groupId;
}
