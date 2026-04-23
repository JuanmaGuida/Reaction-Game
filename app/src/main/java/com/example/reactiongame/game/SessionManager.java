package com.example.reactiongame.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SessionManager {
    public static final int STAGE_COUNT = 3;
    private static final long MIN_DYNAMIC_LIMIT_MS = 3_000L;

    public static class TurnResult {
        private final boolean correct;
        private final boolean stageAdvanced;
        private final boolean finished;
        private final boolean won;
        private final String feedback;

        public TurnResult(boolean correct, boolean stageAdvanced, boolean finished, boolean won, String feedback) {
            this.correct = correct;
            this.stageAdvanced = stageAdvanced;
            this.finished = finished;
            this.won = won;
            this.feedback = feedback;
        }

        public boolean isCorrect() {
            return correct;
        }

        public boolean isStageAdvanced() {
            return stageAdvanced;
        }

        public boolean isFinished() {
            return finished;
        }

        public boolean isWon() {
            return won;
        }

        public String getFeedback() {
            return feedback;
        }
    }

    private final PromptFactory promptFactory = new PromptFactory();
    private final List<Long> successfulTimes = new ArrayList<>();

    private ChallengeConfig config;
    private Prompt currentPrompt;
    private int stage;
    private int lives;
    private int score;
    private int correctAnswers;
    private int totalRounds;
    private int stageProgress;
    private int streak;
    private long currentTimeLimitMs;
    private boolean active;

    public void start(ChallengeConfig config) {
        this.config = config;
        this.stage = 1;
        this.lives = config.getMode() == GameMode.HARD ? 2 : 3;
        this.score = 0;
        this.correctAnswers = 0;
        this.totalRounds = 0;
        this.stageProgress = 0;
        this.streak = 0;
        this.currentTimeLimitMs = config.getBaseTimeLimitMs();
        this.active = true;
        successfulTimes.clear();
        this.currentPrompt = promptFactory.createPrompt(stage, config.isInverseMode());
    }

    public Prompt getCurrentPrompt() {
        return currentPrompt;
    }

    public boolean isActive() {
        return active;
    }

    public long getCurrentTimeLimitMs() {
        return currentTimeLimitMs;
    }

    public int getStage() {
        return stage;
    }

    public int getLives() {
        return lives;
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

    public int getStageProgress() {
        return stageProgress;
    }

    public int getRoundsPerStage() {
        return config.getRoundsPerStage();
    }

    public String getPlayerName() {
        return config.getPlayerName();
    }

    public ChallengeConfig getConfig() {
        return config;
    }

    public double getAverageReactionMs() {
        if (successfulTimes.isEmpty()) {
            return 0;
        }
        long sum = 0;
        for (Long time : successfulTimes) {
            sum += time;
        }
        return (double) sum / successfulTimes.size();
    }

    public TurnResult submitTap(long reactionMs) {
        return evaluate(true, reactionMs);
    }

    public TurnResult submitNoTap() {
        return evaluate(false, currentTimeLimitMs);
    }

    private TurnResult evaluate(boolean userTapped, long reactionMs) {
        totalRounds++;
        boolean correct = currentPrompt.correctAnswer() == userTapped;
        boolean stageAdvanced = false;
        boolean finished = false;
        boolean won = false;
        String feedback;

        if (correct) {
            correctAnswers++;
            stageProgress++;
            streak++;
            if (userTapped) {
                successfulTimes.add(reactionMs);
            }
            if (!config.isTraining()) {
                int reactionBonus = Math.max(15, 140 - (int) (reactionMs / 100));
                score += reactionBonus + stage * 10;
            }
            if (config.isAdaptiveMode() && streak % 3 == 0) {
                currentTimeLimitMs = Math.max(MIN_DYNAMIC_LIMIT_MS, currentTimeLimitMs - 1000L);
            }
            feedback = "Respuesta correcta";
        } else {
            lives--;
            streak = 0;
            if (!config.isTraining()) {
                score = Math.max(0, score - 25);
            }
            if (config.isAdaptiveMode()) {
                currentTimeLimitMs = Math.min(config.getBaseTimeLimitMs(), currentTimeLimitMs + 1000L);
            }
            feedback = userTapped ? "No debías tocar" : "Debías tocar";
        }

        if (lives <= 0) {
            active = false;
            return new TurnResult(correct, false, true, false, feedback);
        }

        if (stageProgress >= config.getRoundsPerStage()) {
            stageAdvanced = true;
            if (stage == STAGE_COUNT) {
                active = false;
                return new TurnResult(correct, true, true, true, feedback);
            }
            stage++;
            stageProgress = 0;
        }

        currentPrompt = promptFactory.createPrompt(stage, config.isInverseMode());
        return new TurnResult(correct, stageAdvanced, finished, won, feedback);
    }

    public MatchResult buildResult(boolean won) {
        return new MatchResult(
                config.getPlayerName(),
                score,
                correctAnswers,
                totalRounds,
                getAverageReactionMs(),
                won,
                config.isTraining()
        );
    }

    public String buildCompactStats() {
        return String.format(
                Locale.getDefault(),
                "Jugador: %s\nModo: %s\nPuntaje: %d\nAciertos: %d/%d\nEtapa: %d/%d\nProgreso: %d/%d\nTiempo actual: %.1f s",
                config.getPlayerName(),
                config.getMode().getLabel(),
                score,
                correctAnswers,
                totalRounds,
                stage,
                STAGE_COUNT,
                stageProgress,
                config.getRoundsPerStage(),
                currentTimeLimitMs / 1000f
        );
    }
}
