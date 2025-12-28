package com.example.dto.profile;

import com.example.enums.ProfileAdminRoleEnum;
import com.example.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileUpdateAdminRequestDTO {
    private String name;
    private String surname;
    private ProfileStatus status;
    private List<ProfileAdminRoleEnum> roleList;
    private String schoolId;
}