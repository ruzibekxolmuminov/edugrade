package com.example.dto.group;

import com.example.enums.GroupLevel;
import lombok.Data;

@Data
public class GroupCreateDTO {
    private String name;
    private GroupLevel level;
    private String mentorId;
    private String schoolId;
    private String facultyId;
}
