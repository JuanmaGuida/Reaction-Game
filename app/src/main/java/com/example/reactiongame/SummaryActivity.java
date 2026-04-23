package com.example.reactiongame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reactiongame.data.ScoreRepository;
import com.example.reactiongame.utilities.ExtraKeys;

import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        String player = intent.getStringExtra(ExtraKeys.RESULT_PLAYER);
        boolean win = intent.getBooleanExtra(ExtraKeys.RESULT_WIN, false);
        int score = intent.getIntExtra(ExtraKeys.RESULT_SCORE, 0);
        int correct = intent.getIntExtra(ExtraKeys.RESULT_CORRECT, 0);
        int total = intent.getIntExtra(ExtraKeys.RESULT_TOTAL, 0);
        double avg = intent.getDoubleExtra(ExtraKeys.RESULT_AVG, 0);
        boolean training = intent.getBooleanExtra(ExtraKeys.RESULT_TRAINING, false);

        TextView title = findViewById(R.id.summaryTitleText);
        TextView body = findViewById(R.id.summaryBodyText);
        TextView best = findViewById(R.id.savedBestText);
        ScoreRepository store = new ScoreRepository(this);

        title.setText(win ? "Resultado final: victoria" : "Resultado final: derrota");
        body.setText(String.format(
                Locale.getDefault(),
                "Jugador: %s\nPuntaje: %d%s\nAciertos: %d/%d\nPromedio de reacción: %.2f s",
                player,
                score,
                training ? " (entrenamiento)" : "",
                correct,
                total,
                avg
        ));
        best.setText(store.getSinglePlayerSummary(player));

        Button playAgain = findViewById(R.id.playAgainButton);
        Button seeRecords = findViewById(R.id.seeRecordsButton);
        Button backMenu = findViewById(R.id.backMenuButton);

        playAgain.setOnClickListener(v -> {
            Intent setup = new Intent(this, MainActivity.class);
            setup.putExtra(ExtraKeys.PLAYER, player);
            startActivity(setup);
            finish();
        });

        seeRecords.setOnClickListener(v -> startActivity(new Intent(this, RecordsActivity.class)));

        backMenu.setOnClickListener(v -> {
            Intent setup = new Intent(this, MainActivity.class);
            setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(setup);
            finish();
        });
    }
}