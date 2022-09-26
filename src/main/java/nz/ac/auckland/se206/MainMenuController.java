package nz.ac.auckland.se206;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.profile.User;

public class MainMenuController {
  @FXML private Label title;
  @FXML private Text username;
  @FXML private Button profileSelectLeft;
  @FXML private Button profileSelectRight;
  @FXML private SVGPath userIcon;
  @FXML private Button signInButton;
  private List<User> userList;
  private int currentUser;

  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
    setUpUsers();
  }

  public void setUpUsers() {
    File folder = new File("src/main/resources/UserProfiles");
    String[] userFileNames = folder.list();
    userList = new ArrayList<>();

    // Scans for all user profiles in the UserProfiles folder
    // Adds each user to the userList
    if (userFileNames != null && userFileNames.length > 0) {
      currentUser = 0;

      if (userFileNames.length == 1) {
        disableUserSelectLeft();
        disableUserSelectRight();
      } else {
        disableUserSelectLeft();
        enableUserSelectRight();
      }

      try {
        for (int i = 0; i < userFileNames.length; i++) {
          File profile = new File("src/main/resources/UserProfiles/" + userFileNames[i]);
          Gson gson = new Gson();
          JsonReader reader = new JsonReader(new FileReader(profile));
          User user = gson.fromJson(reader, User.class);
          userList.add(user);
        }
      } catch (FileNotFoundException ignored) {
        // Ignoring the exception as the file names were found by browsing the folder, so they must
        // exist
      }
      // Sets default user as first profile in folder
      username.setText(userList.get(currentUser).getName());
      username.setStyle("-fx-fill: black");
      userIcon.setStyle("-fx-fill: " + userList.get(currentUser).getColour());
      enableSignIn();
    } else {
      // No users found, so disables ability to sign in
      currentUser = -1;
      username.setText("No Users Found");
      username.setStyle("-fx-fill: grey");
      userIcon.setStyle("-fx-fill: grey");
      disableSignIn();
      disableUserSelectLeft();
      disableUserSelectRight();
    }
  }

  private void enableSignIn() {
    signInButton.setDisable(false);
    signInButton.setVisible(true);
  }

  private void disableSignIn() {
    signInButton.setDisable(true);
    signInButton.setVisible(false);
  }

  private void enableUserSelectLeft() {
    profileSelectLeft.setDisable(false);
    profileSelectLeft.setVisible(true);
  }

  private void disableUserSelectLeft() {
    profileSelectLeft.setDisable(true);
    profileSelectLeft.setVisible(false);
  }

  private void enableUserSelectRight() {
    profileSelectRight.setDisable(false);
    profileSelectRight.setVisible(true);
  }

  private void disableUserSelectRight() {
    profileSelectRight.setDisable(true);
    profileSelectRight.setVisible(false);
  }

  @FXML
  private void onToPreviousUser(MouseEvent e) {
    // Changes current user to previous user in list
    currentUser -= 1;
    username.setText(userList.get(currentUser).getName());
    userIcon.setStyle("-fx-fill: " + userList.get(currentUser).getColour());
    enableUserSelectRight();

    if (currentUser == 0) {
      disableUserSelectLeft();
    }
  }

  @FXML
  private void onToNextUser(MouseEvent e) {
    // Changes current user to next user in list
    currentUser += 1;
    username.setText(userList.get(currentUser).getName());
    userIcon.setStyle("-fx-fill: " + userList.get(currentUser).getColour());
    enableUserSelectLeft();

    if (currentUser == userList.size() - 1) {
      disableUserSelectRight();
    }
  }

  @FXML
  private void onSignIn(ActionEvent e) throws IOException {
    App.setCurrentUser(userList.get(currentUser));

    // Switches to the game scene
    sceneReset();
    App.getGameMenuController().updateScene();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }

  private void sceneReset() {
    // Sets the scene to the initial state
    setUpUsers();
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
