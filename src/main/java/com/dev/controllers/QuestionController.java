package com.dev.controllers;

import com.dev.dto.QuestionDTO;
import com.dev.services.QuestionGeneratorService;
import com.dev.services.StatisticsService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionGeneratorService questionGeneratorService;
    private final StatisticsService statisticsService;

    public QuestionController(QuestionGeneratorService questionGeneratorService,
                              StatisticsService statisticsService) {
        this.questionGeneratorService = questionGeneratorService;
        this.statisticsService = statisticsService;
    }

    // Updated endpoint: accepts both type and level
    @GetMapping("/generate/{type}/{level}")
    public ResponseEntity<QuestionDTO> generateQuestion(@PathVariable String type,
                                                        @PathVariable int level) {
        System.out.println("Entered generateQuestion for type: " + type
                + ", level: " + level);
        return ResponseEntity.ok(questionGeneratorService.generateQuestion(type, level));
    }

    // Include level in the answer submission DTO
    @Data
    public static class AnswerSubmissionDTO {
        private String username;
        private String subjectType;
        private int level; // New field for level
        private Map<String, String> userAnswer;
        private boolean correct;
        private String question;
        private Map<String, String> correctAnswer;
        private String solution;
    }

    @PostMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAnswer(@RequestBody AnswerSubmissionDTO submission) {
        // Record the attempt in statistics
        statisticsService.recordAttempt(
                submission.getUsername(),
                submission.getSubjectType(),
                submission.isCorrect(),
                submission.getQuestion(),
                submission.getUserAnswer().toString(),
                submission.getCorrectAnswer().toString(),
                submission.getSolution()
        );
        System.out.println("QuestionController checkAnswer is called");
        // Generate a new question using both subjectType and level from the submission
        QuestionDTO nextQuestion = questionGeneratorService.generateQuestion(
                submission.getSubjectType(), submission.getLevel());
        Map<String, Object> response = new HashMap<>();
        response.put("correct", submission.isCorrect());
        response.put("nextQuestion", nextQuestion);
        return ResponseEntity.ok(response);
    }
}