package com.example.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class JournalRowDTO {
    private String studentId;
    private Map<LocalDate, String> dailyData; // Sanaga bog'langan baho yoki davomat (masalan: "5", "NB")
}