package com.example.dto.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateDetailRequestDTO {
    private String name;
    private String surname;
    private String phone;
    private String photoId; // Yangi yuklangan rasmning UUID si
}