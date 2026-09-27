package com.platform.smartinternship.service;

import com.platform.smartinternship.dto.MatchResultDto;
import com.platform.smartinternship.model.Internship;
import com.platform.smartinternship.model.Skill;
import com.platform.smartinternship.model.StudentProfile;
import com.platform.smartinternship.model.User;
import com.platform.smartinternship.repository.InternshipRepository;
import com.platform.smartinternship.repository.StudentProfileRepository;
import com.platform.smartinternship.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchingService {

    private final StudentProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;

    public MatchingService(StudentProfileRepository profileRepository,
                           UserRepository userRepository,
                           InternshipRepository internshipRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.internshipRepository = internshipRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchResultDto> getMatchedInternshipsForStudent(String studentEmail) {
        User user = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with email: " + studentEmail));

        StudentProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> profileRepository.findByUserId(user.getId()).orElse(null));

        Set<String> studentNormalizedSkills = new HashSet<>();
        if (profile != null && profile.getSkills() != null) {
            for (Skill s : profile.getSkills()) {
                if (s.getName() != null) {
                    studentNormalizedSkills.add(s.getName().trim().toLowerCase());
                }
            }
        }

        List<Internship> internships = internshipRepository.findAllByOrderByCreatedAtDesc();
        List<MatchResultDto> results = new ArrayList<>();

        for (Internship internship : internships) {
            Set<Skill> requiredSkills = internship.getRequiredSkills() != null
                    ? internship.getRequiredSkills()
                    : Collections.emptySet();

            Set<String> allRequiredNames = new LinkedHashSet<>();
            Set<String> matchedNames = new LinkedHashSet<>();
            Set<String> missingNames = new LinkedHashSet<>();

            for (Skill skill : requiredSkills) {
                if (skill.getName() == null) continue;
                String originalName = skill.getName().trim();
                String normalized = originalName.toLowerCase();
                allRequiredNames.add(originalName);

                if (studentNormalizedSkills.contains(normalized)) {
                    matchedNames.add(originalName);
                } else {
                    missingNames.add(originalName);
                }
            }

            double matchPercentage;
            if (allRequiredNames.isEmpty()) {
                matchPercentage = 100.0;
            } else {
                double rawPercentage = ((double) matchedNames.size() / (double) allRequiredNames.size()) * 100.0;
                matchPercentage = BigDecimal.valueOf(rawPercentage)
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue();
            }

            MatchResultDto dto = new MatchResultDto(
                    internship.getId(),
                    internship.getTitle(),
                    internship.getCompany(),
                    internship.getDescription(),
                    internship.getLocation(),
                    internship.getDeadline(),
                    allRequiredNames,
                    matchedNames,
                    missingNames,
                    matchPercentage
            );

            results.add(dto);
        }

        // Sort descending: highest match percentage first
        results.sort((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));

        return results;
    }
}
