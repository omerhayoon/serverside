package com.dev.services;

import com.dev.dto.QuestionDTO;
import com.dev.models.ProbabilityData;
import com.dev.repository.ProbabilityDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProbabilityGeneratorService {

    private final Random random = new Random();

    @Autowired
    private ProbabilityDataRepository probabilityDataRepository;

    // Lists that will be populated from the database
    private List<String> names = new ArrayList<>();
    private List<String> items = new ArrayList<>();
    private List<String> colors = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Populate lists from database
        loadDataFromDatabase();

        // If no data exists in the database, initialize with default values
        if (names.isEmpty() || items.isEmpty() || colors.isEmpty()) {
            initializeDefaultData();
        }
    }

    private void loadDataFromDatabase() {
        names = probabilityDataRepository.findByType("name")
                .stream()
                .map(ProbabilityData::getValue)
                .collect(Collectors.toList());

        items = probabilityDataRepository.findByType("item")
                .stream()
                .map(ProbabilityData::getValue)
                .collect(Collectors.toList());

        colors = probabilityDataRepository.findByType("color")
                .stream()
                .map(ProbabilityData::getValue)
                .collect(Collectors.toList());
    }

    private void initializeDefaultData() {
        // Only initialize if lists are empty
        if (names.isEmpty()) {
            List<ProbabilityData> nameData = Arrays.asList(
                    new ProbabilityData("name", "דוד"),
                    new ProbabilityData("name", "רון"),
                    new ProbabilityData("name", "אביגיל"),
                    new ProbabilityData("name", "יעל"),
                    new ProbabilityData("name", "יוסף"),
                    new ProbabilityData("name", "מיכל"),
                    new ProbabilityData("name", "דנה"),
                    new ProbabilityData("name", "נועה"),
                    new ProbabilityData("name", "אורי"),
                    new ProbabilityData("name", "איתי"),
                    new ProbabilityData("name", "גיל"),
                    new ProbabilityData("name", "רותם"),
                    new ProbabilityData("name", "שירה"),
                    new ProbabilityData("name", "יונתן"),
                    new ProbabilityData("name", "עידו")
            );
            probabilityDataRepository.saveAll(nameData);
            names = nameData.stream().map(ProbabilityData::getValue).collect(Collectors.toList());
        }

        if (items.isEmpty()) {
            List<ProbabilityData> itemData = Arrays.asList(
                    new ProbabilityData("item", "תפוחים"),
                    new ProbabilityData("item", "אגסים"),
                    new ProbabilityData("item", "תותים"),
                    new ProbabilityData("item", "ספרים"),
                    new ProbabilityData("item", "עפרונות"),
                    new ProbabilityData("item", "מחברות"),
                    new ProbabilityData("item", "כדורים"),
                    new ProbabilityData("item", "בלונים"),
                    new ProbabilityData("item", "סוכריות"),
                    new ProbabilityData("item", "שוקולדים"),
                    new ProbabilityData("item", "מטבעות"),
                    new ProbabilityData("item", "קלפים")
            );
            probabilityDataRepository.saveAll(itemData);
            items = itemData.stream().map(ProbabilityData::getValue).collect(Collectors.toList());
        }

        if (colors.isEmpty()) {
            List<ProbabilityData> colorData = Arrays.asList(
                    new ProbabilityData("color", "אדום"),
                    new ProbabilityData("color", "כחול"),
                    new ProbabilityData("color", "ירוק"),
                    new ProbabilityData("color", "צהוב"),
                    new ProbabilityData("color", "כתום"),
                    new ProbabilityData("color", "סגול"),
                    new ProbabilityData("color", "ורוד"),
                    new ProbabilityData("color", "לבן"),
                    new ProbabilityData("color", "שחור")
            );
            probabilityDataRepository.saveAll(colorData);
            colors = colorData.stream().map(ProbabilityData::getValue).collect(Collectors.toList());
        }
    }

    public QuestionDTO generateProbabilityQuestion(int level) {
        // Validate the level
        if (level < 1 || level > 6) {
            level = 1; // Default to level 1 if invalid
        }

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setType("probability-" + level); // Set the type with level
        questionDTO.setLevel(level); // Explicitly set the level in the DTO

        // Ensure data is loaded from the database
        if (names.isEmpty() || items.isEmpty() || colors.isEmpty()) {
            loadDataFromDatabase();
        }

        switch (level) {
            case 1:
                return generateLevel1Question(level);
            case 2:
                return generateLevel2Question(level);
            case 3:
                return generateLevel3Question(level);
            case 4:
                return generateLevel4Question(level);
            case 5:
                return generateLevel5Question(level);
            case 6:
                return generateLevel6Question(level);
            default:
                throw new IllegalArgumentException("רמת קושי לא חוקית: " + level);
        }
    }

    // רמה 1: הסתברות בסיסית - יחס ישיר
    private QuestionDTO generateLevel1Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level); // Explicitly set the level

        String name = getRandomElement(names);
        String item = getRandomElement(items);

        int total = random.nextInt(5) + 5; // 5-9 items (simpler for level 1)
        int available = random.nextInt(total) + 1; // 1 to total
        int children = random.nextInt(3) + 2; // 2-4 children (simpler for level 1)

        String question = String.format("ל%s יש %d %s והוא רוצה לחלק אותם באופן שווה בין %d ילדים. " +
                "מה הסיכוי שילד יקבל %s אחד?", name, available, item, children, item.substring(0, item.length() - 2));

        double probability = (double) available / children;
        if (probability > 1) probability = 1; // Cannot be more than 100%

        String formattedProbability = formatProbability(probability);
        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));
        dto.setType("probability-" + level);

        String solution = String.format(
                "נחשב את הסיכוי שילד יקבל %s אחד:\n" +
                        "מספר ה%s: %d\n" +
                        "מספר הילדים: %d\n" +
                        "הסיכוי הוא: %d / %d = %s\n" +
                        "לכן הסיכוי שילד יקבל %s אחד הוא %s",
                item.substring(0, item.length() - 2), item, available, children,
                available, children, formattedProbability,
                item.substring(0, item.length() - 2), formattedProbability
        );

        dto.setSolution(solution);
        return dto;
    }

    // רמה 2: הסתברות עם צבעים או תכונות
    private QuestionDTO generateLevel2Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level); // Explicitly set the level

        String name = getRandomElement(names);
        String item = getRandomElement(items);
        String color = getRandomElement(colors);

        int total = random.nextInt(5) + 10; // 10-14 items
        int coloredItems = random.nextInt(total - 1) + 1; // At least one colored item

        String question = String.format("ל%s יש %d %s, מתוכם %d בצבע %s. " +
                        "אם %s בוחר %s אחד באקראי, מה הסיכוי שהוא יהיה בצבע %s?",
                name, total, item, coloredItems, color, name, item.substring(0, item.length() - 2), color);

        double probability = (double) coloredItems / total;
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));
        dto.setType("probability-" + level);

        String solution = String.format(
                "נחשב את הסיכוי לבחירת %s בצבע %s:\n" +
                        "סך כל ה%s: %d\n" +
                        "מספר ה%s בצבע %s: %d\n" +
                        "הסיכוי הוא: %d / %d = %s\n" +
                        "לכן הסיכוי לבחור %s בצבע %s הוא %s",
                item.substring(0, item.length() - 2), color, item, total,
                item, color, coloredItems, coloredItems, total, formattedProbability,
                item.substring(0, item.length() - 2), color, formattedProbability
        );

        dto.setSolution(solution);
        return dto;
    }

    // רמה 3: הסתברות מותנה פשוטה
    private QuestionDTO generateLevel3Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level); // Explicitly set the level

        String name = getRandomElement(names);

        // קלפים משחק רגיל - הסתברות מותנה
        int totalCards = 52;
        int hearts = 13;
        int diamonds = 13;
        int clubs = 13;
        int spades = 13;

        // בוחרים הסתברות מותנה - למשל, מה הסיכוי לקבל קלף אדום ואחריו קלף שחור
        String question = String.format("%s מערבב חפיסת קלפים רגילה המכילה 52 קלפים " +
                "(13 לב, 13, יהלום, 13 תלתן, 13 עלה). " +
                "%s שולף קלף אחד, מניח אותו בצד, ואז שולף קלף שני. " +
                "מה הסיכוי ששני הקלפים יהיו אדומים (לב או יהלום)?", name, name);

        // הסיכוי לשלוף קלף אדום בשליפה ראשונה
        double firstRedProb = (double) (hearts + diamonds) / totalCards;

        // הסיכוי לשלוף קלף אדום בשליפה שניה, בהינתן ששלפנו קלף אדום בשליפה ראשונה
        double secondRedProb = (double) (hearts + diamonds - 1) / (totalCards - 1);

        // הסיכוי המשותף הוא מכפלת ההסתברויות
        double probability = firstRedProb * secondRedProb;
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));
        dto.setType("probability-" + level);

        String solution = String.format(
                "נחשב את ההסתברות לשלוף שני קלפים אדומים ברצף:\n\n" +
                        "סיכוי לקלף אדום ראשון:\n" +
                        "מספר קלפים אדומים: %d (13 לב + 13 יהלום)\n" +
                        "סך כל הקלפים: %d\n" +
                        "הסתברות: %d / %d = %.3f\n\n" +
                        "סיכוי לקלף אדום שני, בהינתן ששלפנו קלף אדום ראשון:\n" +
                        "מספר קלפים אדומים שנותרו: %d - 1 = %d\n" +
                        "סך כל הקלפים שנותרו: %d - 1 = %d\n" +
                        "הסתברות: %d / %d = %.3f\n\n" +
                        "הסיכוי הכולל: %.3f × %.3f = %s\n\n" +
                        "לכן הסיכוי לשלוף שני קלפים אדומים ברצף הוא %s",
                hearts + diamonds, totalCards, hearts + diamonds, totalCards, firstRedProb,
                hearts + diamonds, hearts + diamonds - 1, totalCards, totalCards - 1,
                hearts + diamonds - 1, totalCards - 1, secondRedProb,
                firstRedProb, secondRedProb, formattedProbability, formattedProbability
        );

        dto.setSolution(solution);
        return dto;
    }

    // רמה 4: הסתברות עם הגרלות או שאלות מורכבות יותר
    private QuestionDTO generateLevel4Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level); // Explicitly set the level

        String name = getRandomElement(names);

        // שאלת הגרלה
        int participants = random.nextInt(20) + 30; // 30-49 משתתפים
        int winners = random.nextInt(3) + 1; // 1-3 זוכים

        String question = String.format("בהגרלה משתתפים %d אנשים, וייבחרו באקראי %d זוכים. " +
                        "%s וחברו משתתפים בהגרלה. מה הסיכוי ששניהם ייבחרו כזוכים?",
                participants, winners, name);

        // חישוב הסיכוי ששני אנשים ספציפיים ייבחרו מתוך winners אנשים
        double probability = calculateProbabilityBothSelected(participants, winners);
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));
        dto.setType("probability-" + level);

        // עבור ההסבר, נציג את החישוב בצורה מפורטת
        String solution = String.format(
                "נחשב את ההסתברות ש-%s וחברו ייבחרו שניהם כזוכים:\n\n" +
                        "מספר משתתפים: %d\n" +
                        "מספר זוכים: %d\n\n" +
                        "ראשית, נחשב את מספר האפשרויות הכולל לבחירת %d זוכים מתוך %d משתתפים:\n" +
                        "C(%d,%d) = %d\n\n" +
                        "כעת, אנו רוצים לחשב את מספר האפשרויות כאשר %s וחברו נבחרים כזוכים.\n" +
                        "זה אומר שצריך לבחור עוד %d זוכים מבין %d המשתתפים הנותרים:\n" +
                        "C(%d,%d) = %d\n\n" +
                        "לכן, ההסתברות היא: %d / %d = %s\n\n" +
                        "הסיכוי ש-%s וחברו ייבחרו שניהם כזוכים הוא %s",
                name, participants, winners, winners, participants,
                participants, winners, binomialCoefficient(participants, winners),
                name, winners - 2, participants - 2,
                participants - 2, winners - 2, binomialCoefficient(participants - 2, winners - 2),
                binomialCoefficient(participants - 2, winners - 2), binomialCoefficient(participants, winners),
                formattedProbability, name, formattedProbability
        );

        dto.setSolution(solution);
        return dto;
    }

    // רמה 5: הסתברות מורכבת עם מספר שלבים
    private QuestionDTO generateLevel5Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level); // Explicitly set the level

        String name = getRandomElement(names);

        // שאלה מורכבת - תוחלת, או מספר ניסיונות עד הצלחה
        int successProb = random.nextInt(4) + 1; // 1/5 to 4/5 סיכוי להצלחה

        String question = String.format("%s מטיל קוביה הוגנת עד שמתקבל מספר גדול מ-4 (כלומר 5 או 6). " +
                "מה ההסתברות שיצטרך להטיל את הקובייה בדיוק 3 פעמים עד להצלחה?", name);

        // חישוב ההסתברות לכישלון בהטלה בודדת (לקבל 1-4)
        double failProb = 4.0 / 6; // 2/3
        double successSingleProb = 1 - failProb; // 1/3

        // ההסתברות לכישלון בדיוק בשתי ההטלות הראשונות, ואז הצלחה בשלישית
        double probability = Math.pow(failProb, 2) * successSingleProb;
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));
        dto.setType("probability-" + level);

        String solution = String.format(
                "נחשב את ההסתברות ל-%s להטיל את הקובייה בדיוק 3 פעמים עד להצלחה:\n\n" +
                        "הסתברות לכישלון בהטלה בודדת (לקבל 1-4): 4/6 = 2/3\n" +
                        "הסתברות להצלחה בהטלה בודדת (לקבל 5-6): 2/6 = 1/3\n\n" +
                        "כדי שיידרשו בדיוק 3 הטלות, %s צריך להיכשל בדיוק בשתי ההטלות הראשונות ולהצליח בהטלה השלישית.\n\n" +
                        "הסתברות לכישלון בהטלה ראשונה: 2/3\n" +
                        "הסתברות לכישלון בהטלה שנייה: 2/3\n" +
                        "הסתברות להצלחה בהטלה שלישית: 1/3\n\n" +
                        "נכפיל את ההסתברויות: 2/3 × 2/3 × 1/3 = %s\n\n" +
                        "לכן, ההסתברות ש-%s יצטרך להטיל את הקובייה בדיוק 3 פעמים היא %s",
                name, name, formattedProbability, name, formattedProbability
        );

        dto.setSolution(solution);
        return dto;
    }

    // רמה 6: הסתברות מתקדמת - התפלגויות, שאלות מורכבות במיוחד
    private QuestionDTO generateLevel6Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level); // Explicitly set the level

        String name = getRandomElement(names);

        // שאלה בהתפלגות בינומית
        int trials = random.nextInt(5) + 5; // 5-9 ניסיונות
        int successes = random.nextInt(trials - 1) + 1; // לפחות הצלחה אחת, פחות מסך הניסיונות
        double successProb = (random.nextInt(6) + 2) / 10.0; // הסתברות להצלחה בודדת 0.2-0.7

        String question = String.format("בבחינה יש %d שאלות אמריקאיות. לכל שאלה 4 תשובות אפשריות, ורק אחת מהן נכונה. " +
                        "%s לא למד לבחינה ולכן הוא מנחש את כל התשובות באקראי. " +
                        "מה ההסתברות שהוא יענה נכון על בדיוק %d שאלות?",
                trials, name, successes);

        double singleSuccessProb = 0.25; // 1/4 for multiple choice with 4 options
        double probability = calculateBinomialProbability(trials, successes, singleSuccessProb);
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));
        dto.setType("probability-" + level);

        String solution = String.format(
                "נחשב את ההסתברות ש-%s יענה נכון על בדיוק %d שאלות מתוך %d שאלות בבחינה:\n\n" +
                        "הסתברות לתשובה נכונה בשאלה בודדת: 1/4 = 0.25 (ניחוש מתוך 4 אפשרויות)\n" +
                        "הסתברות לתשובה שגויה בשאלה בודדת: 3/4 = 0.75\n\n" +
                        "נשתמש בנוסחת ההתפלגות הבינומית:\n" +
                        "P(X = %d) = C(%d,%d) × (0.25)^%d × (0.75)^%d\n\n" +
                        "כאשר C(%d,%d) = %d (מספר הדרכים לבחור %d שאלות מתוך %d)\n\n" +
                        "נציב: %d × (0.25)^%d × (0.75)^%d = %d × %.6f × %.6f = %s\n\n" +
                        "לכן, ההסתברות ש-%s יענה נכון על בדיוק %d שאלות היא %s",
                name, successes, trials,
                successes, trials, successes, successes, trials - successes,
                trials, successes, binomialCoefficient(trials, successes), successes, trials,
                binomialCoefficient(trials, successes), successes, trials - successes,
                binomialCoefficient(trials, successes), Math.pow(0.25, successes), Math.pow(0.75, trials - successes),
                formattedProbability, name, successes, formattedProbability
        );

        dto.setSolution(solution);
        return dto;
    }

    // Helper methods

    private String getRandomElement(List<String> list) {
        if (list.isEmpty()) {
            throw new IllegalStateException("Cannot get a random element from an empty list");
        }
        return list.get(random.nextInt(list.size()));
    }

    private String formatProbability(double probability) {
        // If probability is close to a simple fraction, return that
        if (Math.abs(probability - 0.5) < 0.001) return "1/2";
        if (Math.abs(probability - 0.25) < 0.001) return "1/4";
        if (Math.abs(probability - 0.75) < 0.001) return "3/4";
        if (Math.abs(probability - 0.333) < 0.001) return "1/3";
        if (Math.abs(probability - 0.667) < 0.001) return "2/3";
        if (Math.abs(probability - 0.2) < 0.001) return "1/5";
        if (Math.abs(probability - 0.4) < 0.001) return "2/5";
        if (Math.abs(probability - 0.6) < 0.001) return "3/5";
        if (Math.abs(probability - 0.8) < 0.001) return "4/5";

        // Try to find a simple fraction representation
        for (int denominator = 2; denominator <= 20; denominator++) {
            for (int numerator = 1; numerator < denominator; numerator++) {
                double frac = (double) numerator / denominator;
                if (Math.abs(probability - frac) < 0.001) {
                    return numerator + "/" + denominator;
                }
            }
        }

        // If no nice fraction found, return decimal with 3 digits
        return String.format("%.3f", probability);
    }

    private long binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;

        long res = 1;
        k = Math.min(k, n - k);

        for (int i = 0; i < k; i++) {
            res = res * (n - i) / (i + 1);
        }

        return res;
    }

    private double calculateProbabilityBothSelected(int totalPeople, int winners) {
        // P(both selected) = C(winners-2, n-2) / C(winners, n)
        // where C(a,b) is the binomial coefficient - the number of ways to choose a items from b items
        double numerator = binomialCoefficient(totalPeople - 2, winners - 2);
        double denominator = binomialCoefficient(totalPeople, winners);

        return numerator / denominator;
    }

    private double calculateBinomialProbability(int n, int k, double p) {
        // P(X = k) = C(n,k) * p^k * (1-p)^(n-k)
        return binomialCoefficient(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
    }
}