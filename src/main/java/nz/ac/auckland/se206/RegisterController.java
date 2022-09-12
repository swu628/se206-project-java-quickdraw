package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
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
  private void onSignUp() throws IOException {
    // Checks if username is valid
    if (newUsernameField.getText().contains(" ")) {
      sceneReset();
      text.setText("Username cannot contain spaces");
    } else {
      // Creates a folder if needed
      final File profileFolder = new File("src/main/resources/profiles");
      if (!profileFolder.exists()) {
        profileFolder.mkdir();
      }

      // Creates a txt file named as the textfield username if the username is not taken
      File profileInfo =
          new File(
              "src/main/resources/"
                  + profileFolder.getName()
                  + "/"
                  + newUsernameField.getText()
                  + ".txt");
      if (profileInfo.createNewFile()) {
        newUsernameField.setDisable(true);
        signUpButton.setDisable(true);
        text.setText("Account Created");
      } else {
        sceneReset();
        text.setText("This username is taken");
      }
    }
  }

  private void sceneReset() {
    // Sets the scene to the initial state
    text.setText("Enter your username");
    newUsernameField.setDisable(false);
    newUsernameField.setText("");
    signUpButton.setDisable(false);
    backButton.setDisable(false);
  }

  @FXML
  private void onBack(ActionEvent e) {
    // Resets the scene and replaces root scene with main menu scene
    sceneReset();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.MAIN_MENU));
  }
}
