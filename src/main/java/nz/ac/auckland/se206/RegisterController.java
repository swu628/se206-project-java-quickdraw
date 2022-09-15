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
import javafx.scene.text.Text;
import nz.ac.auckland.se206.profile.User;

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
    } else if (newUsernameField.getText().isEmpty()) {
      sceneReset();
      text.setText("Username cannot be empty");
    } else {
      // Creates a folder to store users if needed
      final File Users = new File("src/main/resources/UserProfiles");
      if (!Users.exists()) {
        Users.mkdir();
      }

      // Creates a json file if there are none
      Gson gson = new Gson();

      // Checks if username is taken
      if (new File("src/main/resources/UserProfiles/" + newUsernameField.getText() + ".json")
          .isFile()) {
        sceneReset();
        text.setText("This username is taken");
        //    	  System.out.println("Exists");
        //    	  JsonReader reader = new JsonReader(new
        // FileReader("src/main/resources/UserProfiles/users.json"));
        //    	  User[] users = gson.fromJson(reader,User.class);
        //    	  System.out.println("Username: " + users[0].getName());

        // https://stackoverflow.com/questions/47111676/gson-append-new-object-array-to-existing-json-file
        //    	  List<User> users = gson.fromJson(reader, new TypeToken<List<User>>() {}.getType());
        //    	  User newUser = new User(newUsernameField.getText());
        //    	  users.add(newUser);
        //    	  gson.toJson(users);

      } else {
        // Create json file named as the username
        System.out.println("File created");
        FileWriter fileWriter =
            new FileWriter(
                "src/main/resources/UserProfiles/" + newUsernameField.getText() + ".json");

        // Write user details into the file
        User user = new User(newUsernameField.getText());
        gson.toJson(user, fileWriter);
        fileWriter.close();

        newUsernameField.setDisable(true);
        signUpButton.setDisable(true);
        text.setText("Account Created");
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
