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

    // Map for plural -> singular for items
    private Map<String, String> itemSingularMap = new HashMap<>();

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

        // For items, load the entire ProbabilityData objects so we can build the plural->singular map
        List<ProbabilityData> itemsData = probabilityDataRepository.findByType("item");
        items = itemsData.stream()
                .map(ProbabilityData::getValue)
                .collect(Collectors.toList());
        itemSingularMap = itemsData.stream()
                .collect(Collectors.toMap(
                        ProbabilityData::getValue,
                        pd -> pd.getSingularValue() != null && !pd.getSingularValue().isEmpty()
                                ? pd.getSingularValue()
                                : pd.getValue().substring(0, pd.getValue().length() - 2)
                ));

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
                    // Notice we now provide the singular form as the third argument.
                    new ProbabilityData("item", "תפוחים", "תפוח"),
                    new ProbabilityData("item", "אגסים", "אגס"),
                    new ProbabilityData("item", "תותים", "תות"),
                    new ProbabilityData("item", "ספרים", "ספר"),
                    new ProbabilityData("item", "עפרונות", "עפרון"),
                    new ProbabilityData("item", "מחברות", "מחברת"),
                    new ProbabilityData("item", "כדורים", "כדור"),
                    new ProbabilityData("item", "בלונים", "בלון"),
                    new ProbabilityData("item", "סוכריות", "סוכריה"),
                    new ProbabilityData("item", "שוקולדים", "שוקולד"),
                    new ProbabilityData("item", "מטבעות", "מטבע"),
                    new ProbabilityData("item", "קלפים", "קלף"),
                    new ProbabilityData("item", "פירות", "פרי"),
                    new ProbabilityData("item", "ירקות", "ירק"),
                    new ProbabilityData("item", "חולצות", "חולצה"),
                    new ProbabilityData("item", "מכנסיים", "מכנס")
            );
            probabilityDataRepository.saveAll(itemData);
            items = itemData.stream().map(ProbabilityData::getValue).collect(Collectors.toList());
            itemSingularMap = itemData.stream()
                    .collect(Collectors.toMap(
                            ProbabilityData::getValue,
                            pd -> pd.getSingularValue() != null && !pd.getSingularValue().isEmpty()
                                    ? pd.getSingularValue()
                                    : pd.getValue().substring(0, pd.getValue().length() - 2)
                    ));
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
        dto.setLevel(level);

        String name = getRandomElement(names);
        String item = getRandomElement(items);
        String singularItem = itemSingularMap.get(item);

        int total = random.nextInt(8) + 5; // 5-12 items (increased range)
        int givenOut = random.nextInt(total - 1) + 1; // 1 to total -1
        int children = random.nextInt(3) + 2; // 2-4 children

        //New variables for the question
        int targetChildren = random.nextInt(children - 1) + 1; // 1 to children -1

        String question;
        double probability;
        String solution;

        // Decide which question type to generate
        if (random.nextBoolean()) {
            // Probability of a child getting an item
            question = String.format("ל%s יש %d %s. הוא נתן %d %s לילדים. אם יש %d ילדים, מה הסיכוי שילד יקבל %s אחד?",
                    name, total, item, givenOut, item, children, singularItem);

            probability = (double) givenOut / children;
            probability = Math.min(probability, 1.0); // Ensure probability is not greater than 1

            String formattedProbability = formatProbability(probability);
            dto.setQuestion(question);
            dto.setAnswer(Map.of("x", formattedProbability));

            solution = String.format(
                    "נחשב את הסיכוי שילד יקבל %s אחד:\n" +
                            "מספר ה%s שחולקו: %d\n" +
                            "מספר הילדים: %d\n" +
                            "הסיכוי הוא: %.3f / %.3f = %s\n" +
                            "לכן, הסיכוי שילד יקבל %s אחד הוא %s",
                    singularItem, item, givenOut, children,
                    (double) givenOut, (double) children, formattedProbability,
                    singularItem, formattedProbability
            );
            dto.setSolution(solution);
        } else {
            // Probability of a child *not* getting an item
//            question = String.format("ל%s יש %d %s. הוא חילק %d %s בין %d ילדים. מה הסיכוי שילד לא יקבל %s?",
//                    name, total, item, givenOut, children, singularItem);
            question = String.format("ל%s יש %d %s. הוא חילק %d %s בין %d ילדים. מה הסיכוי שילד לא יקבל %s?",
                    name, total, item, givenOut, item, children, singularItem);


            int notGivenOut = total - givenOut;
            probability = (double) notGivenOut / children;
            probability = Math.min(probability, 1.0);

            String formattedProbability = formatProbability(probability);
            dto.setQuestion(question);
            dto.setAnswer(Map.of("x", formattedProbability));

            solution = String.format(
                    "נחשב את הסיכוי שילד לא יקבל %s:\n" +
                            "מספר ה%s שלא חולקו: %d\n" +
                            "מספר הילדים: %d\n" +
                            "הסיכוי הוא: %.3f / %.3f = %s\n" +
                            "לכן, הסיכוי שילד לא יקבל %s הוא %s",
                    singularItem, item, notGivenOut, children,
                    (double) notGivenOut, (double) children, formattedProbability,
                    singularItem, formattedProbability
            );
            dto.setSolution(solution);
        }
        return dto;
    }

    // רמה 2: הסתברות עם צבעים או תכונות
    private QuestionDTO generateLevel2Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level);

        String name = getRandomElement(names);
        String item = getRandomElement(items);
        String singularItem = itemSingularMap.get(item);
        String color = getRandomElement(colors);

        int total = random.nextInt(5) + 10; // 10-14 items
        int coloredItems = random.nextInt(total - 1) + 1; // at least one colored item

        String question = String.format("ל%s יש %d %s, מתוכם %d בצבע %s. " +
                        "אם %s בוחר %s אחד באקראי, מה הסיכוי שהוא יהיה בצבע %s?",
                name, total, item, coloredItems, color, name, singularItem, color);

        double probability = (double) coloredItems / total;
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));

        String solution = String.format(
                "נחשב את ההסתברות לבחירת %s בצבע %s:\n" +
                        "סך כל ה%s: %d\n" +
                        "מספר ה%s בצבע %s: %d\n" +
                        "הסיכוי הוא: %d / %d = %s\n" +
                        "לכן, ההסתברות לבחור %s בצבע %s היא %s",
                singularItem, color, item, total,
                item, color, coloredItems, coloredItems, total, formattedProbability,
                singularItem, color, formattedProbability
        );
        dto.setSolution(solution);
        return dto;
    }

    // רמה 3: הסתברות מותנה פשוטה
    // רמה 3: הסתברות מותנה פשוטה
    // רמה 3: הסתברות מותנה פשוטה
    // רמה 3: הסתברות מותנה פשוטה
    private QuestionDTO generateLevel3Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level);

        String name = getRandomElement(names);
        int questionType = random.nextInt(3); // 3 different question types

        String question;
        double probability;
        String solution;

        switch (questionType) {
            case 0: // Cards
                // קלפים במשחק רגיל – הסתברות מותנה
                int totalCards = 52;
                String[] suits = {"לב", "יהלום", "תלתן", "עלה"};
                String firstCardSuit = getRandomElement(Arrays.asList(suits));
                String secondCardSuit = getRandomElement(Arrays.asList(suits));

                question = String.format("%s מערבב חפיסת קלפים רגילה המכילה 52 קלפים " +
                                "%s שולף קלף אחד, מניח אותו בצד, ואז שולף קלף שני. " +
                                "מה הסיכוי שהקלף הראשון יהיה %s והקלף השני יהיה %s?",
                        name, name, firstCardSuit, secondCardSuit);

                // Calculate the probability based on the randomly selected suits
                double firstCardProb = 0;
                if (firstCardSuit.equals("לב") || firstCardSuit.equals("יהלום")) {
                    firstCardProb = 13.0 / totalCards;
                } else {
                    firstCardProb = 13.0 / totalCards;
                }

                double secondCardProb = 0;
                if (secondCardSuit.equals("לב") || secondCardSuit.equals("יהלום")) {
                    // Adjust the probability for the second card based on the first card
                    if (firstCardSuit.equals(secondCardSuit)) {
                        secondCardProb = 12.0 / (totalCards - 1);
                    } else {
                        secondCardProb = 13.0 / (totalCards - 1);
                    }

                } else {
                    if (firstCardSuit.equals(secondCardSuit)) {
                        secondCardProb = 12.0 / (totalCards - 1);
                    } else {
                        secondCardProb = 13.0 / (totalCards - 1);
                    }
                }

                probability = firstCardProb * secondCardProb;
                String formattedProbability = formatProbability(probability);

                dto.setQuestion(question);
                dto.setAnswer(Map.of("x", formattedProbability));

                solution = String.format(
                        "נחשב את ההסתברות לשלוף קלף %s ואחריו קלף %s:\n\n" +
                                "סיכוי לקלף %s ראשון:\n" +
                                "מספר קלפי %s: 13\n" +
                                "סך כל הקלפים: 52\n" +
                                "הסתברות: 13 / 52 = %.3f\n\n" +
                                "סיכוי לקלף %s שני, בהינתן ששלפנו קלף %s ראשון:\n" +
                                "מספר קלפי %s שנותרו: %d\n" +
                                "סך כל הקלפים שנותרו: %d\n" +
                                "הסתברות: %d / %d = %.3f\n\n" +
                                "ההסתברות הכוללת: %.3f × %.3f = %s\n\n" +
                                "לכן, ההסתברות לשלוף קלף %s ואחריו קלף %s היא %s",
                        firstCardSuit, secondCardSuit,
                        firstCardSuit, firstCardSuit, firstCardProb,
                        secondCardSuit, firstCardSuit, secondCardSuit,
                        firstCardSuit.equals(secondCardSuit) ? 12 : 13, // Corrected line
                        totalCards - 1,  // Corrected line
                        firstCardSuit.equals(secondCardSuit) ? 12 : 13,  // Corrected line
                        totalCards - 1,  // Corrected line
                        secondCardProb,
                        firstCardProb, secondCardProb, formattedProbability,
                        firstCardSuit, secondCardSuit, formattedProbability
                );

                dto.setSolution(solution);
                break;

            case 1: // Balls in a bag
                String color1 = getRandomElement(colors);
                String color2 = getRandomElement(colors);
                while (color2.equals(color1)) {
                    color2 = getRandomElement(colors); // Ensure colors are different
                }

                int numColor1 = random.nextInt(8) + 3;  // 3-10
                int numColor2 = random.nextInt(8) + 3;  // 3-10
                int totalBalls = numColor1 + numColor2;

                question = String.format(
                        "בקופסה יש %d כדורים בצבע %s ו-%d כדורים בצבע %s. %s מוציא כדור אחד באקראי, בלי להחזיר אותו, ואז מוציא כדור נוסף. מה ההסתברות ששני הכדורים יהיו בצבע %s?",
                        numColor1, color1, numColor2, color2, name, color1);

                probability = ((double) numColor1 / totalBalls) * ((double) (numColor1 - 1) / (totalBalls - 1));
                formattedProbability = formatProbability(probability);

                dto.setQuestion(question);
                dto.setAnswer(Map.of("x", formattedProbability));

                solution = String.format(
                        "ההסתברות להוציא שני כדורים בצבע %s היא:\n" +
                                "ההסתברות להוציא את הכדור הראשון בצבע %s היא %.3f.\n" +  // Changed to %.3f
                                "לאחר מכן, ישנם %.3f כדורים בצבע %s ו- %.3f כדורים בסך הכל.\n" +  // Changed to %.3f
                                "ההסתברות להוציא כדור שני בצבע %s היא %.3f.\n" +  // Changed to %.3f
                                "לכן, ההסתברות הכוללת היא %.3f * %.3f = %s",
                        color1, color1, (double) numColor1 / totalBalls,
                        (double) (numColor1 - 1), color1, (double) (totalBalls - 1),
                        color1, (double) (numColor1 - 1) / (totalBalls - 1),
                        (double) numColor1 / totalBalls, (double) (numColor1 - 1) / (totalBalls - 1), formattedProbability
                );
                dto.setSolution(solution);
                break;

            case 2: // Sequential Events
                String item1 = getRandomElement(items);
                String singularItem1 = itemSingularMap.get(item1);
                String item2 = getRandomElement(items);
                String singularItem2 = itemSingularMap.get(item2);
                while (item2.equals(item1)) {
                    item2 = getRandomElement(items); // Ensure items are different
                }

                int numItem1 = random.nextInt(5) + 3; // 3-7
                int numItem2 = random.nextInt(5) + 3; // 3-7

                question = String.format("%s קונה %d %s ו-%d %s. מה ההסתברות שהוא יבחר %s אחד ואז %s אחד?",
                        name, numItem1, item1, numItem2, item2, singularItem1, singularItem2);

                probability = ((double) numItem1 / (numItem1 + numItem2)) * ((double) numItem2 / (numItem1 + numItem2 - 1));
                formattedProbability = formatProbability(probability);
                dto.setQuestion(question);
                dto.setAnswer(Map.of("x", formattedProbability));

                solution = String.format(
                        "ההסתברות לבחור %s ואז %s היא:\n" +
                                "ההסתברות לבחור %s ראשון היא %.3f\n" +  // Changed to %.3f
                                "לאחר מכן, ההסתברות לבחור %s היא %.3f\n" +  // Changed to %.3f
                                "ההסתברות הכוללת היא %.3f * %.3f = %s",
                        singularItem1, singularItem2,
                        singularItem1, (double) numItem1 / (numItem1 + numItem2),
                        singularItem2, (double) numItem2 / (numItem1 + numItem2 - 1),
                        (double) numItem1 / (numItem1 + numItem2), (double) numItem2 / (numItem1 + numItem2 - 1),
                        formattedProbability
                );
                dto.setSolution(solution);
                break;

            default:
                throw new IllegalArgumentException("Invalid question type");
        }
        return dto;
    }

    // רמה 4: הסתברות עם הגרלות או שאלות מורכבות יותר
    private QuestionDTO generateLevel4Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level);

        String name = getRandomElement(names);

        int participants = random.nextInt(20) + 30; // 30-49 משתתפים
        int winners = random.nextInt(3) + 1; // 1-3 זוכים

        String question = String.format("בהגרלה משתתפים %d אנשים, וייבחרו באקראי %d זוכים. " +
                        "%s וחברו משתתפים בהגרלה. מה הסיכוי ששניהם ייבחרו כזוכים?",
                participants, winners, name);

        double probability = calculateProbabilityBothSelected(participants, winners);
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));

        String solution = String.format(
                "נחשב את ההסתברות ש-%s וחברו ייבחרו שניהם כזוכים:\n\n" +
                        "מספר משתתפים: %d\n" +
                        "מספר זוכים: %d\n\n" +
                        "ראשית, נחשב את מספר האפשרויות הכולל לבחירת %d זוכים מתוך %d משתתפים:\n" +
                        "C(%d,%d) = %d\n\n" +
                        "כעת, נחשב את מספר האפשרויות כאשר %s וחברו נבחרים כזוכים (נבחרים כבר 2, והנותר לבחור %d מתוך %d):\n" +
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
        dto.setLevel(level);

        String name = getRandomElement(names);

        String question = String.format("%s מטיל קוביה הוגנת עד שמתקבל מספר גדול מ-4 (כלומר 5 או 6). " +
                "מה ההסתברות שיצטרך להטיל את הקובייה בדיוק 3 פעמים עד להצלחה?", name);

        double failProb = 4.0 / 6; // probability of getting 1-4
        double successSingleProb = 1 - failProb; // probability of getting 5-6

        double probability = Math.pow(failProb, 2) * successSingleProb;
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));

        String solution = String.format(
                "נחשב את ההסתברות ש-%s יצטרך להטיל את הקובייה בדיוק 3 פעמים עד להצלחה:\n\n" +
                        "הסתברות לכישלון בהטלה בודדת (לקבל 1-4): 4/6 = 2/3\n" +
                        "הסתברות להצלחה בהטלה בודדת (לקבל 5-6): 2/6 = 1/3\n\n" +
                        "כדי שיידרשו בדיוק 3 הטלות, %s צריך להיכשל בשתי ההטלות הראשונות ולהצליח בשלישית.\n\n" +
                        "נכפיל: 2/3 × 2/3 × 1/3 = %s\n\n" +
                        "לכן, ההסתברות ש-%s יצטרך להטיל את הקובייה בדיוק 3 פעמים היא %s",
                name, name, formattedProbability, name, formattedProbability
        );
        dto.setSolution(solution);
        return dto;
    }

    // רמה 6: הסתברות מתקדמת – התפלגויות ושאלות מורכבות במיוחד
    private QuestionDTO generateLevel6Question(int level) {
        QuestionDTO dto = new QuestionDTO();
        dto.setType("probability-" + level);
        dto.setLevel(level);

        String name = getRandomElement(names);

        int trials = random.nextInt(5) + 5; // 5-9 ניסיונות
        int successes = random.nextInt(trials - 1) + 1; // לפחות 1 הצלחה
        double successProb = (random.nextInt(6) + 2) / 10.0; // בין 0.2 ל-0.7 (אינו בשימוש בחישוב הבא)

        String question = String.format("בבחינה יש %d שאלות אמריקאיות. לכל שאלה 4 תשובות אפשריות, ורק אחת נכונה. " +
                        "%s לא למד ולכן מנחש את כל התשובות. מה ההסתברות שהוא יענה נכון על בדיוק %d שאלות?",
                trials, name, successes);

        double singleSuccessProb = 0.25; // 1/4 לתשובה נכונה
        double probability = calculateBinomialProbability(trials, successes, singleSuccessProb);
        String formattedProbability = formatProbability(probability);

        dto.setQuestion(question);
        dto.setAnswer(Map.of("x", formattedProbability));

        String solution = String.format(
                "נחשב את ההסתברות ש-%s יענה נכון על בדיוק %d שאלות מתוך %d:\n\n" +
                        "הסתברות לתשובה נכונה בשאלה בודדת: 1/4 = 0.25\n" +
                        "הסתברות לתשובה שגויה בשאלה בודדת: 3/4 = 0.75\n\n" +
                        "נשתמש בנוסחת התפלגות בינומית:\n" +
                        "P(X = %d) = C(%d,%d) × (0.25)^%d × (0.75)^(%d)\n\n" +
                        "כאשר C(%d,%d) = %d\n\n" +
                        "נציב ונקבל: %d × (0.25)^%d × (0.75)^(%d) = %s\n\n" +
                        "לכן, ההסתברות ש-%s יענה נכון על בדיוק %d שאלות היא %s",
                name, successes, trials,
                successes, trials, successes, successes, trials - successes,
                trials, successes, binomialCoefficient(trials, successes),
                binomialCoefficient(trials, successes), successes, trials - successes,
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
        if (Math.abs(probability - 0.5) < 0.001) return "1/2";
        if (Math.abs(probability - 0.25) < 0.001) return "1/4";
        if (Math.abs(probability - 0.75) < 0.001) return "3/4";
        if (Math.abs(probability - 0.333) < 0.001) return "1/3";
        if (Math.abs(probability - 0.667) < 0.001) return "2/3";
        if (Math.abs(probability - 0.2) < 0.001) return "1/5";
        if (Math.abs(probability - 0.4) < 0.001) return "2/5";
        if (Math.abs(probability - 0.6) < 0.001) return "3/5";
        if (Math.abs(probability - 0.8) < 0.001) return "4/5";

        for (int denominator = 2; denominator <= 20; denominator++) {
            for (int numerator = 1; numerator < denominator; numerator++) {
                double frac = (double) numerator / denominator;
                if (Math.abs(probability - frac) < 0.001) {
                    return numerator + "/" + denominator;
                }
            }
        }
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
        double numerator = binomialCoefficient(totalPeople - 2, winners - 2);
        double denominator = binomialCoefficient(totalPeople, winners);
        return numerator / denominator;
    }

    private double calculateBinomialProbability(int n, int k, double p) {
        return binomialCoefficient(n, k) * Math.pow(p, k) * Math.pow(1 - p, n - k);
    }
}
