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

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
    setUpUsers();
  }

  /**
   * This method loads all the users in the user profiles folder and displays it in the main menu
   * scene.
   */
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

  /** This method enables the signin button */
  private void enableSignIn() {
    signInButton.setDisable(false);
    signInButton.setVisible(true);
  }

  /** This method disables the signin button */
  private void disableSignIn() {
    signInButton.setDisable(true);
    signInButton.setVisible(false);
  }

  /** This method enables the previous user button */
  private void enableUserSelectLeft() {
    profileSelectLeft.setDisable(false);
    profileSelectLeft.setVisible(true);
  }

  /** This method disables the previous user button */
  private void disableUserSelectLeft() {
    profileSelectLeft.setDisable(true);
    profileSelectLeft.setVisible(false);
  }

  /** This method enables the next user button */
  private void enableUserSelectRight() {
    profileSelectRight.setDisable(false);
    profileSelectRight.setVisible(true);
  }

  /** This method disables the next user button */
  private void disableUserSelectRight() {
    profileSelectRight.setDisable(true);
    profileSelectRight.setVisible(false);
  }

  /**
   * This method changes the selected user to the previous user in the user array.
   *
   * @param e the mouse even that triggered this method
   */
  @FXML
  private void onToPreviousUser(MouseEvent e) {
    AudioController.playPencilWrite();
    // Changes current user to previous user in list
    currentUser -= 1;
    username.setText(userList.get(currentUser).getName());
    userIcon.setStyle("-fx-fill: " + userList.get(currentUser).getColour());
    enableUserSelectRight();

    if (currentUser == 0) {
      disableUserSelectLeft();
    }
  }

  /**
   * This method changes the selected user to the next user in the user array.
   *
   * @param e the mouse even that triggered this method
   */
  @FXML
  private void onToNextUser(MouseEvent e) {
    AudioController.playPencilWrite();
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
    AudioController.playButtonClick();
    App.setCurrentUser(userList.get(currentUser));

    // Switches to the game scene
    setUpUsers();
    App.getGameMenuController().updateScene();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }

  @FXML
  private void onCreateAccount(ActionEvent e) {
    AudioController.playButtonClick();
    // Resets the scene and replaces root scene with register scene
    setUpUsers();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.REGISTER));
  }

  /** This method quits the game */
  @FXML
  private void onQuitGame() {
    Platform.exit();
  }
}
