package com.example.dto;

import com.example.enums.GroupLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupDTO {
    private String id;
    private String name;
    private GroupLevel level;
    private Boolean visible;
    private LocalDateTime createdAt;
    private String mentorId;
    private String schoolId;
    private String facultyId;
}
