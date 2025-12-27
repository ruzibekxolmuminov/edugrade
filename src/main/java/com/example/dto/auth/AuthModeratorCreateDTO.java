package com.example.dto.auth;

import com.example.enums.ProfileGender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthModeratorCreateDTO {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String password;
    private ProfileGender gender;
    private String schoolId;
    private LocalDate birthDate;
    private String pinfl;
    private String passportSeries;
    private String passportNumber;
}
