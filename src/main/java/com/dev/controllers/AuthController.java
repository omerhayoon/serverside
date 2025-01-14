package com.dev.controllers;

import com.dev.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private DbUtils dbUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        String hashedPassword = DbUtils.hashPasswordMD5(password);
        if (dbUtils.checkCredentials(username, hashedPassword)) {
            ResponseCookie cookie = ResponseCookie.from("session_id", "unique_session_id")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(7 * 24 * 60 * 60) // 7 ימים
                    .build();

            return ResponseEntity.ok()
                    .header("Set-Cookie", cookie.toString())
                    .body("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("session_id", null)
                .httpOnly(true)
                .path("/")
                .maxAge(0) // מחיקת ה-cookie
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body("Logged out successfully");
    }
}
