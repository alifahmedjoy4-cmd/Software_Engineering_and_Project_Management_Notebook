<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.text.SimpleDateFormat, model.GameScore, util.Html" %>
<%
    List<GameScore> scores = (List<GameScore>) request.getAttribute("scores");
    String dbError = (String) request.getAttribute("dbError");
    SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy, HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ALIF AHMED JOY &ndash; MATH WHIZ | Leaderboard</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="card wide">
    <h1>ALIF AHMED JOY &ndash; MATH WHIZ</h1>
    <h2>Top 10 Leaderboard</h2>

    <% if (dbError != null) { %>
        <div class="alert"><%= Html.escape(dbError) %></div>
    <% } else if (scores == null || scores.isEmpty()) { %>
        <p class="sub">No scores yet. Be the first to play!</p>
    <% } else { %>
    <table>
        <tr><th>Rank</th><th>Player</th><th>Score</th><th>Difficulty</th><th>Date / Time</th></tr>
        <% int rank = 1; for (GameScore g : scores) { %>
        <tr>
            <td><%= rank++ %></td>
            <td><%= Html.escape(g.getPlayerName()) %></td>
            <td><%= g.getScore() %> / 10</td>
            <td><%= Html.escape(g.getDifficulty()) %></td>
            <td><%= fmt.format(g.getPlayedAt()) %></td>
        </tr>
        <% } %>
    </table>
    <% } %>

    <p class="links"><a class="btn small" href="<%= request.getContextPath() %>/index.jsp">Play Game</a></p>
</div>
</body>
</html>
