package com.dev.models;

import javax.persistence.*;

@Entity
@Table(name = "user_levels")
public class UserLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "subject_type", nullable = false)
    private String subjectType;

    @Column(nullable = false)
    private int level;

    // Default constructor
    public UserLevel() {
    }

    // Constructor
    public UserLevel(String username, String subjectType, int level) {
        this.username = username;
        this.subjectType = subjectType;
        this.level = level;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}