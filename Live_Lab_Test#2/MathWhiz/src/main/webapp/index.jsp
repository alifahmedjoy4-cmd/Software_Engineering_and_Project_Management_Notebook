<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="util.Html" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ALIF AHMED JOY &ndash; MATH WHIZ</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="card">
    <h1>ALIF AHMED JOY &ndash; MATH WHIZ</h1>
    <p class="sub">Four questions &bull; Prime &bull; Modulus &bull; Trigonometry &bull; 10 marks</p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="alert"><%= Html.escape((String) request.getAttribute("error")) %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/game">
        <input type="hidden" name="action" value="start">
        <label for="playerName">Player Name</label>
        <input type="text" id="playerName" name="playerName" maxlength="100" placeholder="Enter your name" required autofocus>
        <button type="submit" class="btn">Start Game</button>
    </form>

    <p class="links"><a href="<%= request.getContextPath() %>/leaderboard">View Leaderboard</a></p>
</div>
</body>
</html>
