
        package org.example.servlet;

import org.example.calculator.ScientificCalculator;
import org.example.database.DatabaseConnection;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.math.BigDecimal;

@WebServlet("/calculator")
public class CalculatorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            String operation = request.getParameter("operation");
            String aValue = request.getParameter("a");
            String bValue = request.getParameter("b");

            if (operation == null || aValue == null) {
                throw new IllegalArgumentException("Missing input");
            }

            double a = Double.parseDouble(aValue);
            double b = 0;

            if (bValue != null && !bValue.isBlank()) {
                b = Double.parseDouble(bValue);
            }

            if (!Double.isFinite(a) || !Double.isFinite(b)) {
                throw new IllegalArgumentException("Invalid number");
            }

            // Perform calculation
            double result = ScientificCalculator.calculate(
                    operation, a, b
            );

            if (!Double.isFinite(result)) {
                throw new ArithmeticException("Invalid result");
            }

            String expression = createExpression(operation, a, b);

            // Convert result to BigDecimal
            BigDecimal decimalResult = BigDecimal.valueOf(result);

            // Save calculation in MySQL
            String sql = "INSERT INTO calculation_history "
                    + "(expression, result) VALUES (?, ?)";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, expression);

                // Integer and Decimal result support
                ps.setBigDecimal(2, decimalResult);

                ps.executeUpdate();
            }

            response.setStatus(HttpServletResponse.SC_OK);

            response.getWriter().write(
                    "{\"success\":true,\"result\":"
                            + result + "}"
            );

        } catch (NumberFormatException e) {

            sendError(response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid number");

        } catch (IllegalArgumentException | ArithmeticException e) {

            sendError(response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            sendError(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Calculation or database error");
        }
    }

    private String createExpression(String operation,
                                    double a, double b) {

        switch (operation) {

            case "add":
                return a + " + " + b;

            case "subtract":
                return a + " - " + b;

            case "multiply":
                return a + " × " + b;

            case "divide":
                return a + " ÷ " + b;

            case "power":
                return a + " ^ " + b;

            case "sqrt":
                return "√(" + a + ")";

            case "sin":
                return "sin(" + a + "°)";

            case "cos":
                return "cos(" + a + "°)";

            case "tan":
                return "tan(" + a + "°)";

            case "log":
                return "log(" + a + ")";

            case "ln":
                return "ln(" + a + ")";

            case "percent":
                return a + "%";

            default:
                return operation;
        }
    }

    private void sendError(HttpServletResponse response,
                           int status,
                           String error) throws IOException {

        response.setStatus(status);

        response.getWriter().write(
                "{\"success\":false,\"error\":\""
                        + escapeJson(error)
                        + "\"}"
        );
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "Unknown error";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
