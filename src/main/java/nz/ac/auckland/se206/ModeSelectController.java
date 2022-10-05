package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class ModeSelectController {

  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  @FXML
  private void onBack(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getGameController().updateScene();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }

  @FXML
  private void onZenMode(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getGameController().updateScene();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }

  @FXML
  private void onNormalMode(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getGameController().updateScene();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }
}
