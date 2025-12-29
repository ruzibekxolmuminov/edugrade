package com.example.dto.school;

import lombok.Data;

@Data
public class SchoolUpdateDTO {
    private String name;
    private Integer number;
    private String address;
    private String attachId;
    private Integer regionId;
}
