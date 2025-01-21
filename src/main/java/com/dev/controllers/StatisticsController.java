package com.dev.controllers;

import com.dev.models.MistakeLog;
import com.dev.services.StatisticsService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Data
    public static class AttemptRequest {
        private String username;
        private String subjectType;
        private boolean correct;
        private String question;
        private String userAnswer;
        private String correctAnswer;
        private String solution;
    }

    @PostMapping("/record")
    public ResponseEntity<Void> recordAttempt(@RequestBody AttemptRequest request) {
        System.out.println("In record function");
        statisticsService.recordAttempt(
                request.getUsername(),
                request.getSubjectType(),
                request.isCorrect(),
                request.getQuestion(),
                request.getUserAnswer(),
                request.getCorrectAnswer(),
                request.getSolution()
        );
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(@PathVariable String username) {
        Map<String, Object> statistics = statisticsService.getUserStatistics(username);
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/{username}/subject/{subjectType}")
    public ResponseEntity<List<MistakeLog>> getMistakesBySubject(
            @PathVariable String username,
            @PathVariable String subjectType) {
        List<MistakeLog> mistakes = statisticsService.getMistakeLogRepository()
                .findByUsernameAndSubjectType(username, subjectType);
        return ResponseEntity.ok(mistakes);
    }

    @GetMapping("/{username}/mistakes")
    public ResponseEntity<List<MistakeLog>> getUnresolvedMistakes(@PathVariable String username) {
        List<MistakeLog> mistakes = statisticsService.getMistakeLogRepository()
                .findByUsernameAndResolvedFalse(username);
        return ResponseEntity.ok(mistakes);
    }

    @DeleteMapping("/mistakes/{mistakeId}")
    public ResponseEntity<Void> deleteMistake(@PathVariable Long mistakeId) {
        statisticsService.deleteMistake(mistakeId);
        return ResponseEntity.noContent().build();
    }
}
