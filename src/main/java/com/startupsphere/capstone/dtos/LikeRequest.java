package com.startupsphere.capstone.dtos;

public class LikeRequest {
    private Integer userId;
    private Long startupId; // Optional
    private Integer investorId; // Optional

    // Getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getStartupId() {
        return startupId;
    }

    public void setStartupId(Long startupId) {
        this.startupId = startupId;
    }

    public Integer getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Integer investorId) {
        this.investorId = investorId;
    }

    // Custom validation method
    public void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (startupId == null && investorId == null) {
            throw new IllegalArgumentException("Either startupId or investorId must be provided");
        }
    }
}