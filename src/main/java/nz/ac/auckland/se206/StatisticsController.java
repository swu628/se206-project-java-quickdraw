package nz.ac.auckland.se206;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
  @FXML private ImageView underThirtyBadge;
  @FXML private ImageView underTwentyBadge;
  @FXML private ImageView underTenBadge;
  @FXML private ImageView zenBadge;
  private int wordsHistoryStart;
  private int wordsHistoryEnd;
  private ArrayList<String> history;
  private ArrayList<Integer> timeTakenHistory;
  private int countsUnderThirty;
  private int countsUnderTwenty;
  private int countsUnderTen;

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

    timeTakenHistory = currentUser.getTimeTakenHistory();
    setBadge(currentUser);
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

  /**
   * This method will give user badges based on their history
   *
   * @param currentUser is the account that is currently logged in
   */
  private void setBadge(User currentUser) {
    // Set default to zero
    countsUnderThirty = 0;
    countsUnderTwenty = 0;
    countsUnderTen = 0;

    // Counts the number of games where the time spent is under 30 seconds
    for (int i = 0; i < timeTakenHistory.size(); i++) {
      if (timeTakenHistory.get(i) <= 30) {
        countsUnderThirty++;
      }
      if (timeTakenHistory.get(i) <= 20) {
        countsUnderTwenty++;
      }
      if (timeTakenHistory.get(i) <= 10) {
        countsUnderTen++;
      }
    }

    // Update corresponding badge
    if (countsUnderThirty < 3) {
      currentUser.setUnderThirtyBadge("/images/under30_opacity80.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
    } else if (countsUnderThirty >= 3 && countsUnderThirty < 10) {
      currentUser.setUnderThirtyBadge("/images/under30_bronze.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
    } else if (countsUnderThirty >= 10 && countsUnderThirty < 50) {
      currentUser.setUnderThirtyBadge("/images/under30_silver.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
    } else {
      currentUser.setUnderThirtyBadge("/images/under30_gold.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
    }

    if (countsUnderTwenty < 3) {
      currentUser.setUnderTwentyBadge("/images/under20_opacity80.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
    } else if (countsUnderTwenty >= 3 && countsUnderTwenty < 10) {
      currentUser.setUnderTwentyBadge("/images/under20_bronze.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
    } else if (countsUnderTwenty >= 10 && countsUnderTwenty < 50) {
      currentUser.setUnderTwentyBadge("/images/under20_silver.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
    } else {
      currentUser.setUnderTwentyBadge("/images/under20_gold.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
    }

    if (countsUnderTen < 3) {
      currentUser.setUnderTenBadge("/images/under10_opacity80.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
    } else if (countsUnderTen >= 3 && countsUnderTen < 10) {
      currentUser.setUnderTenBadge("/images/under10_bronze.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
    } else if (countsUnderTen >= 10 && countsUnderTen < 50) {
      currentUser.setUnderTenBadge("/images/under10_silver.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
    } else {
      currentUser.setUnderTenBadge("/images/under10_gold.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
    }

    if (currentUser.getNumberOfZenPlayed() < 3) {
      currentUser.setZenBadge("/images/zen_opacity80.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
    } else if (currentUser.getNumberOfZenPlayed() >= 3 && currentUser.getNumberOfZenPlayed() < 10) {
      currentUser.setZenBadge("/images/zen_bronze.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
    } else if (currentUser.getNumberOfZenPlayed() >= 10
        && currentUser.getNumberOfZenPlayed() < 50) {
      currentUser.setZenBadge("/images/zen_silver.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
    } else {
      currentUser.setZenBadge("/images/zen_gold.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
    }
  }
}
