package com.example.reactiongame;

public class GameUiState {
    public enum Phase {
        WAITING,
        ACTIVE,
        TRANSITION,
        FINISHED
    }

    private final Phase phase;
    private final String headerText;
    private final String ruleText;
    private final String stimulusText;
    private final String helperText;
    private final String statsText;
    private final int stimulusColor;
    private final boolean reactEnabled;
    private final boolean showNextDelay;
    private final boolean showFinishButton;
    private final boolean playPositiveBeep;
    private final boolean playNegativeBeep;

    public GameUiState(Phase phase, String headerText, String ruleText, String stimulusText, String helperText, String statsText, int stimulusColor, boolean reactEnabled, boolean showNextDelay, boolean showFinishButton, boolean playPositiveBeep, boolean playNegativeBeep) {
        this.phase = phase;
        this.headerText = headerText;
        this.ruleText = ruleText;
        this.stimulusText = stimulusText;
        this.helperText = helperText;
        this.statsText = statsText;
        this.stimulusColor = stimulusColor;
        this.reactEnabled = reactEnabled;
        this.showNextDelay = showNextDelay;
        this.showFinishButton = showFinishButton;
        this.playPositiveBeep = playPositiveBeep;
        this.playNegativeBeep = playNegativeBeep;
    }

    public Phase getPhase() {
        return phase;
    }

    public String getHeaderText() {
        return headerText;
    }

    public String getRuleText() {
        return ruleText;
    }

    public String getStimulusText() {
        return stimulusText;
    }

    public String getHelperText() {
        return helperText;
    }

    public String getStatsText() {
        return statsText;
    }

    public int getStimulusColor() {
        return stimulusColor;
    }

    public boolean isReactEnabled() {
        return reactEnabled;
    }

    public boolean isShowNextDelay() {
        return showNextDelay;
    }

    public boolean isShowFinishButton() {
        return showFinishButton;
    }

    public boolean isPlayPositiveBeep() {
        return playPositiveBeep;
    }

    public boolean isPlayNegativeBeep() {
        return playNegativeBeep;
    }
}
