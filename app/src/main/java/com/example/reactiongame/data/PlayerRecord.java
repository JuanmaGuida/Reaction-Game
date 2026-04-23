package com.example.reactiongame.data;

public class PlayerRecord {
    private final String playerName;
    private final int bestScore;
    private final double bestAverage;

    public PlayerRecord(String playerName, int bestScore, double bestAverage) {
        this.playerName = playerName;
        this.bestScore = bestScore;
        this.bestAverage = bestAverage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getBestScore() {
        return bestScore;
    }

    public double getBestAverage() {
        return bestAverage;
    }
}
