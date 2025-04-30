package com.startupsphere.capstone.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bookmarks")
public class Bookmarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp = Instant.now();

      @ManyToOne(cascade = CascadeType.ALL) // Adjust as necessary
    private User user;

    @ManyToOne(cascade = CascadeType.ALL) // Adjust as necessary
    private Startup startup;

    @ManyToOne(cascade = CascadeType.ALL) // Adjust as necessary
    private Investor investor;
    // --- Constructors ---

    public Bookmarks() {
        // Default constructor
    }

    public Bookmarks(User user, Startup startup, Investor investor) {
        this.user = user;
        this.startup = startup;
        this.investor = investor;
        this.timestamp = Instant.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
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

