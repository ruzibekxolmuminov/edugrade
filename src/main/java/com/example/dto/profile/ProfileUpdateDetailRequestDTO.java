package com.example.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateDetailRequestDTO {
    private String phone;
    private String email;
    private String photoId;
}