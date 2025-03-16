package com.dev.services;

import com.dev.models.SubjectStatistics;
import com.dev.models.UserLevel;
import com.dev.repository.SubjectStatisticsRepository;
import com.dev.repository.UserLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdaptiveLearningService {

    @Autowired
    private SubjectStatisticsRepository subjectStatisticsRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    // Constants for adaptive learning algorithm
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 6;
    private static final int CONSECUTIVE_CORRECT_TO_LEVEL_UP = 3;
    private static final int CONSECUTIVE_WRONG_TO_LEVEL_DOWN = 2;
    private static final double MIN_SUCCESS_RATE_FOR_LEVEL_UP = 80.0;

    // Cache to track consecutive correct/wrong answers
    private Map<String, Integer> consecutiveCorrectMap = new HashMap<>();
    private Map<String, Integer> consecutiveWrongMap = new HashMap<>();

    /**
     * Get recommended level for a user in a specific subject type.
     * The subjectType is normalized to its base type (e.g., "probability-3" -> "probability").
     */
    public int getRecommendedLevel(String username, String subjectType) {
        String baseSubjectType = getBaseSubjectType(subjectType);

        // First, check if user has a saved level for the base subject type
        Optional<UserLevel> userLevelOpt = userLevelRepository.findByUsernameAndSubjectType(username, baseSubjectType);
        if (userLevelOpt.isPresent()) {
            return userLevelOpt.get().getLevel();
        }

        // Get user's statistics for this base subject
        Optional<SubjectStatistics> statsOpt = subjectStatisticsRepository.findByUsernameAndSubjectType(username, baseSubjectType);
        if (!statsOpt.isPresent()) {
            // No history for this subject, start at level 1
            saveUserLevel(username, baseSubjectType, 1);
            return 1;
        }

        SubjectStatistics stats = statsOpt.get();

        // If user has very few attempts, start conservatively
        if (stats.getTotalQuestions() < 5) {
            saveUserLevel(username, baseSubjectType, 1);
            return 1;
        }

        // Calculate success rate
        double successRate = stats.getSuccessRate();

        // Determine level based on success rate
        int recommendedLevel;
        if (successRate < 40) {
            recommendedLevel = 1; // Struggling, keep at easiest level
        } else if (successRate < 60) {
            recommendedLevel = 2; // Below average
        } else if (successRate < 75) {
            recommendedLevel = 3; // Average
        } else if (successRate < 85) {
            recommendedLevel = 4; // Above average
        } else if (successRate < 95) {
            recommendedLevel = 5; // Strong
        } else {
            recommendedLevel = 6; // Excellent
        }

        // Save the recommended level using the base subject type
        saveUserLevel(username, baseSubjectType, recommendedLevel);
        return recommendedLevel;
    }

    /**
     * Process an answer and determine if the user should level up, down, or stay the same.
     * Uses the normalized base subject type.
     */
    public int processAnswer(String username, String subjectType, int currentLevel, boolean isCorrect) {
        String baseSubjectType = getBaseSubjectType(subjectType);
        String userSubjectKey = username + ":" + baseSubjectType;

        if (isCorrect) {
            // Reset consecutive wrong counter
            consecutiveWrongMap.put(userSubjectKey, 0);

            // Increment consecutive correct counter
            int consecutiveCorrect = consecutiveCorrectMap.getOrDefault(userSubjectKey, 0) + 1;
            consecutiveCorrectMap.put(userSubjectKey, consecutiveCorrect);

            // Check if user should level up
            if (consecutiveCorrect >= CONSECUTIVE_CORRECT_TO_LEVEL_UP && currentLevel < MAX_LEVEL) {
                // Check if user has good success rate overall for this base subject type
                Optional<SubjectStatistics> statsOpt = subjectStatisticsRepository.findByUsernameAndSubjectType(username, baseSubjectType);
                if (statsOpt.isPresent() && statsOpt.get().getSuccessRate() >= MIN_SUCCESS_RATE_FOR_LEVEL_UP) {
                    // Level up and reset counter
                    consecutiveCorrectMap.put(userSubjectKey, 0);
                    int newLevel = Math.min(currentLevel + 1, MAX_LEVEL);
                    saveUserLevel(username, baseSubjectType, newLevel);
                    return newLevel;
                }
            }
        } else {
            // Reset consecutive correct counter
            consecutiveCorrectMap.put(userSubjectKey, 0);

            // Increment consecutive wrong counter
            int consecutiveWrong = consecutiveWrongMap.getOrDefault(userSubjectKey, 0) + 1;
            consecutiveWrongMap.put(userSubjectKey, consecutiveWrong);

            // Check if user should level down
            if (consecutiveWrong >= CONSECUTIVE_WRONG_TO_LEVEL_DOWN && currentLevel > MIN_LEVEL) {
                // Level down and reset counter
                consecutiveWrongMap.put(userSubjectKey, 0);
                int newLevel = Math.max(currentLevel - 1, MIN_LEVEL);
                saveUserLevel(username, baseSubjectType, newLevel);
                return newLevel;
            }
        }
        // No change in level
        return currentLevel;
    }

    /**
     * Save user's current level using the normalized base subject type.
     */
    @Transactional
    public void saveUserLevel(String username, String subjectType, int level) {
        // subjectType here is already the base subject type
        Optional<UserLevel> existingLevel = userLevelRepository.findByUsernameAndSubjectType(username, subjectType);
        if (existingLevel.isPresent()) {
            // Update existing record
            UserLevel userLevel = existingLevel.get();
            userLevel.setLevel(level);
            userLevelRepository.save(userLevel);
        } else {
            // Create new record
            UserLevel userLevel = new UserLevel(username, subjectType, level);
            userLevelRepository.save(userLevel);
        }
    }

    /**
     * Extract the base subject type from a subject type with level.
     * For example: "probability-3" -> "probability".
     */
    private String getBaseSubjectType(String subjectType) {
        if (subjectType == null) {
            return "";
        }
        if (subjectType.contains("-")) {
            return subjectType.split("-")[0];
        }
        return subjectType;
    }
}
