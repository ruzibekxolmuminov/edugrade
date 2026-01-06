package com.example.dto.auth;

import com.example.enums.profile.ProfileGender;
import com.example.enums.profile.ProfileRoleEnum;
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
    private String email;
    private String phone;
    private String photoId;
    private ProfileGender gender;
    private String schoolId;
    private LocalDate birthDate;
    private String pinfl;
    private String passportSeries;
    private String passportNumber;
    private List<ProfileRoleEnum> roles;
}
