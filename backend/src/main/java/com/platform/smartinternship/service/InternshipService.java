package com.platform.smartinternship.service;

import com.platform.smartinternship.dto.InternshipRequest;
import com.platform.smartinternship.dto.InternshipResponse;
import com.platform.smartinternship.model.Internship;
import com.platform.smartinternship.model.Skill;
import com.platform.smartinternship.model.User;
import com.platform.smartinternship.repository.InternshipRepository;
import com.platform.smartinternship.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InternshipService {

    private final InternshipRepository internshipRepository;
    private final UserRepository userRepository;
    private final SkillService skillService;

    public InternshipService(InternshipRepository internshipRepository,
                             UserRepository userRepository,
                             SkillService skillService) {
        this.internshipRepository = internshipRepository;
        this.userRepository = userRepository;
        this.skillService = skillService;
    }

    @Transactional(readOnly = true)
    public List<InternshipResponse> getAllInternships() {
        return internshipRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InternshipResponse getInternshipById(Long id) {
        Internship internship = internshipRepository.findWithSkillsById(id)
                .orElseThrow(() -> new IllegalArgumentException("Internship not found with id: " + id));
        return mapToResponse(internship);
    }

    @Transactional
    public InternshipResponse createInternship(InternshipRequest request, String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with email: " + adminEmail));

        Internship internship = new Internship(
                request.getTitle().trim(),
                request.getCompany().trim(),
                request.getDescription(),
                request.getLocation().trim(),
                request.getDeadline(),
                admin
        );

        if (request.getRequiredSkills() != null && !request.getRequiredSkills().isEmpty()) {
            Set<Skill> skills = skillService.getOrCreateSkills(request.getRequiredSkills());
            internship.setRequiredSkills(skills);
        }

        Internship saved = internshipRepository.save(internship);
        return mapToResponse(saved);
    }

    @Transactional
    public InternshipResponse updateInternship(Long id, InternshipRequest request) {
        Internship internship = internshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Internship not found with id: " + id));

        internship.setTitle(request.getTitle().trim());
        internship.setCompany(request.getCompany().trim());
        internship.setDescription(request.getDescription());
        internship.setLocation(request.getLocation().trim());
        internship.setDeadline(request.getDeadline());

        if (request.getRequiredSkills() != null) {
            Set<Skill> skills = skillService.getOrCreateSkills(request.getRequiredSkills());
            internship.setRequiredSkills(skills);
        }

        Internship updated = internshipRepository.save(internship);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteInternship(Long id) {
        if (!internshipRepository.existsById(id)) {
            throw new IllegalArgumentException("Internship not found with id: " + id);
        }
        internshipRepository.deleteById(id);
    }

    public InternshipResponse mapToResponse(Internship internship) {
        Set<String> skills = internship.getRequiredSkills() != null
                ? internship.getRequiredSkills().stream().map(Skill::getName).collect(Collectors.toSet())
                : Set.of();

        String adminEmail = internship.getPostedBy() != null ? internship.getPostedBy().getEmail() : "system";

        return new InternshipResponse(
                internship.getId(),
                internship.getTitle(),
                internship.getCompany(),
                internship.getDescription(),
                internship.getLocation(),
                internship.getDeadline(),
                skills,
                adminEmail,
                internship.getCreatedAt()
        );
    }
}
