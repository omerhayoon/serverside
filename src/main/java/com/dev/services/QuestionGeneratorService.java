package com.dev.services;

import com.dev.dto.QuestionDTO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class QuestionGeneratorService {

    private final Random random = new Random();

    public QuestionDTO generateQuestion(String type) {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setType(type);

        switch (type) {
            case "addition":
                return generateAdditionQuestion();
            case "subtraction":
                return generateSubtractionQuestion();
            case "multiplication":
                return generateMultiplicationQuestion();
            case "division":
                return generateDivisionQuestion();
            case "linear":
                return generateLinearQuestion();
            case "system":
                return generateSystemQuestion();
            default:
                throw new IllegalArgumentException("Unknown question type: " + type);
        }
    }

    private QuestionDTO generateAdditionQuestion() {
        QuestionDTO dto = new QuestionDTO();
        int num1 = random.nextInt(20);
        int num2 = random.nextInt(20);

        dto.setQuestion(String.format("%d + %d = ?", num1, num2));
        dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
        dto.setSolution(String.format("%d + %d = %d\nחיבור פשוט של שני המספרים",
                num1, num2, num1 + num2));

        return dto;
    }

    private QuestionDTO generateMultiplicationQuestion() {
        QuestionDTO dto = new QuestionDTO();
        int factor1 = random.nextInt(10) + 1;
        int factor2 = random.nextInt(10) + 1;

        dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
        dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
        dto.setSolution(String.format("%d × %d = %d\nכפל פשוט של שני המספרים",
                factor1, factor2, factor1 * factor2));

        return dto;
    }

    private QuestionDTO generateSubtractionQuestion() {
        QuestionDTO dto = new QuestionDTO();
        int minuend = random.nextInt(20);
        int subtrahend = random.nextInt(minuend + 1);

        dto.setQuestion(String.format("%d - %d = ?", minuend, subtrahend));
        dto.setAnswer(Map.of("x", String.valueOf(minuend - subtrahend)));
        dto.setSolution(String.format("%d - %d = %d\nחיסור פשוט של שני המספרים",
                minuend, subtrahend, minuend - subtrahend));

        return dto;
    }

    private QuestionDTO generateDivisionQuestion() {
        QuestionDTO dto = new QuestionDTO();
        int divisor = random.nextInt(10) + 1;
        int multiplier = random.nextInt(10) + 1;
        int product = divisor * multiplier;

        dto.setQuestion(String.format("%d ÷ %d = ?", product, divisor));
        dto.setAnswer(Map.of("x", String.valueOf(multiplier)));
        dto.setSolution(String.format("%d ÷ %d = %d\nחילוק פשוט של המספרים",
                product, divisor, multiplier));

        return dto;
    }

    private QuestionDTO generateLinearQuestion() {
        QuestionDTO dto = new QuestionDTO();
        int a = random.nextInt(5) + 1;
        int b = random.nextInt(20);
        int x = random.nextInt(10);
        int result = a * x + b;

        dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
        dto.setAnswer(Map.of("x", String.valueOf(x)));
        dto.setSolution(String.format("נתון: %dx + %d = %d\n\n" +
                        "נחסר %d משני האגפים:\n" +
                        "%dx = %d\n\n" +
                        "נחלק ב-%d את שני האגפים:\n" +
                        "x = %d",
                a, b, result, b, a, result - b, a, x));

        return dto;
    }

    private QuestionDTO generateSystemQuestion() {
        QuestionDTO dto = new QuestionDTO();
        int x = random.nextInt(5) + 1;
        int y = random.nextInt(5) + 1;

        String question = String.format("2x + y = %d\nx + y = %d",
                2*x + y, x + y);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", String.valueOf(x), "y", String.valueOf(y)));
        dto.setSolution(String.format("נתון:\n" +
                        "2x + y = %d  (1)\n" +
                        "x + y = %d     (2)\n\n" +
                        "נחסר את משוואה (2) ממשוואה (1):\n" +
                        "x = %d\n\n" +
                        "נציב את x במשוואה (2):\n" +
                        "%d + y = %d\n" +
                        "y = %d\n\n" +
                        "x = %d, y = %d",
                2*x + y, x + y, x, x, x + y, y, x, y));

        return dto;
    }
}