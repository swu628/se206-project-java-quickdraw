package nz.ac.auckland.se206;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RegisterController {
  @FXML private Text text;

  @FXML private Button signUpButton;

  @FXML private Button backButton;

  @FXML private TextField newUsernameField;

  @FXML
  private void onSignUp() {
    // Checks if username is valid
    if (newUsernameField.getText().trim().isEmpty()) {
      text.setText("Enter valid username");
      newUsernameField.setText("");
    }
    // TODO Check if username is taken
    else {
      newUsernameField.setDisable(true);
      signUpButton.setDisable(true);
      text.setText("Account Created");
      // TODO create file named as the textfield username
    }
  }

  @FXML
  private void onBack(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.MAIN_MENU));
  }
}
