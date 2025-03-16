package com.dev.services;

import com.dev.dto.QuestionDTO;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Random;

@Service
public class QuestionGeneratorService {

    private final Random random = new Random();

    // New method that accepts both the question type and a difficulty level.
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
                throw new IllegalArgumentException("Unknown question type: " + type);
        }
    }

    private QuestionDTO generateAdditionQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("addition");
        dto.setLevel(level);
        int num1, num2, num3;
        switch (level) {
            case 1:
                num1 = random.nextInt(5) + 1;
                num2 = random.nextInt(5) + 1;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(String.format("%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 2:
                num1 = random.nextInt(10) + 1;
                num2 = random.nextInt(10) + 1;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(String.format("%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 3:
                num1 = random.nextInt(40) + 10;
                num2 = random.nextInt(40) + 10;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(String.format("%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 4:
                num1 = random.nextInt(400) + 100;
                num2 = random.nextInt(400) + 100;
                dto.setQuestion(String.format("%d + %d = ?", num1, num2));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2)));
                dto.setSolution(String.format("%d + %d = %d", num1, num2, num1 + num2));
                break;
            case 5:
                num1 = random.nextInt(30) + 10;
                num2 = random.nextInt(30) + 10;
                num3 = random.nextInt(30) + 10;
                dto.setQuestion(String.format("%d + %d + %d = ?", num1, num2, num3));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2 + num3)));
                dto.setSolution(String.format("%d + %d + %d = %d", num1, num2, num3, num1 + num2 + num3));
                break;
            case 6:
                int num4;
                num1 = random.nextInt(90) + 10;
                num2 = random.nextInt(90) + 10;
                num3 = random.nextInt(90) + 10;
                num4 = random.nextInt(90) + 10;
                dto.setQuestion(String.format("%d + %d + %d + %d = ?", num1, num2, num3, num4));
                dto.setAnswer(Map.of("x", String.valueOf(num1 + num2 + num3 + num4)));
                dto.setSolution(String.format("%d + %d + %d + %d = %d", num1, num2, num3, num4, num1 + num2 + num3 + num4));
                break;
            default:
                throw new IllegalArgumentException("Invalid level: " + level);
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
                subtrahend = random.nextInt(minuend);
                break;
            case 2:
                minuend = random.nextInt(10) + 5;
                subtrahend = random.nextInt(5);
                break;
            case 3:
                minuend = random.nextInt(50) + 30;
                subtrahend = random.nextInt(20) + 10;
                break;
            case 4:
                minuend = random.nextInt(100) + 100;
                subtrahend = random.nextInt(90) + 10;
                break;
            case 5:
                int a = random.nextInt(20) + 30;
                int b = random.nextInt(10) + 10;
                int c = random.nextInt(5) + 1;
                dto.setQuestion(String.format("%d - %d - %d = ?", a, b, c));
                dto.setAnswer(Map.of("x", String.valueOf(a - b - c)));
                dto.setSolution(String.format("%d - %d - %d = %d", a, b, c, a - b - c));
                return dto;
            case 6:
                a = random.nextInt(20) + 40;
                b = random.nextInt(10) + 10;
                c = random.nextInt(10) + 5;
                int d = random.nextInt(5) + 1;
                dto.setQuestion(String.format("%d - %d - %d - %d = ?", a, b, c, d));
                dto.setAnswer(Map.of("x", String.valueOf(a - b - c - d)));
                dto.setSolution(String.format("%d - %d - %d - %d = %d", a, b, c, d, a - b - c - d));
                return dto;
            default:
                minuend = random.nextInt(10) + 5;
                subtrahend = random.nextInt(5);
        }
        dto.setQuestion(String.format("%d - %d = ?", minuend, subtrahend));
        dto.setAnswer(Map.of("x", String.valueOf(minuend - subtrahend)));
        dto.setSolution(String.format("%d - %d = %d", minuend, subtrahend, minuend - subtrahend));
        return dto;
    }

    private QuestionDTO generateMultiplicationQuestion(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("multiplication");
        dto.setLevel(level);
        int factor1, factor2, factor3;
        switch (level) {
            case 1:
                factor1 = random.nextInt(3) + 1;
                factor2 = random.nextInt(3) + 1;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(String.format("%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 2:
                factor1 = random.nextInt(5) + 2;
                factor2 = random.nextInt(5) + 2;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(String.format("%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 3:
                factor1 = random.nextInt(10) + 10;
                factor2 = random.nextInt(10) + 1;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(String.format("%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 4:
                factor1 = random.nextInt(40) + 10;
                factor2 = random.nextInt(40) + 10;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(String.format("%d × %d = %d", factor1, factor2, factor1 * factor2));
                break;
            case 5:
                factor1 = random.nextInt(5) + 2;
                factor2 = random.nextInt(5) + 2;
                factor3 = random.nextInt(5) + 1;
                dto.setQuestion(String.format("%d × %d × %d = ?", factor1, factor2, factor3));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2 * factor3)));
                dto.setSolution(String.format("%d × %d × %d = %d", factor1, factor2, factor3, factor1 * factor2 * factor3));
                break;
            case 6:
                int factor4;
                factor1 = random.nextInt(4) + 2;
                factor2 = random.nextInt(4) + 2;
                factor3 = random.nextInt(4) + 2;
                factor4 = random.nextInt(4) + 1;
                dto.setQuestion(String.format("%d × %d × %d × %d = ?", factor1, factor2, factor3, factor4));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2 * factor3 * factor4)));
                dto.setSolution(String.format("%d × %d × %d × %d = %d", factor1, factor2, factor3, factor4, factor1 * factor2 * factor3 * factor4));
                break;
            default:
                factor1 = random.nextInt(5) + 2;
                factor2 = random.nextInt(5) + 2;
                dto.setQuestion(String.format("%d × %d = ?", factor1, factor2));
                dto.setAnswer(Map.of("x", String.valueOf(factor1 * factor2)));
                dto.setSolution(String.format("%d × %d = %d", factor1, factor2, factor1 * factor2));
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
                divisor = random.nextInt(10) + 2;
                multiplier = random.nextInt(10) + 1;
                product = divisor * multiplier + random.nextInt(divisor);
                dto.setQuestion(String.format("%d ÷ %d = ? (שארית ?)", product, divisor));
                int quotient = product / divisor;
                int remainder = product % divisor;
                dto.setAnswer(Map.of("x", quotient + " (שארית " + remainder + ")"));
                dto.setSolution(String.format("%d ÷ %d = %d עם שארית %d", product, divisor, quotient, remainder));
                return dto;
            case 6:
                divisor = random.nextInt(5) + 2;
                multiplier = random.nextInt(5) + 2;
                product = divisor * multiplier;
                int addend = random.nextInt(10) + 1;
                dto.setQuestion(String.format("%d ÷ %d + %d = ?", product, divisor, addend));
                dto.setAnswer(Map.of("x", String.valueOf(multiplier + addend)));
                dto.setSolution(String.format("%d ÷ %d + %d = %d + %d = %d", product, divisor, addend, multiplier, addend, multiplier + addend));
                return dto;
            default:
                divisor = random.nextInt(5) + 1;
                multiplier = random.nextInt(5) + 1;
                product = divisor * multiplier;
        }
        dto.setQuestion(String.format("%d ÷ %d = ?", product, divisor));
        dto.setAnswer(Map.of("x", String.valueOf(multiplier)));
        dto.setSolution(String.format("%d ÷ %d = %d", product, divisor, multiplier));
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
                b = random.nextInt(5);
                x = random.nextInt(5) + 1;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 3:
                a = random.nextInt(3) + 1;
                b = random.nextInt(10);
                x = random.nextInt(5) + 1;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 4:
                a = random.nextInt(5) + 2;
                b = random.nextInt(10) + 5;
                x = random.nextInt(5) + 1;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 5:
                a = random.nextInt(5) + 2;
                b = random.nextInt(10) + 5;
                x = random.nextInt(5) + 10;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            case 6:
                a = random.nextInt(5) + 2;
                b = random.nextInt(10) + 5;
                x = random.nextInt(5) + 10;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
                break;
            default:
                a = random.nextInt(3) + 1;
                b = random.nextInt(10);
                x = random.nextInt(5) + 1;
                result = a * x + b;
                dto.setQuestion(String.format("%dx + %d = %d", a, b, result));
        }
        dto.setAnswer(Map.of("x", String.valueOf(x)));

        // Create a detailed step-by-step solution with proper formatting.
        // We wrap the solution in a div with RTL direction and wrap math expressions entirely in spans with LTR direction.
        StringBuilder solution = new StringBuilder();
        solution.append("<div dir='rtl'>");
        solution.append("פתרון למשוואה: ").append(dto.getQuestion()).append("<br/><br/>");

        if (dto.getQuestion().contains("x +")) {
            // Format: x + b = c
            String[] parts = dto.getQuestion().split(" [+=] ");
            int c = Integer.parseInt(parts[2]);
            int bValue = Integer.parseInt(parts[1]);
            solution.append("נעביר את ").append(bValue).append(" לצד ימין:<br/>");
            solution.append("<span dir='ltr'>x = ").append(c).append(" - ").append(bValue).append("</span><br/>");
            solution.append("<span dir='ltr'>x = ").append(x).append("</span><br/>");
        } else if (dto.getQuestion().contains("x -")) {
            // Format: x - b = c
            String[] parts = dto.getQuestion().split(" [-=] ");
            int c = Integer.parseInt(parts[2]);
            int bValue = Integer.parseInt(parts[1]);
            solution.append("נוסיף ").append(bValue).append(" לשני צדדי המשוואה:<br/>");
            solution.append("<span dir='ltr'>x = ").append(c).append(" + ").append(bValue).append("</span><br/>");
            solution.append("<span dir='ltr'>x = ").append(x).append("</span><br/>");
        } else {
            // Format: ax + b = c
            String[] parts = dto.getQuestion().split("[x +]=");
            int aValue = Integer.parseInt(parts[0].trim());
            int bValue = Integer.parseInt(parts[1].trim());
            int cValue = Integer.parseInt(parts[2].trim());

            solution.append("צעד 1: נעביר את ").append(bValue).append(" לצד ימין של המשוואה<br/>");
            solution.append("<span dir='ltr'>").append(aValue).append("x = ").append(cValue).append(" - ").append(bValue).append("</span><br/>");
            solution.append("<span dir='ltr'>").append(aValue).append("x = ").append(cValue - bValue).append("</span><br/><br/>");

            solution.append("צעד 2: נחלק את שני צדדי המשוואה ב-").append(aValue).append("<br/>");
            solution.append("<span dir='ltr'>x = ").append(cValue - bValue).append(" ÷ ").append(aValue).append("</span><br/>");
            solution.append("<span dir='ltr'>x = ").append(x).append("</span><br/>");
        }

        solution.append("<br/>תשובה: <span dir='ltr'>x = ").append(x).append("</span>");
        solution.append("</div>");
        dto.setSolution(solution.toString());

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
                a1 = 1;
                b1 = 1;
                c1 = x + y;
                a2 = 1;
                b2 = -1;
                c2 = x - y;
                break;
            case 2:
                x = random.nextInt(4) + 1;
                y = random.nextInt(4) + 1;
                a1 = 2;
                b1 = 1;
                c1 = 2 * x + y;
                a2 = 1;
                b2 = 1;
                c2 = x + y;
                break;
            case 3:
                x = random.nextInt(5) + 1;
                y = random.nextInt(5) + 1;
                a1 = 3;
                b1 = 2;
                c1 = 3 * x + 2 * y;
                a2 = 2;
                b2 = -1;
                c2 = 2 * x - y;
                break;
            case 4:
                x = random.nextInt(5) + 1;
                y = random.nextInt(5) + 1;
                a1 = 2;
                b1 = -3;
                c1 = 2 * x - 3 * y;
                a2 = -1;
                b2 = 2;
                c2 = -x + 2 * y;
                break;
            case 5:
                x = random.nextInt(3) + 1;
                y = random.nextInt(3) + 1;
                a1 = 4;
                b1 = 3;
                c1 = 4 * x + 3 * y;
                a2 = 3;
                b2 = -5;
                c2 = 3 * x - 5 * y;
                break;
            case 6:
                x = random.nextInt(3) - 1;
                y = random.nextInt(3) - 1;
                a1 = 5;
                b1 = 7;
                c1 = 5 * x + 7 * y;
                a2 = 3;
                b2 = -11;
                c2 = 3 * x - 11 * y;
                break;
            default:
                x = random.nextInt(3) + 1;
                y = random.nextInt(3) + 1;
                a1 = 1;
                b1 = 1;
                c1 = x + y;
                a2 = 1;
                b2 = -1;
                c2 = x - y;
        }

        // Format the equations with proper LTR direction.
        String eq1Html = "<span dir='ltr'>" + formatEquation(a1, b1, c1).trim() + "</span>";
        String eq2Html = "<span dir='ltr'>" + formatEquation(a2, b2, c2).trim() + "</span>";
        dto.setQuestion(eq1Html + "<br/>" + eq2Html);

        // Set the answer.
        dto.setAnswer(Map.of("x", String.valueOf(x), "y", String.valueOf(y)));

        // Generate and wrap the solution in RTL container.
        String solution = generateSystemSolution(a1, b1, c1, a2, b2, c2, x, y);
        // If generateSystemSolution doesn't already wrap its content in a RTL container,
        // we enforce it here.
        if (!solution.contains("dir='rtl'")) {
            solution = "<div dir='rtl'>" + solution + "</div>";
        }
        dto.setSolution(solution);

        return dto;
    }


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
            if (b == 1) {
                equation.append("y");
            } else {
                equation.append(b).append("y");
            }
        } else if (b < 0) {
            if (a != 0) equation.append(" - ");
            else equation.append("-");
            if (b == -1) {
                equation.append("y");
            } else {
                equation.append(-b).append("y");
            }
        }
        equation.append(" = ").append(c);
        return equation.toString();
    }

    private String generateSystemSolution(int a1, int b1, int c1, int a2, int b2, int c2, int x, int y) {
        StringBuilder sol = new StringBuilder();
        sol.append("נתונים:\n");
        sol.append("(1) ").append(formatEquation(a1, b1, c1)).append("\n");
        sol.append("(2) ").append(formatEquation(a2, b2, c2)).append("\n\n");

        sol.append("פתרון בשיטת האלימינציה:\n");
        int lcm = Math.abs(lcm(b1, b2));
        int m1 = lcm / Math.abs(b1);
        int m2 = lcm / Math.abs(b2);
        if (b1 * b2 < 0) {
            sol.append(String.format("נכפיל את (1) ב-%d ואת (2) ב-%d, ואז נחבר:\n", m1, m2));
            int newA = a1 * m1 + a2 * m2;
            int newC = c1 * m1 + c2 * m2;
            sol.append(String.format("%dx = %d\n", newA, newC));
            sol.append(String.format("מכאן, x = %d\n\n", x));
        } else {
            sol.append(String.format("נכפיל את (1) ב-%d ואת (2) ב-%d, ואז נחסר:\n", m1, m2));
            int newA = a1 * m1 - a2 * m2;
            int newC = c1 * m1 - c2 * m2;
            sol.append(String.format("%dx = %d\n", newA, newC));
            sol.append(String.format("מכאן, x = %d\n\n", x));
        }

        sol.append("נציב את x במשוואה (1) לחישוב y:\n");
        sol.append(formatEquation(a1, b1, c1)).append("\n");
        sol.append(String.format("נציב x = %d:\n", x));
        int term = a1 * x;
        sol.append(String.format("   %d × %d + %dy = %d\n", a1, x, b1, c1));
        sol.append(String.format("   %d + %dy = %d\n", term, b1, c1));
        sol.append(String.format("   %dy = %d - %d = %d\n", b1, c1, term, c1 - term));
        sol.append(String.format("   y = %d ÷ %d = %d\n\n", c1 - term, b1, y));

        sol.append(String.format("מסקנה: x = %d, y = %d", x, y));

        // Replace regular line breaks with HTML line breaks for display
        return sol.toString();
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
