package com.example.reactiongame.game;

public class Prompt {
    private final int stage;
    private final String ruleText;
    private final String visibleText;
    private final int accentColor;
    private final boolean correctAnswer;
    private final String detailText;

    public Prompt(int stage, String ruleText, String visibleText, int accentColor, boolean correctAnswer, String detailText) {
        this.stage = stage;
        this.ruleText = ruleText;
        this.visibleText = visibleText;
        this.accentColor = accentColor;
        this.correctAnswer = correctAnswer;
        this.detailText = detailText;
    }

    public int getStage() {
        return stage;
    }

    public String getRuleText() {
        return ruleText;
    }

    public String getVisibleText() {
        return visibleText;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public boolean correctAnswer() {
        return correctAnswer;
    }

    public String getDetailText() {
        return detailText;
    }
}
