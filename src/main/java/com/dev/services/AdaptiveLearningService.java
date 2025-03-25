package com.dev.services;

import com.dev.models.SubjectStatistics;
import com.dev.models.UserLevel;
import com.dev.repository.SubjectStatisticsRepository;
import com.dev.repository.UserLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    /**
     * Get the recommended level for a user in a given subject.
     * The subjectType is normalized to its base type.
     */
    public int getRecommendedLevel(String username, String subjectType) {
        String baseSubjectType = getBaseSubjectType(subjectType);
        // First, check if the user has a saved level for the base subject type.
        Optional<UserLevel> userLevelOpt = userLevelRepository.findByUsernameAndSubjectType(username, baseSubjectType);
        if (userLevelOpt.isPresent()) {
            return userLevelOpt.get().getLevel();
        }

        // Retrieve user's statistics for this base subject.
        Optional<SubjectStatistics> statsOpt = subjectStatisticsRepository.findByUsernameAndSubjectType(username, baseSubjectType);
        if (!statsOpt.isPresent()) {
            // No history for this subject – start at level 1.
            saveUserLevel(username, baseSubjectType, 1);
            return 1;
        }

        SubjectStatistics stats = statsOpt.get();
        // If the user has very few attempts, start conservatively.
        if (stats.getTotalQuestions() < 5) {
            saveUserLevel(username, baseSubjectType, 1);
            return 1;
        }

        // Calculate success rate.
        double successRate = stats.getSuccessRate();
        // Determine level based on success rate.
        int recommendedLevel;
        if (successRate < 40) {
            recommendedLevel = 1;
        } else if (successRate < 60) {
            recommendedLevel = 2;
        } else if (successRate < 75) {
            recommendedLevel = 3;
        } else if (successRate < 85) {
            recommendedLevel = 4;
        } else if (successRate < 95) {
            recommendedLevel = 5;
        } else {
            recommendedLevel = 6;
        }

        // Save the recommended level using the base subject type.
        saveUserLevel(username, baseSubjectType, recommendedLevel);
        return recommendedLevel;
    }

    /**
     * Process an answer and adjust the user's level up or down.
     */
    public int processAnswer(String username, String subjectType, int currentLevel, boolean isCorrect) {
        String baseSubjectType = getBaseSubjectType(subjectType);
        // Get or create SubjectStatistics
        Optional<SubjectStatistics> statsOpt = subjectStatisticsRepository.findByUsernameAndSubjectType(username, baseSubjectType);
        SubjectStatistics stats = statsOpt.orElseGet(() -> {
            SubjectStatistics newStats = new SubjectStatistics();
            newStats.setUsername(username);
            newStats.setSubjectType(baseSubjectType);
            return newStats;
        });
        if (isCorrect) {
            int consecutiveCorrect = stats.getConsecutiveCorrect() + 1;
            stats.setConsecutiveCorrect(consecutiveCorrect);

            if (consecutiveCorrect >= CONSECUTIVE_CORRECT_TO_LEVEL_UP && currentLevel < MAX_LEVEL) {
                if (stats.getSuccessRate() >= MIN_SUCCESS_RATE_FOR_LEVEL_UP) {
                    stats.setConsecutiveCorrect(0);
                    // Reset after leveling up
                    int newLevel = Math.min(currentLevel + 1, MAX_LEVEL);
                    saveUserLevel(username, baseSubjectType, newLevel);
                    subjectStatisticsRepository.save(stats);
                    return newLevel;
                }
            }
        } else {
            stats.setConsecutiveCorrect(0);
            // Reset on wrong answer
            // You could add consecutiveWrong tracking if desired
            if (currentLevel > MIN_LEVEL) {
                // For simplicity, level down immediately on wrong answer (or add consecutive wrong logic)
                int newLevel = Math.max(currentLevel - 1, MIN_LEVEL);
                saveUserLevel(username, baseSubjectType, newLevel);
                subjectStatisticsRepository.save(stats);
                return newLevel;
            }
        }

        // Update statistics
        stats.setTotalQuestions(stats.getTotalQuestions() + 1);
        if (isCorrect) {
            stats.setCorrectAnswers(stats.getCorrectAnswers() + 1);
        }
        stats.setLastAttempt(LocalDateTime.now());
        subjectStatisticsRepository.save(stats);
        return currentLevel;
    }

    /**
     * Save the user's current level for the given subject.
     */
    @Transactional
    public void saveUserLevel(String username, String subjectType, int level) {
        Optional<UserLevel> existingLevel = userLevelRepository.findByUsernameAndSubjectType(username, subjectType);
        if (existingLevel.isPresent()) {
            UserLevel userLevel = existingLevel.get();
            userLevel.setLevel(level);
            userLevelRepository.save(userLevel);
        } else {
            UserLevel userLevel = new UserLevel(username, subjectType, level);
            userLevelRepository.save(userLevel);
        }
    }

    /**
     * Extract the base subject type from the provided subjectType.
     * If the subjectType ends with a hyphen followed by digits (e.g. "probability-3"),
     * that part is removed.
     * Otherwise, the subjectType is returned unchanged.
     */
    private String getBaseSubjectType(String subjectType) {
        if (subjectType == null) {
            return "";
        }
        // Only remove the trailing "-number" if it exists.
        if (subjectType.matches(".*-\\d+$")) {
            return subjectType.substring(0, subjectType.lastIndexOf("-"));
        }
        return subjectType;
    }
}