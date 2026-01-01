package com.example.dto.attendance;

import com.example.enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceCreateDTO {
    private String studentId;
    private String scheduleId;
    private String schoolId;
    private String groupId;
    private AttendanceStatus status;
    private String reason;
    private LocalDate date;

}
