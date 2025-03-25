package com.dev.controllers;

import com.dev.services.AdaptiveLearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/learning")
public class AdaptiveLearningController {

    @Autowired
    private AdaptiveLearningService adaptiveLearningService;

    /**
     * Get the recommended level for a user in a specific subject.
     * This GET endpoint still uses request parameters.
     */
    @GetMapping("/recommended-level")
    public ResponseEntity<Map<String, Object>> getRecommendedLevel(
            @RequestParam String username,
            @RequestParam String subjectType) {

        int recommendedLevel = adaptiveLearningService.getRecommendedLevel(username, subjectType);
        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("subjectType", subjectType);
        response.put("recommendedLevel", recommendedLevel);

        return ResponseEntity.ok(response);
    }

    /**
     * Process an answer and get the updated recommended level.
     * Updated to accept a JSON request body instead of individual request parameters.
     */
    @PostMapping("/process-answer")
    public ResponseEntity<Map<String, Object>> processAnswer(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String subjectType = (String) request.get("subjectType");
        int currentLevel = (int) request.get("currentLevel");
        boolean isCorrect = (boolean) request.get("isCorrect");

        int newLevel = adaptiveLearningService.processAnswer(username, subjectType, currentLevel, isCorrect);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("subjectType", subjectType);
        response.put("previousLevel", currentLevel);
        response.put("isCorrect", isCorrect);
        response.put("newLevel", newLevel);
        response.put("levelChanged", newLevel != currentLevel);

        return ResponseEntity.ok(response);
    }

    /**
     * Save a user's level for a specific subject.
     * This endpoint remains unchanged as it already accepts a JSON body.
     */
    @PostMapping("/save-user-level")
    public ResponseEntity<Map<String, Object>> saveUserLevel(@RequestBody Map<String, Object> request) {

        String username = (String) request.get("username");
        String subjectType = (String) request.get("subjectType");
        int level = (int) request.get("level");

        adaptiveLearningService.saveUserLevel(username, subjectType, level);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("subjectType", subjectType);
        response.put("level", level);
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
}