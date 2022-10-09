package nz.ac.auckland.se206;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.profile.User;

public class StatisticsController {
  @FXML private Label username;
  @FXML private Label gamesWon;
  @FXML private Label gamesLost;
  @FXML private Label fastestTime;
  @FXML private TextArea wordsHistory;
  @FXML private Button wordsHistoryNext;
  @FXML private Button wordsHistoryPrevious;
  private int wordsHistoryStart;
  private int wordsHistoryEnd;
  private ArrayList<String> history;

  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  public void updateScene(User currentUser) {
    // Sets the statistics scene to show current user's statistics
    username.setText(currentUser.getName() + "'s statistics");
    gamesWon.setText(String.valueOf(currentUser.getGamesWon()));
    gamesLost.setText(String.valueOf(currentUser.getGamesLost()));

    // Fastest won game default value is -1
    if (currentUser.getFastestWon() == -1) {
      fastestTime.setText("-");
    } else {
      fastestTime.setText(currentUser.getFastestWon() + " second(s)");
    }

    // Displays first 5 words encountered by the user
    wordsHistoryStart = 0;
    wordsHistoryEnd = 4;
    history = currentUser.getWordsHistory();

    StringBuilder sb = new StringBuilder();

    for (int i = wordsHistoryStart; i < history.size() && i <= wordsHistoryEnd; i++) {
      sb.append(history.get(i)).append(System.lineSeparator());
    }

    // Enables the next selector and disables the previous selector
    wordsHistoryPrevious.setVisible(false);
    wordsHistoryPrevious.setDisable(true);
    wordsHistoryNext.setVisible(true);
    wordsHistoryNext.setDisable(false);

    wordsHistory.setText(sb.toString());
  }

  @FXML
  private void onToNextWordHistory() {
    // Gets next 5 words in history
    wordsHistoryStart = wordsHistoryEnd + 1;
    wordsHistoryEnd += 5;

    StringBuilder sb = new StringBuilder();

    for (int i = wordsHistoryStart; i < history.size() && i <= wordsHistoryEnd; i++) {
      sb.append(history.get(i)).append(System.lineSeparator());
    }

    if (wordsHistoryEnd >= history.size() - 1) {
      // Disables the next selector
      wordsHistoryNext.setVisible(false);
      wordsHistoryNext.setDisable(true);
    }

    // Enables the previous selector
    wordsHistoryPrevious.setVisible(true);
    wordsHistoryPrevious.setDisable(false);

    wordsHistory.setText(sb.toString());
  }

  @FXML
  private void onToPreviousWordHistory() {
    // Gets previous 5 words in history
    wordsHistoryEnd = wordsHistoryStart - 1;
    wordsHistoryStart -= 5;

    StringBuilder sb = new StringBuilder();

    for (int i = wordsHistoryStart; i < history.size() && i <= wordsHistoryEnd; i++) {
      sb.append(history.get(i)).append(System.lineSeparator());
    }

    if (wordsHistoryStart == 0) {
      // Disables the previous selector
      wordsHistoryPrevious.setVisible(false);
      wordsHistoryPrevious.setDisable(true);
    }

    // Enables the next selector
    wordsHistoryNext.setVisible(true);
    wordsHistoryNext.setDisable(false);

    wordsHistory.setText(sb.toString());
  }

  @FXML
  private void onGameMenu(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }
}
