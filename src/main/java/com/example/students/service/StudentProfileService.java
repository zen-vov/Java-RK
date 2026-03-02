package com.example.students.service;

import com.example.students.dto.profile.StudentProfileRequest;
import com.example.students.dto.profile.StudentProfileResponse;
import com.example.students.entity.Student;
import com.example.students.entity.StudentProfile;
import com.example.students.exception.ConflictException;
import com.example.students.exception.ResourceNotFoundException;
import com.example.students.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentProfileService {

    private final StudentProfileRepository profileRepository;
    private final StudentService studentService;

    public StudentProfileResponse create(StudentProfileRequest request) {
        if (profileRepository.existsByStudentId(request.getStudentId())) {
            throw new ConflictException("This student already has a profile");
        }

        Student student = studentService.findEntityById(request.getStudentId());

        StudentProfile profile = StudentProfile.builder()
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .student(student)
                .build();

        student.setProfile(profile);

        StudentProfile savedProfile = profileRepository.save(profile);
        return toResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public List<StudentProfileResponse> getAll() {
        return profileRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentProfileResponse getById(Long id) {
        StudentProfile profile = findEntityById(id);
        return toResponse(profile);
    }

    public StudentProfileResponse update(Long id, StudentProfileRequest request) {
        StudentProfile profile = findEntityById(id);

        if (!profile.getStudent().getId().equals(request.getStudentId())
                && profileRepository.existsByStudentId(request.getStudentId())) {
            throw new ConflictException("This student already has a profile");
        }

        Student student = studentService.findEntityById(request.getStudentId());

        profile.setAddress(request.getAddress());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setBirthDate(request.getBirthDate());
        profile.setStudent(student);

        StudentProfile updatedProfile = profileRepository.save(profile);
        return toResponse(updatedProfile);
    }

    public void delete(Long id) {
        StudentProfile profile = findEntityById(id);
        profileRepository.delete(profile);
    }

    @Transactional(readOnly = true)
    public StudentProfile findEntityById(Long id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found with id: " + id));
    }

    private StudentProfileResponse toResponse(StudentProfile profile) {
        return StudentProfileResponse.builder()
                .id(profile.getId())
                .studentId(profile.getStudent().getId())
                .address(profile.getAddress())
                .phoneNumber(profile.getPhoneNumber())
                .birthDate(profile.getBirthDate())
                .build();
    }
}
