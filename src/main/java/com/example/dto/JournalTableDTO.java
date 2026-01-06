package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class JournalTableDTO {
    private List<JournalStudentDTO> students; // Talabalar ro'yxati
    private List<LocalDate> lessonDates;      // Ustunlar uchun dars kunlari
    private List<JournalRowDTO> rows;         // Har bir talaba uchun baholar qatori
}