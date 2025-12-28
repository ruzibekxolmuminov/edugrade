package com.example.dto.profile;

import com.example.enums.ProfileAdminRoleEnum;
import com.example.enums.ProfileModeratorEnum;
import com.example.enums.ProfileRoleEnum;
import com.example.enums.ProfileStatus;
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
