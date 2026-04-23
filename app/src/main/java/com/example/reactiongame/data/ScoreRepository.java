package com.example.reactiongame.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.reactiongame.game.MatchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ScoreRepository {
    private static final String PREF_NAME = "reaction_local_records";
    private static final String SCORE_PREFIX = "score_";
    private static final String AVG_PREFIX = "avg_";

    private final SharedPreferences preferences;

    public ScoreRepository(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveIfBeatsPrevious(MatchResult result) {
        if (result.isTraining()) {
            return;
        }

        String player = reformat(result.getPlayerName());
        String scoreKey = SCORE_PREFIX + player;
        String avgKey = AVG_PREFIX + player;

        int storedScore = preferences.getInt(scoreKey, -1);
        float storedAvg = preferences.getFloat(avgKey, Float.MAX_VALUE);

        boolean better = result.getScore() > storedScore ||
                (result.getScore() == storedScore && result.getAverageReactionMs() > 0 && result.getAverageReactionMs() < storedAvg);

        if (better) {
            preferences.edit()
                    .putInt(scoreKey, result.getScore())
                    .putFloat(avgKey, (float) result.getAverageReactionMs())
                    .apply();
        }
    }

    public String getSinglePlayerSummary(String playerName) {
        String player = reformat(playerName);
        int bestScore = preferences.getInt(SCORE_PREFIX + player, -1);
        float bestAvg = preferences.getFloat(AVG_PREFIX + player, Float.MAX_VALUE);
        if (bestScore < 0) {
            return "Sin récord guardado todavía";
        }
        String avgText = bestAvg == Float.MAX_VALUE ? "Sin dato" : String.format(Locale.getDefault(), "%.0f ms", bestAvg);
        return String.format(Locale.getDefault(), "Mejor puntaje guardado: %d\nMejor promedio: %s", bestScore, avgText);
    }

    public List<PlayerRecord> getAllRecordsSorted() {
        Map<String, ?> all = preferences.getAll();
        List<PlayerRecord> result = new ArrayList<>();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String key = entry.getKey();
            if (!key.startsWith(SCORE_PREFIX)) {
                continue;
            }
            String player = key.substring(SCORE_PREFIX.length());
            int score = preferences.getInt(key, 0);
            float avg = preferences.getFloat(AVG_PREFIX + player, Float.MAX_VALUE);
            result.add(new PlayerRecord(player, score, avg));
        }
        Collections.sort(result, new Comparator<PlayerRecord>() {
            @Override
            public int compare(PlayerRecord a, PlayerRecord b) {
                int scoreCompare = Integer.compare(b.getBestScore(), a.getBestScore());
                if (scoreCompare != 0) {
                    return scoreCompare;
                }
                return Double.compare(a.getBestAverage(), b.getBestAverage());
            }
        });
        return result;
    }

    public String buildRecordsReport() {
        List<PlayerRecord> records = getAllRecordsSorted();
        if (records.isEmpty()) {
            return "Todavía no hay puntajes guardados.";
        }
        StringBuilder builder = new StringBuilder();
        int position = 1;
        for (PlayerRecord record : records) {
            if (position > 1) {
                builder.append("\n\n");
            }
            builder.append(position)
                    .append(". ")
                    .append(record.getPlayerName())
                    .append("\nPuntaje: ")
                    .append(record.getBestScore())
                    .append("\nPromedio: ")
                    .append(record.getBestAverage() == Float.MAX_VALUE ? "Sin dato" : String.format(Locale.getDefault(), "%.0f ms", record.getBestAverage()));
            position++;
        }
        return builder.toString();
    }

    private String reformat(String value) {
        return value.trim().replace(" ", "_");
    }
}
