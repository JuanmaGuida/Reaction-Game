package com.example.reactiongame;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reactiongame.game.GameMode;
import com.example.reactiongame.utilities.ExtraKeys;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText playerInput;
    private RadioGroup modeGroup;
    private SeekBar roundsSeek;
    private SeekBar timeSeek;
    private CheckBox inverseCheck;
    private CheckBox adaptiveCheck;
    private TextView roundsValue;
    private TextView timeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        String playerFromIntent = getIntent().getStringExtra(ExtraKeys.PLAYER);
        if (playerFromIntent != null) {
            playerInput.setText(playerFromIntent);
        }
        configureSliders();
        configureButtons();
    }

    private void bindViews() {
        playerInput = findViewById(R.id.playerName);
        modeGroup = findViewById(R.id.difficultyMode);
        roundsSeek = findViewById(R.id.seekRounds);
        timeSeek = findViewById(R.id.seekTime);
        inverseCheck = findViewById(R.id.checkInverse);
        adaptiveCheck = findViewById(R.id.checkAdaptive);
        roundsValue = findViewById(R.id.textRoundsValue);
        timeValue = findViewById(R.id.textTimeValue);
    }

    private void configureSliders() {
        roundsSeek.setOnSeekBarChangeListener(new BasicSeekListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                roundsValue.setText(String.format(Locale.getDefault(), "%d rondas por etapa", progress + 5));
            }
        });
        timeSeek.setOnSeekBarChangeListener(new BasicSeekListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeValue.setText(String.format(Locale.getDefault(), "%d segundos máximos", progress + 5));
            }
        });
        roundsSeek.setProgress(15);
        updateDefaultSeconds();
        modeGroup.setOnCheckedChangeListener((group, checkedId) -> updateDefaultSeconds());
    }

    private void configureButtons() {
        Button startButton = findViewById(R.id.buttonStart);
        Button recordsButton = findViewById(R.id.buttonRecords);

        startButton.setOnClickListener(v -> launchGame());
        recordsButton.setOnClickListener(v -> startActivity(new Intent(this, RecordsActivity.class)));
    }

    private void updateDefaultSeconds() {
        int seconds;
        switch (getSelectedMode()) {
            case HARD:
                seconds = 10;
                break;
            case MEDIUM:
                seconds = 15;
                break;
            default:
                seconds = 20;
                break;
        }
        timeSeek.setProgress(seconds - 5);
    }

    private GameMode getSelectedMode() {
        int id = modeGroup.getCheckedRadioButtonId();
        if (id == R.id.trainingMode) {
            return GameMode.TRAINING;
        }
        if (id == R.id.mediumMode) {
            return GameMode.MEDIUM;
        }
        if (id == R.id.hardMode) {
            return GameMode.HARD;
        }
        return GameMode.EASY;
    }

    private void launchGame() {
        String player = playerInput.getText().toString().trim();
        if (TextUtils.isEmpty(player)) {
            playerInput.setError("Ingresá un nombre de jugador");
            return;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(ExtraKeys.PLAYER, player);
        intent.putExtra(ExtraKeys.MODE, getSelectedMode().name());
        intent.putExtra(ExtraKeys.ROUNDS_PER_STAGE, roundsSeek.getProgress() + 5);
        intent.putExtra(ExtraKeys.BASE_LIMIT_MS, Math.min((timeSeek.getProgress() + 5) * 1000L, 30_000L));
        intent.putExtra(ExtraKeys.INVERSE, inverseCheck.isChecked());
        intent.putExtra(ExtraKeys.ADAPTIVE, adaptiveCheck.isChecked());
        startActivity(intent);
    }

    private abstract static class BasicSeekListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }
}
