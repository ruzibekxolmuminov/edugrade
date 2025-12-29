package com.example.dto.auth;

import com.example.enums.profile.ProfileAdminRoleEnum;
import com.example.enums.profile.ProfileGender;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AuthAdminCreateDTO {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String password;
    private ProfileGender gender;
    private String schoolId;
    private LocalDate birthDate;
    private String pinfl;
    private String passportSeries;
    private String groupId;
    private String passportNumber;
    private List<ProfileAdminRoleEnum> roles;
}
