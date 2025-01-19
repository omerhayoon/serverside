package com.dev.controllers;

import com.dev.models.Review;
import com.dev.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // מציין שזה REST controller
@RequestMapping("/api/reviews") // כל הנתיבים יתחילו ב-/api/reviews
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // GET /api/reviews - מחזיר את כל הביקורות
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        System.out.println("trying to get AllReviews");
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // GET /api/reviews/{id} - מחזיר ביקורת ספציפית לפי ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        try {
            Review review = reviewService.getReviewById(id);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /api/reviews - יוצר ביקורת חדשה
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        try {
            Review savedReview = reviewService.addReview(review);
            return ResponseEntity.ok(savedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/reviews/{id} - מוחק ביקורת
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
