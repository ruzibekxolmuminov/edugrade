package com.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GradeLogResponseDTO {
    private String whoChangedName;
    private Integer oldGrade;
    private Integer newGrade;
    private LocalDateTime date;
    private String reason;
}