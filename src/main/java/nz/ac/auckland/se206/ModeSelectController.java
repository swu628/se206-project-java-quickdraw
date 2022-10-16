package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class ModeSelectController {
  private static ActionEvent event;

  /**
   * This method returns the action event.
   *
   * @return action event
   */
  public static ActionEvent getActionEvent() {
    return event;
  }

  @FXML private CheckBox checkBoxHidWordMode;
  @FXML private Button zenModeButton;
  @FXML private Button normalModeButton;
  private Tooltip zenModeMessage;
  private Tooltip normalModeMessage;
  private Tooltip hiddenModeMessage;

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font. It also sets the tooltips for the game modes.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);

    zenModeMessage = new Tooltip();
    normalModeMessage = new Tooltip();
    hiddenModeMessage = new Tooltip();

    // Sets the display message hover to appear after 100ms
    zenModeMessage.setShowDelay(Duration.millis(100));
    // Sets the display message hover to appear after 100ms
    normalModeMessage.setShowDelay(Duration.millis(100));
    // Sets the display message hover to appear after 100ms
    hiddenModeMessage.setShowDelay(Duration.millis(100));

    // Sets the display message font styles
    zenModeMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
    // Sets the display message font styles
    normalModeMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
    // Sets the display message font styles
    hiddenModeMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
    // Sets the text for zen mode
    zenModeMessage.setText(
        "The same as normal game mode, but there is no time limit and user can use different colours."
            + System.lineSeparator()
            + "Zen mode games are not tracked.");
    // Sets the text for normal mode
    normalModeMessage.setText(
        "Draw the given word within the time limit to win."
            + System.lineSeparator()
            + "Games are tracked in user's statistics.");
    // Sets the text for hidden mode
    hiddenModeMessage.setText(
        "Additional option for normal and zen mode."
            + System.lineSeparator()
            + "Instead of giving words, the word definition is given to the user."
            + System.lineSeparator()
            + "Hidden mode games are not tracked");

    // Adding tooltip to respective buttons/checkbox
    Tooltip.install(zenModeButton, zenModeMessage);
    Tooltip.install(normalModeButton, normalModeMessage);
    Tooltip.install(checkBoxHidWordMode, hiddenModeMessage);
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

  /** This method plays the pencil write sound. */
  @FXML
  private void onPlayWriteSound() {
    AudioController.playPencilWrite();
  }
}
