package com.startupsphere.capstone.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startupId")
    private Startup startup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investorId")
    private Investor investor;

    @PrePersist
    protected void onCreate() {
        timestamp = Instant.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Startup getStartup() { return startup; }
    public void setStartup(Startup startup) { this.startup = startup; }

    public Investor getInvestor() { return investor; }
    public void setInvestor(Investor investor) { this.investor = investor; }
}