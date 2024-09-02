package com.wordle;

import com.wordle.game.GameManager;
import com.wordle.ui.GameWindowController;
import com.wordle.utils.GameState;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class App extends Application {

    private GameWindowController gameWindowController;
    private StackPane rootPane = new StackPane();
    private GameManager gameManager = new GameManager();

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wordle/ui/GameWindow.fxml"));
            Parent gameView = loader.load();
            gameWindowController = loader.getController();

            // load GameState and initialize the GameWindowController with it
            GameState gameState = gameManager.loadGameState();
            if (gameState != null && gameState.getCurrentWord() != null && !gameState.isGameOver()) {
                gameWindowController.initializeWithGameState(gameState);
            } else {
                gameWindowController.startNewGame();
                // new game if no saved state
            }

            loadCustomFont();

            gameWindowController.setPrimaryStage(primaryStage);
            gameWindowController.setRootPane(rootPane);

            rootPane.getChildren().add(gameView);

            Scene scene = new Scene(rootPane, 800, 640);
            scene.getStylesheets().add(getClass().getResource("/com/wordle/utils/styles.css").toExternalForm());

            Scale scale = new Scale(1, 1, 0, 0);
            rootPane.widthProperty().addListener((obs, oldVal, newVal) -> scale.setPivotX(newVal.doubleValue() / 2));
            rootPane.heightProperty().addListener((obs, oldVal, newVal) -> scale.setPivotY(newVal.doubleValue() / 2));

            DoubleBinding scaleBinding = Bindings.createDoubleBinding(() ->
                Math.min(scene.widthProperty().get() / 800, scene.heightProperty().get() / 640),
                scene.widthProperty(), scene.heightProperty());

            scale.xProperty().bind(scaleBinding);
            scale.yProperty().bind(scaleBinding);
            rootPane.getTransforms().add(scale);

            Image applicationIcon = new Image(getClass().getResourceAsStream("/com/wordle/images/wordle_icon.png"));
            primaryStage.getIcons().add(applicationIcon);

            primaryStage.setTitle("Wordle");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        gameWindowController.closeStatsScreen();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void loadCustomFont() {
        @SuppressWarnings("unused")
        Font franklinFont = Font.loadFont(getClass().getResourceAsStream("/com/wordle/fonts/franklin-normal-900.ttf"), 18);
        @SuppressWarnings("unused")
        Font karnakFont = Font.loadFont(getClass().getResourceAsStream("/com/wordle/fonts/karnak-normal-400.ttf"), 30);
    }
}
