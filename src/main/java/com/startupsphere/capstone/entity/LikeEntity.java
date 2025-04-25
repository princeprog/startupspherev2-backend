package com.startupsphere.capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "like")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    public LikeEntity() {}

    // Parameterized constructor
    public LikeEntity(Long timestamp, User user, Long startupId, Long investorId) {
        this.timestamp = timestamp;
        this.user = user;
        this.startupId = startupId;
        this.investorId = investorId;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        return "LikeEntity{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", user=" + user +
                ", startupId=" + startupId +
                ", investorId=" + investorId +
                '}';
    }
}