package com.dev.controllers;

import com.dev.dto.QuestionDTO;
import com.dev.services.ProbabilityGeneratorService;
import com.dev.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/probability")
public class ProbabilityController {

    private final ProbabilityGeneratorService probabilityGeneratorService;
    private final StatisticsService statisticsService;

    public ProbabilityController(ProbabilityGeneratorService probabilityGeneratorService,
                                 StatisticsService statisticsService) {
        this.probabilityGeneratorService = probabilityGeneratorService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/generate/{level}")
    public ResponseEntity<QuestionDTO> generateQuestion(@PathVariable int level) {
        if (level < 1 || level > 6) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(probabilityGeneratorService.generateProbabilityQuestion(level));
    }

    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAnswer(@RequestBody QuestionController.AnswerSubmissionDTO submission) {
        // Record the attempt in statistics
        statisticsService.recordAttempt(
                submission.getUsername(),
                submission.getSubjectType(), // This will be "probability-X" where X is the level
                submission.isCorrect(),
                submission.getQuestion(),
                submission.getUserAnswer().toString(),
                submission.getCorrectAnswer().toString(),
                submission.getSolution()
        );

        System.out.println("Probability Controller checkAnswer is called");

        // Generate a new question for the next attempt (same level)
        int level = determineLevel(submission.getSubjectType());
        QuestionDTO nextQuestion = probabilityGeneratorService.generateProbabilityQuestion(level);

        Map<String, Object> response = new HashMap<>();
        response.put("correct", submission.isCorrect());
        response.put("nextQuestion", nextQuestion);

        return ResponseEntity.ok(response);
    }

    // Helper method to determine level from subjectType
    private int determineLevel(String subjectType) {
        // Extract level number from subjectType (e.g., "probability-3" -> 3)
        if (subjectType != null && subjectType.startsWith("probability-")) {
            try {
                return Integer.parseInt(subjectType.substring(12));
            } catch (NumberFormatException e) {
                return 1; // Default to level 1 if parsing fails
            }
        }
        return 1; // Default to level 1
    }
}