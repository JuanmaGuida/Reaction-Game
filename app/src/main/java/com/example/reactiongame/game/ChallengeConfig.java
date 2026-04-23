package com.example.reactiongame.game;

public class ChallengeConfig {
    private final String playerName;
    private final GameMode mode;
    private final int roundsPerStage;
    private final long baseTimeLimitMs;
    private final boolean inverseMode;
    private final boolean adaptiveMode;

    public ChallengeConfig(String playerName, GameMode mode, int roundsPerStage, long baseTimeLimitMs, boolean inverseMode, boolean adaptiveMode) {
        this.playerName = playerName;
        this.mode = mode;
        this.roundsPerStage = roundsPerStage;
        this.baseTimeLimitMs = baseTimeLimitMs;
        this.inverseMode = inverseMode;
        this.adaptiveMode = adaptiveMode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public GameMode getMode() {
        return mode;
    }

    public int getRoundsPerStage() {
        return roundsPerStage;
    }

    public long getBaseTimeLimitMs() {
        return baseTimeLimitMs;
    }

    public boolean isInverseMode() {
        return inverseMode;
    }

    public boolean isAdaptiveMode() {
        return adaptiveMode;
    }

    public boolean isTraining() {
        return mode == GameMode.TRAINING;
    }
}
