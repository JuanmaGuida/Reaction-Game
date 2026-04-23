package com.example.reactiongame.game;

import android.graphics.Color;

import java.util.Random;

public class PromptFactory {
    private static final String[] WORDS = {"CASA", "SOL", "VECTOR", "ERROR",
            "CODIGO", "NUBE", "ATENCION", "MOTOR", "TECLADO", "ROBOT"};
    private static final int[] COLORS = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN};
    private static final String[] COLOR_NAMES = {"ROJO", "VERDE", "AZUL", "AMARILLO", "VIOLETA", "CELESTE"};
    private final Random random = new Random();

    public Prompt createPrompt(int stage, boolean inverseMode) {
        switch (stage) {
            case 1:
                return createColorPrompt(inverseMode);
            case 2:
                return createNumberPrompt(inverseMode);
            default:
                return createWordPrompt(inverseMode);
        }
    }

    private Prompt createColorPrompt(boolean inverseMode) {
        int index = random.nextInt(COLOR_NAMES.length);
        int targetIndex = random.nextInt(COLOR_NAMES.length);
        boolean correctAnswer;
        String rule;
        if (inverseMode) {
            rule = "Modo inverso: NO tocar si aparece " + COLOR_NAMES[targetIndex];
            correctAnswer = index != targetIndex;
        } else {
            rule = "Tocar solo si aparece " + COLOR_NAMES[targetIndex];
            correctAnswer = index == targetIndex;
        }
        return new Prompt(1, rule, COLOR_NAMES[index], COLORS[index], correctAnswer, "Etapa 1: colores");
    }

    private Prompt createNumberPrompt(boolean inverseMode) {
        int value = 10 + random.nextInt(190);
        boolean correctAnswer;
        String rule;
        if (inverseMode) {
            rule = "Modo inverso: NO tocar si el número es primo";
            correctAnswer = !isPrime(value);
        } else {
            rule = "Tocar solo si el número es PAR";
            correctAnswer = value % 2 == 0;
        }
        return new Prompt(2, rule, String.valueOf(value), Color.WHITE, correctAnswer, "Etapa 2: números");
    }

    private Prompt createWordPrompt(boolean inverseMode) {
        String word = WORDS[random.nextInt(WORDS.length)];
        boolean correctAnswer;
        String rule;
        if (inverseMode) {
            String forbiddenWord = WORDS[random.nextInt(WORDS.length)];
            rule = "Modo inverso: NO tocar si la palabra es " + forbiddenWord;
            correctAnswer = !forbiddenWord.equals(word);
        } else {
            rule = "Tocar solo si la palabra tiene 5 letras o más";
            correctAnswer = word.length() >= 5;
        }
        return new Prompt(3, rule, word, Color.WHITE, correctAnswer, "Etapa 3: palabras");
    }

    private boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
