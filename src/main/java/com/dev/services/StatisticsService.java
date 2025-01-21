package com.dev.services;

import com.dev.models.MistakeLog;
import com.dev.models.SubjectStatistics;
import com.dev.repository.MistakeLogRepository;
import com.dev.repository.SubjectStatisticsRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StatisticsService {
    private final SubjectStatisticsRepository subjectStatisticsRepository;
    private final MistakeLogRepository mistakeLogRepository;

    public StatisticsService(SubjectStatisticsRepository subjectStatisticsRepository,
                             MistakeLogRepository mistakeLogRepository) {
        this.subjectStatisticsRepository = subjectStatisticsRepository;
        this.mistakeLogRepository = mistakeLogRepository;
    }

    public MistakeLogRepository getMistakeLogRepository() {
        return mistakeLogRepository;
    }

    public SubjectStatisticsRepository getSubjectStatisticsRepository() {
        return subjectStatisticsRepository;
    }
    private String formatAnswer(String answer) {
        if((answer.contains("x") && answer.contains("y") )){
            if(answer.contains("y=}")){
                return answer.substring(answer.indexOf('=')+1,answer.indexOf(','));
            }else{
                return answer.substring(answer.indexOf("y="),answer.length()-1);
            }
        }else{
            return answer.substring(answer.indexOf("x=")+2,answer.indexOf("}"));
        }
//{x=14}

    }

    public void recordAttempt(String username, String subjectType, boolean correct,
                              String question, String userAnswer, String correctAnswer,
                              String solution) {
        // Get or create statistics for this subject
        System.out.println("Entered record b4 crash");
        SubjectStatistics stats = subjectStatisticsRepository
                .findByUsernameAndSubjectType(username, subjectType)
                .orElse(new SubjectStatistics());
        System.out.println("Question "+ question);
        System.out.println("user answer "+userAnswer);
        System.out.println("correctAnswer "+correctAnswer);
        if (stats.getId() == null) {
            stats.setUsername(username);
            stats.setSubjectType(subjectType);
        }

        // Update statistics
        stats.setTotalQuestions(stats.getTotalQuestions() + 1);
        if (correct) {
            stats.setCorrectAnswers(stats.getCorrectAnswers() + 1);
        } else {
            // Log the mistake
            MistakeLog mistakeLog = new MistakeLog();
            mistakeLog.setUsername(username);
            mistakeLog.setSubjectType(subjectType);
            mistakeLog.setQuestion(question);
            mistakeLog.setUserAnswer(formatAnswer(userAnswer));
            mistakeLog.setCorrectAnswer(formatAnswer(correctAnswer));
            mistakeLog.setSolution(solution);
            mistakeLog.setMistakeDate(LocalDateTime.now());
            mistakeLogRepository.save(mistakeLog);
        }

        stats.setLastAttempt(LocalDateTime.now());
        subjectStatisticsRepository.save(stats);
    }


    public void deleteMistake(Long mistakeId) {
        MistakeLog mistakeLog = mistakeLogRepository.findById(mistakeId)
                .orElseThrow(() -> new RuntimeException("MistakeLog not found with id: " + mistakeId));

        mistakeLogRepository.delete(mistakeLog);
    }

    public Map<String, Object> getUserStatistics(String username) {
        Map<String, Object> statistics = new HashMap<>();

        // Get all subject statistics
        List<SubjectStatistics> subjectStats = subjectStatisticsRepository.findByUsername(username);

        // Calculate overall statistics
        int totalQuestions = 0;
        int totalCorrect = 0;

        for (SubjectStatistics stats : subjectStats) {
            totalQuestions += stats.getTotalQuestions();
            totalCorrect += stats.getCorrectAnswers();
        }

        // Add overall statistics
        statistics.put("totalQuestions", totalQuestions);
        statistics.put("totalCorrect", totalCorrect);
        statistics.put("overallSuccessRate",
                totalQuestions > 0 ? (double) totalCorrect / totalQuestions * 100 : 0);

        // Add subject-specific statistics
        Map<String, Map<String, Object>> subjectSpecificStats = new HashMap<>();
        for (SubjectStatistics stats : subjectStats) {
            Map<String, Object> subjectData = new HashMap<>();
            subjectData.put("totalQuestions", stats.getTotalQuestions());
            subjectData.put("correctAnswers", stats.getCorrectAnswers());
            subjectData.put("successRate", stats.getSuccessRate());
            subjectData.put("lastAttempt", stats.getLastAttempt());

            subjectSpecificStats.put(stats.getSubjectType(), subjectData);
        }
        statistics.put("subjectStatistics", subjectSpecificStats);

        // Add unresolved mistakes
        List<MistakeLog> unresolvedMistakes =
                mistakeLogRepository.findByUsernameAndResolvedFalse(username);
        statistics.put("unresolvedMistakes", unresolvedMistakes);

        return statistics;
    }
}