package com.dev.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mistake_logs")
public class MistakeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(nullable = false)
    private String subjectType;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String correctAnswer;

    @Column(nullable = false)
    private String userAnswer;

    @Column(nullable = false)
    private String solution;

    @Column(name = "mistake_date", nullable = false)
    private LocalDateTime mistakeDate;

    private boolean resolved = false;

    @Column(name = "resolved_date")
    private LocalDateTime resolvedDate;

    // Getters and setters
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public LocalDateTime getMistakeDate() {
        return mistakeDate;
    }

    public void setMistakeDate(LocalDateTime mistakeDate) {
        this.mistakeDate = mistakeDate;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
}
