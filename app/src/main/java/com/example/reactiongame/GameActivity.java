package com.example.reactiongame;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.reactiongame.game.ChallengeConfig;
import com.example.reactiongame.game.GameMode;
import com.example.reactiongame.game.MatchResult;
import com.example.reactiongame.data.ScoreRepository;
import com.example.reactiongame.utilities.ExtraKeys;

import java.util.Locale;

public class GameActivity extends AppCompatActivity {
    private TextView stateText;
    private TextView ruleText;
    private TextView stimulusText;
    private TextView helperText;
    private TextView statsText;
    private TextView countdownText;
    private Button reactButton;

    private MatchViewModel viewModel;
    private CountDownTimer countDownTimer;
    private long roundStartMillis;
    private boolean waitingTransition;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ToneGenerator toneGenerator;
    private ScoreRepository scoreRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80);
        scoreRepository = new ScoreRepository(this);
        viewModel = new ViewModelProvider(this).get(MatchViewModel.class);

        bindViews();
        subscribeUi();
        startFromIntent();
    }

    private void bindViews() {
        stateText = findViewById(R.id.stateText);
        ruleText = findViewById(R.id.ruleText);
        stimulusText = findViewById(R.id.stimulusText);
        helperText = findViewById(R.id.helperText);
        statsText = findViewById(R.id.statsText);
        countdownText = findViewById(R.id.countdownText);
        reactButton = findViewById(R.id.reactButton);

        reactButton.setOnClickListener(v -> onTapPressed());
    }

    private void subscribeUi() {
        viewModel.getUi().observe(this, state -> {
            if (state == null) {
                return;
            }
            stateText.setText(state.getHeaderText());
            ruleText.setText(state.getRuleText());
            stimulusText.setText(state.getStimulusText());
            stimulusText.setTextColor(state.getStimulusColor());
            helperText.setText(state.getHelperText());
            statsText.setText(state.getStatsText() + "\n\n" + scoreRepository.getSinglePlayerSummary(viewModel.getPlayerName()));
            reactButton.setEnabled(state.isReactEnabled());

            if (state.isPlayPositiveBeep()) {
                toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 120);
            }
            if (state.isPlayNegativeBeep()) {
                toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 180);
            }

            if (state.getPhase() == GameUiState.Phase.ACTIVE) {
                waitingTransition = false;
                startRoundTimer(viewModel.getCurrentRoundLimit());
            } else if (state.getPhase() == GameUiState.Phase.TRANSITION) {
                cancelTimer();
                countdownText.setText("Preparando próxima ronda...");
                if (!waitingTransition) {
                    waitingTransition = true;
                    handler.postDelayed(() -> {
                        waitingTransition = false;
                        viewModel.prepareFollowingRound();
                    }, 900L);
                }
            } else if (state.getPhase() == GameUiState.Phase.FINISHED) {
                cancelTimer();
                countdownText.setText("Juego terminado");
                handler.postDelayed(this::openSummary, 650L);
            }
        });
    }

    private void startFromIntent() {
        Intent intent = getIntent();
        ChallengeConfig config = new ChallengeConfig(
                intent.getStringExtra(ExtraKeys.PLAYER),
                GameMode.valueOf(intent.getStringExtra(ExtraKeys.MODE)),
                intent.getIntExtra(ExtraKeys.ROUNDS_PER_STAGE, 20),
                intent.getLongExtra(ExtraKeys.BASE_LIMIT_MS, 20_000L),
                intent.getBooleanExtra(ExtraKeys.INVERSE, false),
                intent.getBooleanExtra(ExtraKeys.ADAPTIVE, false)
        );
        viewModel.startSession(config);
    }

    private void onTapPressed() {
        if (!viewModel.isRunning()) {
            return;
        }
        long reaction = System.currentTimeMillis() - roundStartMillis;
        viewModel.registerTap(reaction);
    }

    private void startRoundTimer(long totalMillis) {
        cancelTimer();
        roundStartMillis = System.currentTimeMillis();
        countDownTimer = new CountDownTimer(totalMillis, 100L) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText(String.format(Locale.getDefault(), "Tiempo restante: %.1f s", millisUntilFinished / 1000f));
            }

            @Override
            public void onFinish() {
                countdownText.setText("Tiempo agotado");
                viewModel.registerTimeout();
            }
        };
        countDownTimer.start();
    }

    private void openSummary() {
        MatchResult result = viewModel.getFinalResult();
        if (result == null) {
            return;
        }
        scoreRepository.saveIfBeatsPrevious(result);
        Intent summaryIntent = new Intent(this, SummaryActivity.class);
        summaryIntent.putExtra(ExtraKeys.RESULT_WIN, result.isWin());
        summaryIntent.putExtra(ExtraKeys.RESULT_PLAYER, result.getPlayerName());
        summaryIntent.putExtra(ExtraKeys.RESULT_SCORE, result.getScore());
        summaryIntent.putExtra(ExtraKeys.RESULT_CORRECT, result.getCorrectAnswers());
        summaryIntent.putExtra(ExtraKeys.RESULT_TOTAL, result.getTotalRounds());
        summaryIntent.putExtra(ExtraKeys.RESULT_AVG, result.getAverageReactionSeconds());
        summaryIntent.putExtra(ExtraKeys.RESULT_TRAINING, result.isTraining());
        startActivity(summaryIntent);
        finish();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    protected void onDestroy() {
        cancelTimer();
        handler.removeCallbacksAndMessages(null);
        if (toneGenerator != null) {
            toneGenerator.release();
        }
        super.onDestroy();
    }
}
