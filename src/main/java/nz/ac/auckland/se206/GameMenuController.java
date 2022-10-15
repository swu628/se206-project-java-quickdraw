package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameMenuController {
  @FXML private Text welcomeText;
  @FXML private Button startGameButton;
  @FXML private Button statisticsButton;
  @FXML private Button signOutButton;
  @FXML private Button quitButton;

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  /** This method updates the scene to the user's username */
  public void updateScene() {
    welcomeText.setText("Welcome " + App.getCurrentUser().getName() + "!");
  }

  /**
   * This method will change the scene to the mode select screen.
   *
   * @param e the action event that triggered this method
   */
  @FXML
  private void onStartGame(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.MODE_SELECT));
  }

  /**
   * This method changes the scene to the statistics scene
   *
   * @param e the action event that triggered this method
   */
  @FXML
  private void onShowStatistics(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getStatisticsController().updateScene(App.getCurrentUser());

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.STATISTICS));
  }

  /**
   * This method changes the scene to the difficulty settings scene
   *
   * @param e the action event that triggered this method
   */
  @FXML
  private void onShowDifficultySettings(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    App.getDifficultySettingsController().setUpScene(App.getCurrentUser());

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.DIFFICULTY_SETTINGS));
  }

  /**
   * This method signs out the user and changes the scene to the main menu scene
   *
   * @param e the action event that triggered this method
   */
  @FXML
  private void onSignOut(ActionEvent e) {
    App.setCurrentUser(null);
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    // Updates the main menu with all the current user profiles that exist
    App.getMainMenuController().setUpUsers();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.MAIN_MENU));
  }

  /** This method quits the game */
  @FXML
  private void onQuitGame() {
    Platform.exit();
  }
}
