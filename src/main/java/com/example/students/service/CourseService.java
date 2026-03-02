package com.example.students.service;

import com.example.students.dto.course.CourseRequest;
import com.example.students.dto.course.CourseResponse;
import com.example.students.entity.Course;
import com.example.students.entity.Student;
import com.example.students.exception.ConflictException;
import com.example.students.exception.ResourceNotFoundException;
import com.example.students.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;

    public CourseResponse create(CourseRequest request) {
        validateCourseName(request.getStudentId(), request.getName(), null);

        Student student = studentService.findEntityById(request.getStudentId());

        Course course = Course.builder()
                .name(request.getName())
                .description(request.getDescription())
                .credits(request.getCredits())
                .student(student)
                .build();

        Course savedCourse = courseRepository.save(course);
        return toResponse(savedCourse);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        Course course = findEntityById(id);
        return toResponse(course);
    }

    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findEntityById(id);
        validateCourseName(request.getStudentId(), request.getName(), id);

        Student student = studentService.findEntityById(request.getStudentId());

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setStudent(student);

        Course updatedCourse = courseRepository.save(course);
        return toResponse(updatedCourse);
    }

    public void delete(Long id) {
        Course course = findEntityById(id);
        courseRepository.delete(course);
    }

    @Transactional(readOnly = true)
    public Course findEntityById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private void validateCourseName(Long studentId, String name, Long courseId) {
        boolean exists = courseId == null
                ? courseRepository.existsByStudentIdAndNameIgnoreCase(studentId, name)
                : courseRepository.existsByStudentIdAndNameIgnoreCaseAndIdNot(studentId, name, courseId);

        if (exists) {
            throw new ConflictException("Student already has a course with this name");
        }
    }

    private CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .studentId(course.getStudent().getId())
                .name(course.getName())
                .description(course.getDescription())
                .credits(course.getCredits())
                .build();
    }
}
