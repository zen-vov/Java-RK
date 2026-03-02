package com.example.students.dto.course;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CourseResponse {

    private Long id;
    private Long studentId;
    private String name;
    private String description;
    private Integer credits;
}
