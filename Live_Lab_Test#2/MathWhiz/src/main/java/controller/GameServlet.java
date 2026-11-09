package controller;

import dao.ScoreDAO;
import model.GameScore;
import model.Question;
import model.QuestionResult;
import service.MathService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Front controller of MATH WHIZ.
 *   GET  /game         -> game page (four questions)
 *   POST /game         -> action=start (new game) or action=submit (evaluate + save)
 *   GET  /result       -> result page
 *   GET  /leaderboard  -> top 10 scores
 */
@WebServlet(urlPatterns = {"/game", "/result", "/leaderboard"})
public class GameServlet extends HttpServlet {

    private final ScoreDAO scoreDAO = new ScoreDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession();

        switch (path) {
            case "/leaderboard":
                try {
                    req.setAttribute("scores", scoreDAO.getTopTen());
                } catch (SQLException e) {
                    log("Could not load leaderboard", e);
                    req.setAttribute("dbError", "Could not load the leaderboard. Please check the database connection.");
                }
                req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
                break;

            case "/result":
                if (session.getAttribute("results") == null) {
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                    return;
                }
                req.getRequestDispatcher("/result.jsp").forward(req, resp);
                break;

            default: // /game
                if (session.getAttribute("questions") == null) {
                    resp.sendRedirect(req.getContextPath() + "/index.jsp");
                    return;
                }
                req.getRequestDispatcher("/game.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if ("start".equals(action)) {
            startGame(req, resp);
        } else if ("submit".equals(action)) {
            submitAnswers(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    // ---------- Start Game ----------
    private void startGame(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("playerName");
        name = (name == null) ? "" : name.trim();

        if (name.isEmpty() || name.length() > 100) {
            req.setAttribute("error", "Please enter your name (1 to 100 characters).");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("playerName", name);
        session.setAttribute("questions", MathService.generateQuestions());
        session.removeAttribute("results");
        session.removeAttribute("totalScore");
        session.removeAttribute("saveError");
        resp.sendRedirect(req.getContextPath() + "/game");
    }

    // ---------- Submit Answers ----------
    @SuppressWarnings("unchecked")
    private void submitAnswers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        String playerName = (String) session.getAttribute("playerName");
        if (questions == null || playerName == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        double[] answers = new double[questions.size()];
        List<String> rawAnswers = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            String raw = req.getParameter("answer" + i);
            raw = (raw == null) ? "" : raw.trim();
            rawAnswers.add(raw);
            try {
                answers[i] = parseAnswer(questions.get(i), raw);
            } catch (NumberFormatException e) {
                errors.add("Question " + (i + 1) + ": please enter a valid " + expectedFormat(questions.get(i)) + ".");
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/game.jsp").forward(req, resp);
            return;
        }

        // Evaluate using the real Java methods
        List<QuestionResult> results = MathService.evaluate(questions, answers, rawAnswers);
        int totalScore = MathService.totalScore(results);

        // Save to database
        session.removeAttribute("saveError");
        try {
            scoreDAO.save(new GameScore(playerName, totalScore, MathService.DIFFICULTY,
                    new Timestamp(System.currentTimeMillis())));
        } catch (SQLException e) {
            log("Could not save score", e);
            session.setAttribute("saveError", "Your score could not be saved to the database.");
        }

        session.setAttribute("results", results);
        session.setAttribute("totalScore", totalScore);
        session.removeAttribute("questions");   // the game is finished
        resp.sendRedirect(req.getContextPath() + "/result");
    }

    private double parseAnswer(Question q, String raw) {
        switch (q.getType()) {
            case PRIME:
                int p = Integer.parseInt(raw);
                if (p != 0 && p != 1) throw new NumberFormatException("prime answer must be 0 or 1");
                return p;
            case MOD:
                return Integer.parseInt(raw);
            default:
                double d = Double.parseDouble(raw);
                if (Double.isNaN(d) || Double.isInfinite(d)) throw new NumberFormatException("not finite");
                return d;
        }
    }

    private String expectedFormat(Question q) {
        switch (q.getType()) {
            case PRIME: return "answer (1 or 0)";
            case MOD:   return "integer";
            default:    return "decimal number";
        }
    }
}
