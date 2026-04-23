package com.example.reactiongame.game;

import android.graphics.Color;

import java.util.Random;

public class PromptFactory {
    private static final String[] WORDS = {"CASA", "SOL", "VECTOR", "ERROR",
            "CODIGO", "NUBE", "ATENCION", "MOTOR", "TECLADO", "ROBOT"};
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
        int[] palette = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN};
        String[] names = {"ROJO", "VERDE", "AZUL", "AMARILLO", "VIOLETA", "CELESTE"};
        int index = random.nextInt(names.length);
        boolean correctAnswer;
        String rule;
        if (inverseMode) {
            rule = "Modo inverso: NO tocar si aparece ROJO";
            correctAnswer = index != 0;
        } else {
            rule = "Tocar solo si aparece un color frío (AZUL, CELESTE o VIOLETA)";
            correctAnswer = index == 2 || index == 4 || index == 5;
        }
        return new Prompt(1, rule, names[index], palette[index], correctAnswer, "Etapa 1: colores");
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
            rule = "Modo inverso: NO tocar si la palabra es ERROR";
            correctAnswer = !"ERROR".equals(word);
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
