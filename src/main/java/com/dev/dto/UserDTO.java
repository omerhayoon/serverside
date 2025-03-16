package com.dev.dto;

import com.dev.models.User;

public class UserDTO {
    private String username;
    private String name;
    private String email;
    private String profileIcon;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.profileIcon = user.getProfileIcon();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(String profileIcon) {
        this.profileIcon = profileIcon;
    }
}