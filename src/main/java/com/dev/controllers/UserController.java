package com.dev.controllers;

import com.dev.BasicResponse;
import com.dev.models.User;
import com.dev.repository.UserRepository;
import com.dev.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/update-profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String name = (String) request.get("name");
        String profileIcon = (String) request.get("profileIcon");

        Map<String, Object> response = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "שם משתמש חסר");
            return ResponseEntity.badRequest().body(response);
        }

        if (name == null || name.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "שם פרטי חסר");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Optional<User> userOpt = userRepository.findByUsername(username);

            if (!userOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "משתמש לא נמצא");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOpt.get();
            user.setName(name);

            // Set profile icon if provided
            if (profileIcon != null && !profileIcon.trim().isEmpty()) {
                user.setProfileIcon(profileIcon);
            }

            userRepository.save(user);

            response.put("success", true);
            response.put("message", "פרופיל עודכן בהצלחה");
            response.put("user", new HashMap<String, Object>() {{
                put("username", user.getUsername());
                put("name", user.getName());
                put("email", user.getEmail());
                put("profileIcon", user.getProfileIcon());
            }});

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "שגיאה בעדכון הפרופיל: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}