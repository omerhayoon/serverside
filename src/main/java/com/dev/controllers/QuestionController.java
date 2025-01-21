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

    @GetMapping("/generate/{type}")
    public ResponseEntity<QuestionDTO> generateQuestion(@PathVariable String type) {
        System.out.println("Entered Generate");
        return ResponseEntity.ok(questionGeneratorService.generateQuestion(type));
    }

    @Data
    public static class AnswerSubmissionDTO {
        private String username;
        private String subjectType;
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
        System.out.println("Question Controller checkAnswer is called");
        // Generate a new question for the next attempt
        QuestionDTO nextQuestion = questionGeneratorService.generateQuestion(submission.getSubjectType());

        Map<String, Object> response = new HashMap<>();
        response.put("correct", submission.isCorrect());
        response.put("nextQuestion", nextQuestion);

        return ResponseEntity.ok(response);
    }
}