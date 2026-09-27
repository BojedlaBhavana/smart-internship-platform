package com.platform.smartinternship.repository;

import com.platform.smartinternship.model.Internship;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    @EntityGraph(attributePaths = {"requiredSkills", "postedBy"})
    List<Internship> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"requiredSkills", "postedBy"})
    Optional<Internship> findWithSkillsById(Long id);
}
