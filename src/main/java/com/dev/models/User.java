package com.dev.models;

import javax.persistence.*;

@Entity // Marks this class as a JPA entity
@Table(name = "users") // Specifies the table name in the database
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private int id;

    @Column(nullable = false, unique = true) // Username must be unique
    private String username;

    @Column(nullable = false) // Password cannot be null
    private String password;

    @Column(nullable = false, unique = true) // Email must be unique
    private String email;

    @Column(name = "is_admin", nullable = false) // Maps to 'is_admin' column
    private boolean isAdmin;

    // Default constructor (required by JPA)
    public User() {}

    // Constructor
    public User(int id, String username, String password, String email, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
