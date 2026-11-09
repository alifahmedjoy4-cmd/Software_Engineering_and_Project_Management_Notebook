package model;

import java.io.Serializable;

/** One generated question. Only the parameters are stored; the answer is always calculated by MathService. */
public class Question implements Serializable {

    public enum Type { PRIME, MOD, TRIG }

    private final int number;     // 1..4
    private final Type type;
    private final int marks;
    private final int a;          // prime candidate or modulus dividend
    private final int b;          // modulus divisor
    private final String func;    // "sin", "cos" or "tan"
    private final double angle;   // degrees

    private Question(int number, Type type, int marks, int a, int b, String func, double angle) {
        this.number = number; this.type = type; this.marks = marks;
        this.a = a; this.b = b; this.func = func; this.angle = angle;
    }

    public static Question prime(int number, int n) { return new Question(number, Type.PRIME, 2, n, 0, null, 0); }
    public static Question mod(int number, int a, int b) { return new Question(number, Type.MOD, 2, a, b, null, 0); }
    public static Question trig(int number, String func, double degrees) { return new Question(number, Type.TRIG, 3, 0, 0, func, degrees); }

    public int getNumber() { return number; }
    public Type getType() { return type; }
    public int getMarks() { return marks; }
    public int getA() { return a; }
    public int getB() { return b; }
    public String getFunc() { return func; }
    public double getAngle() { return angle; }

    public String getAreaName() {
        switch (type) {
            case PRIME: return "Prime Number";
            case MOD:   return "Modulus Operation";
            default:    return "Trigonometry";
        }
    }

    public String getText() {
        switch (type) {
            case PRIME: return "Is " + a + " a prime number?";
            case MOD:   return "What is the remainder of " + a + " % " + b + " ?";
            default:    return "Calculate " + func + "(" + (int) angle + "\u00B0). Round to 2 decimal places.";
        }
    }

    public String getHint() {
        switch (type) {
            case PRIME: return "Choose 1 for Prime, 0 for Not Prime";
            case MOD:   return "Integer answer";
            default:    return "Angle is in degrees. Example: 0.87";
        }
    }
}
