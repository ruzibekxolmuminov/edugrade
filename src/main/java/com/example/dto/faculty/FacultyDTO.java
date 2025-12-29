package com.example.dto.faculty;

import lombok.Data;

@Data
public class FacultyDTO {
    private String name;
    private String description;
    private Boolean visible;
    private String school_id;
}
