package com.example.dto;

import com.example.enums.SubjectCategory;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubjectFullInfoDTO {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createdDate;
    private SubjectCategory category;
}
