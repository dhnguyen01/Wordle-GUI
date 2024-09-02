package com.wordle.game;

public class GameBoard {
    public String correctWord;
    private final int maxAttempts = 6;
    private int currentAttempt = 0;
    private String[] guesses = new String[maxAttempts];
    private boolean gameOver = false;

    public GameBoard() {
        this.correctWord = WordSelector.getRandomWord();
    }

    public boolean guessWord(String guess) {
        if (currentAttempt < maxAttempts) {
            guesses[currentAttempt] = guess;
            currentAttempt++;
            return guess.equals(correctWord);
        }
        return false;
    }

    public void setGameOver() {
        gameOver = true;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String Word) {
        this.correctWord = Word;
    }

    public String[] getGuesses() {
        return guesses;
    }

    public boolean isGameOver() {
        return currentAttempt >= maxAttempts || gameOver;
    }

    public int getCurrentAttempt() {
        return currentAttempt;
    }
}
