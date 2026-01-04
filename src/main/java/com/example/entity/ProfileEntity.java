package com.example.entity;

import com.example.enums.profile.ProfileGender;
import com.example.enums.profile.ProfileStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "firstname")
    private String firstname;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "birthdate")
    private LocalDate birthdate;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
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
    private ProfileGender gender;
    @Column(name = "school_id")
    private String schoolId;
    @Column(name = "group_id")
    private String groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private GroupEntity group;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", insertable = false, updatable = false)
    private SchoolEntity school;

    @OneToMany(mappedBy = "profile")
    private List<ProfileRoleEntity> roles;

}
