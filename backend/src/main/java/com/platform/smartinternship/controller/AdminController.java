package com.platform.smartinternship.controller;

import com.platform.smartinternship.dto.ApiResponse;
import com.platform.smartinternship.dto.InternshipRequest;
import com.platform.smartinternship.dto.InternshipResponse;
import com.platform.smartinternship.service.InternshipService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/internships")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final InternshipService internshipService;

    public AdminController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InternshipResponse>> createInternship(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody InternshipRequest request) {
        try {
            InternshipResponse created = internshipService.createInternship(request, userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.ok("Internship created successfully", created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InternshipResponse>> updateInternship(
            @PathVariable Long id,
            @Valid @RequestBody InternshipRequest request) {
        try {
            InternshipResponse updated = internshipService.updateInternship(id, request);
            return ResponseEntity.ok(ApiResponse.ok("Internship updated successfully", updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInternship(@PathVariable Long id) {
        try {
            internshipService.deleteInternship(id);
            return ResponseEntity.ok(ApiResponse.ok("Internship deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
