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

  @FXML private Button startButton;

  @FXML private Button quitButton;

  @FXML private Button createButton;

  @FXML private TextField usernameField;

  @FXML
  private void onSignIn(ActionEvent e) {
    if (usernameField.getText().trim().isEmpty()) {
      infoText.setText("Enter valid username");
      usernameField.setText("");
    } else {
      // TODO Add username check function

      Button button = (Button) e.getSource();
      Scene currentScene = button.getScene();
      currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME));
    }
  }

  @FXML
  private void onCreateAccount(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.REGISTER));
  }

  @FXML
  private void onQuitGame() {
    Platform.exit();
  }
}
