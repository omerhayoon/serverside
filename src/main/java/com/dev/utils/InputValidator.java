package com.dev.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class InputValidator {
    private static final String SPECIAL_CHARS = "!@#$%^&*";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*]).{6,12}$"
    );
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@(.+)$"
    );
    public List<String> validateEmail(String email) {
        List<String> errors = new ArrayList<>();

        if (email == null || email.isEmpty()) {
            errors.add("כתובת אימייל היא שדה חובה");
            return errors;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("כתובת האימייל אינה בפורמט תקין");
        }

        return errors;
    }
    public List<String> validateUsername(String username) {
        List<String> errors = new ArrayList<>();

        if (username == null || username.isEmpty()) {
            errors.add("שם משתמש הוא שדה חובה");
            return errors;
        }

        if (username.length() < 6) {
            errors.add("שם המשתמש חייב להכיל לפחות 6 תווים");
        }

        // אופציונלי - בדיקה שאין תווים מיוחדים בשם משתמש
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            errors.add("שם המשתמש יכול להכיל רק אותיות ומספרים");
        }

        return errors;
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 12) {
            return false;
        }

        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public List<String> validatePassword(String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            errors.add("סיסמה לא יכולה להיות ריקה");
            return errors;
        }
        if( !password.equals(confirmPassword)) {
            errors.add("password mismatch");
            return errors;
        }

        if (password.length() < 6 || password.length() > 12) {
            errors.add("אורך הסיסמה חייב להיות בין 6 ל-12 תווים");
        }

        if (!password.matches(".*[0-9].*")) {
            errors.add("הסיסמה חייבת להכיל לפחות מספר אחד");
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            errors.add("הסיסמה חייבת להכיל לפחות אות אחת");
        }

        if (!containsSpecialChar(password)) {
            errors.add("הסיסמה חייבת להכיל לפחות תו מיוחד אחד");
        }

        return errors;
    }

    private boolean containsSpecialChar(String password) {
        return password.chars().anyMatch(ch -> SPECIAL_CHARS.indexOf(ch) != -1);
    }
}
