package model;

import java.io.Serializable;

/** The evaluated outcome of one question (shown on the result page). */
public class QuestionResult implements Serializable {
    private final int number;
    private final String questionText;
    private final String userAnswer;
    private final String correctAnswer;
    private final boolean correct;
    private final int marksAvailable;
    private final int marksEarned;

    public QuestionResult(int number, String questionText, String userAnswer, String correctAnswer,
                          boolean correct, int marksAvailable) {
        this.number = number; this.questionText = questionText; this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer; this.correct = correct;
        this.marksAvailable = marksAvailable; this.marksEarned = correct ? marksAvailable : 0;
    }

    public int getNumber() { return number; }
    public String getQuestionText() { return questionText; }
    public String getUserAnswer() { return userAnswer; }
    public String getCorrectAnswer() { return correctAnswer; }
    public boolean isCorrect() { return correct; }
    public int getMarksAvailable() { return marksAvailable; }
    public int getMarksEarned() { return marksEarned; }
}
