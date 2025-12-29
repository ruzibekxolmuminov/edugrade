package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherSubjectDTO {
    private Integer id;
    private String teacherId;
    private Integer subjectId;
    private String schoolId;
}