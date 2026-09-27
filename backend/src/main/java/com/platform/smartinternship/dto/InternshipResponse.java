package com.platform.smartinternship.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class InternshipResponse {

    private Long id;
    private String title;
    private String company;
    private String description;
    private String location;
    private LocalDate deadline;
    private Set<String> requiredSkills;
    private String postedByEmail;
    private LocalDateTime createdAt;

    public InternshipResponse() {
    }

    public InternshipResponse(Long id, String title, String company, String description,
                              String location, LocalDate deadline, Set<String> requiredSkills,
                              String postedByEmail, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.description = description;
        this.location = location;
        this.deadline = deadline;
        this.requiredSkills = requiredSkills;
        this.postedByEmail = postedByEmail;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPostedByEmail() {
        return postedByEmail;
    }

    public void setPostedByEmail(String postedByEmail) {
        this.postedByEmail = postedByEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
