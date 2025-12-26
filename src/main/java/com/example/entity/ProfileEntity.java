package com.example.entity;

import com.example.enums.ProfileGender;
import com.example.enums.ProfileRole;
import com.example.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "surname")
    private String surname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "birthdate")
    private String birthdate;
    @Column(name = "patronymic")
    private String patronymic;
    @Column(name = "passport_series")
    private String passportSeries;
    @Column(name = "passport_number")
    private String passportNumber;
    @Column(name = "pinfl")
    private String pinfl;
    @Column(name = "password")
    private String password;
    @Column(name = "visible")
    private Boolean visible;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Enumerated(value = EnumType.STRING)
    private ProfileStatus status;
    @Enumerated(value = EnumType.STRING)
    private ProfileRole role;
    @Enumerated(value = EnumType.STRING)
    private ProfileGender gender;
    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolEntity school;
    @OneToMany(mappedBy = "profile")
    private List<ProfileRoleEntity> roles;

}
