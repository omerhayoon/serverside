package com.dev.utils;

import com.dev.models.User;
import com.dev.models.UserSession;
import com.dev.repository.UserSessionRepository;
import com.dev.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
public class SessionManager {

    private final UserSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionManager(UserSessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    // יצירת סשן חדש
    public String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusDays(7); // הסשן יפוג בעוד 7 ימים

        // יצירת אובייקט UserSession ושמירתו במסד הנתונים
        UserSession session = new UserSession();
        session.setUsername(user.getUsername());
        session.setSessionId(sessionId);
        session.setCreatedAt(now);
        session.setExpiresAt(expiresAt);

        sessionRepository.save(session); // שמירת הסשן בבסיס הנתונים
        return sessionId;
    }

    // שליפת המשתמש לפי מזהה סשן
    public User getUserBySessionId(String sessionId) {
        Optional<UserSession> userSessionOpt = sessionRepository.findBySessionId(sessionId);
        if (userSessionOpt.isPresent()) {
            UserSession userSession = userSessionOpt.get();

            // בדיקת תוקף הסשן
            if (userSession.getExpiresAt().isAfter(LocalDateTime.now())) {
                return userRepository.findByUsername(userSession.getUsername()).orElse(null);
            } else {
                // אם הסשן פג תוקף, מחיקה שלו
                sessionRepository.delete(userSession);
            }
        }
        return null; // אם הסשן לא קיים או פג תוקף
    }

    @Transactional
    public void removeSession(String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }
}
