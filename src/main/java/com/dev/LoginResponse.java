package com.dev;

import com.dev.dto.UserDTO;
import com.dev.models.User;

public class LoginResponse extends BasicResponse {
    private String sessionId;
    private String username;
    private String name;
    private String email;
    private String profileIcon;

    public LoginResponse(boolean success, Integer errorCode, String sessionId, UserDTO user) {
        super(success, errorCode);
        this.sessionId = sessionId;
        if (user != null) {
            this.username = user.getUsername();
            this.name = user.getName();
            this.email = user.getEmail();
            this.profileIcon = user.getProfileIcon();
        }
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileIcon() {
        return profileIcon;
    }
}