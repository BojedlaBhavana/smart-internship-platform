package com.platform.smartinternship.dto;

import java.time.LocalDate;
import java.util.Set;

public class MatchResultDto {

    private Long internshipId;
    private String title;
    private String company;
    private String description;
    private String location;
    private LocalDate deadline;
    private Set<String> requiredSkills;
    private Set<String> matchedSkills;
    private Set<String> missingSkills;
    private double matchPercentage;

    public MatchResultDto() {
    }

    public MatchResultDto(Long internshipId, String title, String company, String description,
                          String location, LocalDate deadline, Set<String> requiredSkills,
                          Set<String> matchedSkills, Set<String> missingSkills, double matchPercentage) {
        this.internshipId = internshipId;
        this.title = title;
        this.company = company;
        this.description = description;
        this.location = location;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills;
        this.matchedSkills = matchedSkills;
        this.missingSkills = missingSkills;
        this.matchPercentage = matchPercentage;
    }

    public Long getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(Long internshipId) {
        this.internshipId = internshipId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Set<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Set<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public Set<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(Set<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public Set<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(Set<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }
}
