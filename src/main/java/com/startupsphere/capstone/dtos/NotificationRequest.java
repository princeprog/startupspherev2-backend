package com.startupsphere.capstone.dtos;

public class NotificationRequest {
    private String remarks;
    private Long startupId;
    private Integer userId;

    public String getRemarks(){
        return remarks;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    public Long getStartupId(){
        return startupId;
    }

    public void setStartupId(Long startupId){
        this.startupId = startupId;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }
}
