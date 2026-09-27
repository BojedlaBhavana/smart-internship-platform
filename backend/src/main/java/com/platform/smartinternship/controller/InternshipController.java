package com.platform.smartinternship.controller;

import com.platform.smartinternship.dto.ApiResponse;
import com.platform.smartinternship.dto.InternshipResponse;
import com.platform.smartinternship.service.InternshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internships")
public class InternshipController {

    private final InternshipService internshipService;

    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InternshipResponse>>> getAllInternships() {
        List<InternshipResponse> list = internshipService.getAllInternships();
        return ResponseEntity.ok(ApiResponse.ok("Internships fetched successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InternshipResponse>> getInternshipById(@PathVariable Long id) {
        try {
            InternshipResponse item = internshipService.getInternshipById(id);
            return ResponseEntity.ok(ApiResponse.ok("Internship fetched successfully", item));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
