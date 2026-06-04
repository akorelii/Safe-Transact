package com.safetransact.model;

import jakarta.persistence.*;

@Entity
@Table(name = "advisors")
public class Advisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "quota_limit", nullable = false)
    private int quotaLimit;

    @Column(name = "current_used")
    private int currentUsed = 0;

    // Getter ve Setter Metotlari
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public int getQuotaLimit() { return quotaLimit; }
    public void setQuotaLimit(int quotaLimit) { this.quotaLimit = quotaLimit; }
    public int getCurrentUsed() { return currentUsed; }
    public void setCurrentUsed(int currentUsed) { this.currentUsed = currentUsed; }
}