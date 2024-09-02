package com.wordle.game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.wordle.utils.GameState;

public class GameManager {
    private static final String SAVE_PATH = "gameState.ser";
    public GameBoard gameBoard;

    public GameManager() {
        this.gameBoard = new GameBoard();
    }

    public boolean makeGuess(String guess) {
        return gameBoard.guessWord(guess);
    }

    public String getCorrectWord() {
        return gameBoard.getCorrectWord();
    }

    public void setCorrectWord(String word) {
        gameBoard.setCorrectWord(word);
    }

    public String[] getGuesses() {
        return gameBoard.getGuesses();
    }

    public boolean isGameOver() {
        return gameBoard.isGameOver();
    }

    public int getCurrentAttempt() {
        return gameBoard.getCurrentAttempt();
    }

    public void setGameOver() {
        gameBoard.setGameOver();
    }
    

    public GameState loadGameState() {
        File file = new File(SAVE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                GameState gameState = (GameState) ois.readObject();
                if (gameState.isGameOver()) {
                    gameState = new GameState();
                } else {
                }
                return gameState;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<String> getWordsList() {
        try {
            List<String> words = Files.readAllLines(Paths.get("src/main/resources/com/wordle/allowed_guesses.txt"));
            return words;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
