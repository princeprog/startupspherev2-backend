package com.startupsphere.capstone.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "startup_stakeholders")
public class StartupStakeholder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stakeholder_id", nullable = false)
    private Stakeholder stakeholder;

    @Column(nullable = false)
    private String role;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime dateJoined;

    @Column(nullable = false)
    private String status;

    public StartupStakeholder() {
    }

    public StartupStakeholder(Startup startup, Stakeholder stakeholder, String role, LocalDateTime dateJoined, String status) {
        this.startup = startup;
        this.stakeholder = stakeholder;
        this.role = role;
        this.dateJoined = dateJoined;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Startup getStartup() {
        return startup;
    }

    public void setStartup(Startup startup) {
        this.startup = startup;
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

    public LocalDateTime getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}