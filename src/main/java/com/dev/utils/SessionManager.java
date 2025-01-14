package com.dev.utils;

import org.springframework.stereotype.Component;
import com.dev.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SessionManager {

    private final Map<String, User> sessions = new HashMap<>();

    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        return sessionId;
    }

    public User getUserBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
