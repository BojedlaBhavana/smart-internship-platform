package com.platform.smartinternship.config;

import com.platform.smartinternship.model.Internship;
import com.platform.smartinternship.model.Role;
import com.platform.smartinternship.model.Skill;
import com.platform.smartinternship.model.StudentProfile;
import com.platform.smartinternship.model.User;
import com.platform.smartinternship.repository.InternshipRepository;
import com.platform.smartinternship.repository.StudentProfileRepository;
import com.platform.smartinternship.repository.UserRepository;
import com.platform.smartinternship.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final StudentProfileRepository profileRepository;
    private final InternshipRepository internshipRepository;
    private final SkillService skillService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           StudentProfileRepository profileRepository,
                           InternshipRepository internshipRepository,
                           SkillService skillService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.internshipRepository = internshipRepository;
        this.skillService = skillService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Seed Admin if not present
        User admin = userRepository.findByEmail("admin@platform.com").orElseGet(() -> {
            User newAdmin = new User(
                    "System Admin",
                    "admin@platform.com",
                    passwordEncoder.encode("admin123"),
                    Role.ROLE_ADMIN
            );
            logger.info("Seeding default Admin: admin@platform.com / admin123");
            return userRepository.save(newAdmin);
        });

        // Seed Sample Student if not present
        if (!userRepository.existsByEmail("student@platform.com")) {
            User student = new User(
                    "Alex Johnson",
                    "student@platform.com",
                    passwordEncoder.encode("student123"),
                    Role.ROLE_STUDENT
            );
            User savedStudent = userRepository.save(student);

            StudentProfile profile = new StudentProfile(savedStudent);
            profile.setDegree("B.Tech in Computer Science");
            profile.setUniversity("Tech University");
            profile.setGraduationYear(2027);
            profile.setBio("Passionate software engineering student eager to learn backend architectures and cloud technologies.");

            Set<Skill> studentSkills = skillService.getOrCreateSkills(
                    List.of("Java", "Spring Boot", "MySQL", "REST API", "Git")
            );
            profile.setSkills(studentSkills);
            profileRepository.save(profile);

            logger.info("Seeding default Student: student@platform.com / student123");
        }

        // Seed Sample Internships if empty
        if (internshipRepository.count() == 0) {
            Internship intern1 = new Internship(
                    "Java Backend Engineering Intern",
                    "TechCorp Solutions",
                    "Join our core services team to develop robust Spring Boot REST microservices and optimize MySQL queries.",
                    "Remote / New York",
                    LocalDate.now().plusMonths(2),
                    admin
            );
            intern1.setRequiredSkills(skillService.getOrCreateSkills(List.of("Java", "Spring Boot", "MySQL", "REST API")));
            internshipRepository.save(intern1);

            Internship intern2 = new Internship(
                    "Full Stack Software Intern",
                    "CloudVanguard Inc.",
                    "Work with our agile team on building end-to-end features using Java, Spring Boot, React, and Docker.",
                    "San Francisco, CA",
                    LocalDate.now().plusMonths(1),
                    admin
            );
            intern2.setRequiredSkills(skillService.getOrCreateSkills(List.of("Java", "Spring Boot", "React", "Docker")));
            internshipRepository.save(intern2);

            Internship intern3 = new Internship(
                    "Frontend UI/UX Intern",
                    "PixelCraft Media",
                    "Design and build responsive web applications with modern HTML, CSS, JavaScript, and Figma.",
                    "Austin, TX",
                    LocalDate.now().plusMonths(3),
                    admin
            );
            intern3.setRequiredSkills(skillService.getOrCreateSkills(List.of("HTML", "CSS", "JavaScript", "Figma")));
            internshipRepository.save(intern3);

            Internship intern4 = new Internship(
                    "Cloud DevOps Intern",
                    "Nexus Cloud Labs",
                    "Automate CI/CD pipelines, containerize microservices, and deploy to AWS infrastructure.",
                    "Seattle, WA",
                    LocalDate.now().plusMonths(2),
                    admin
            );
            intern4.setRequiredSkills(skillService.getOrCreateSkills(List.of("Docker", "Kubernetes", "AWS", "Linux")));
            internshipRepository.save(intern4);

            logger.info("Seeded sample internships with skills for matching tests.");
        }
    }
}
