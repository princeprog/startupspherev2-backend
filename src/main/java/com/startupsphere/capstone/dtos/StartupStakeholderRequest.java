package com.startupsphere.capstone.dtos;

public class StartupStakeholderRequest {

    private Long stakeholderId;
    private String role;
    private String status;
    private Long startupId;

    public StartupStakeholderRequest(Long stakeholderId, String role, String status, Long startupId) {
        this.stakeholderId = stakeholderId;
        this.role = role;
        this.status = status;
        this.startupId = startupId;
    }

    public Long getStakeholderId() {
        return stakeholderId;
    }

    public void setStakeholderId(Long stakeholderId) {
        this.stakeholderId = stakeholderId;
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

    public Long getStartupId() {
        return startupId;
    }

    public void setStartupId(Long startupId) {
        this.startupId = startupId;
    }
}
