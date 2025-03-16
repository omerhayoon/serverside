package com.dev.models;

import javax.persistence.*;

@Entity
@Table(name = "probability_data")
public class ProbabilityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;  // "name", "item", "color", etc.

    @Column(nullable = false)
    private String value;

    // Default constructor
    public ProbabilityData() {
    }

    // Constructor
    public ProbabilityData(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}