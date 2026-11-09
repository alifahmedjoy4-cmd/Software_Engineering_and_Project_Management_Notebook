package service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** TASK 09 - test cases from the assignment. */
class MathServiceTest {

    @Test void primeCases() {
        assertEquals(1, MathService.isPrime(7));
        assertEquals(0, MathService.isPrime(8));
        assertEquals(0, MathService.isPrime(1));
        assertEquals(0, MathService.isPrime(0));
        assertEquals(0, MathService.isPrime(-5));
        assertEquals(1, MathService.isPrime(2));
        assertEquals(1, MathService.isPrime(97));
    }

    @Test void modulusCases() {
        assertEquals(2, MathService.calculateMod(17, 5));
        assertEquals(-2, MathService.calculateMod(-17, 5));
        assertEquals(2, MathService.calculateMod(17, -5));
        assertEquals(0, MathService.calculateMod(0, 7));
    }

    @Test void divisorZeroIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> MathService.calculateMod(10, 0));
    }

    @Test void trigCases() {
        assertEquals(0.5, MathService.calculateSin(30), 1e-9);
        assertEquals(0.5, MathService.calculateCos(60), 1e-9);
        assertEquals(1.0, MathService.calculateTan(45), 1e-9);
        assertTrue(Double.isNaN(MathService.calculateTan(90)));
        assertTrue(Double.isNaN(MathService.calculateTan(270)));
    }

    @Test void generatedQuestionsAreAlwaysAnswerable() {
        for (int i = 0; i < 500; i++) {
            var qs = MathService.generateQuestions();
            assertEquals(4, qs.size());
            for (var q : qs) assertFalse(Double.isNaN(MathService.expectedAnswer(q)));
        }
    }

    @Test void allCorrectGivesTenMarks() {
        var qs = MathService.generateQuestions();
        double[] ans = new double[4];
        var raw = new java.util.ArrayList<String>();
        for (int i = 0; i < 4; i++) { ans[i] = MathService.expectedAnswer(qs.get(i)); raw.add(String.valueOf(ans[i])); }
        assertEquals(10, MathService.totalScore(MathService.evaluate(qs, ans, raw)));
    }

    @Test void wrongAnswersGiveNoMarks() {
        var qs = MathService.generateQuestions();
        double[] ans = new double[4];
        var raw = new java.util.ArrayList<String>();
        for (int i = 0; i < 4; i++) { ans[i] = MathService.expectedAnswer(qs.get(i)) + 5; raw.add("x"); }
        assertEquals(0, MathService.totalScore(MathService.evaluate(qs, ans, raw)));
    }
}
