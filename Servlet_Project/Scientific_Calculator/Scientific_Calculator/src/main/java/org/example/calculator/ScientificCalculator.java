
package org.example.calculator;

public class ScientificCalculator {

    public static double calculate(
            String operation,
            double a,
            double b) {

        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            throw new IllegalArgumentException("Invalid number");
        }

        switch (operation) {

            case "add":
                return a + b;

            case "subtract":
                return a - b;

            case "multiply":
                return a * b;

            case "divide":

                if (b == 0) {
                    throw new ArithmeticException(
                            "Cannot divide by zero"
                    );
                }

                return a / b;

            case "power":
                return Math.pow(a, b);

            case "sqrt":

                if (a < 0) {
                    throw new ArithmeticException(
                            "Square root of negative number is invalid"
                    );
                }

                return Math.sqrt(a);

            case "sin":
                return Math.sin(Math.toRadians(a));

            case "cos":
                return Math.cos(Math.toRadians(a));

            case "tan":

                if (Math.abs(Math.cos(Math.toRadians(a))) < 1E-12) {
                    throw new ArithmeticException(
                            "Tangent is undefined"
                    );
                }

                return Math.tan(Math.toRadians(a));

            case "log":

                if (a <= 0) {
                    throw new ArithmeticException(
                            "Log requires a positive number"
                    );
                }

                return Math.log10(a);

            case "ln":

                if (a <= 0) {
                    throw new ArithmeticException(
                            "Ln requires a positive number"
                    );
                }

                return Math.log(a);

            case "percent":
                return a / 100.0;

            default:
                throw new IllegalArgumentException(
                        "Invalid operation: " + operation
                );
        }
    }
}
