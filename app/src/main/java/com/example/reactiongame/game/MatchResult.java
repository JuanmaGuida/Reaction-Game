package com.example.reactiongame.game;

public class MatchResult {
    private final String playerName;
    private final int score;
    private final int correctAnswers;
    private final int totalRounds;
    private final double averageReactionMs;
    private final boolean win;
    private final boolean training;

    public MatchResult(String playerName, int score, int correctAnswers, int totalRounds, double averageReactionMs, boolean win, boolean training) {
        this.playerName = playerName;
        this.score = score;
        this.correctAnswers = correctAnswers;
        this.totalRounds = totalRounds;
        this.averageReactionMs = averageReactionMs;
        this.win = win;
        this.training = training;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public double getAverageReactionMs() {
        return averageReactionMs;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isTraining() {
        return training;
    }
}
