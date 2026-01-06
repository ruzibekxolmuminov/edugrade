package com.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class GradebookTableDTO {
    private List<String> columns; // Ustun nomlari (Date, Quiz, Midterm va h.k.)
    private List<GradebookRowDTO> rows;
}