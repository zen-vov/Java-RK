package com.example.students.repository;

import com.example.students.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    boolean existsByStudentId(Long studentId);
}
