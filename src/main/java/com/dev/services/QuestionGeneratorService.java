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
        dto.setType("addition");

        // Determine difficulty (1-6)
        int difficulty = random.nextInt(6) + 1;
        dto.setLevel(difficulty); // Set the level in the DTO

        int num1, num2, num3;
        switch (difficulty) {
            case 1: // Very simple addition with single-digit numbers
                num1 = random.nextInt(5) + 1; // 1-5
                num2 = random.nextInt(5) + 1; // 1-5
                break;
            case 2: // Simple addition with slightly larger numbers
                num1 = random.nextInt(10) + 1; // 1-10
                num2 = random.nextInt(10) + 1; // 1-10
                break;
            case 3: // Double-digit addition
                num1 = random.nextInt(40) + 10; // 10-49
                num2 = random.nextInt(40) + 10; // 10-49
                break;
            case 4: // Triple-digit addition
                num1 = random.nextInt(400) + 100; // 100-499
                num2 = random.nextInt(400) + 100; // 100-499
                break;
            case 5: // Multiple addends
                num1 = random.nextInt(30) + 10; // 10-39
                num2 = random.nextInt(30) + 10; // 10-39
                num3 = random.nextInt(30) + 10; // 10-39
                dto.setQuestion(String.format("%d + %d + %d = ?", num1, num2, num3));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2 + num3)));
                dto.setSolution(String.format("%d + %d + %d = %d\nחיבור פשוט של שלושת המספרים",
                        num1, num2, num3, num1 + num2 + num3));
                return dto;
            case 6: // Mixed operations
                num1 = random.nextInt(20) + 10; // 10-29
                num2 = random.nextInt(10) + 5; // 5-14
                num3 = random.nextInt(5) + 1; // 1-5
                dto.setQuestion(String.format("%d + %d × %d = ?", num1, num2, num3));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + (num2 * num3))));
                dto.setSolution(String.format("%d + %d × %d = %d + %d = %d\nיש לחשב קודם את הכפל ואז להוסיף את התוצאה למספר הראשון",
                        num1, num2, num3, num1, (num2 * num3), num1 + (num2 * num3)));
                return dto;
            default:
                num1 = random.nextInt(10); // 0-9
                num2 = random.nextInt(10); // 0-9
        }

        dto.setQuestion(String.format("%d + %d = ?", num1, num2));
        dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
        dto.setSolution(String.format("%d + %d = %d\nחיבור פשוט של שני המספרים",
                num1, num2, num1 + num2));

        return dto;
    }

    private QuestionDTO generateMultiplicationQuestion() {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("multiplication");

        // Determine difficulty (1-6)
        int difficulty = random.nextInt(6) + 1;
        dto.setLevel(difficulty); // Set the level in the DTO

        int factor1, factor2;
        switch (difficulty) {
            case 1: // Very simple multiplication with single-digit numbers 1-3
                factor1 = random.nextInt(3) + 1; // 1-3
                factor2 = random.nextInt(3) + 1; // 1-3
                break;
            case 2: // Simple multiplication with single-digit numbers
                factor1 = random.nextInt(5) + 1; // 1-5
                factor2 = random.nextInt(5) + 1; // 1-5
                break;
            case 3: // Single-digit × double-digit
                factor1 = random.nextInt(9) + 1; // 1-9
                factor2 = random.nextInt(40) + 10; // 10-49
                break;
            case 4: // Double-digit × double-digit
                factor1 = random.nextInt(40) + 10; // 10-49
                factor2 = random.nextInt(40) + 10; // 10-49
                break;
            case 5: // Triple-digit × single-digit
                factor1 = random.nextInt(400) + 100; // 100-499
                factor2 = random.nextInt(9) + 1; // 1-9
                break;
            case 6: // Multiple operations
                factor1 = random.nextInt(5) + 1; // 1-5
                factor2 = random.nextInt(5) + 1; // 1-5
                int factor3 = random.nextInt(5) + 1; // 1-5
                dto.setQuestion(String.format("%d × %d × %d = ?", factor1, factor2, factor3));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2 * factor3)));
                dto.setSolution(String.format("%d × %d × %d = %d × %d = %d\nכפל של שלושה גורמים",
                        factor1, factor2, factor3, factor1 * factor2, factor3, factor1 * factor2 * factor3));
                return dto;
            default:
                factor1 = random.nextInt(5) + 1; // 1-5
                factor2 = random.nextInt(5) + 1; // 1-5
        }

        dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
        dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
        dto.setSolution(String.format("%d × %d = %d\nכפל פשוט של שני המספרים",
                factor1, factor2, factor1 * factor2));

        return dto;
    }

    private QuestionDTO generateSubtractionQuestion() {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("subtraction");

        // Determine difficulty (1-6)
        int difficulty = random.nextInt(6) + 1;
        dto.setLevel(difficulty); // Set the level in the DTO

        int minuend, subtrahend;
        switch (difficulty) {
            case 1: // Very simple subtraction with single-digit numbers
                minuend = random.nextInt(5) + 5; // 5-9
                subtrahend = random.nextInt(minuend); // 0 to minuend-1
                break;
            case 2: // Simple subtraction with single-digit numbers
                minuend = random.nextInt(10) + 5; // 5-14
                subtrahend = random.nextInt(5); // 0-4
                break;
            case 3: // Double-digit subtraction, no borrowing
                minuend = random.nextInt(90) + 10; // 10-99
                subtrahend = random.nextInt(minuend % 10) + (minuend / 10) * 10 - 10;
                break;
            case 4: // Double-digit subtraction with borrowing
                minuend = random.nextInt(90) + 10; // 10-99
                subtrahend = random.nextInt(minuend - 1) + 1; // 1 to minuend-1
                break;
            case 5: // Triple-digit subtraction
                minuend = random.nextInt(400) + 100; // 100-499
                subtrahend = random.nextInt(minuend - 1) + 1; // 1 to minuend-1
                break;
            case 6: // Multiple operations
                minuend = random.nextInt(30) + 50; // 50-79
                subtrahend = random.nextInt(30) + 10; // 10-39
                int add = random.nextInt(20) + 5; // 5-24
                dto.setQuestion(String.format("%d - %d + %d = ?", minuend, subtrahend, add));
                dto.setAnswer(Map.of("x", String.valueOf(minuend - subtrahend + add)));
                dto.setSolution(String.format("%d - %d + %d = %d + %d = %d\nחישוב משמאל לימין: חיסור ואז חיבור",
                        minuend, subtrahend, add, minuend - subtrahend, add, minuend - subtrahend + add));
                return dto;
            default:
                minuend = random.nextInt(10) + 5; // 5-14
                subtrahend = random.nextInt(5); // 0-4
        }

        dto.setQuestion(String.format("%d - %d = ?", minuend, subtrahend));
        dto.setAnswer(Map.of("x", String.valueOf(minuend - subtrahend)));
        dto.setSolution(String.format("%d - %d = %d\nחיסור פשוט של שני המספרים",
                minuend, subtrahend, minuend - subtrahend));

        return dto;
    }

    private QuestionDTO generateDivisionQuestion() {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("division");

        // Determine difficulty (1-6)
        int difficulty = random.nextInt(6) + 1;
        dto.setLevel(difficulty); // Set the level in the DTO

        int divisor, multiplier, product;
        switch (difficulty) {
            case 1: // Very simple division, numbers 1-5
                divisor = random.nextInt(3) + 1; // 1-3
                multiplier = random.nextInt(3) + 1; // 1-3
                product = divisor * multiplier;
                break;
            case 2: // Simple division, no remainder, numbers 1-10
                divisor = random.nextInt(5) + 1; // 1-5
                multiplier = random.nextInt(5) + 1; // 1-5
                product = divisor * multiplier;
                break;
            case 3: // Division with larger numbers, no remainder
                divisor = random.nextInt(10) + 1; // 1-10
                multiplier = random.nextInt(10) + 1; // 1-10
                product = divisor * multiplier;
                break;
            case 4: // Division with larger numbers, no remainder
                divisor = random.nextInt(12) + 1; // 1-12
                multiplier = random.nextInt(10) + 5; // 5-14
                product = divisor * multiplier;
                break;
            case 5: // Division with remainder
                divisor = random.nextInt(10) + 2; // 2-11
                multiplier = random.nextInt(10) + 1; // 1-10
                product = divisor * multiplier + random.nextInt(divisor); // Add remainder
                dto.setQuestion(String.format("%d ÷ %d = ? (שארית ?)", product, divisor));
                int quotient = product / divisor;
                int remainder = product % divisor;
                dto.setAnswer(Map.of("x", quotient + " (שארית " + remainder + ")"));
                dto.setSolution(String.format("%d ÷ %d = %d עם שארית %d\nחילוק עם שארית",
                        product, divisor, quotient, remainder));
                return dto;
            case 6: // Division as part of a compound expression
                divisor = random.nextInt(5) + 2; // 2-6
                multiplier = random.nextInt(5) + 2; // 2-6
                product = divisor * multiplier;
                int addend = random.nextInt(10) + 1; // 1-10
                dto.setQuestion(String.format("%d ÷ %d + %d = ?", product, divisor, addend));
                dto.setAnswer(Map.of("x", String.valueOf(multiplier + addend)));
                dto.setSolution(String.format("%d ÷ %d + %d = %d + %d = %d\nקודם מחשבים את החילוק ואז מוסיפים את המספר הנוסף",
                        product, divisor, addend, multiplier, addend, multiplier + addend));
                return dto;
            default:
                divisor = random.nextInt(5) + 1; // 1-5
                multiplier = random.nextInt(5) + 1; // 1-5
                product = divisor * multiplier;
        }

        dto.setQuestion(String.format("%d ÷ %d = ?", product, divisor));
        dto.setAnswer(Map.of("x", String.valueOf(multiplier)));
        dto.setSolution(String.format("%d ÷ %d = %d\nחילוק פשוט של המספרים",
                product, divisor, multiplier));

        return dto;
    }

    private QuestionDTO generateLinearQuestion() {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("linear");

        // Determine difficulty (1-6)
        int difficulty = random.nextInt(6) + 1;
        dto.setLevel(difficulty); // Set the level in the DTO

        int a, b, x, result;
        switch (difficulty) {
            case 1: // Very simple linear equation with x+b=c or x-b=c
                x = random.nextInt(5) + 1; // 1-5
                b = random.nextInt(5) + 1; // 1-5
                boolean isAddition = random.nextBoolean();
                if (isAddition) {
                    result = x + b;
                    dto.setQuestion(String.format("x + %d = %d", b, result));
                } else {
                    result = x + b; // To ensure positive value after subtraction
                    dto.setQuestion(String.format("x - %d = %d", b, result - b));
                }
                break;
            case 2: // Basic linear equation ax + b = c
                a = random.nextInt(3) + 1; // 1-3
                b = random.nextInt(5); // 0-4
                x = random.nextInt(5) + 1; // 1-5
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 3: // Linear equation with negative coefficient
                a = random.nextInt(3) + 1; // 1-3
                b = random.nextInt(10); // 0-9
                x = random.nextInt(5) + 1; // 1-5
                a = -a; // Make coefficient negative
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 4: // Linear equation with variable on both sides
                a = random.nextInt(5) + 2; // 2-6
                int c = random.nextInt(3) + 1; // coefficient on right side, 1-3
                x = random.nextInt(5) + 1; // 1-5
                b = random.nextInt(10); // 0-9
                dto.setQuestion(String.format("%dx + %d = %dx", a, b, c));
                dto.setAnswer(Map.of("x", String.valueOf(x)));
                dto.setSolution(String.format("נתון: %dx + %d = %dx\n\n" +
                                "נעביר את כל האיברים עם המשתנה לצד אחד:\n" +
                                "%dx - %dx = -%d\n" +
                                "%dx = -%d\n\n" +
                                "נחלק ב-%d את שני האגפים:\n" +
                                "x = %d",
                        a, b, c, a, c, b, a - c, b, a - c, x));
                return dto;
            case 5: // Linear equation with fractions
                a = random.nextInt(4) + 2; // 2-5
                b = random.nextInt(10); // 0-9
                x = random.nextInt(4) + 1; // 1-4

                dto.setQuestion(String.format("%dx/2 + %d = %d", a, b, (a * x) / 2 + b));
                dto.setAnswer(Map.of("x", String.valueOf(x)));
                dto.setSolution(String.format("נתון: %dx/2 + %d = %d\n\n" +
                                "נחסר %d משני האגפים:\n" +
                                "%dx/2 = %d\n\n" +
                                "נכפיל ב-2 את שני האגפים:\n" +
                                "%dx = %d\n\n" +
                                "נחלק ב-%d את שני האגפים:\n" +
                                "x = %d",
                        a, b, (a * x) / 2 + b, b, a, (a * x) / 2, a, a * x, a, x));
                return dto;
            case 6: // More complex linear equation
                a = random.nextInt(3) + 2; // 2-4
                b = random.nextInt(3) + 1; // 1-3
                int d = random.nextInt(3) + 1; // 1-3
                x = random.nextInt(3) + 1; // 1-3

                dto.setQuestion(String.format("%d(%dx + %d) = %d", b, a, d, b * (a * x + d)));
                dto.setAnswer(Map.of("x", String.valueOf(x)));
                dto.setSolution(String.format("נתון: %d(%dx + %d) = %d\n\n" +
                                "נפתח את הסוגריים:\n" +
                                "%dx + %d = %d\n\n" +
                                "נחסר %d משני האגפים:\n" +
                                "%dx = %d\n\n" +
                                "נחלק ב-%d את שני האגפים:\n" +
                                "x = %d",
                        b, a, d, b * (a * x + d), b * a, b * d, b * (a * x + d), b * d, b * a, b * (a * x + d) - b * d, b * a, x));
                return dto;
            default:
                a = random.nextInt(3) + 1; // 1-3
                b = random.nextInt(10); // 0-9
                x = random.nextInt(5) + 1; // 1-5
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
        }

        dto.setAnswer(Map.of("x", String.valueOf(x)));
        dto.setSolution(String.format("נתון: %s\n\n" +
                        "נפעל לפי כללי האלגברה כדי לבודד את x:\n" +
                        "x = %d",
                dto.getQuestion(), x));

        return dto;
    }

    private QuestionDTO generateSystemQuestion() {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("system");

        // Determine difficulty (1-6)
        int difficulty = random.nextInt(6) + 1;
        dto.setLevel(difficulty); // Set the level in the DTO

        int x, y;
        int a1, b1, c1; // coefficients for first equation
        int a2, b2, c2; // coefficients for second equation

        switch (difficulty) {
            case 1: // Very simple system with easy coefficients, x and y are small positive integers
                x = random.nextInt(3) + 1; // 1-3
                y = random.nextInt(3) + 1; // 1-3

                // First equation: x + y = c1
                a1 = 1;
                b1 = 1;
                c1 = x + y;

                // Second equation: x - y = c2 or similar
                a2 = 1;
                b2 = -1;
                c2 = x - y;
                break;

            case 2: // Simple system with one coefficient > 1
                x = random.nextInt(4) + 1; // 1-4
                y = random.nextInt(4) + 1; // 1-4

                // First equation: 2x + y = c1
                a1 = 2;
                b1 = 1;
                c1 = 2 * x + y;

                // Second equation: x + y = c2
                a2 = 1;
                b2 = 1;
                c2 = x + y;
                break;

            case 3: // System with more varied coefficients
                x = random.nextInt(5) + 1; // 1-5
                y = random.nextInt(5) + 1; // 1-5

                // First equation: 3x + 2y = c1
                a1 = 3;
                b1 = 2;
                c1 = 3 * x + 2 * y;

                // Second equation: 2x - y = c2
                a2 = 2;
                b2 = -1;
                c2 = 2 * x - y;
                break;

            case 4: // System with negative coefficients and may have negative solutions
                x = random.nextInt(5) - 2; // -2 to 2
                y = random.nextInt(5) - 2; // -2 to 2

                // First equation: 2x - 3y = c1
                a1 = 2;
                b1 = -3;
                c1 = 2 * x - 3 * y;

                // Second equation: -x + 2y = c2
                a2 = -1;
                b2 = 2;
                c2 = -x + 2 * y;
                break;

            case 5: // Complex system with larger coefficients
                x = random.nextInt(3) + 1; // 1-3
                y = random.nextInt(3) + 1; // 1-3

                // First equation: 4x + 3y = c1
                a1 = 4;
                b1 = 3;
                c1 = 4 * x + 3 * y;

                // Second equation: 3x - 5y = c2
                a2 = 3;
                b2 = -5;
                c2 = 3 * x - 5 * y;
                break;

            case 6: // Most challenging with mixed coefficients and potentially fractions in solution
                x = random.nextInt(3) - 1; // -1 to 1
                y = random.nextInt(3) - 1; // -1 to 1

                // Create equations with prime coefficients to make solutions potentially fractional
                a1 = 5;
                b1 = 7;
                c1 = 5 * x + 7 * y;

                // Second equation with different primes
                a2 = 3;
                b2 = -11;
                c2 = 3 * x - 11 * y;
                break;

            default:
                x = random.nextInt(3) + 1; // 1-3
                y = random.nextInt(3) + 1; // 1-3

                // Default to simple system
                a1 = 1;
                b1 = 1;
                c1 = x + y;

                a2 = 1;
                b2 = -1;
                c2 = x - y;
        }

        // Format the equations nicely
        String equation1 = formatEquation(a1, b1, c1);
        String equation2 = formatEquation(a2, b2, c2);

        String question = String.format("%s\n%s", equation1, equation2);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", String.valueOf(x), "y", String.valueOf(y)));

        // Generate a solution explanation
        String solution = generateSystemSolution(a1, b1, c1, a2, b2, c2, x, y);
        dto.setSolution(solution);

        return dto;
    }

    // Helper method to format equation in the form ax + by = c
    private String formatEquation(int a, int b, int c) {
        StringBuilder equation = new StringBuilder();

        // Handle first term (ax)
        if (a == 1) {
            equation.append("x");
        } else if (a == -1) {
            equation.append("-x");
        } else if (a != 0) {
            equation.append(a).append("x");
        }

        // Handle second term (by)
        if (b > 0) {
            if (a != 0) equation.append(" + "); // Add plus sign if there was a first term
            if (b == 1) {
                equation.append("y");
            } else {
                equation.append(b).append("y");
            }
        } else if (b < 0) {
            if (a != 0) equation.append(" - "); // Add minus sign if there was a first term
            else equation.append("-"); // Just minus if it's the first term

            if (b == -1) {
                equation.append("y");
            } else {
                equation.append(-b).append("y"); // Use -b to make it positive after the minus sign
            }
        }

        // Add equals sign and constant
        equation.append(" = ").append(c);

        return equation.toString();
    }

    // Helper method to generate step-by-step solution for system of equations
    private String generateSystemSolution(int a1, int b1, int c1, int a2, int b2, int c2, int x, int y) {
        StringBuilder solution = new StringBuilder();

        solution.append("נתון:\n");
        solution.append(formatEquation(a1, b1, c1)).append("  (1)\n");
        solution.append(formatEquation(a2, b2, c2)).append("  (2)\n\n");

        // A common approach is elimination method:
        solution.append("נשתמש בשיטת האלימינציה:\n");

        // Decide which variable to eliminate first (usually y is a good choice)
        int lcm = Math.abs(lcm(b1, b2));
        int m1 = lcm / Math.abs(b1);
        int m2 = lcm / Math.abs(b2);

        if (b1 * b2 < 0) { // If signs are different, we'll add equations
            solution.append("נכפיל את משוואה (1) ב-").append(m1).append(":\n");
            solution.append(formatEquation(a1 * m1, b1 * m1, c1 * m1)).append("\n\n");

            solution.append("נכפיל את משוואה (2) ב-").append(m2).append(":\n");
            solution.append(formatEquation(a2 * m2, b2 * m2, c2 * m2)).append("\n\n");

            solution.append("נחבר את שתי המשוואות כדי להעלים את y:\n");

            int newA = a1 * m1 + a2 * m2;
            int newC = c1 * m1 + c2 * m2;

            solution.append(newA).append("x = ").append(newC).append("\n");
            solution.append("x = ").append(newC).append(" / ").append(newA).append(" = ").append(x).append("\n\n");
        } else { // If signs are the same, we'll subtract equations
            solution.append("נכפיל את משוואה (1) ב-").append(m1).append(":\n");
            solution.append(formatEquation(a1 * m1, b1 * m1, c1 * m1)).append("\n\n");

            solution.append("נכפיל את משוואה (2) ב-").append(m2).append(":\n");
            solution.append(formatEquation(a2 * m2, b2 * m2, c2 * m2)).append("\n\n");

            solution.append("נחסר את המשוואה השנייה מהראשונה כדי להעלים את y:\n");

            int newA = a1 * m1 - a2 * m2;
            int newC = c1 * m1 - c2 * m2;

            solution.append(newA).append("x = ").append(newC).append("\n");
            solution.append("x = ").append(newC).append(" / ").append(newA).append(" = ").append(x).append("\n\n");
        }

        // Now substitute x back to find y
        solution.append("נציב את x במשוואה (1) כדי למצוא את y:\n");
        solution.append(formatEquation(a1, b1, c1)).append("\n");
        solution.append(a1).append(" · ").append(x).append(" + ").append(b1).append("y = ").append(c1).append("\n");

        int xTerm = a1 * x;
        solution.append(xTerm).append(" + ").append(b1).append("y = ").append(c1).append("\n");
        solution.append(b1).append("y = ").append(c1).append(" - ").append(xTerm).append("\n");
        solution.append(b1).append("y = ").append(c1 - xTerm).append("\n");
        solution.append("y = ").append((c1 - xTerm)).append(" / ").append(b1).append(" = ").append(y).append("\n\n");

        solution.append("x = ").append(x).append(", y = ").append(y);

        return solution.toString();
    }

    // Helper method to calculate least common multiple (LCM)
    private int lcm(int a, int b) {
        return Math.abs(a * b) / gcd(a, b);
    }

    // Helper method to calculate greatest common divisor (GCD)
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