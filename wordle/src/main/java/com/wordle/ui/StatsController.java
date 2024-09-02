package com.wordle.ui;
import com.wordle.utils.StatsManager;

import java.io.IOException;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

public class StatsController {

    private GameControl gameControl;

    public void setGameControl(GameControl gameControl) {
        this.gameControl = gameControl;
    }

    @FXML
    private Label titleLabel;
    @FXML
    private Label statisticsLabel;
    @FXML
    private Label gamesPlayedLabel;
    @FXML
    private Label winPercentageLabel;
    @FXML
    private Label currentStreakLabel;
    @FXML
    private Label bestStreakLabel;

    @FXML
    private Button nextButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label playedDescription;

    @FXML
    private Label winPercentageDescription;

    @FXML
    private Label currentStreakDescription;

    @FXML
    private Label bestStreakDescription;
    
    @FXML
    private BarChart<String, Number> guessDistributionChart;

    private int gamesPlayed;
    private int gamesWon;
    private int currentStreak;
    private int bestStreak;
    private int firstTry;
    private int secondTry;
    private int thirdTry;
    private int fourthTry;
    private int fifthTry;
    private int sixthTry;

    private StatsManager statsManager = new StatsManager();

    @FXML
    public void initialize() {
        nextButton.getStyleClass().add("default-button");
        nextButton.getStyleClass().add("franklin-font");
        cancelButton.getStyleClass().add("default-button");
        cancelButton.getStyleClass().add("franklin-font");
        gamesPlayedLabel.getStyleClass().add("franklin-font");
        winPercentageLabel.getStyleClass().add("franklin-font");
        currentStreakLabel.getStyleClass().add("franklin-font");
        bestStreakLabel.getStyleClass().add("franklin-font");
        statisticsLabel.getStyleClass().add("franklin-font");
        playedDescription.getStyleClass().add("franklin-font");
        winPercentageDescription.getStyleClass().add("franklin-font");
        currentStreakDescription.getStyleClass().add("franklin-font");
        bestStreakDescription.getStyleClass().add("franklin-font");
        titleLabel.getStyleClass().add("title-font");


        try {
            Properties statsProps = statsManager.loadStats();  
            gamesPlayed = Integer.parseInt(statsProps.getProperty("gamesPlayed", "0"));
            gamesWon = Integer.parseInt(statsProps.getProperty("gamesWon", "0"));
            currentStreak = Integer.parseInt(statsProps.getProperty("currentStreak", "0"));
            bestStreak = Integer.parseInt(statsProps.getProperty("bestStreak", "0"));
            firstTry = Integer.parseInt(statsProps.getProperty("firstTry", "0"));
            secondTry = Integer.parseInt(statsProps.getProperty("secondTry", "0"));
            thirdTry = Integer.parseInt(statsProps.getProperty("thirdTry", "0"));
            fourthTry = Integer.parseInt(statsProps.getProperty("fourthTry", "0"));
            fifthTry = Integer.parseInt(statsProps.getProperty("fifthTry", "0"));
            sixthTry = Integer.parseInt(statsProps.getProperty("sixthTry", "0"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        updateStatsDisplay();
        setUpGuessDistributionChart();
    }

    public void updateStatsDisplay() {
        gamesPlayedLabel.setText(String.valueOf(gamesPlayed));
        double winPercentage = gamesPlayed > 0 ? (double) gamesWon / gamesPlayed * 100 : 0;
        winPercentageLabel.setText(String.valueOf((int) winPercentage));
        currentStreakLabel.setText(String.valueOf(currentStreak));
        bestStreakLabel.setText(String.valueOf(bestStreak));
    }

    public void disableEndLabels() {
        statisticsLabel.setVisible(false);
        nextButton.setVisible(false);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    @FXML
    private void handleNextGameAction(ActionEvent event) {
        gameControl.resetGame();
        gameControl.closeStatsScreen();
    }

    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        if (gameControl != null) {
            gameControl.closeStatsScreen();
        }
    }

    private void setUpGuessDistributionChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Guess Distribution");
        
        series.getData().add(new XYChart.Data<>("1", getGuessCount(1)));
        series.getData().add(new XYChart.Data<>("2", getGuessCount(2)));
        series.getData().add(new XYChart.Data<>("3", getGuessCount(3)));
        series.getData().add(new XYChart.Data<>("4", getGuessCount(4)));
        series.getData().add(new XYChart.Data<>("5", getGuessCount(5)));
        series.getData().add(new XYChart.Data<>("6", getGuessCount(6)));

        for(Node n:guessDistributionChart.lookupAll(".default-color0.chart-bar")) {
            n.setStyle("-fx-bar-fill: #68b653;");
        }

        guessDistributionChart.getData().clear();
        guessDistributionChart.getData().add(series);
    }

    private int getGuessCount(int guessNumber) {
        switch (guessNumber) {
            case 1:
                return firstTry;
            case 2:
                return secondTry;
            case 3:
                return thirdTry;
            case 4:
                return fourthTry;
            case 5:
                return fifthTry;
            case 6:
                return sixthTry;
            default:
                return 0;
        }
    }
}
