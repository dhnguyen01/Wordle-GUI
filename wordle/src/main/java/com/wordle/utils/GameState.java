package com.wordle.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {

    private static final long serialVersionUID = -1858860668642467772L;

    private String currentWord;
    private int attempts;
    private List<String> gridState;
    private boolean gameOver;

    public GameState() {
        this.gridState = new ArrayList<>();
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public List<String> getGridState() {
        return gridState;
    }

    public void setGridState(List<String> gridState) {
        this.gridState = gridState;
    }
}
