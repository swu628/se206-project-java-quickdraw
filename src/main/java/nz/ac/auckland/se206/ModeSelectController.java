package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Font;

public class ModeSelectController {

  @FXML private CheckBox checkBoxHidWordMode;
  private static ActionEvent event;

  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  public static ActionEvent getActionEvent() {
    return event;
  }

  @FXML
  private void onBack(ActionEvent e) {
    event = e;
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }

  @FXML
  private void onZenMode(ActionEvent e) {
    event = e;
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getGameController().updateScene(checkBoxHidWordMode.isSelected());

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }

  @FXML
  private void onNormalMode(ActionEvent e) {
    event = e;
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getGameController().updateScene(checkBoxHidWordMode.isSelected());

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }
}
