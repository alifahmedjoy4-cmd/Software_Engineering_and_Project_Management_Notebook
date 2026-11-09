<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.QuestionResult, util.Html" %>
<%
    List<QuestionResult> results = (List<QuestionResult>) session.getAttribute("results");
    if (results == null) { response.sendRedirect(request.getContextPath() + "/index.jsp"); return; }
    Integer total = (Integer) session.getAttribute("totalScore");
    String player = (String) session.getAttribute("playerName");
    String saveError = (String) session.getAttribute("saveError");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ALIF AHMED JOY &ndash; MATH WHIZ | Result</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="card wide">
    <h1>ALIF AHMED JOY &ndash; MATH WHIZ</h1>
    <h2>Result for <%= Html.escape(player) %></h2>
    <div class="score"><%= total %> <span>/ 10</span></div>

    <% if (saveError != null) { %><div class="alert"><%= Html.escape(saveError) %></div><% } %>

    <table>
        <tr><th>#</th><th>Question</th><th>Your answer</th><th>Correct answer</th><th>Marks</th></tr>
        <% for (QuestionResult r : results) { %>
        <tr class="<%= r.isCorrect() ? "ok" : "bad" %>">
            <td><%= r.getNumber() %></td>
            <td><%= Html.escape(r.getQuestionText()) %></td>
            <td><%= Html.escape(r.getUserAnswer()) %></td>
            <td><%= Html.escape(r.getCorrectAnswer()) %></td>
            <td><%= r.getMarksEarned() %> / <%= r.getMarksAvailable() %></td>
        </tr>
        <% } %>
    </table>

    <p class="links">
        <a class="btn small" href="<%= request.getContextPath() %>/index.jsp">Play Again</a>
        <a class="btn small alt" href="<%= request.getContextPath() %>/leaderboard">Leaderboard</a>
    </p>
</div>
</body>
</html>
