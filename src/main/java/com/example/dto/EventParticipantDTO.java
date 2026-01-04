package com.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventParticipantDTO {
    private String studentId;
    private String fullName;
    private String phone;
    private LocalDateTime registrationDate;
}