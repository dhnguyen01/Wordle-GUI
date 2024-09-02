package com.wordle.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class StatsManager {

    private static final String STATS_FILE_PATH = "stats.properties";

    public void saveStats(int gamesPlayed, int gamesWon, int currentStreak, int bestStreak, 
                          int firstTry, int secondTry, int thirdTry, int fourthTry, int fifthTry, int sixthTry) throws IOException {
        Properties props = new Properties();
        props.setProperty("gamesPlayed", String.valueOf(gamesPlayed));
        props.setProperty("gamesWon", String.valueOf(gamesWon));
        props.setProperty("currentStreak", String.valueOf(currentStreak));
        props.setProperty("bestStreak", String.valueOf(bestStreak));
        props.setProperty("firstTry", String.valueOf(firstTry));
        props.setProperty("secondTry", String.valueOf(secondTry));
        props.setProperty("thirdTry", String.valueOf(thirdTry));
        props.setProperty("fourthTry", String.valueOf(fourthTry));
        props.setProperty("fifthTry", String.valueOf(fifthTry));
        props.setProperty("sixthTry", String.valueOf(sixthTry));

        props.store(Files.newOutputStream(Paths.get(STATS_FILE_PATH)), "Game Stats");
    }

    public Properties loadStats() throws IOException {
        Properties props = new Properties();
        if (Files.exists(Paths.get(STATS_FILE_PATH))) {
            props.load(Files.newInputStream(Paths.get(STATS_FILE_PATH)));
        }
        return props;
    }
}

