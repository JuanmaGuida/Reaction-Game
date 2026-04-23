package com.example.reactiongame.game;

public enum GameMode {
    TRAINING,
    EASY,
    MEDIUM,
    HARD;

    public String getLabel() {
        switch (this) {
            case TRAINING:
                return "Entrenamiento";
            case EASY:
                return "Fácil";
            case MEDIUM:
                return "Medio";
            default:
                return "Difícil";
        }
    }
}
