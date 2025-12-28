package com.example.dto.profile;

import com.example.enums.ProfileRoleEnum;
import com.example.enums.ProfileStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileFilterDTO {
    private String query; // ism, familiya yoki username bo'yicha qidirish
    private ProfileRoleEnum role;
    private String schoolId;
    private ProfileStatus status;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
}