package com.example.students.repository;

import com.example.students.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByStudentIdAndNameIgnoreCase(Long studentId, String name);

    boolean existsByStudentIdAndNameIgnoreCaseAndIdNot(Long studentId, String name, Long id);
}
