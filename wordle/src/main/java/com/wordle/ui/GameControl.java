package com.wordle.ui;

public interface GameControl {
    void resetGame();
    void closeStatsScreen();
    int getCurrentAttempt();
}
