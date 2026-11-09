<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Question, util.Html" %>
<%
    List<Question> questions = (List<Question>) session.getAttribute("questions");
    if (questions == null) { response.sendRedirect(request.getContextPath() + "/index.jsp"); return; }
    List<String> errors = (List<String>) request.getAttribute("errors");
    String player = (String) session.getAttribute("playerName");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>ALIF AHMED JOY &ndash; MATH WHIZ | Game</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
<div class="card wide">
    <h1>ALIF AHMED JOY &ndash; MATH WHIZ</h1>
    <p class="sub">Good luck, <strong><%= Html.escape(player) %></strong>! Difficulty: NORMAL</p>

    <% if (errors != null) { %>
        <div class="alert">
            <% for (String e : errors) { %><div><%= Html.escape(e) %></div><% } %>
        </div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/game">
        <input type="hidden" name="action" value="submit">

        <% for (int i = 0; i < questions.size(); i++) {
               Question q = questions.get(i);
               String prev = request.getParameter("answer" + i);
               if (prev == null) prev = "";
        %>
        <div class="question">
            <div class="qhead">
                <span class="qno">Q<%= q.getNumber() %></span>
                <span class="area"><%= q.getAreaName() %></span>
                <span class="marks"><%= q.getMarks() %> marks</span>
            </div>
            <p class="qtext"><%= Html.escape(q.getText()) %></p>

            <% if (q.getType() == Question.Type.PRIME) { %>
                <select name="answer<%= i %>" required>
                    <option value="" <%= prev.isEmpty() ? "selected" : "" %>>-- choose --</option>
                    <option value="1" <%= prev.equals("1") ? "selected" : "" %>>1 (Prime)</option>
                    <option value="0" <%= prev.equals("0") ? "selected" : "" %>>0 (Not Prime)</option>
                </select>
            <% } else if (q.getType() == Question.Type.MOD) { %>
                <input type="number" step="1" name="answer<%= i %>" value="<%= Html.escape(prev) %>" placeholder="Integer" required>
            <% } else { %>
                <input type="text" inputmode="decimal" name="answer<%= i %>" value="<%= Html.escape(prev) %>" placeholder="e.g. 0.87" required>
            <% } %>
            <small><%= q.getHint() %></small>
        </div>
        <% } %>

        <button type="submit" class="btn">Submit Answers</button>
    </form>
</div>
</body>
</html>
