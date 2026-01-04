package com.example.dto;

import com.example.enums.EventType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateDTO {
    private String schoolId;
    private String title;
    private String description;
    private String imageId;
    private LocalDateTime eventDate;
    private EventType type;
}