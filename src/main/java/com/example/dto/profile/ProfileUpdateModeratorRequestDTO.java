package com.example.dto.profile;

import com.example.enums.profile.ProfileRoleEnum;
import com.example.enums.profile.ProfileStatus;
import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateModeratorRequestDTO {
    private String name;
    private String surname;
    private ProfileStatus status;
    private List<ProfileRoleEnum> roleList;
    private String schoolId;
}
