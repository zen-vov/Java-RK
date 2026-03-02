package com.example.students.controller;

import com.example.students.dto.profile.StudentProfileRequest;
import com.example.students.dto.profile.StudentProfileResponse;
import com.example.students.service.StudentProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileService profileService;

    @PostMapping
    public ResponseEntity<StudentProfileResponse> create(@Valid @RequestBody StudentProfileRequest request) {
        StudentProfileResponse response = profileService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<StudentProfileResponse> getAll() {
        return profileService.getAll();
    }

    @GetMapping("/{id}")
    public StudentProfileResponse getById(@PathVariable Long id) {
        return profileService.getById(id);
    }

    @PutMapping("/{id}")
    public StudentProfileResponse update(@PathVariable Long id, @Valid @RequestBody StudentProfileRequest request) {
        return profileService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
