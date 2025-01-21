package com.dev;

import com.dev.models.User;

public class LoginResponse extends BasicResponse {
    private String sessionId;
    private User user;
    private String userName;  // הוספת שדה userName

    public LoginResponse(boolean success, Integer errorCode, String sessionId, User user) {
        super(success, errorCode); // קריאה לבנאי של BasicResponse
        this.sessionId = sessionId;
        this.user = user;
        this.userName = (user != null) ? user.getName() : null;  // הוספת השם מתוך אובייקט ה-User
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
        this.userName = (user != null) ? user.getName() : null;  // עדכון השם אם ה-user משתנה
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
