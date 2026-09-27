package com.platform.smartinternship.repository;

import com.platform.smartinternship.model.StudentProfile;
import com.platform.smartinternship.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    @EntityGraph(attributePaths = {"skills", "user"})
    Optional<StudentProfile> findByUser(User user);

    @EntityGraph(attributePaths = {"skills", "user"})
    Optional<StudentProfile> findByUserId(Long userId);
}
