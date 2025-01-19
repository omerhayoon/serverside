package com.dev.models;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "reviews")
public class Review {
    @Id // מציין שזה המפתח הראשי
    @GeneratedValue(strategy = GenerationType.IDENTITY) // מציין שהערך מתמלא אוטומטית
    private Long id;

    @Column(nullable = false) // מציין שהשדה לא יכול להיות ריק
    private String username;

    @Column(columnDefinition = "TEXT") // מציין שזה שדה מסוג TEXT
    private String content;

    @Column(name = "created_at", insertable = false, updatable = false)
    // insertable = false כי הדאטהבייס מכניס את הערך
    // updatable = false כי אי אפשר לעדכן את התאריך
    private LocalDateTime createdAt;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}