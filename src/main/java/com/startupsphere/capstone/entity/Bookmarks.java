package com.startupsphere.capstone.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bookmarks")
public class Bookmarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    @JoinColumn(name = "users")
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "startupId", nullable = true)
    private Startup startup;

    @ManyToOne(optional = true)
    @JoinColumn(name = "investorId", nullable = true)
    private Investor investor;



    public Bookmarks() {
        // Default constructor
    }

    public Bookmarks(User user, Startup startup, Investor investor) {
        this.user = user;
        this.startup = startup;
        this.investor = investor;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Startup getStartup() {
        return startup;
    }

    public void setStartup(Startup startup) {
        this.startup = startup;
    }

    public Investor getInvestor() {
        return investor;
    }

    public void setInvestor(Investor investor) {
        this.investor = investor;
    }
}

