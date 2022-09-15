package nz.ac.auckland.se206;

import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class MainMenuController {
  @FXML private Text infoText;

  @FXML private TextField usernameField;

  @FXML
  private void onSignIn(ActionEvent e) {
    File profile = new File("src/main/resources/UserProfiles/" + usernameField.getText() + ".json");
    // Checks if username entered exists
    if (profile.isFile()) {
      // Sets current profile as the username entered and switches to the game scene
      App.setCurrentProfile(profile);
      sceneReset();
      Button button = (Button) e.getSource();
      Scene currentScene = button.getScene();
      currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
    } else {
      infoText.setText("Enter a valid username");
      usernameField.setText("");
    }
  }

  private void sceneReset() {
    // Sets the scene to the initial state
    infoText.setText("Sign in to your account");
    usernameField.setText("");
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
