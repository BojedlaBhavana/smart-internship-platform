package com.platform.smartinternship.controller;

import com.platform.smartinternship.dto.ApiResponse;
import com.platform.smartinternship.dto.MatchResultDto;
import com.platform.smartinternship.service.MatchingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<List<MatchResultDto>>> getDashboardMatches(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<MatchResultDto> matches = matchingService.getMatchedInternshipsForStudent(userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.ok("Matched internships fetched successfully", matches));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
