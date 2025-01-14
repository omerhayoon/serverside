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

    @PostMapping("/sign-up")
    public BasicResponse signUp(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email
    ) {
        System.out.println("Hello from sign up");
        try {
            userService.registerUser(username, password, email);
            return new BasicResponse(true,200);
        } catch (RuntimeException e) {
            return new BasicResponse(false,400);
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

            // הגדרת Cookie
            ResponseCookie cookie = ResponseCookie.from("session_id", sessionId)
                    .httpOnly(true)
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
    public BasicResponse logout(@CookieValue(name = "session_id", required = false) String sessionId) {
        System.out.println("Entered logout");
        if (sessionId != null) {
            sessionManager.invalidateSession(sessionId);

            // יצירת קוקי חדש עם זמן חיים 0 כדי למחוק אותו
            ResponseCookie cookie = ResponseCookie.from("session_id", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)  // גורם למחיקת הקוקי
                    .build();

            return new BasicResponse(true,200);
        }

        return new BasicResponse(false,225);

    }

//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@CookieValue(name = "session_id", required = false) String sessionId) {
//        if (sessionId != null) {
//            sessionManager.invalidateSession(sessionId);
//
//            // יצירת קוקי חדש עם זמן חיים 0 כדי למחוק אותו
//            ResponseCookie cookie = ResponseCookie.from("session_id", "")
//                    .httpOnly(true)
//                    .path("/")
//                    .maxAge(0)  // גורם למחיקת הקוקי
//                    .build();
//
//            return ResponseEntity.ok()
//                    .header("Set-Cookie", cookie.toString())
//                    .body(new BasicResponse(true, 200));
//        }
//
//        return ResponseEntity.badRequest()
//                .body(new BasicResponse(false, 400));
//    }
    @PostMapping("/check-session")
    public ResponseEntity<?> checkSession(@CookieValue("session_id") String sessionId) {
        System.out.println("Received session ID: " + sessionId); // בדיקה בלוג
        User user = sessionManager.getUserBySessionId(sessionId);

        if (user != null) {
            return ResponseEntity.ok(Map.of("success", true, "user", user));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false));
    }

}
