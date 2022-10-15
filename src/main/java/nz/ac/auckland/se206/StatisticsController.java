package nz.ac.auckland.se206;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.util.Duration;
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
  @FXML private ImageView consecutiveWinsBadge;
  private int wordsHistoryStart;
  private int wordsHistoryEnd;
  private ArrayList<String> history;
  private ArrayList<Integer> timeTakenHistory;
  private ArrayList<String> wonOrLostHistory;
  private int countsUnderThirty;
  private int countsUnderTwenty;
  private int countsUnderTen;
  private Tooltip underThirtyMessage = new Tooltip();
  private Tooltip underTwentyMessage = new Tooltip();
  private Tooltip underTenMessage = new Tooltip();
  private Tooltip zenMessage = new Tooltip();
  private Tooltip consecutiveWinsMessage = new Tooltip();

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font. It also initializes the tooltip setttings.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
    // Sets the display message hover to appear after 150ms
    consecutiveWinsMessage.setShowDelay(Duration.millis(150));
    // Sets the display message hover to appear after 150ms
    zenMessage.setShowDelay(Duration.millis(150));
    // Sets the display message hover to appear after 150ms
    underTenMessage.setShowDelay(Duration.millis(150));
    // Sets the display message hover to appear after 150ms
    underTwentyMessage.setShowDelay(Duration.millis(150));
    // Sets the display message hover to appear after 150ms
    underThirtyMessage.setShowDelay(Duration.millis(150));

    // Sets the display message font styles
    zenMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
    // Sets the display message font styles
    consecutiveWinsMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
    // Sets the display message font styles
    underTenMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
    // underTenMessage the display message font styles
    underTwentyMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
    // Sets the display message font styles
    underThirtyMessage.setStyle("-fx-font-size: 16px; -fx-font-family: 'Indie Flower'");
  }

  /**
   * This method updates the statistics with the values of the user. Updates the games won, games
   * lost, the fastest time and words history to the user's.
   *
   * @param currentUser the current user that is logged in
   */
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
    disableHistoryPrevious();
    if (history.size() > 5) {
      enableHistoryNext();
    } else {
      disableHistoryNext();
    }

    wordsHistory.setText(sb.toString());

    // display badges
    timeTakenHistory = currentUser.getTimeTakenHistory();
    wonOrLostHistory = currentUser.getWonOrLostHistory();
    setBadge(currentUser);
  }

  /**
   * This method displays the next 5 words in the user's words history. It will disable the next
   * button if it is the last 5 words of the user's history
   */
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
      disableHistoryNext();
    }

    enableHistoryPrevious();

    wordsHistory.setText(sb.toString());
  }

  /**
   * This method displays the previous 5 words in the user's words history It will disable the
   * previous button if it is the first 5 words of the user's history
   */
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
      disableHistoryPrevious();
    }

    enableHistoryNext();

    wordsHistory.setText(sb.toString());
  }

  /** This method enables the next button for words history */
  private void enableHistoryNext() {
    wordsHistoryNext.setVisible(true);
    wordsHistoryNext.setDisable(false);
  }

  /** This method disables the next button for words history */
  private void disableHistoryNext() {
    wordsHistoryNext.setVisible(false);
    wordsHistoryNext.setDisable(true);
  }

  /** This method enables the previous button for words history */
  private void enableHistoryPrevious() {
    wordsHistoryPrevious.setVisible(true);
    wordsHistoryPrevious.setDisable(false);
  }

  /** This method disables the previous button for words history */
  private void disableHistoryPrevious() {
    wordsHistoryPrevious.setVisible(false);
    wordsHistoryPrevious.setDisable(true);
  }

  /**
   * This method sets the scene to the game menu.
   *
   * @param e the event that triggered this method
   */
  @FXML
  private void onGameMenu(ActionEvent e) {
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }

  /**
   * This method will give user badges based on their history and display its message
   *
   * @param currentUser is the account that is currently logged in
   */
  private void setBadge(User currentUser) {
    // Set default to zero
    countsUnderThirty = 0;
    countsUnderTwenty = 0;
    countsUnderTen = 0;

    // Counts the number of games where the time spent is under 30, 20 and 10 seconds
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

    // Update under 30 seconds badge based on the number of games won
    // Also when hover, display message of the badge
    if (countsUnderThirty < 3) {
      // Sets disabled under thirty seconds wins badge
      currentUser.setUnderThirtyBadge("/images/under30_opacity80.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
      underThirtyMessage.setText(
          "Under 30 seconds badge \nTry to win three rounds in under thirty seconds to earn the bronze badge :)");
      Tooltip.install(underThirtyBadge, underThirtyMessage);
    } else if (countsUnderThirty >= 3 && countsUnderThirty < 10) {
      // Sets bronze under thirty seconds wins badge
      currentUser.setUnderThirtyBadge("/images/under30_bronze.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
      underThirtyMessage.setText(
          "Well done! \nTry to win ten rounds in under thirty seconds to earn the silver badge :)");
      Tooltip.install(underThirtyBadge, underThirtyMessage);
    } else if (countsUnderThirty >= 10 && countsUnderThirty < 50) {
      // Sets silver under thirty seconds wins badge
      currentUser.setUnderThirtyBadge("/images/under30_silver.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
      underThirtyMessage.setText(
          "Good job! \nTry to win fifty rounds in under thirty seconds to earn the gold badge :)");
      Tooltip.install(underThirtyBadge, underThirtyMessage);
    } else {
      // Sets gold under thirty seconds wins badge
      currentUser.setUnderThirtyBadge("/images/under30_gold.png");
      underThirtyBadge.setImage(new Image(currentUser.getUnderThirtyBadge()));
      underThirtyMessage.setText(
          "Well done! \nYou have earned all the badges for under 30 seconds badge :)");
      Tooltip.install(underThirtyBadge, underThirtyMessage);
    }

    // Update under 20 seconds badge based on the number of games won
    // Also when hover, display message of the badge
    if (countsUnderTwenty < 3) {
      // Sets disabled under twenty seconds wins badge
      currentUser.setUnderTwentyBadge("/images/under20_opacity80.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
      underTwentyMessage.setText(
          "Under 20 seconds badge \nTry to win three rounds in under twenty seconds to earn the bronze badge :)");
      Tooltip.install(underTwentyBadge, underTwentyMessage);
    } else if (countsUnderTwenty >= 3 && countsUnderTwenty < 10) {
      // Sets bronze under twenty seconds wins badge
      currentUser.setUnderTwentyBadge("/images/under20_bronze.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
      underTwentyMessage.setText(
          "Well done! \nTry to win ten rounds in under twenty seconds to earn the silver badge :)");
      Tooltip.install(underTwentyBadge, underTwentyMessage);
    } else if (countsUnderTwenty >= 10 && countsUnderTwenty < 50) {
      // Sets silver under twenty seconds wins badge
      currentUser.setUnderTwentyBadge("/images/under20_silver.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
      underTwentyMessage.setText(
          "Good job! \nTry to win fifty rounds in under twenty seconds to earn the gold badge :)");
      Tooltip.install(underTwentyBadge, underTwentyMessage);
    } else {
      // Sets gold under twenty seconds wins badge
      currentUser.setUnderTwentyBadge("/images/under20_gold.png");
      underTwentyBadge.setImage(new Image(currentUser.getUnderTwentyBadge()));
      underTwentyMessage.setText(
          "Well done! \nYou have earned all the badges for under 20 seconds badge :)");
      Tooltip.install(underTwentyBadge, underTwentyMessage);
    }

    // Update under 10 seconds badge based on the number of games won
    // Also when hover, display message of the badge
    if (countsUnderTen < 3) {
      // Sets disabled under ten seconds wins badge
      currentUser.setUnderTenBadge("/images/under10_opacity80.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
      underTenMessage.setText(
          "Under 10 seconds badge \nTry to win three rounds in under ten seconds to earn the bronze badge :)");
      Tooltip.install(underTenBadge, underTenMessage);
    } else if (countsUnderTen < 10) {
      // Sets bronze under ten seconds wins badge
      currentUser.setUnderTenBadge("/images/under10_bronze.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
      underTenMessage.setText(
          "Well done! \nTry to win ten rounds in under ten seconds to earn the silver badge :)");
      Tooltip.install(underTenBadge, underTenMessage);
    } else if (countsUnderTen < 50) {
      // Sets silver under ten seconds wins badge
      currentUser.setUnderTenBadge("/images/under10_silver.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
      underTenMessage.setText(
          "Good job! \nTry to win fifty rounds in under ten seconds to earn the gold badge :)");
      Tooltip.install(underTenBadge, underTenMessage);
    } else {
      // Sets gold under ten seconds wins badge
      currentUser.setUnderTenBadge("/images/under10_gold.png");
      underTenBadge.setImage(new Image(currentUser.getUnderTenBadge()));
      underTenMessage.setText(
          "Good job! \nYou have earned all the badges for under 10 seconds badge :)");
      Tooltip.install(underTenBadge, underTenMessage);
    }

    // Update zen mode badge based on the number of games played
    // Also when hover, display message of the badge
    if (currentUser.getNumberOfZenPlayed() < 3) {
      // Sets bronze disabled mode badge
      currentUser.setZenBadge("/images/zen_opacity80.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
      zenMessage.setText("Zen mode badge \nPlay three rounds of zen mode to earn this badge :)");
      Tooltip.install(zenBadge, zenMessage);
    } else if (currentUser.getNumberOfZenPlayed() >= 3 && currentUser.getNumberOfZenPlayed() < 10) {
      // Sets bronze zen mode badge
      currentUser.setZenBadge("/images/zen_bronze.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
      zenMessage.setText("Good job! \nPlay ten rounds of zen mode to earn this badge :)");
      Tooltip.install(zenBadge, zenMessage);
    } else if (currentUser.getNumberOfZenPlayed() >= 10
        && currentUser.getNumberOfZenPlayed() < 50) {
      // Sets silver zen mode badge
      currentUser.setZenBadge("/images/zen_silver.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
      zenMessage.setText("Good job! \nPlay fifty rounds of zen mode to earn this badge :)");
      Tooltip.install(zenBadge, zenMessage);
    } else {
      // Sets gold zen mode badge
      currentUser.setZenBadge("/images/zen_gold.png");
      zenBadge.setImage(new Image(currentUser.getZenBadge()));
      zenMessage.setText("Good job! \nYou have earned all the badges for zen mode :)");
      Tooltip.install(zenBadge, zenMessage);
    }

    // Gets the number of consecutive wins for the user
    int numberOfConsecutiveWins = 0;
    for (int i = 0; i < currentUser.getWonOrLostHistory().size(); i++) {
      if (wonOrLostHistory.get(i).equals("won")) {
        numberOfConsecutiveWins++;
      } else if (wonOrLostHistory.get(i).equals("lost")) {
        numberOfConsecutiveWins = 0;
      }
    }

    // Update consecutive wins badge based on the number of games won consecutively
    // Also when hover, display message of the badge
    if (numberOfConsecutiveWins < 3) {
      currentUser.setConsecutiveWinsBadge("/images/consecutiveWins_opacity80.png");
      consecutiveWinsBadge.setImage(new Image(currentUser.getConsecutiveWinsBadge()));
      consecutiveWinsMessage.setText(
          "Consecutive wins badge \nTry to win three rounds consecutively to earn the bronze badge :)");
      Tooltip.install(consecutiveWinsBadge, consecutiveWinsMessage);
    } else if (numberOfConsecutiveWins < 10) {
      currentUser.setConsecutiveWinsBadge("/images/consecutiveWins_bronze.png");
      consecutiveWinsBadge.setImage(new Image(currentUser.getConsecutiveWinsBadge()));
      consecutiveWinsMessage.setText(
          "Good job! \nTry to win ten rounds consecutively to earn the silver badge :)");
      Tooltip.install(consecutiveWinsBadge, consecutiveWinsMessage);
    } else if (numberOfConsecutiveWins < 50) {
      currentUser.setConsecutiveWinsBadge("/images/consecutiveWins_silver.png");
      consecutiveWinsBadge.setImage(new Image(currentUser.getConsecutiveWinsBadge()));
      consecutiveWinsMessage.setText(
          "Well done! \nTry to win fifty rounds consecutively to earn the gold badge :)");
      Tooltip.install(consecutiveWinsBadge, consecutiveWinsMessage);
    } else {
      currentUser.setConsecutiveWinsBadge("/images/consecutiveWinsgold.png");
      consecutiveWinsBadge.setImage(new Image(currentUser.getConsecutiveWinsBadge()));
      consecutiveWinsMessage.setText(
          "Good job! \nYou have earned all the badges for consecutive wins :)");
      Tooltip.install(consecutiveWinsBadge, consecutiveWinsMessage);
    }
  }
}
