package nz.ac.auckland.se206;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainMenuController {
  @FXML private Text infoText;

  @FXML private Button signInButton;

  @FXML private Button quitButton;

  @FXML private Button createButton;

  @FXML private TextField usernameField;

  @FXML
  private void onSignIn(ActionEvent e) {
    if (usernameField.getText().trim().isEmpty()) {
      infoText.setText("Enter a valid username");
      usernameField.setText("");
    } else {
      // TODO Add username check function
      sceneReset();
      Button button = (Button) e.getSource();
      Scene currentScene = button.getScene();
      currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
    }
  }

  private void sceneReset() {
    // Sets the scene to the initial state
    infoText.setText("Sign in to your account");
    usernameField.setText("");
    signInButton.setText("Sign in");
    createButton.setText("Create new account");
    quitButton.setText("Quit game");
  }

  @FXML
  private void onCreateAccount(ActionEvent e) {
    // Resets the scene and replaces root scene with register scene
    sceneReset();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.REGISTER));
  }

  @FXML
  private void onQuitGame() {
    Platform.exit();
  }
}
