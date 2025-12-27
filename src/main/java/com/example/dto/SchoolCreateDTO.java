package com.example.dto;

import lombok.Data;

@Data
public class SchoolCreateDTO {
    private String name;
    private Integer number;
    private String address;
    private String attachId;
    private Integer regionId;
}
