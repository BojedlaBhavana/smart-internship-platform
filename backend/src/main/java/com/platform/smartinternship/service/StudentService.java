package com.platform.smartinternship.service;

import com.platform.smartinternship.dto.UpdateProfileRequest;
import com.platform.smartinternship.dto.UserProfileDto;
import com.platform.smartinternship.model.Skill;
import com.platform.smartinternship.model.StudentProfile;
import com.platform.smartinternship.model.User;
import com.platform.smartinternship.repository.StudentProfileRepository;
import com.platform.smartinternship.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final SkillService skillService;

    public StudentService(UserRepository userRepository,
                          StudentProfileRepository profileRepository,
                          SkillService skillService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.skillService = skillService;
    }

    @Transactional(readOnly = true)
    public UserProfileDto getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        StudentProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> profileRepository.findByUserId(user.getId())
                        .orElse(null));

        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        if (profile != null) {
            dto.setDegree(profile.getDegree());
            dto.setUniversity(profile.getUniversity());
            dto.setGraduationYear(profile.getGraduationYear());
            dto.setBio(profile.getBio());
            dto.setSkills(profile.getSkills() != null
                    ? profile.getSkills().stream().map(Skill::getName).collect(Collectors.toSet())
                    : Collections.emptySet());
        } else {
            dto.setSkills(Collections.emptySet());
        }

        return dto;
    }

    @Transactional
    public UserProfileDto updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            user.setFullName(request.getFullName().trim());
            userRepository.save(user);
        }

        StudentProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> new StudentProfile(user));

        profile.setDegree(request.getDegree());
        profile.setUniversity(request.getUniversity());
        profile.setGraduationYear(request.getGraduationYear());
        profile.setBio(request.getBio());

        if (request.getSkills() != null) {
            Set<Skill> skills = skillService.getOrCreateSkills(request.getSkills());
            profile.setSkills(skills);
        }

        StudentProfile saved = profileRepository.save(profile);

        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setDegree(saved.getDegree());
        dto.setUniversity(saved.getUniversity());
        dto.setGraduationYear(saved.getGraduationYear());
        dto.setBio(saved.getBio());
        dto.setSkills(saved.getSkills() != null
                ? saved.getSkills().stream().map(Skill::getName).collect(Collectors.toSet())
                : Collections.emptySet());

        return dto;
    }
}
