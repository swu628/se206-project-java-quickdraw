package nz.ac.auckland.se206;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.profile.User;

public class StatisticsController {
  @FXML private Label username;
  @FXML private Label gamesWon;
  @FXML private Label gamesLost;
  @FXML private Label fastestTime;
  @FXML private TextArea wordsHistory;

  public void onScene(User currentUser) {
    // Sets the statistics scene to show current user's statistics
    username.setText(currentUser.getName() + "'s statistics");
    gamesWon.setText(String.valueOf(currentUser.getGamesWon()));
    gamesLost.setText(String.valueOf(currentUser.getGamesLost()));

    // Fastests won game default value is -1
    if (currentUser.getFastestWon() == -1) {
      fastestTime.setText("-");
    } else {
      fastestTime.setText(currentUser.getFastestWon() + " second(s)");
    }

    // Displays list of words encountered by the user
    StringBuilder sb = new StringBuilder();
    ArrayList<String> history = currentUser.getWordsHistory();

    for (String word : history) {
      sb.append(word).append(System.lineSeparator());
    }

    wordsHistory.setText(sb.toString());
  }

  @FXML
  private void onGameMenu(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }
}
