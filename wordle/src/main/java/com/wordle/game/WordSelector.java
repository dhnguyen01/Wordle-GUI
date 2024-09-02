package com.wordle.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class WordSelector {
    private static final String WORDS_FILE = "src/main/resources/com/wordle/words.txt";

    public static String getRandomWord() {
        try {
            List<String> words = Files.readAllLines(Paths.get(WORDS_FILE));
            Random random = new Random();
            return words.get(random.nextInt(words.size())).toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
