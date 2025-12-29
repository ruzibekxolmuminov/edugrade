package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BulkAssignDTO {
    private List<String> studentIds;
    private String groupId;
}