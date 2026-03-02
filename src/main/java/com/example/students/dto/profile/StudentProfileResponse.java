package com.example.students.dto.profile;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class StudentProfileResponse {

    private Long id;
    private Long studentId;
    private String address;
    private String phoneNumber;
    private LocalDate birthDate;
}
