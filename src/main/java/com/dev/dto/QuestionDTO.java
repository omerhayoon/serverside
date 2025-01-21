package com.dev.dto;

import lombok.Data;

import java.util.Map;

//DTO - Data transfer object -> אובייקטים שאנחנו שולחים ללקוח
@Data
public class QuestionDTO {
    private String question;
    private Map<String, String> answer;  // Can contain x, y for system equations
    private String solution;
    private String type;
}