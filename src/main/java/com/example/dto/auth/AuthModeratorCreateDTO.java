package com.example.dto.auth;

import com.example.enums.profile.ProfileGender;
import com.example.enums.profile.ProfileModeratorEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AuthModeratorCreateDTO {
    @NotNull(message = "Ism kiritilmadi!")
    private String firstName;
    @NotNull(message = "Familya kiritilmadi!")
    private String lastName;
    @NotNull(message = "Otasining ismi kiritilmadi")
    private String patronymic;
    @NotNull(message = "Parol kiritilmadi!")
    @Size(min = 8, max = 20)
    private String password;
    private String phone;
    private String email;
    @NotNull(message = "Jins kiritilmadi!")
    private ProfileGender gender;
    @NotNull(message = "Maktab id si kiritilmadi!")
    private String schoolId;
    @NotNull(message = "Tugilgan kun kiritilmadi!")
    private LocalDate birthDate;
    @NotNull(message = "Pinf kiritilmadi!")
    @Size(min = 14 , max = 14)
    private String pinfl;
    @NotNull(message = "Passport seriyasi kiritilmadi!")
    @Size(min = 2, max = 2)
    private String passportSeries;
    @NotNull(message = "Passport raqami kiritilmadi!")
    @Size(min = 7, max = 7)
    private String passportNumber;
    @NotNull(message = "Role kiritilmadi!")
    private List<ProfileModeratorEnum> roles;
}
