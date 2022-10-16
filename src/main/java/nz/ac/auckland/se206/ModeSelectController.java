package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.profile.User;

public class ModeSelectController {

  @FXML private CheckBox checkBoxHidWordMode;
  @FXML private Label accuracyDifficultyLabel;
  @FXML private Label wordDifficultyLabel;
  @FXML private Label confidenceDifficultyLabel;
  @FXML private Label timeDifficultyLabel;
  private static ActionEvent event;

  /**
   * This method returns the action event.
   *
   * @return action event
   */
  public static ActionEvent getActionEvent() {
    return event;
  }

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  /**
   * This method will change the scene to the game menu
   *
   * @param e The acton event that triggered this method
   */
  @FXML
  private void onBack(ActionEvent e) {
    AudioController.playButtonClick();
    event = e;
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    // Sets the scene to the game menu
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }

  /**
   * This method will change the scene to the game
   *
   * @param e The acton event that triggered this method
   */
  @FXML
  private void onPlayZenMode(ActionEvent e) {
    AudioController.playButtonClick();
    event = e;
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    // Updating the game scene to get ready for a new game
    App.getGameController().updateScene(checkBoxHidWordMode.isSelected());

    // Sets the scene to the game
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }

  /**
   * This method will change the scene to the game
   *
   * @param e The acton event that triggered this method
   */
  @FXML
  private void onPlayNormalMode(ActionEvent e) {
    AudioController.playButtonClick();
    event = e;
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    // Updating the game scene to get ready for a new game
    App.getGameController().updateScene(checkBoxHidWordMode.isSelected());

    // Sets the scene to the game menu
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
  }

  /** This method sets up the scene with the users specified difficulty settings. */
  public void setupScene() {
    User currentUser = App.getCurrentUser();

    // Displays the difficulty for the game
    accuracyDifficultyLabel.setText("Accuracy: " + currentUser.getAccuracyDifficulty().toString());
    timeDifficultyLabel.setText("Time: " + currentUser.getTimeDifficulty().toString());
    confidenceDifficultyLabel.setText(
        "Confidence: " + currentUser.getConfidenceDifficulty().toString());
    wordDifficultyLabel.setText("Word: " + currentUser.getWordsDifficulty().toString());
  }
}
