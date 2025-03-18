package com.dev.services;

import com.dev.dto.QuestionDTO;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;

@Service
public class QuestionGeneratorService {

    private final Random random = new Random();
    // Right-to-left marker for Hebrew text
    private final String rtl = "\u200F";

    // Main entry method that routes based on question type and difficulty level.
    public QuestionDTO generateQuestion(String type, int level) {
        switch (type.toLowerCase()) {
            case "addition":
                return generateAdditionQuestion(level);
            case "subtraction":
                return generateSubtractionQuestion(level);
            case "multiplication":
                return generateMultiplicationQuestion(level);
            case "division":
                return generateDivisionQuestion(level);
            case "linear":
                return generateLinearQuestion(level);
            case "system":
                return generateSystemQuestion(level);
            default:
                throw new IllegalArgumentException("Unknown question type " + type);
        }
    }

    private QuestionDTO generateAdditionQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("addition");
        dto.setLevel(level);
        int num1, num2, num3, num4;
        switch (level) {
            case 1:
                num1 = random.nextInt(5) + 1;
                num2 = random.nextInt(5) + 1;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(rtl + String.format("נתון\n%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 2:
                num1 = random.nextInt(10) + 1;
                num2 = random.nextInt(10) + 1;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(rtl + String.format("נתון\n%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 3:
                num1 = random.nextInt(40) + 10;
                num2 = random.nextInt(40) + 10;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(rtl + String.format("נתון\n%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 4:
                num1 = random.nextInt(400) + 100;
                num2 = random.nextInt(400) + 100;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(rtl + String.format("נתון\n%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 5:
                num1 = random.nextInt(30) + 10;
                num2 = random.nextInt(30) + 10;
                num3 = random.nextInt(30) + 10;
                dto.setQuestion(String.format("%d + %d + %d = ?", num1, num2, num3));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2 + num3)));
                dto.setSolution(rtl + String.format("נתון\n%d + %d + %d = %d\nחיבור פשוט של שלושת המספרים",
                        num1, num2, num3, num1 + num2 + num3));
                return dto;
            case 6:
                num1 = random.nextInt(20) + 10;
                num2 = random.nextInt(10) + 5;
                num3 = random.nextInt(5) + 1;
                int calc = num1 + (num2 * num3);
                dto.setQuestion(String.format("%d + %d × %d = ?", num1, num2, num3));
                dto.setAnswer(Map.of("x", String.valueOf(calc)));
                dto.setSolution(rtl + String.format("נתון\n%d + %d × %d = %d + %d = %d\nיש לחשב קודם את הכפל ואז להוסיף את התוצאה למספר הראשון",
                        num1, num2, num3, num1, (num2 * num3), calc));
                return dto;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
        return dto;
    }

    private QuestionDTO generateSubtractionQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("subtraction");
        dto.setLevel(level);
        int minuend, subtrahend;
        switch (level) {
            case 1:
                minuend = random.nextInt(5) + 5;
                subtrahend = random.nextInt(minuend - 1) + 1; // ensure nonzero and less than minuend
                break;
            case 2:
                minuend = random.nextInt(10) + 5;
                subtrahend = random.nextInt(5) + 1;
                break;
            case 3:
                minuend = random.nextInt(90) + 10;
                // ensure subtrahend is at least 1 and less than (minuend - 9)
                subtrahend = random.nextInt(minuend - 9) + 1;
                break;
            case 4:
                minuend = random.nextInt(90) + 10;
                subtrahend = random.nextInt(minuend - 1) + 1;
                break;
            case 5:
                minuend = random.nextInt(400) + 100;
                subtrahend = random.nextInt(minuend - 1) + 1;
                break;
            case 6:
                minuend = random.nextInt(30) + 50;
                subtrahend = random.nextInt(30) + 10;
                int add = random.nextInt(20) + 5;
                dto.setQuestion(String.format("%d - %d + %d = ?", minuend, subtrahend, add));
                int result = minuend - subtrahend + add;
                dto.setAnswer(Map.of("x", String.valueOf(result)));
                dto.setSolution(rtl + String.format("נתון\n%d - %d + %d = %d + %d = %d\nחישוב משמאל לימין חיסור ואז חיבור",
                        minuend, subtrahend, add, minuend - subtrahend, add, result));
                return dto;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
        dto.setQuestion(String.format("%d - %d = ?", minuend, subtrahend));
        int result = minuend - subtrahend;
        dto.setAnswer(Map.of("x", String.valueOf(result)));
        dto.setSolution(rtl + String.format("נתון\n%d - %d = %d\nחיסור פשוט של שני המספרים", minuend, subtrahend, result));
        return dto;
    }

    private QuestionDTO generateMultiplicationQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("multiplication");
        dto.setLevel(level);
        int factor1, factor2, factor3, factor4;
        switch (level) {
            case 1:
                factor1 = random.nextInt(3) + 1;
                factor2 = random.nextInt(3) + 1;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(rtl + String.format("נתון\n%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 2:
                factor1 = random.nextInt(5) + 1;
                factor2 = random.nextInt(5) + 1;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(rtl + String.format("נתון\n%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 3:
                factor1 = random.nextInt(9) + 1;
                factor2 = random.nextInt(40) + 10;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(rtl + String.format("נתון\n%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 4:
                factor1 = random.nextInt(40) + 10;
                factor2 = random.nextInt(40) + 10;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(rtl + String.format("נתון\n%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 5:
                factor1 = random.nextInt(400) + 100;
                factor2 = random.nextInt(9) + 1;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(rtl + String.format("נתון\n%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 6:
                factor1 = random.nextInt(5) + 1;
                factor2 = random.nextInt(5) + 1;
                factor3 = random.nextInt(5) + 1;
                dto.setQuestion(String.format("%d × %d × %d = ?", factor1, factor2, factor3));
                int prod = factor1 * factor2 * factor3;
                dto.setAnswer(Map.of("x", String.valueOf(prod)));
                dto.setSolution(rtl + String.format("נתון\n%d × %d × %d = %d × %d = %d\nכפל של שלושה גורמים",
                        factor1, factor2, factor3, factor1 * factor2, factor3, prod));
                return dto;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
        return dto;
    }

    private QuestionDTO generateDivisionQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("division");
        dto.setLevel(level);
        int divisor, multiplier, product;
        switch (level) {
            case 1:
                divisor = random.nextInt(3) + 1;
                multiplier = random.nextInt(3) + 1;
                product = divisor * multiplier;
                break;
            case 2:
                divisor = random.nextInt(5) + 1;
                multiplier = random.nextInt(5) + 1;
                product = divisor * multiplier;
                break;
            case 3:
                divisor = random.nextInt(10) + 1;
                multiplier = random.nextInt(10) + 1;
                product = divisor * multiplier;
                break;
            case 4:
                divisor = random.nextInt(12) + 1;
                multiplier = random.nextInt(10) + 5;
                product = divisor * multiplier;
                break;
            case 5:
                // Exact division with no remainder
                divisor = random.nextInt(10) + 2;
                multiplier = random.nextInt(10) + 1;
                product = divisor * multiplier;
                dto.setQuestion(String.format("%d ÷ %d = ?", product, divisor));
                dto.setAnswer(Map.of("x", String.valueOf(multiplier)));
                dto.setSolution(rtl + String.format("נתון\n%d ÷ %d = %d\nחילוק פשוט של המספרים", product, divisor, multiplier));
                return dto;
            case 6:
                divisor = random.nextInt(5) + 2;
                multiplier = random.nextInt(5) + 2;
                product = divisor * multiplier;
                int addend = random.nextInt(10) + 1;
                dto.setQuestion(String.format("%d ÷ %d + %d = ?", product, divisor, addend));
                int finalResult = multiplier + addend;
                dto.setAnswer(Map.of("x", String.valueOf(finalResult)));
                dto.setSolution(rtl + String.format("נתון\n%d ÷ %d + %d = %d + %d = %d\nקודם מחשבים את החילוק ואז מוסיפים את המספר הנוסף",
                        product, divisor, addend, multiplier, addend, finalResult));
                return dto;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
        dto.setQuestion(String.format("%d ÷ %d = ?", product, divisor));
        dto.setAnswer(Map.of("x", String.valueOf(multiplier)));
        dto.setSolution(rtl + String.format("נתון\n%d ÷ %d = %d\nחילוק פשוט של המספרים", product, divisor, multiplier));
        return dto;
    }

    private QuestionDTO generateLinearQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("linear");
        dto.setLevel(level);
        int a, b, x, result;
        switch (level) {
            case 1:
                x = random.nextInt(5) + 1;
                b = random.nextInt(5) + 1;
                boolean isAddition = random.nextBoolean();
                if (isAddition) {
                    result = x + b;
                    dto.setQuestion(String.format("x + %d = %d", b, result));
                } else {
                    result = x + b;
                    dto.setQuestion(String.format("x - %d = %d", b, result - b));
                }
                break;
            case 2:
                a = random.nextInt(3) + 1;
                b = random.nextInt(5) + 1;
                x = random.nextInt(5) + 1;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 3:
                a = random.nextInt(3) + 1;
                b = random.nextInt(10) + 1;
                x = random.nextInt(5) + 1;
                a = -a;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 4:
                a = random.nextInt(5) + 2;
                int cCoeff = random.nextInt(3) + 1;
                x = random.nextInt(5) + 1;
                b = random.nextInt(10) + 1;
                dto.setQuestion(String.format("%dx + %d = %dx", a, b, cCoeff));
                dto.setAnswer(Map.of("x", String.valueOf(x)));
                dto.setSolution(rtl + String.format("נתון\n%dx + %d = %dx\n\nנעביר את כל האיברים עם x לצד אחד\n%d - %d = -%d\n%d = -%d\n\nנחלק ב-(%d - %d) את שני האגפים\nx = %d",
                        a, b, cCoeff, a, cCoeff, b, a - cCoeff, b, a - cCoeff, x));
                return dto;
            case 5:
                // Generate whole-number linear equation without fractions
                a = random.nextInt(20) + 2;  // coefficient from 2 to 21
                b = random.nextInt(20) + 1;  // constant from 1 to 20
                x = random.nextInt(20) + 1;  // x from 1 to 20
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                dto.setAnswer(Map.of("x", String.valueOf(x)));
                dto.setSolution(rtl + String.format("נתון\n%dx + %d = %d\n\nנפתור לפי כללי האלגברה\nx = (%d - %d) / %d = %d",
                        a, b, result, result, b, a, x));
                return dto;
            case 6:
                a = random.nextInt(3) + 2;
                b = random.nextInt(3) + 1;
                int d = random.nextInt(3) + 1;
                x = random.nextInt(3) + 1;
                dto.setQuestion(String.format("%d(%dx + %d) = %d", b, a, d, b * (a * x + d)));
                dto.setAnswer(Map.of("x", String.valueOf(x)));
                dto.setSolution(rtl + String.format("נתון\n%d(%dx + %d) = %d\n\nנפתח את הסוגריים\n%d x + %d = %d\n\nנחסר %d משני האגפים\n%d x = %d\n\nנחלק ב-%d את שני האגפים\nx = %d",
                        b, a, d, b * (a * x + d), b * a, b * d, b * (a * x + d), b, b * a, b * (a * x + d) - b * d, b * a, x));
                return dto;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
        dto.setAnswer(Map.of("x", String.valueOf(x)));
        dto.setSolution(rtl + String.format("נתון\n%s\n\nנפעל לפי כללי האלגברה כדי לבודד את x\nx = %d",
                dto.getQuestion(), x));
        return dto;
    }

    private QuestionDTO generateSystemQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("system");
        dto.setLevel(level);
        int x, y;
        int a1, b1, c1;
        int a2, b2, c2;
        switch (level) {
            case 1:
                x = random.nextInt(3) + 1;
                y = random.nextInt(3) + 1;
                a1 = 1; b1 = 1; c1 = x + y;
                a2 = 1; b2 = -1; c2 = x - y;
                break;
            case 2:
                x = random.nextInt(4) + 1;
                y = random.nextInt(4) + 1;
                a1 = 2; b1 = 1; c1 = 2 * x + y;
                a2 = 1; b2 = 1; c2 = x + y;
                break;
            case 3:
                x = random.nextInt(5) + 1;
                y = random.nextInt(5) + 1;
                a1 = 3; b1 = 2; c1 = 3 * x + 2 * y;
                a2 = 2; b2 = -1; c2 = 2 * x - y;
                break;
            case 4:
                x = random.nextInt(5) - 2;
                y = random.nextInt(5) - 2;
                a1 = 2; b1 = -3; c1 = 2 * x - 3 * y;
                a2 = -1; b2 = 2; c2 = -x + 2 * y;
                break;
            case 5:
                x = random.nextInt(3) + 1;
                y = random.nextInt(3) + 1;
                a1 = 4; b1 = 3; c1 = 4 * x + 3 * y;
                a2 = 3; b2 = -5; c2 = 3 * x - 5 * y;
                break;
            case 6:
                x = random.nextInt(3) - 1;
                y = random.nextInt(3) - 1;
                a1 = 5; b1 = 7; c1 = 5 * x + 7 * y;
                a2 = 3; b2 = -11; c2 = 3 * x - 11 * y;
                break;
            default:
                throw new IllegalArgumentException("Invalid level " + level);
        }
        String equation1 = formatEquation(a1, b1, c1);
        String equation2 = formatEquation(a2, b2, c2);
        String question = String.format("%s\n%s", equation1, equation2);
        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", String.valueOf(x), "y", String.valueOf(y)));
        String solution = generateSystemSolution(a1, b1, c1, a2, b2, c2, x, y);
        dto.setSolution(rtl + solution);
        return dto;
    }

    // Helper method to format an equation in the form ax + by = c without extra colons.
    private String formatEquation(int a, int b, int c) {
        StringBuilder equation = new StringBuilder();
        if (a == 1) {
            equation.append("x");
        } else if (a == -1) {
            equation.append("-x");
        } else if (a != 0) {
            equation.append(a).append("x");
        }
        if (b > 0) {
            if (a != 0) equation.append(" + ");
            equation.append(b == 1 ? "y" : b + "y");
        } else if (b < 0) {
            if (a != 0) equation.append(" - ");
            else equation.append("-");
            equation.append(b == -1 ? "y" : (-b) + "y");
        }
        equation.append(" = ").append(c);
        return equation.toString();
    }

    // Helper method to generate a step-by-step solution for a system of equations.
    private String generateSystemSolution(int a1, int b1, int c1, int a2, int b2, int c2, int x, int y) {
        StringBuilder solution = new StringBuilder();
        solution.append("נתון\n");
        solution.append(formatEquation(a1, b1, c1)).append("  (1)\n");
        solution.append(formatEquation(a2, b2, c2)).append("  (2)\n\n");
        solution.append("נשתמש בשיטת האלימינציה\n");
        int lcm = Math.abs(lcm(b1, b2));
        int m1 = lcm / Math.abs(b1);
        int m2 = lcm / Math.abs(b2);
        if (b1 * b2 < 0) {
            solution.append("נכפיל את משוואה (1) ב-").append(m1).append("\n");
            solution.append(formatEquation(a1 * m1, b1 * m1, c1 * m1)).append("\n\n");
            solution.append("נכפיל את משוואה (2) ב-").append(m2).append("\n");
            solution.append(formatEquation(a2 * m2, b2 * m2, c2 * m2)).append("\n\n");
            solution.append("נחבר את שתי המשוואות לביטול y\n");
            int newA = a1 * m1 + a2 * m2;
            int newC = c1 * m1 + c2 * m2;
            solution.append(newA).append("x = ").append(newC).append("\n");
            solution.append("x = ").append(newC).append(" / ").append(newA).append(" = ").append(x).append("\n\n");
        } else {
            solution.append("נכפיל את משוואה (1) ב-").append(m1).append("\n");
            solution.append(formatEquation(a1 * m1, b1 * m1, c1 * m1)).append("\n\n");
            solution.append("נכפיל את משוואה (2) ב-").append(m2).append("\n");
            solution.append(formatEquation(a2 * m2, b2 * m2, c2 * m2)).append("\n\n");
            solution.append("נחסר את משוואה (2) מהמשוואה (1) לביטול y\n");
            int newA = a1 * m1 - a2 * m2;
            int newC = c1 * m1 - c2 * m2;
            solution.append(newA).append("x = ").append(newC).append("\n");
            solution.append("x = ").append(newC).append(" / ").append(newA).append(" = ").append(x).append("\n\n");
        }
        solution.append("נציב את ערך x במשוואה (1) למציאת y\n");
        solution.append(formatEquation(a1, b1, c1)).append("\n");
        solution.append(a1).append(" · ").append(x).append(" + ").append(b1).append("y = ").append(c1).append("\n");
        int xTerm = a1 * x;
        solution.append(xTerm).append(" + ").append(b1).append("y = ").append(c1).append("\n");
        solution.append(b1).append("y = ").append(c1).append(" - ").append(xTerm).append("\n");
        solution.append(b1).append("y = ").append(c1 - xTerm).append("\n");
        solution.append("y = ").append(c1 - xTerm).append(" / ").append(b1).append(" = ").append(y).append("\n\n");
        solution.append("x = ").append(x).append(" , y = ").append(y);
        return solution.toString();
    }


    private int lcm(int a, int b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    private int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}
