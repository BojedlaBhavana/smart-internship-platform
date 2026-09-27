package com.platform.smartinternship.controller;

import com.platform.smartinternship.dto.ApiResponse;
import com.platform.smartinternship.dto.UpdateProfileRequest;
import com.platform.smartinternship.dto.UserProfileDto;
import com.platform.smartinternship.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            UserProfileDto profile = studentService.getProfileByEmail(userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.ok("Profile fetched successfully", profile));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        try {
            UserProfileDto profile = studentService.updateProfile(userDetails.getUsername(), request);
            return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", profile));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
