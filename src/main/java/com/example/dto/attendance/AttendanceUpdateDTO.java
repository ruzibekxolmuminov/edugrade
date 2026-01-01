package com.example.dto.attendance;

import com.example.enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceUpdateDTO {
    private AttendanceStatus status;
    private String reason;
}
