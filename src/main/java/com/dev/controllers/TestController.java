package com.dev.controllers;

import org.springframework.web.bind.annotation.*;
import com.dev.BasicResponse; // For returning responses
import com.dev.User; // The User class
import com.dev.utils.DbUtils; // Database utility class
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api") // Base URL for all routes in this controller
public class TestController {

    @Autowired
    private DbUtils dbUtils;

    // Test endpoint
    @GetMapping("/")
    public String test() {
        return "Hello From Server!";
    }

    // Check if a username is available
    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam String username) {
        return dbUtils.checkIfUsernameAvailable(username);
    }

    // Sign up endpoint
    @RequestMapping("/sign-up")
    public BasicResponse register(String username, String password, String confirmPassword, String email) {
        System.out.println("Tryed to register");
        System.out.println(username + " username ");
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return new BasicResponse(false, 404); // Error code for mismatch passwords
        }

        if (!dbUtils.checkIfUsernameAvailable(username)) {
            System.out.println("Username is already taken.");
            return new BasicResponse(false, 409); // Error code for username taken
        }
        boolean success = dbUtils.addUser(username, password, email, false);
        if (success) {
            System.out.println("User added successfully.");
            return new BasicResponse(true, null);
        } else {
            System.out.println("Failed to add user.");
            return new BasicResponse(false, 500); // Internal server error
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public BasicResponse login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        System.out.println("Login attempt for username: " + username);

        if (dbUtils.checkCredentials(username, password)) {
            System.out.println("Login successful.");
            return new BasicResponse(true, null);
        } else {
            System.out.println("Invalid credentials.");
            return new BasicResponse(false, 401); // Unauthorized error code
        }
    }

    // Add note endpoint
    @PostMapping("/add-note")
    public BasicResponse addNote(
            @RequestParam String username,
            @RequestParam String text
    ) {
        System.out.println("Add note request for username: " + username);

        User user = dbUtils.getUserByUsername(username);
        if (user != null) {
            System.out.println("User found. Note can be added.");
            // Assuming User class has a method for handling notes
            return new BasicResponse(true, null);
        } else {
            System.out.println("User not found.");
            return new BasicResponse(false, 404); // Error for user not found
        }
    }
}
