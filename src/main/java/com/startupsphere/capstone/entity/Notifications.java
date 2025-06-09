package com.startupsphere.capstone.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    private String remarks;

    private boolean isViewed = false;
    private LocalDateTime viewedAt;

    public Notifications(){}

    public Notifications(int id, String remarks, Startup startup, User user, boolean isViewed, LocalDateTime viewedAt){
        this.id = id;
        this.remarks = remarks;
        this.startup = startup;
        this.user = user;
        this.isViewed = isViewed;
        this.viewedAt = viewedAt;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    public String getRemarks(){
        return remarks;
    }

    public void setStartup(Startup startup){
        this.startup = startup;
    }

    public Startup getStartup(){
        return startup;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public boolean isViewed(){
        return isViewed;
    }

    public void setViewed(boolean isViewed){
        this.isViewed = isViewed;
        if(isViewed){
            this.viewedAt = LocalDateTime.now();
        }
    }

    public LocalDateTime getViewedAt(){
        return viewedAt;
    }
}
