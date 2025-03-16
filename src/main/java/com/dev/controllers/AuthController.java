package com.dev.controllers;

import com.dev.BasicResponse;
import com.dev.LoginResponse;
import com.dev.RegisterResponse;
import com.dev.dto.UserDTO;
import com.dev.models.User;
import com.dev.services.UserService;
import com.dev.utils.Constants;
import com.dev.utils.InputValidator;
import com.dev.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private InputValidator inputValidator;

    @PostMapping("/sign-up")
    public RegisterResponse signUp(
            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String email
    ) {
        System.out.println("Hello from sign up");
        System.out.println(password + "password");
        System.out.println(confirmPassword + "confirm password");
        try {
            if (name == null || name.trim().isEmpty()) {
                return new RegisterResponse(false, Constants.ERROR_CODE_BAD, "Invalid name");
            }

            List<String> usernameErrors = inputValidator.validateUsername(username);
            if (!usernameErrors.isEmpty()) {
                return new RegisterResponse(false, Constants.ERROR_CODE_BAD, "Invalid username");
            }

            List<String> emailErrors = inputValidator.validateEmail(email);
            if (!emailErrors.isEmpty()) {
                return new RegisterResponse(false, Constants.ERROR_CODE_BAD, "Invalid email");
            }

            List<String> passwordErrors = inputValidator.validatePassword(password, confirmPassword);
            if (!passwordErrors.isEmpty()) {
                return new RegisterResponse(false, Constants.ERROR_CODE_BAD, "Invalid Password");
            }

            if (userService.isUsernameOrEmailTaken(username, email)) {
                return new RegisterResponse(false, Constants.ERROR_CODE_BAD, "Email/Username is taken");
            }

            userService.registerUser(name, username, password, confirmPassword, email);
            return new RegisterResponse(true, Constants.ERROR_CODE_OK, "Success");
        } catch (RuntimeException e) {
            return new RegisterResponse(false, Constants.ERROR_CODE_BAD, "Error occurred during sign up");
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
                return new BasicResponse(true, Constants.ERROR_CODE_OK);
            }
            System.out.println("user taken");
            return new BasicResponse(false, Constants.ERROR_CODE_BAD);
        } catch (RuntimeException e) {
            return new BasicResponse(false, Constants.ERROR_CODE_BAD);
        }
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        User newUser = userService.login(username, password);
        if (newUser != null) {
            String sessionId = sessionManager.createSession(newUser);
            System.out.println("Created session: " + sessionId + " for user: " + newUser.getUsername());

            UserDTO user = new UserDTO(newUser);
            user.setUsername(user.getUsername());
            user.setName(user.getName());
            user.setEmail(user.getEmail());

            ResponseCookie cookie = ResponseCookie.from("session_id", sessionId)
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60)
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            System.out.println("Set cookie header: " + cookie.toString());

            return new LoginResponse(true, Constants.ERROR_CODE_OK, sessionId, user);
        }
        return new LoginResponse(false, Constants.ERROR_CODE_BAD, null, null);
    }

    @PostMapping("/logout")
    public ResponseEntity<BasicResponse> logout(
            @CookieValue(name = "session_id", required = false) String sessionId,
            HttpServletResponse response) {

        System.out.println("Entered logout with sessionId: " + sessionId);

        if (sessionId != null) {
            try {
                sessionManager.removeSession(sessionId);

                // יצירת cookie ריק שיגרום למחיקת הקוקי הקיים – ללא הגדרת domain כדי לשמור על עקביות
                ResponseCookie cookie = ResponseCookie.from("session_id", "")
                        .httpOnly(false)
                        .path("/")
                        .maxAge(0)
                        .build();

                response.setHeader("Set-Cookie", cookie.toString());

                return ResponseEntity.ok()
                        .header("Set-Cookie", cookie.toString())
                        .body(new BasicResponse(true, Constants.ERROR_CODE_OK));
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "No session ID"));
        }

        User user = sessionManager.getUserBySessionId(sessionId);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            response.put("profileIcon", user.getProfileIcon());
            response.put("isAdmin", user.isAdmin());
            response.put("message", "Valid session");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Invalid session"));
    }
}
