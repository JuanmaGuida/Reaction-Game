package com.example.reactiongame;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.reactiongame.game.ChallengeConfig;
import com.example.reactiongame.game.MatchResult;
import com.example.reactiongame.game.Prompt;
import com.example.reactiongame.game.SessionManager;

import java.util.Locale;

public class MatchViewModel extends ViewModel {
    private final SessionManager manager = new SessionManager();
    private final MutableLiveData<GameUiState> ui = new MutableLiveData<>();
    private MatchResult finalResult;

    public LiveData<GameUiState> getUi() {
        return ui;
    }

    public void startSession(ChallengeConfig config) {
        manager.start(config);
        finalResult = null;
        publishCurrent("Tocá el botón si corresponde. Si no corresponde, no hagas nada.", false, false, GameUiState.Phase.ACTIVE, false);
    }

    public long getCurrentRoundLimit() {
        return manager.getCurrentTimeLimitMs();
    }

    public boolean isRunning() {
        return manager.isActive();
    }

    public MatchResult getFinalResult() {
        return finalResult;
    }

    public String getPlayerName() {
        return manager.getPlayerName();
    }

    public void registerTap(long reactionMs) {
        SessionManager.TurnResult result = manager.submitTap(reactionMs);
        consumeTurnResult(result);
    }

    public void registerTimeout() {
        SessionManager.TurnResult result = manager.submitNoTap();
        consumeTurnResult(result);
    }

    public void prepareFollowingRound() {
        if (manager.isActive()) {
            publishCurrent("Nueva ronda lista", false, false, GameUiState.Phase.ACTIVE, false);
        }
    }

    private void consumeTurnResult(SessionManager.TurnResult result) {
        if (result.isFinished()) {
            finalResult = manager.buildResult(result.isWon());
            publishFinished(result);
            return;
        }

        String helper = result.getFeedback();
        if (result.isStageAdvanced()) {
            helper += " · Pasaste de etapa";
        }
        publishCurrent(helper, result.isCorrect(), !result.isCorrect(), GameUiState.Phase.TRANSITION, true);
    }

    private void publishCurrent(String helper, boolean positiveBeep, boolean negativeBeep, GameUiState.Phase phase, boolean showDelay) {
        Prompt prompt = manager.getCurrentPrompt();
        String header = String.format(Locale.getDefault(), "Etapa %d de %d · Vidas %d", manager.getStage(), SessionManager.STAGE_COUNT, manager.getLives());
        ui.setValue(new GameUiState(
                phase,
                header,
                prompt.getRuleText(),
                prompt.getVisibleText(),
                helper + "\n" + prompt.getDetailText(),
                manager.buildCompactStats(),
                prompt.getAccentColor(),
                phase == GameUiState.Phase.ACTIVE,
                showDelay,
                false,
                positiveBeep,
                negativeBeep
        ));
    }

    private void publishFinished(SessionManager.TurnResult result) {
        String title = result.isWon() ? "Ganaste la partida" : "Perdiste la partida";
        String helper = result.getFeedback() + "\nSe abrirá el resumen final";
        ui.setValue(new GameUiState(
                GameUiState.Phase.FINISHED,
                title,
                "Fin del juego",
                result.isWon() ? "VICTORIA" : "DERROTA",
                helper,
                manager.buildCompactStats(),
                0xFFFFFFFF,
                false,
                false,
                true,
                result.isCorrect(),
                !result.isCorrect()
        ));
    }
}
