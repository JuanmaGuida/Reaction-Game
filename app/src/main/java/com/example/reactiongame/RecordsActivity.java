package com.example.reactiongame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.reactiongame.data.ScoreRepository;

public class RecordsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        TextView textRecords = findViewById(R.id.recordsText);
        Button home = findViewById(R.id.homeButton);
        ScoreRepository store = new ScoreRepository(this);
        textRecords.setText(store.buildRecordsReport());

        home.setOnClickListener(v -> {
            Intent setup = new Intent(this, MainActivity.class);
            setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(setup);
            finish();
        });
    }
}
