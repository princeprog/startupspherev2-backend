package com.startupsphere.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bookmarks")
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long timestamp;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column
    private Long startupId;

    @Column
    private Long investorId;

    // Default constructor
    public BookmarkEntity() {}

    // Parameterized constructor
    public BookmarkEntity(Long timestamp, User user, Long startupId, Long investorId) {
        this.timestamp = timestamp;
        this.user = user;
        this.startupId = startupId;
        this.investorId = investorId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getStartupId() {
        return startupId;
    }

    public void setStartupId(Long startupId) {
        this.startupId = startupId;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "BookmarkEntity{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", user=" + user +
                ", startupId=" + startupId +
                ", investorId=" + investorId +
                '}';
    }
}