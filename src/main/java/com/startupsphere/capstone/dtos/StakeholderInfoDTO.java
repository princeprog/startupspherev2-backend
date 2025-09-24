package com.startupsphere.capstone.dto;

import com.startupsphere.capstone.entity.Stakeholder;

import java.time.LocalDateTime;

public class StakeholderInfoDTO {
    private Stakeholder stakeholder;
    private String role;
    private String status;
    private LocalDateTime dateJoined;

    public StakeholderInfoDTO(Stakeholder stakeholder, String role, String status, LocalDateTime dateJoined) {
        this.stakeholder = stakeholder;
        this.role = role;
        this.status = status;
        this.dateJoined = dateJoined;
    }

    public Stakeholder getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(Stakeholder stakeholder) {
        this.stakeholder = stakeholder;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        this.dateJoined = dateJoined;
    }
}