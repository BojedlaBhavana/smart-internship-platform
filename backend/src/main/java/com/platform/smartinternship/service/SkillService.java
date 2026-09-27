package com.platform.smartinternship.service;

import com.platform.smartinternship.model.Skill;
import com.platform.smartinternship.repository.SkillRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Transactional
    public Set<Skill> getOrCreateSkills(Collection<String> skillNames) {
        if (skillNames == null || skillNames.isEmpty()) {
            return Collections.emptySet();
        }

        Set<Skill> result = new HashSet<>();
        for (String rawName : skillNames) {
            if (rawName == null || rawName.trim().isEmpty()) {
                continue;
            }
            String cleanName = rawName.trim();
            Skill skill = skillRepository.findByNameIgnoreCase(cleanName)
                    .orElseGet(() -> skillRepository.save(new Skill(cleanName)));
            result.add(skill);
        }
        return result;
    }
}
