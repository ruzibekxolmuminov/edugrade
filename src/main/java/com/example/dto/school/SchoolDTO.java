package com.example.dto.school;

import com.example.enums.SchoolStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchoolDTO {
    private String id;
    private String name;
    private String address;
    private Integer number;
    private Boolean visible;
    private SchoolStatus status;
    private LocalDateTime createdAt;
    private String attachId;
    private Integer regionId;
}
