package com.dev.controllers;

import com.dev.models.Review;
import com.dev.models.User;
import com.dev.services.ReviewService;
import com.dev.utils.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private SessionManager sessionManager;

    @GetMapping
    public ResponseEntity<List<Review>> getReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    public ResponseEntity<Review> addReview(
            @Valid @RequestBody Review review,
            HttpServletRequest request) {
        // Retrieve session_id from cookies
        String sessionId = null;
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("session_id".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        if (sessionId == null) {
            return ResponseEntity.status(401).build();
        }
        System.out.println("ryr ");
        // Get the logged in user from session
        User user = sessionManager.getUserBySessionId(sessionId);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        System.out.println("ryr ");
        // Set the reviewer's first name from the User entity
        review.setName(user.getName());
        review.setUserName(user.getUsername());
        System.out.println(review.getName());
        Review savedReview = reviewService.addReview(review);
        return ResponseEntity.ok(savedReview);
    }
}
