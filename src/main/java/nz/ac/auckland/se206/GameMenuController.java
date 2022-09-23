package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GameMenuController {
  @FXML private Text welcomeText;
  @FXML private Button startGameButton;
  @FXML private Button statisticsButton;
  @FXML private Button signOutButton;
  @FXML private Button quitButton;

  public void updateScene() {
    welcomeText.setText("Welcome " + App.getCurrentUser().getName());
  }

  @FXML
  private void onStartGame(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getGameController().updateScene();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }

  @FXML
  private void onShowStatistics(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getStatisticsController().updateScene(App.getCurrentUser());

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.STATISTICS));
  }

  @FXML
  private void onSignOut(ActionEvent e) {
    App.setCurrentUser(null);
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.MAIN_MENU));
  }

  @FXML
  private void onQuitGame() {
    Platform.exit();
  }
}
