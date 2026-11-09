package service;

import model.Question;
import model.QuestionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * All mathematical logic of MATH WHIZ.
 * Every correct answer is CALCULATED here - nothing is stored manually.
 */
public class MathService {

    public static final String DIFFICULTY = "NORMAL";
    public static final double TRIG_TOLERANCE = 0.01;   // answers are rounded to 2 decimals
    private static final double INT_TOLERANCE = 1e-9;

    private static final int[] SIN_COS_ANGLES = {0, 30, 45, 60, 90, 120, 150, 180, 210, 240, 270, 300, 330, 360};
    private static final int[] TAN_ANGLES     = {0, 30, 45, 60, 120, 135, 150, 180, 210, 225, 300, 315, 330, 360};

    // ---------------- TASK 03 : Prime ----------------
    /** Returns 1 if number is prime, otherwise 0. Numbers less than 2 are not prime. */
    public static int isPrime(int number) {
        if (number < 2) return 0;
        if (number == 2) return 1;
        if (number % 2 == 0) return 0;
        for (int i = 3; (long) i * i <= number; i += 2) {
            if (number % i == 0) return 0;
        }
        return 1;
    }

    // ---------------- TASK 04 : Modulus ----------------
    /** Returns a % b. Throws IllegalArgumentException when the divisor is zero. */
    public static int calculateMod(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Divisor must not be zero.");
        }
        return a % b;
    }

    // ---------------- TASK 05 : Trigonometry ----------------
    public static double calculateSin(double degree) {
        return Math.sin(Math.toRadians(degree));
    }

    public static double calculateCos(double degree) {
        return Math.cos(Math.toRadians(degree));
    }

    /** Returns tan(degree). Returns Double.NaN where tangent is undefined (90, 270, ... degrees). */
    public static double calculateTan(double degree) {
        double cos = Math.cos(Math.toRadians(degree));
        if (Math.abs(cos) < 1e-12) {
            return Double.NaN;   // undefined
        }
        return Math.tan(Math.toRadians(degree));
    }

    // ---------------- TASK 06 : Game logic ----------------
    /** Creates four questions: one prime, one modulus and two (different) trigonometric ones. */
    public static List<Question> generateQuestions() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        List<Question> list = new ArrayList<>();

        // Q1 - prime (half of the time a real prime)
        boolean wantPrime = rnd.nextBoolean();
        int n;
        do {
            n = rnd.nextInt(2, 100);
        } while ((isPrime(n) == 1) != wantPrime);
        list.add(Question.prime(1, n));

        // Q2 - modulus (positive integers, divisor never zero)
        list.add(Question.mod(2, rnd.nextInt(10, 100), rnd.nextInt(2, 13)));

        // Q3, Q4 - trigonometry with two different functions
        String[] funcs = {"sin", "cos", "tan"};
        int f1 = rnd.nextInt(3);
        int f2 = (f1 + 1 + rnd.nextInt(2)) % 3;
        list.add(randomTrig(3, funcs[f1], rnd));
        list.add(randomTrig(4, funcs[f2], rnd));
        return list;
    }

    private static Question randomTrig(int number, String func, ThreadLocalRandom rnd) {
        int[] angles = func.equals("tan") ? TAN_ANGLES : SIN_COS_ANGLES;
        return Question.trig(number, func, angles[rnd.nextInt(angles.length)]);
    }

    /** Calculates the correct answer of a question using the Java methods above. */
    public static double expectedAnswer(Question q) {
        switch (q.getType()) {
            case PRIME: return isPrime(q.getA());
            case MOD:   return calculateMod(q.getA(), q.getB());
            default:
                switch (q.getFunc()) {
                    case "sin": return calculateSin(q.getAngle());
                    case "cos": return calculateCos(q.getAngle());
                    default:    return calculateTan(q.getAngle());
                }
        }
    }

    public static boolean isCorrect(Question q, double answer) {
        double expected = expectedAnswer(q);
        if (Double.isNaN(expected)) return false;
        double tol = (q.getType() == Question.Type.TRIG) ? TRIG_TOLERANCE : INT_TOLERANCE;
        return Math.abs(expected - answer) <= tol;
    }

    /** Evaluates all answers and returns one result per question. */
    public static List<QuestionResult> evaluate(List<Question> questions, double[] answers, List<String> rawAnswers) {
        List<QuestionResult> results = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            double expected = expectedAnswer(q);
            String shown = (q.getType() == Question.Type.TRIG)
                    ? String.format(Locale.US, "%.4f", expected)
                    : String.valueOf((int) expected);
            results.add(new QuestionResult(q.getNumber(), q.getText(), rawAnswers.get(i), shown,
                    isCorrect(q, answers[i]), q.getMarks()));
        }
        return results;
    }

    /** Scoring logic: prime 2 + mod 2 + trig 3 + trig 3 = 10 marks maximum. */
    public static int totalScore(List<QuestionResult> results) {
        int totalScore = 0;
        for (QuestionResult r : results) {
            totalScore += r.getMarksEarned();
        }
        return totalScore;
    }
}
