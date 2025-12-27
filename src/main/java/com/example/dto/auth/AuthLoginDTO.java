package com.example.dto.auth;

import lombok.Data;

@Data
public class AuthLoginDTO {
    private String passportNumber;
    private String password;
}
