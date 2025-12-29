package com.example.dto;

import com.example.enums.GroupLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupCreateDTO {
    private String name;
    private GroupLevel level;
    private String mentorId;
    private String schoolId;
    private String facultyId;
}
