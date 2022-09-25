package nz.ac.auckland.se206;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.profile.User;

public class MainMenuController {
  @FXML private Label title;
  @FXML private Text infoText;
  @FXML private TextField usernameField;

  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  @FXML
  private void onSignIn(ActionEvent e) throws IOException {
    File profile = new File("src/main/resources/UserProfiles/" + usernameField.getText() + ".json");
    // Checks if username entered exists
    if (profile.isFile()) {
      // Sets the current user as the username entered
      Gson gson = new Gson();
      JsonReader reader = new JsonReader(new FileReader(profile));
      User user = gson.fromJson(reader, User.class);
      App.setCurrentUser(user);

      // Switches to the game scene
      sceneReset();
      App.getGameMenuController().updateScene();
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
    infoText.setText("");
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
