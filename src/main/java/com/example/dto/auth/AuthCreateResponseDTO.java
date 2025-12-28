package com.example.dto.auth;

import com.example.entity.ProfileEntity;
import com.example.entity.ProfileRoleEntity;
import com.example.enums.ProfileGender;
import com.example.enums.ProfileRoleEnum;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AuthCreateResponseDTO {
    private String id;
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
    private List<ProfileRoleEnum> roles;
}
