package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class MainMenuController {
  @FXML private Button startButton;

  @FXML private Button quitButton;

  @FXML
  private void onStartGame(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }

  @FXML
  private void onQuitGame() {
    Platform.exit();
  }
}
