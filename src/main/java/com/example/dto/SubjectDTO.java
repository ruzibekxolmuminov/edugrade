package com.example.dto;

import com.example.enums.SubjectCategory;
import lombok.Data;

@Data
public class SubjectDTO {
    private String name;
    private String description;
    private SubjectCategory category;
}
