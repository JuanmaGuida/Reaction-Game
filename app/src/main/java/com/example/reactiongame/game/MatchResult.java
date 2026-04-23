package com.example.reactiongame.game;

public class MatchResult {
    private final String playerName;
    private final int score;
    private final int correctAnswers;
    private final int totalRounds;
    private final double averageReactionSeconds;
    private final boolean win;
    private final boolean training;

    public MatchResult(String playerName, int score, int correctAnswers, int totalRounds, double averageReactionSeconds, boolean win, boolean training) {
        this.playerName = playerName;
        this.score = score;
        this.correctAnswers = correctAnswers;
        this.totalRounds = totalRounds;
        this.averageReactionSeconds = averageReactionSeconds;
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

    public double getAverageReactionSeconds() {
        return averageReactionSeconds;
    }

    public boolean isWin() {
        return win;
    }

    public boolean isTraining() {
        return training;
    }
}
