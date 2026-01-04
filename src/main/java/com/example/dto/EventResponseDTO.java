package com.example.dto;

import com.example.enums.EventType;
import com.example.enums.GeneralStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDTO {
    private Integer id;
    private String title;
    private String description;
    private String imageId;
    private LocalDateTime eventDate;
    private EventType type;
    private GeneralStatus status;
}