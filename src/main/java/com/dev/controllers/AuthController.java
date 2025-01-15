package com.dev.controllers;

import com.dev.BasicResponse;
import com.dev.LoginResponse;
import com.dev.models.User;
import com.dev.services.UserService;
import com.dev.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

    //isUsernameOrEmailTaken
    @PostMapping("/sign-up")
    public BasicResponse signUp(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email
    ) {
        System.out.println("Hello from sign up");
        try {
            userService.registerUser(username, password, email);
            return new BasicResponse(true, 200);
        } catch (RuntimeException e) {
            return new BasicResponse(false, 400);
        }
    }

    @PostMapping("/check-availability")
    public BasicResponse checkAvailability(
            @RequestParam String username,
            @RequestParam String email
    ) {
        System.out.println("Hello from checkAvailability");
        try {
            boolean response = userService.isUsernameOrEmailTaken(username, email);
            if (!response) {
                System.out.println("user free");
                return new BasicResponse(true, 200);
            }
            System.out.println("user taken");
            return new BasicResponse(false, 400);
        } catch (RuntimeException e) {
            return new BasicResponse(false, 400);
        }
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        User user = userService.login(username, password);
        if (user != null) {
            String sessionId = sessionManager.createSession(user);
            System.out.println("Created session: " + sessionId + " for user: " + user.getUsername());

            // הגדרתי secure - false http only - false
            // כי אם לא לא נוכל לגשת מלוקל הוסט
            ResponseCookie cookie = ResponseCookie.from("session_id", sessionId)
                    .httpOnly(false).secure(false)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 ימים
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            System.out.println("Set cookie header: " + cookie.toString());


            return new LoginResponse(true, 200, sessionId, user);
        }
        return new LoginResponse(false, 400, null, null);
    }

    @PostMapping("/logout")
    public ResponseEntity<BasicResponse> logout(
            @CookieValue(name = "session_id", required = false) String sessionId,
            HttpServletResponse response) {

        System.out.println("Entered logout with sessionId: " + sessionId);

        if (sessionId != null) {
            try {
                // מחיקת הסשן מהמערכת
                sessionManager.invalidateSession(sessionId);

                // יצירת cookie ריק שיגרום למחיקת ה-cookie הקיים
                ResponseCookie cookie = ResponseCookie.from("session_id", "")
                        .httpOnly(false)
                        .path("/")
                        .maxAge(0)
                        .domain("localhost")
                        .build();

                // הוספת ה-cookie לתגובה
                response.setHeader("Set-Cookie", cookie.toString());

                return ResponseEntity.ok()
                        .header("Set-Cookie", cookie.toString())
                        .body(new BasicResponse(true, 200));
            } catch (Exception e) {
                System.out.println("Error during logout: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new BasicResponse(false, 500));
            }
        }

        return ResponseEntity.ok(new BasicResponse(false, 225));
    }

    @PostMapping("/check-session")
    public ResponseEntity<?> checkSession(@CookieValue(value = "session_id", required = false) String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            System.out.println("No session ID found in cookies.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "No session ID"));
        }

        System.out.println("Received session ID: " + sessionId);
        User user = sessionManager.getUserBySessionId(sessionId);
        System.out.println(user);

        if (user != null) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Valid session"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid session"));
    }


}
