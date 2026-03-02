package com.example.students.dto.student;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudentResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
}
