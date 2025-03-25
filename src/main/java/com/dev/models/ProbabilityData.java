package com.dev.models;

import javax.persistence.*;

@Entity
@Table(name = "probability_data")
public class ProbabilityData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String value;
    private String singularValue; // Add this line

    // Constructors
    public ProbabilityData() {}

    public ProbabilityData(String type, String value) {
        this.type = type;
        this.value = value;
    }
    public ProbabilityData(String type, String value,String singularValue) {
        this.type = type;
        this.value = value;
        this.singularValue = singularValue;
    }


    // Getters and setters...

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

    public String getSingularValue() {
        return singularValue;
    }

    public void setSingularValue(String singularValue) {
        this.singularValue = singularValue;
    }


}