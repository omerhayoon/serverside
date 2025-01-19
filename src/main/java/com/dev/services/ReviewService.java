package com.dev.services;

import com.dev.models.Review;
import com.dev.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews(){
        return reviewRepository.findAll();
    }
    public Review addReview(Review review) {
        // כאן אפשר להוסיף בדיקות לוגיות לפני השמירה
        if (review.getContent() == null || review.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (review.getUsername() == null || review.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        return reviewRepository.save(review);
    }
    // מוצא ביקורת לפי ID
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    // מוחק ביקורת
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }
}
