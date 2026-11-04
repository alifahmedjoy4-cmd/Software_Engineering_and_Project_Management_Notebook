
package org.example.servlet;

import org.example.database.DatabaseConnection;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Calculation History</title>");

        out.println("<style>");
        out.println("body {");
        out.println("font-family: Arial;");
        out.println("background: #eeeaff;");
        out.println("padding: 30px;");
        out.println("}");

        out.println("table {");
        out.println("background: white;");
        out.println("border-collapse: collapse;");
        out.println("width: 100%;");
        out.println("}");

        out.println("th, td {");
        out.println("padding: 12px;");
        out.println("border: 1px solid #ddd;");
        out.println("text-align: center;");
        out.println("}");

        out.println("th {");
        out.println("background: #2860e8;");
        out.println("color: white;");
        out.println("}");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        out.println("<h1>Calculation History</h1>");

        out.println("<table>");

        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Expression</th>");
        out.println("<th>Result</th>");
        out.println("<th>Date</th>");
        out.println("</tr>");

        String sql =
                "SELECT * FROM calculation_history ORDER BY id DESC";

        try (
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>"
                        + rs.getInt("id")
                        + "</td>");

                out.println("<td>"
                        + escape(rs.getString("expression"))
                        + "</td>");

                out.println("<td>"
                        + rs.getDouble("result")
                        + "</td>");

                out.println("<td>"
                        + escape(rs.getString("created_at"))
                        + "</td>");

                out.println("</tr>");
            }

        } catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<tr><td colspan='4'>Database connection failed: "
                            + escape(e.getMessage())
                            + "</td></tr>"
            );
        }

        out.println("</table>");

        out.println("<br>");

        out.println("<a href='index.html'>Back to Calculator</a>");

        out.println("</body>");
        out.println("</html>");
    }
}
