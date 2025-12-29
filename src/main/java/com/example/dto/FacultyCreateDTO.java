package com.example.dto;

import lombok.Data;

@Data
public class FacultyCreateDTO {
    private String id;
    private String name;
    private String description;
    private Boolean visible;
    private String schoolId;
}
