package com.example.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateDTO {
    private String oldPassword;
    private String newPassword;
}