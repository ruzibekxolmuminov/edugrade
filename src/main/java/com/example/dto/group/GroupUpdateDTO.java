package com.example.dto.group;

import com.example.enums.GroupLevel;
import lombok.Data;

@Data
public class GroupUpdateDTO {
    private String name;
    private GroupLevel level;
    private String mentorId;
    private String facultyId;
}
