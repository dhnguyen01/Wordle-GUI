package com.wordle.ui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import com.wordle.game.GameManager;
import com.wordle.utils.GameState;
import com.wordle.utils.StatsManager;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;


@SuppressWarnings("unused")
public class GameWindowController implements GameControl {

    private StackPane rootPane;

    public void setRootPane(StackPane rootPane) {
        this.rootPane = rootPane;
    }

    @Override
    public void resetGame() {
        handleReset();
    }

    @Override
    public void closeStatsScreen() {
        removeEndScreen();
    }

    @Override
    public int getCurrentAttempt() {
        return currentRow + 1;
    }

    @FXML
    private GridPane keyboardGrid;

    @FXML
    private Button reset;

    private Stage primaryStage;

    @FXML
    private Label[][] gridLabels = new Label[6][5];

    // labels for game board
    @FXML
    private Label grid00;
    @FXML
    private Label grid01;
    @FXML
    private Label grid02;
    @FXML
    private Label grid03;
    @FXML
    private Label grid04;
    @FXML
    private Label grid10;
    @FXML
    private Label grid11;
    @FXML
    private Label grid12;
    @FXML
    private Label grid13;
    @FXML
    private Label grid14;
    @FXML
    private Label grid20;
    @FXML
    private Label grid21;
    @FXML
    private Label grid22;
    @FXML
    private Label grid23;
    @FXML
    private Label grid24;
    @FXML
    private Label grid30;
    @FXML
    private Label grid31;
    @FXML
    private Label grid32;
    @FXML
    private Label grid33;
    @FXML
    private Label grid34;
    @FXML
    private Label grid40;
    @FXML
    private Label grid41;
    @FXML
    private Label grid42;
    @FXML
    private Label grid43;
    @FXML
    private Label grid44;
    @FXML
    private Label grid50;
    @FXML
    private Label grid51;
    @FXML
    private Label grid52;
    @FXML
    private Label grid53;
    @FXML
    private Label grid54;

    @FXML
    private Label wordleLabel;

    @FXML
    private Label messageLabel;

    @FXML Button statsButton;

    @FXML
    private Label winLabel;

    @FXML
    private Button nextGameButton;

    private StatsManager statsManager = new StatsManager();

    private GameManager gameManager = new GameManager();

    private int attempts;
    private String currentWord;

    private List<String> allowedWords = new ArrayList<>();

    // curr position for next letter to be placed on game board
    private int currentRow = 0;
    private int currentCol = 0;

    // array to keep track of disabled keys on keyboard
    private boolean keyDisabled[] = new boolean[26];

    @FXML
    public void initialize() {

        allowedWords = gameManager.getWordsList();

        gridLabels[0] = new Label[]{grid00, grid01, grid02, grid03, grid04};
        gridLabels[1] = new Label[]{grid10, grid11, grid12, grid13, grid14};
        gridLabels[2] = new Label[]{grid20, grid21, grid22, grid23, grid24};
        gridLabels[3] = new Label[]{grid30, grid31, grid32, grid33, grid34};
        gridLabels[4] = new Label[]{grid40, grid41, grid42, grid43, grid44};
        gridLabels[5] = new Label[]{grid50, grid51, grid52, grid53, grid54};

        for (Label[] row : gridLabels) {
            for (Label label : row) {
                applyStylesToLabel(label);
            }
        }

        messageLabel.setText("The correct word was: " + gameManager.getCorrectWord());
        messageLabel.getStyleClass().add("franklin-font");
        messageLabel.setVisible(false);

        wordleLabel.getStyleClass().add("title-font");
        nextGameButton.getStyleClass().add("franklin-font");

        for (Node hboxNode : keyboardGrid.getChildren()) {
            if (hboxNode instanceof HBox) {
                HBox hbox = (HBox) hboxNode;
                for (Node buttonNode : hbox.getChildren()) {
                    if (buttonNode instanceof Button) {
                        Button button = (Button) buttonNode;
                        applyStylesToKeyboard(button);
                    }
                }
            }
        }

        winLabel.setVisible(false);
        reset.getStyleClass().add("default-button");
        nextGameButton.getStyleClass().add("default-button");
        statsButton.getStyleClass().add("default-button");
    }

    public void startNewGame() {
        handleNextGame();
    }

    public void initializeWithGameState(GameState gameState) {

        this.currentWord = gameState.getCurrentWord();
        List<String> gridState = gameState.getGridState();

        gameManager.setCorrectWord(currentWord);
    
        softReset();

        this.currentWord = gameState.getCurrentWord();

        System.out.println("Current word: " + currentWord);

        messageLabel.setText("The correct word is: " + currentWord);
        messageLabel.setVisible(false);
    
        for (int i = 0; i < gridState.size(); i++) {
            String row = gridState.get(i);
            simulateRowInput(row);
            if (i < gridState.size() - 1 || (i == gridState.size() - 1 && row.length() == 5)) {
                handleEnterClick();
            }
        }
    }
    
    public void saveGameState(GameState gameState, boolean isGameOver) {
        gameState.setCurrentWord(gameManager.getCorrectWord());
        gameState.setGameOver(isGameOver);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameState.ser"))) {
            oos.writeObject(gameState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void simulateRowInput(String rowData) {
        for (int j = 0; j < rowData.length(); j++) {
            char letter = rowData.charAt(j);
            processLetter(String.valueOf(letter));
        }
    }

    public GameState getCurrentGameState() {
        GameState gameState = new GameState();
        gameState.setAttempts(attempts);
    
        ArrayList<String> gridState = new ArrayList<>();
        for (Label[] row : gridLabels) {
            StringBuilder rowData = new StringBuilder();
            for (Label label : row) {
                rowData.append(label.getText());
            }
            gridState.add(rowData.toString());
        }
    
        gameState.setGridState(gridState);
        return gameState;
    }

    private void applyStylesToLabel(Label label) {
        label.setStyle("-fx-border-color: gray; " +
                       "-fx-border-width: 2; " +
                       "-fx-border-style: solid; " +
                       "-fx-font-size: 24; " +
                       "-fx-font-weight: bold; " +
                       "-fx-text-fill: black; " +
                       "-fx-alignment: center; " +
                       "-fx-min-width: 50; " +
                       "-fx-min-height: 50; " +
                       "-fx-max-width: 50; " +
                       "-fx-max-height: 50;");
        label.getStyleClass().add("default-grid");
        label.getStyleClass().add("franklin-font");
    }

    private void applyStylesToKeyboard(Button button) {
        button.setStyle("-fx-font-size: 18; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: black; " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 5; " +
                        "-fx-background-radius: 5;");
        button.getStyleClass().add("default-keyboard");
        button.getStyleClass().add("franklin-font");
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void handleLetterClick(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            processLetter(clickedButton.getText());
        }
    }

    public void processLetter(String letter) {
        if (currentCol < 5 && !gameManager.isGameOver()) {
            gridLabels[currentRow][currentCol].setText(letter);
            currentCol++;
        }
        saveGameState(getCurrentGameState(), false);
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        String keyPressed = event.getText().toUpperCase();

        if (event.getCode() == KeyCode.BACK_SPACE && currentCol > 0 && !gameManager.isGameOver()) {
            currentCol--;
            gridLabels[currentRow][currentCol].setText("");
            saveGameState(getCurrentGameState(), false);
            event.consume();
        } else if (event.getCode() == KeyCode.ENTER && currentCol == 5) {
            saveGameState(getCurrentGameState(), false);
            handleEnterClick();
            event.consume();
        } else if (keyPressed.matches("[A-Z]") && keyDisabled[keyPressed.charAt(0) - 'A']) {
            event.consume();
            return;
        } else {
            String character = event.getText().toUpperCase();
            if (character.matches("[A-Z]") && currentCol < 5) {
                gridLabels[currentRow][currentCol].setText(character);
                currentCol++;
                saveGameState(getCurrentGameState(), false);
            }
            
        }
    }

    public void handleDeleteClick() {
        if (currentCol > 0 && !gameManager.isGameOver()) {
            currentCol--;
            gridLabels[currentRow][currentCol].setText("");
            saveGameState(getCurrentGameState(), false);
        }
        
    }

    public void handleEnterClick() {
        if (currentCol == 5 && !gameManager.isGameOver()) {
            String guess = getCurrentGuess();
            if (isValidWord(guess)) {
                boolean isCorrect = gameManager.makeGuess(guess);
                processGuessResult(isCorrect);
            } else {
                messageLabel.setText("Not in word list");
                messageLabel.setVisible(true);
                fadeOutLabel(messageLabel, 1.5);
            }
            return;
        } else if (!gameManager.isGameOver()) {
            messageLabel.setText("Not enough letters");
            messageLabel.setVisible(true);
            fadeOutLabel(messageLabel, 1.5);
        }
    }
    
    private String getCurrentGuess() {
        StringBuilder guess = new StringBuilder();
        for (int col = 0; col < 5; col++) {
            guess.append(gridLabels[currentRow][col].getText());
        }
        return guess.toString();
    }

    private void processGuessResult(boolean isCorrect) {
        if (isCorrect) {
            gameManager.setGameOver();
            saveGameState(getCurrentGameState(), true);
            for (int col = 0; col < 5; col++) {
                makeBackgroundCorrect(gridLabels[currentRow][col]);
            }
            updateKeyboardStyles(getCurrentGuess(), getCurrentGuess());
            winLabel.setVisible(true);
            winLabel.setText("You Won!");
            saveStats(isCorrect);
            nextGameButton.setDisable(false);
            showEndScreen("Congratulations!", true);
        } else {
            String correctWord = gameManager.getCorrectWord();

            // count letters used in correct word
            int[] correctWordLetterCounts = new int[26];
            for (int i = 0; i < 5; i++) {
                correctWordLetterCounts[correctWord.charAt(i) - 'A']++;
            }

            // mark letters in correct position
            for (int i = 0; i < 5; i++) {
                if (correctWord.charAt(i) == gridLabels[currentRow][i].getText().charAt(0)) {
                    makeBackgroundCorrect(gridLabels[currentRow][i]);
                    correctWordLetterCounts[correctWord.charAt(i) - 'A']--;
                }
            }
            
            // mark yellow letters
            for (int i = 0; i < 5; i++) {
                if (!gridLabels[currentRow][i].getStyleClass().contains("correct")) {
                    char guessedChar = this.getCurrentGuess().charAt(i);
                    if (correctWordLetterCounts[guessedChar - 'A'] > 0) {
                        makeBackgroundPresent(gridLabels[currentRow][i]);
                        correctWordLetterCounts[guessedChar - 'A']--;
                    } else {
                        makeBackgroundIncorrect(gridLabels[currentRow][i]);
                    }
                }
            }

            updateKeyboardStyles(getCurrentGuess(), correctWord);

            if (currentRow < 5) {
                currentRow++;
                currentCol = 0;
                saveGameState(getCurrentGameState(), false);
            } else {
                gameManager.setGameOver();
                saveGameState(getCurrentGameState(), true);
                saveStats(isCorrect);
                nextGameButton.setDisable(false);
                winLabel.setVisible(true);
                winLabel.setText("Game Over!");
                messageLabel.setVisible(true);
                showEndScreen("You lost!", true);
            }
        }
    }

    private void saveStats(boolean isCorrect) {
        try {
            Properties stats = statsManager.loadStats();
            int gamesPlayed = Integer.parseInt(stats.getProperty("gamesPlayed", "0")) + 1;
            int gamesWon = Integer.parseInt(stats.getProperty("gamesWon", "0")) + (isCorrect ? 1 : 0);
            int currentStreak = isCorrect ? Integer.parseInt(stats.getProperty("currentStreak", "0")) + 1 : 0;
            int bestStreak = Math.max(currentStreak, Integer.parseInt(stats.getProperty("bestStreak", "0")));
            int firstTry = Integer.parseInt(stats.getProperty("firstTry", "0")) + (currentRow == 0 ? 1 : 0);
            int secondTry = Integer.parseInt(stats.getProperty("secondTry", "0")) + (currentRow == 1 ? 1 : 0);
            int thirdTry = Integer.parseInt(stats.getProperty("thirdTry", "0")) + (currentRow == 2 ? 1 : 0);
            int fourthTry = Integer.parseInt(stats.getProperty("fourthTry", "0")) + (currentRow == 3 ? 1 : 0);
            int fifthTry = Integer.parseInt(stats.getProperty("fifthTry", "0")) + (currentRow == 4 ? 1 : 0);
            int sixthTry = Integer.parseInt(stats.getProperty("sixthTry", "0")) + ((currentRow == 5 && isCorrect) ? 1 : 0);
    
            statsManager.saveStats(gamesPlayed, gamesWon, currentStreak, bestStreak, 
                                   firstTry, secondTry, thirdTry, fourthTry, fifthTry, sixthTry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private void makeBackgroundCorrect(Label label) {
        label.getStyleClass().remove("default-grid");
        label.getStyleClass().add("correct");
    }
    
    private void makeBackgroundPresent(Label label) {
        label.getStyleClass().remove("default-grid");
        label.getStyleClass().add("present");

    }

    private void makeBackgroundIncorrect(Label label) {
        label.getStyleClass().remove("default-grid");
        label.getStyleClass().add("incorrect");

    }

    private void updateKeyboardStyles(String guess, String correctWord) {
        for (Node hboxNode : keyboardGrid.getChildren()) {
            if (hboxNode instanceof HBox) {
                HBox hbox = (HBox) hboxNode;
                for (Node buttonNode : hbox.getChildren()) {
                    if (buttonNode instanceof Button) {
                        Button button = (Button) buttonNode;
                        String letter = button.getText().toUpperCase();
                        updateButtonStyle(button, letter, guess, correctWord);
                    }
                }
            }
        }
    }

    private void updateButtonStyle(Button button, String letter, String guess, String correctWord) {
        if (letter.length() != 1) {
            return;
        }

        if (button.getStyleClass().contains("correct-button") || button.getStyleClass().contains("incorrect")) {
            return;
        }

        button.getStyleClass().removeAll("default-keyboard", "correct-button", "present-button", "incorrect");

        if (guess.length() != correctWord.length()) return;

        boolean isCorrect = false;
        boolean isPresent = false;

        Map<Character, Integer> letterCount = new HashMap<>();
        for (char c : correctWord.toCharArray()) {
            letterCount.put(c, letterCount.getOrDefault(c, 0) + 1);
        }

        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == correctWord.charAt(i)) {
                if (letter.charAt(0) == guessChar) {
                    isCorrect = true;
                }
                if (letterCount.containsKey(guessChar)) {
                    letterCount.put(guessChar, letterCount.get(guessChar) - 1);
                }
            }
        }

        if (!isCorrect) {
            for (int i = 0; i < guess.length(); i++) {
                char guessChar = guess.charAt(i);
                if (letter.charAt(0) == guessChar && correctWord.charAt(i) != guessChar) {
                    if (letterCount.getOrDefault(guessChar, 0) > 0) {
                        isPresent = true;
                        letterCount.put(guessChar, letterCount.get(guessChar) - 1);
                    }
                }
            }
        }

        if (isCorrect) {
            button.getStyleClass().add("correct-button");
        } else if (isPresent) {
            button.getStyleClass().add("present-button");
        } else if (guess.contains(letter)) {
            button.getStyleClass().add("incorrect");
            keyDisabled[letter.charAt(0) - 'A'] = true;
            button.setDisable(true);
        } else {
            button.getStyleClass().add("default-keyboard");
        }
    }

    public void softReset() {
        currentRow = 0;
        currentCol = 0;
        attempts = 0;
        nextGameButton.setDisable(true);
        winLabel.setVisible(false);

        for (Label[] row : gridLabels) {
            for (Label label : row) {
                label.setText("");
                label.getStyleClass().remove("correct");
                label.getStyleClass().remove("present");
                label.getStyleClass().remove("incorrect");
                label.getStyleClass().remove("default-grid");
                applyStylesToLabel(label);
            }
        }

        for (Node hboxNode : keyboardGrid.getChildren()) {
            if (hboxNode instanceof HBox) {
                HBox hbox = (HBox) hboxNode;
                for (Node buttonNode : hbox.getChildren()) {
                    if (buttonNode instanceof Button) {
                        Button button = (Button) buttonNode;
                        button.getStyleClass().remove("correct-button");
                        button.getStyleClass().remove("present-button");
                        button.getStyleClass().remove("incorrect");
                        button.getStyleClass().remove("default-keyboard");
                        applyStylesToKeyboard(button);
                        button.setDisable(false);
                    }
                }
            }
        }

        keyDisabled = new boolean[26];
    }


    public void handleReset() {
        gameManager = new GameManager();
        currentRow = 0;
        currentCol = 0;
        attempts = 0;
        nextGameButton.setDisable(true);
        winLabel.setVisible(false);
        saveGameState(getCurrentGameState(), true);

        for (Label[] row : gridLabels) {
            for (Label label : row) {
                label.setText("");
                label.getStyleClass().remove("correct");
                label.getStyleClass().remove("present");
                label.getStyleClass().remove("incorrect");
                label.getStyleClass().remove("default-grid");
                applyStylesToLabel(label);
            }
        }

        messageLabel.setText("Correct word: " + gameManager.getCorrectWord());
        messageLabel.setVisible(false);

        System.out.println("Correct word: " + gameManager.getCorrectWord());

        for (Node hboxNode : keyboardGrid.getChildren()) {
            if (hboxNode instanceof HBox) {
                HBox hbox = (HBox) hboxNode;
                for (Node buttonNode : hbox.getChildren()) {
                    if (buttonNode instanceof Button) {
                        Button button = (Button) buttonNode;
                        button.getStyleClass().remove("correct-button");
                        button.getStyleClass().remove("present-button");
                        button.getStyleClass().remove("incorrect");
                        button.getStyleClass().remove("default-keyboard");
                        applyStylesToKeyboard(button);
                        button.setDisable(false);
                    }
                }
            }
        }

        keyDisabled = new boolean[26];
    }

    @FXML
    private void handleNextGame() {
        handleReset();
    }
    
    @FXML
    public void handleStatsClick() {
        showEndScreen("Your Stats", false);
    }

    public void showEndScreen(String message, boolean isEnd) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wordle/ui/Stats.fxml"));
            Parent endScreen = loader.load();
            StatsController endScreenController = loader.getController();
            endScreenController.setTitle(message);
            endScreenController.setGameControl(this);
            if (!isEnd) {
                endScreenController.disableEndLabels();
            }
    
            rootPane.getChildren().add(endScreen);
    
            rootPane.getChildren().get(0).setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeEndScreen() {
        if (rootPane.getChildren().size() > 1) {
            rootPane.getChildren().remove(1);
            rootPane.getChildren().get(0).setDisable(false);
        }
    }

    private boolean isValidWord(String word) {
        return (allowedWords.contains(word.toLowerCase()));
    }

    public void fadeOutLabel(Label label, double seconds) {
        FadeTransition fade = new FadeTransition(Duration.seconds(seconds), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(event -> label.setVisible(false));
        fade.play();
    }

}
