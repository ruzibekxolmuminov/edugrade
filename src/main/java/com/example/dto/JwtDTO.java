package com.example.dto;

import com.example.enums.ProfileRoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtDTO {
    private String username;
    private List<ProfileRoleEnum> roles;
    private Integer code;

    public JwtDTO() {

    }

    public JwtDTO(String username, List<ProfileRoleEnum> roles) {
        this.username = username;
        this.roles = roles;
    }
}
