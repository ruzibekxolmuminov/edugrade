package com.example.dto.profile;

import com.example.enums.ProfileRoleEnum;
import com.example.enums.ProfileStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileInfoDTO {
    private String id;
    private String name;
    private String surname;
    private String patronymic;
    private String username;
    private String phone;
    private String pinfl;
    private String passportSeries;
    private String passportNumber;
    private ProfileStatus status;
    private List<ProfileRoleEnum> roleList;
    private String schoolId;
    private String schoolName;
    private LocalDateTime createdDate;
}