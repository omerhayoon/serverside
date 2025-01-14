//package com.dev.controllers;
//
//import org.springframework.web.bind.annotation.*;
//import com.dev.BasicResponse; // For returning responses
//import com.dev.utils.DbUtils; // Database utility class
//import org.springframework.beans.factory.annotation.Autowired;
//
//@RestController
//@RequestMapping("/api") // Base URL for all routes in this controller
//public class TestController {
//
//    @Autowired
//    private DbUtils dbUtils;
//
//    // Test endpoint
//    @GetMapping("/")
//    public String test() {
//        return "Hello From Server!";
//    }
//
//
//    @GetMapping("/check-username")
//    public boolean checkUsername(@RequestParam String username) {
//        return dbUtils.checkIfUsernameAvailable(username);
//    }
//
//
//    @RequestMapping("/sign-up")
//    public BasicResponse register(String username, String password, String confirmPassword, String email) {
//        try {
//            System.out.println("Tryed to register");
//            System.out.println(username + " username ");
//            if (!password.equals(confirmPassword)) {
//                System.out.println("Passwords do not match.");
//                return new BasicResponse(false, 404); // Error code for mismatch passwords
//            }
//
//            if (!dbUtils.checkIfUsernameAvailable(username)) {
//                System.out.println("Username is already taken.");
//                return new BasicResponse(false, 409); // Error code for username taken
//            }
//            String hashedPassword = DbUtils.hashPasswordMD5(password);
//            boolean success = dbUtils.addUser(username, hashedPassword, email, false);
//            if (success) {
//                System.out.println("User added successfully.");
//                return new BasicResponse(true, null);
//            } else {
//                System.out.println("Failed to add user.");
//                return new BasicResponse(false, 500); // Internal server error
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @PostMapping("/login")
//    public BasicResponse login(
//            @RequestParam String username,
//            @RequestParam String password
//    ) {
//        try {
//            System.out.println("Login attempt for username: " + username);
//            String hashedPassword = DbUtils.hashPasswordMD5(password);
//            if (dbUtils.checkCredentials(username, hashedPassword)) {
//                System.out.println("Login successful.");
//                System.out.println(username + " "+password+" "+" "+hashedPassword);
//                return new BasicResponse(true, null);
//            } else {
//                System.out.println("login failed"+ username + " "+password+" "+" "+hashedPassword);
//                System.out.println("Invalid credentials.");
//                return new BasicResponse(false, 401); // Unauthorized error code
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
