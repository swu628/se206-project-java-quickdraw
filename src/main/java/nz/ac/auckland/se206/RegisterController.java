package nz.ac.auckland.se206;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.profile.User;

public class RegisterController {
  @FXML private Text text;

  @FXML private Button signUpButton;

  @FXML private Button backButton;

  @FXML private TextField newUsernameField;

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  /**
   * This method signs in the user.
   *
   * @throws IOException if the user profile could not be created
   */
  @FXML
  private void onSignUp() throws IOException {
    // Checks if username is valid
    if (newUsernameField.getText().contains(" ")) {
      sceneReset();
      text.setText("Username cannot contain spaces");
    } else if (newUsernameField.getText().isEmpty()) {
      sceneReset();
      text.setText("Username cannot be empty");
    } else {
      // Creates a folder to store users if needed
      final File Users = new File("src/main/resources/UserProfiles");
      if (!Users.exists()) {
        Users.mkdir();
      }

      // Checks if username is taken
      if (new File("src/main/resources/UserProfiles/" + newUsernameField.getText() + ".json")
          .isFile()) {
        sceneReset();
        text.setText("This username is taken");
      } else {
        // Create json file named as the username
        FileWriter fileWriter =
            new FileWriter(
                "src/main/resources/UserProfiles/" + newUsernameField.getText() + ".json");

        // Write user details into the file
        Gson gson = new Gson();
        gson.toJson(new User(newUsernameField.getText()), fileWriter);
        fileWriter.close();

        newUsernameField.setDisable(true);
        signUpButton.setDisable(true);
        text.setStyle("-fx-fill: green;");
        text.setText("Account Created");
      }
    }
  }

  /** This method resets the scene. */
  private void sceneReset() {
    // Sets the scene to the initial state
    text.setText("");
    text.setStyle("-fx-fill: red;");
    newUsernameField.setDisable(false);
    newUsernameField.setText("");
    signUpButton.setDisable(false);
    backButton.setDisable(false);
  }

  /**
   * This method sets the scene to the main menu.
   *
   * @param e the action event that triggered this method
   */
  @FXML
  private void onBack(ActionEvent e) {
    AudioController.playButtonClick();
    // Resets the scene and replaces root scene with main menu scene
    sceneReset();
    // Sets up main menu scene
    App.getMainMenuController().setUpUsers();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.MAIN_MENU));
  }
}
