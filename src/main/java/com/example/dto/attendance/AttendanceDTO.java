package com.example.dto.attendance;

import com.example.enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceDTO {
    private String id;
    private String studentId;
    private String scheduleId;
    private Integer subjectId;
    private String schoolId;
    private String groupId;
    private AttendanceStatus status;
    private String reason;
    private LocalDate date;
    private LocalDateTime createdAt;
}
