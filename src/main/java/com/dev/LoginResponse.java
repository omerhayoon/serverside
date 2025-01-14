package com.dev;


import com.dev.BasicResponse;
import com.dev.models.User;

public class LoginResponse extends BasicResponse {
    private String sessionId;
    private User user;

    public LoginResponse(boolean success, Integer errorCode, String sessionId, User user) {
        super(success, errorCode); // קריאה לבנאי של BasicResponse
        this.sessionId = sessionId;
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
