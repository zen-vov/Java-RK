package com.example.students.service;

import com.example.students.dto.student.StudentRequest;
import com.example.students.dto.student.StudentResponse;
import com.example.students.entity.Student;
import com.example.students.exception.ConflictException;
import com.example.students.exception.ResourceNotFoundException;
import com.example.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentResponse create(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Student with this email already exists");
        }

        Student student = Student.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();

        Student savedStudent = studentRepository.save(student);
        return toResponse(savedStudent);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> getAll() {
        return studentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        Student student = findEntityById(id);
        return toResponse(student);
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findEntityById(id);

        if (!student.getEmail().equalsIgnoreCase(request.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Student with this email already exists");
        }

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());

        Student updatedStudent = studentRepository.save(student);
        return toResponse(updatedStudent);
    }

    public void delete(Long id) {
        Student student = findEntityById(id);
        studentRepository.delete(student);
    }

    @Transactional(readOnly = true)
    public Student findEntityById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .age(student.getAge())
                .build();
    }
}
