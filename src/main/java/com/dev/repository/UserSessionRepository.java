package com.dev.repository;

import com.dev.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    // שליפת סשן לפי sessionId
    Optional<UserSession> findBySessionId(String sessionId);

    // מחיקת סשן לפי sessionId
    void deleteBySessionId(String sessionId);
}
