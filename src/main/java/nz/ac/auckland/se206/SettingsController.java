package nz.ac.auckland.se206;

import com.google.gson.Gson;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.profile.User;

public class SettingsController {
  public enum AccuracyDifficulty {
    EASY(3, "The player wins the game if the word to draw is within the top 3 guesses"),
    MEDIUM(2, "The player wins the game if the word to draw is within the top 2 guesses"),
    HARD(1, "The player wins the game if the word to draw is within the top 1 guess");

    private final int topNumberGuesses;
    private final String difficultyDescription;

    AccuracyDifficulty(int numGuesses, String description) {
      topNumberGuesses = numGuesses;
      difficultyDescription = description;
    }

    public int getNumGuesses() {
      return topNumberGuesses;
    }

    public String getDescription() {
      return difficultyDescription;
    }
  }

  public enum WordsDifficulty {
    EASY(
        new CategoryManager.Difficulty[] {CategoryManager.Difficulty.EASY},
        "Only words classified as EASY are selected"),
    MEDIUM(
        new CategoryManager.Difficulty[] {
          CategoryManager.Difficulty.EASY, CategoryManager.Difficulty.MEDIUM
        },
        "Only words classified as EASY or MEDIUM are selected"),
    HARD(
        new CategoryManager.Difficulty[] {
          CategoryManager.Difficulty.EASY,
          CategoryManager.Difficulty.MEDIUM,
          CategoryManager.Difficulty.HARD
        },
        "Only words classified as EASY, MEDIUM or HARD are selected"),
    MASTER(
        new CategoryManager.Difficulty[] {CategoryManager.Difficulty.HARD},
        "Only words classified as HARD are selected");

    private final CategoryManager.Difficulty[] difficultyDescriptions;
    private final String difficultyDescription;

    WordsDifficulty(CategoryManager.Difficulty[] difficulties, String description) {
      difficultyDescriptions = difficulties;
      difficultyDescription = description;
    }

    public CategoryManager.Difficulty[] getDifficulties() {
      return difficultyDescriptions;
    }

    public String getDescription() {
      return difficultyDescription;
    }
  }

  public enum TimeDifficulty {
    EASY(60, "The player gets 60 seconds to draw the selected word"),
    MEDIUM(45, "The player gets 45 seconds to draw the selected word"),
    HARD(30, "The player gets 30 seconds to draw the selected word"),
    MASTER(15, "The player gets 15 seconds to draw the selected word");

    private final int maxTime;
    private final String difficultyDescription;

    TimeDifficulty(int maxTime, String description) {
      this.maxTime = maxTime;
      difficultyDescription = description;
    }

    public int getMaxTime() {
      return maxTime;
    }

    public String getDescription() {
      return difficultyDescription;
    }
  }

  public enum ConfidenceDifficulty {
    EASY(1.0, "The confidence level of the correct prediction must be at least 1%"),
    MEDIUM(10.0, "The confidence level of the correct prediction must be at least 10%"),
    HARD(25.0, "The confidence level of the correct prediction must be at least 25%"),
    MASTER(50.0, "The confidence level of the correct prediction must be at least 50%");

    private final double minConfidence;
    private final String difficultyDescription;

    ConfidenceDifficulty(double minConfidence, String description) {
      this.minConfidence = minConfidence;
      difficultyDescription = description;
    }

    public double getMinConfidence() {
      return minConfidence;
    }

    public String getDescription() {
      return difficultyDescription;
    }
  }

  private User currentUser;
  private int accuracyDifficulty;
  private int wordsDifficulty;
  private int timeDifficulty;
  private int confidenceDifficulty;
  private List<AccuracyDifficulty> accuracyDifficultyList;
  private List<WordsDifficulty> wordsDifficultyList;
  private List<TimeDifficulty> timeDifficultyList;
  private List<ConfidenceDifficulty> confidenceDifficultyList;
  @FXML private Label accuracySettingsLabel;
  @FXML private Label wordsSettingsLabel;
  @FXML private Label timeSettingsLabel;
  @FXML private Label confidenceSettingsLabel;
  @FXML private Button accuracySelectLeft;
  @FXML private Button accuracySelectRight;
  @FXML private Button wordsSelectLeft;
  @FXML private Button wordsSelectRight;
  @FXML private Button timeSelectLeft;
  @FXML private Button timeSelectRight;
  @FXML private Button confidenceSelectLeft;
  @FXML private Button confidenceSelectRight;
  @FXML private Button accuracyHelp;
  @FXML private Button wordsHelp;
  @FXML private Button timeHelp;
  @FXML private Button confidenceHelp;
  @FXML private AnchorPane accuracyDescriptionPane;
  @FXML private AnchorPane wordDescriptionPane;
  @FXML private AnchorPane timeDescriptionPane;
  @FXML private AnchorPane confidenceDescriptionPane;
  @FXML private TextArea accuracyDescription;
  @FXML private TextArea wordDescription;
  @FXML private TextArea timeDescription;
  @FXML private TextArea confidenceDescription;
  @FXML private Slider musicSlider;
  @FXML private Slider sfxSlider;
  @FXML private Slider ttsSlider;
  @FXML private Button difficultyButton;
  @FXML private Button audioButton;
  @FXML private AnchorPane audioSettings;
  @FXML private AnchorPane difficultySettings;

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);

    // Creates arrays which store each difficulty so that the user can scroll through them
    accuracyDifficultyList = new ArrayList<>(Arrays.asList(AccuracyDifficulty.values()));
    wordsDifficultyList = new ArrayList<>(Arrays.asList(WordsDifficulty.values()));
    timeDifficultyList = new ArrayList<>(Arrays.asList(TimeDifficulty.values()));
    confidenceDifficultyList = new ArrayList<>(Arrays.asList(ConfidenceDifficulty.values()));

    // Enabling difficulty settings
    difficultyButton.setStyle("-fx-text-fill: black");
    difficultySettings.setVisible(true);
    difficultySettings.setDisable(false);

    // Disabling audio settings
    audioButton.setStyle("-fx-text-fill: grey");
    audioSettings.setVisible(false);
    audioSettings.setDisable(true);
  }

  /** This method changes settings to difficulty settings */
  @FXML
  private void changeToDifficulty() {
    AudioController.playPencilWrite();
    // Enabling difficulty settings
    difficultyButton.setStyle("-fx-text-fill: black");
    difficultySettings.setVisible(true);
    difficultySettings.setDisable(false);

    // Disabling audio settings
    audioButton.setStyle("-fx-text-fill: grey");
    audioSettings.setVisible(false);
    audioSettings.setDisable(true);
  }

  /** This method changes settings to audio settings */
  @FXML
  private void changeToAudio() {
    AudioController.playPencilWrite();
    // Disabling difficulty settings
    difficultyButton.setStyle("-fx-text-fill: grey");
    difficultySettings.setVisible(false);
    difficultySettings.setDisable(true);

    // Enabling audio settings
    audioButton.setStyle("-fx-text-fill: black");
    audioSettings.setVisible(true);
    audioSettings.setDisable(false);
  }

  /** This method changes the music volume. */
  @FXML
  public void onChangeMusicVolume() {
    AudioController.setMusicVolumeScale(musicSlider.getValue() / 100.0);
  }

  /** This method changes the sfx volume. */
  @FXML
  public void onChangeSfxVolume() {
    AudioController.setSfxVolumeScale(sfxSlider.getValue() / 100.0);
  }

  /**
   * This method sets up the scene's labels according to the specified user's saved difficulty
   * settings
   *
   * @param user the current user
   */
  public void setUpScene(User user) {
    currentUser = user;

    // Sets the difficulties to those specified by the user profile
    accuracyDifficulty = accuracyDifficultyList.indexOf(currentUser.getAccuracyDifficulty());
    wordsDifficulty = wordsDifficultyList.indexOf(currentUser.getWordsDifficulty());
    timeDifficulty = timeDifficultyList.indexOf(currentUser.getTimeDifficulty());
    confidenceDifficulty = confidenceDifficultyList.indexOf(currentUser.getConfidenceDifficulty());
    // Sets the difficulty labels to those specified by the user profile
    accuracySettingsLabel.setText(accuracyDifficultyList.get(accuracyDifficulty).toString());
    wordsSettingsLabel.setText(wordsDifficultyList.get(wordsDifficulty).toString());
    timeSettingsLabel.setText(timeDifficultyList.get(timeDifficulty).toString());
    confidenceSettingsLabel.setText(confidenceDifficultyList.get(confidenceDifficulty).toString());

    // Enables/disables the arrows depending on difficulty position in its respective array
    if (accuracyDifficulty == 0) {
      disableAccuracySelectLeft();
      enableAccuracySelectRight();
    } else if (accuracyDifficulty == accuracyDifficultyList.size() - 1) {
      disableAccuracySelectRight();
      enableAccuracySelectLeft();
    } else {
      enableAccuracySelectLeft();
      enableAccuracySelectRight();
    }

    // Enables/disables the arrows depending on difficulty position in its respective array
    if (wordsDifficulty == 0) {
      disableWordsSelectLeft();
      enableWordsSelectRight();
    } else if (wordsDifficulty == wordsDifficultyList.size() - 1) {
      disableWordsSelectRight();
      enableWordsSelectLeft();
    } else {
      enableWordsSelectLeft();
      enableWordsSelectRight();
    }

    // Enables/disables the arrows depending on difficulty position in its respective array
    if (timeDifficulty == 0) {
      disableTimeSelectLeft();
      enableTimeSelectRight();
    } else if (timeDifficulty == timeDifficultyList.size() - 1) {
      disableTimeSelectRight();
      enableTimeSelectLeft();
    } else {
      enableTimeSelectLeft();
      enableTimeSelectRight();
    }

    // Enables/disables the arrows depending on difficulty position in its respective array
    if (confidenceDifficulty == 0) {
      disableConfidenceSelectLeft();
      enableConfidenceSelectRight();
    } else if (confidenceDifficulty == confidenceDifficultyList.size() - 1) {
      disableConfidenceSelectRight();
      enableConfidenceSelectLeft();
    } else {
      enableConfidenceSelectLeft();
      enableConfidenceSelectRight();
    }

    // Disables all help panes
    accuracyDescriptionPane.setVisible(false);
    accuracyDescriptionPane.setDisable(true);
    wordDescriptionPane.setVisible(false);
    wordDescriptionPane.setDisable(true);
    timeDescriptionPane.setVisible(false);
    timeDescriptionPane.setDisable(true);
    confidenceDescriptionPane.setVisible(false);
    confidenceDescriptionPane.setDisable(true);
  }

  /** This method changes the difficulty to the previous difficulty in the accuracy array */
  @FXML
  private void onToPreviousAccuracyDifficulty() {
    AudioController.playPencilWrite();
    // Changes current accuracy difficulty to previous accuracy difficulty in list
    accuracyDifficulty -= 1;
    accuracySettingsLabel.setText(accuracyDifficultyList.get(accuracyDifficulty).toString());
    accuracyDescription.setText(accuracyDifficultyList.get(accuracyDifficulty).getDescription());
    enableAccuracySelectRight();

    // Disables the previous arrow if we are at first difficulty
    if (accuracyDifficulty == 0) {
      disableAccuracySelectLeft();
    }
  }

  /** This method changes the difficulty to the next difficulty in the accuracy array */
  @FXML
  private void onToNextAccuracyDifficulty() {
    AudioController.playPencilWrite();
    // Changes current accuracy difficulty to next accuracy difficulty in list
    accuracyDifficulty += 1;
    accuracySettingsLabel.setText(accuracyDifficultyList.get(accuracyDifficulty).toString());
    accuracyDescription.setText(accuracyDifficultyList.get(accuracyDifficulty).getDescription());
    enableAccuracySelectLeft();

    // Disables the next arrow if we are at last difficulty
    if (accuracyDifficulty == accuracyDifficultyList.size() - 1) {
      disableAccuracySelectRight();
    }
  }

  /** This method changes the difficulty to the previous difficulty in the word array */
  @FXML
  private void onToPreviousWordDifficulty() {
    AudioController.playPencilWrite();
    // Changes current word difficulty to previous word difficulty in list
    wordsDifficulty -= 1;
    wordsSettingsLabel.setText(wordsDifficultyList.get(wordsDifficulty).toString());
    wordDescription.setText(wordsDifficultyList.get(wordsDifficulty).getDescription());
    enableWordsSelectRight();

    // Disables the previous arrow if we are at first difficulty
    if (wordsDifficulty == 0) {
      disableWordsSelectLeft();
    }
  }

  /** This method changes the difficulty to the next difficulty in the word array */
  @FXML
  private void onToNextWordDifficulty() {
    AudioController.playPencilWrite();
    // Changes current word difficulty to next word difficulty in list
    wordsDifficulty += 1;
    wordsSettingsLabel.setText(wordsDifficultyList.get(wordsDifficulty).toString());
    wordDescription.setText(wordsDifficultyList.get(wordsDifficulty).getDescription());
    enableWordsSelectLeft();

    // Disables the next arrow if we are at last difficulty
    if (wordsDifficulty == wordsDifficultyList.size() - 1) {
      disableWordsSelectRight();
    }
  }

  /** This method changes the difficulty to the previous difficulty in the time array */
  @FXML
  private void onToPreviousTimeDifficulty() {
    AudioController.playPencilWrite();
    // Changes current time difficulty to previous time difficulty in list
    timeDifficulty -= 1;
    timeSettingsLabel.setText(timeDifficultyList.get(timeDifficulty).toString());
    timeDescription.setText(timeDifficultyList.get(timeDifficulty).getDescription());
    enableTimeSelectRight();

    // Disables the previous arrow if we are at first difficulty
    if (timeDifficulty == 0) {
      disableTimeSelectLeft();
    }
  }

  /** This method changes the difficulty to the next difficulty in the time array */
  @FXML
  private void onToNextTimeDifficulty() {
    AudioController.playPencilWrite();
    // Changes current time difficulty to next time difficulty in list
    timeDifficulty += 1;
    timeSettingsLabel.setText(timeDifficultyList.get(timeDifficulty).toString());
    timeDescription.setText(timeDifficultyList.get(timeDifficulty).getDescription());
    enableTimeSelectLeft();

    // Disables the next arrow if we are at last difficulty
    if (timeDifficulty == timeDifficultyList.size() - 1) {
      disableTimeSelectRight();
    }
  }

  /** This method changes the difficulty to the previous difficulty in the confidence array */
  @FXML
  private void onToPreviousConfidenceDifficulty() {
    AudioController.playPencilWrite();
    // Changes current confidence difficulty to previous confidence difficulty in list
    confidenceDifficulty -= 1;
    confidenceSettingsLabel.setText(confidenceDifficultyList.get(confidenceDifficulty).toString());
    confidenceDescription.setText(
        confidenceDifficultyList.get(confidenceDifficulty).getDescription());
    enableConfidenceSelectRight();

    // Disables the previous arrow if we are at first difficulty
    if (confidenceDifficulty == 0) {
      disableConfidenceSelectLeft();
    }
  }

  /** This method changes the difficulty to the next difficulty in the confidence array */
  @FXML
  private void onToNextConfidenceDifficulty() {
    AudioController.playPencilWrite();
    // Changes current confidence difficulty to next confidence difficulty in list
    confidenceDifficulty += 1;
    confidenceSettingsLabel.setText(confidenceDifficultyList.get(confidenceDifficulty).toString());
    confidenceDescription.setText(
        confidenceDifficultyList.get(confidenceDifficulty).getDescription());
    enableConfidenceSelectLeft();

    // Disables the next arrow if we are at last difficulty
    if (confidenceDifficulty == confidenceDifficultyList.size() - 1) {
      disableConfidenceSelectRight();
    }
  }

  /** This method enables the previous accuracy button */
  private void enableAccuracySelectLeft() {
    accuracySelectLeft.setDisable(false);
    accuracySelectLeft.setVisible(true);
  }

  /** This method disables the previous accuracy button */
  private void disableAccuracySelectLeft() {
    accuracySelectLeft.setDisable(true);
    accuracySelectLeft.setVisible(false);
  }

  /** This method enables the next accuracy difficulty button */
  private void enableAccuracySelectRight() {
    accuracySelectRight.setDisable(false);
    accuracySelectRight.setVisible(true);
  }

  /** This method disables the next accuracy difficulty button */
  private void disableAccuracySelectRight() {
    accuracySelectRight.setDisable(true);
    accuracySelectRight.setVisible(false);
  }

  /** This method enables the previous word difficulty button */
  private void enableWordsSelectLeft() {
    wordsSelectLeft.setDisable(false);
    wordsSelectLeft.setVisible(true);
  }

  /** This method disables the previous word difficulty button */
  private void disableWordsSelectLeft() {
    wordsSelectLeft.setDisable(true);
    wordsSelectLeft.setVisible(false);
  }

  /** This method enables the next word difficulty button */
  private void enableWordsSelectRight() {
    wordsSelectRight.setDisable(false);
    wordsSelectRight.setVisible(true);
  }

  /** This method disables the next word difficulty button */
  private void disableWordsSelectRight() {
    wordsSelectRight.setDisable(true);
    wordsSelectRight.setVisible(false);
  }

  /** This method enables the previous time difficulty button */
  private void enableTimeSelectLeft() {
    timeSelectLeft.setDisable(false);
    timeSelectLeft.setVisible(true);
  }

  /** This method disables the previous time difficulty button */
  private void disableTimeSelectLeft() {
    timeSelectLeft.setDisable(true);
    timeSelectLeft.setVisible(false);
  }

  /** This method enables the next time difficulty button */
  private void enableTimeSelectRight() {
    timeSelectRight.setDisable(false);
    timeSelectRight.setVisible(true);
  }

  /** This method disables the next time difficulty button */
  private void disableTimeSelectRight() {
    timeSelectRight.setDisable(true);
    timeSelectRight.setVisible(false);
  }

  /** This method enables the previous confidence difficulty button */
  private void enableConfidenceSelectLeft() {
    confidenceSelectLeft.setDisable(false);
    confidenceSelectLeft.setVisible(true);
  }

  /** This method disables the previous confidence difficulty button */
  private void disableConfidenceSelectLeft() {
    confidenceSelectLeft.setDisable(true);
    confidenceSelectLeft.setVisible(false);
  }

  /** This method enables the next confidence difficulty button */
  private void enableConfidenceSelectRight() {
    confidenceSelectRight.setDisable(false);
    confidenceSelectRight.setVisible(true);
  }

  /** This method disable the next confidence difficulty button */
  private void disableConfidenceSelectRight() {
    confidenceSelectRight.setDisable(true);
    confidenceSelectRight.setVisible(false);
  }

  /** This method toggles the accuracy help blurb */
  @FXML
  private void onGetAccuracyHelp() {
    AudioController.playPencilWrite();
    // Checks if help blurb is enabled/disabled and does the opposite
    if (accuracyDescriptionPane.isVisible()) {
      // Disables help blurb for respective accuracy difficulty
      accuracyDescriptionPane.setVisible(false);
      accuracyDescriptionPane.setDisable(true);
    } else {
      // Enables help blurb for respective accuracy difficulty
      accuracyDescription.setText(accuracyDifficultyList.get(accuracyDifficulty).getDescription());
      accuracyDescriptionPane.setVisible(true);
      accuracyDescriptionPane.setDisable(false);
    }
  }

  /** This method toggles the word help blurb */
  @FXML
  private void onGetWordsHelp() {
    AudioController.playPencilWrite();
    // Checks if help blurb is enabled/disabled and does the opposite
    if (wordDescriptionPane.isVisible()) {
      // Disables help blurb for respective accuracy difficulty
      wordDescriptionPane.setVisible(false);
      wordDescriptionPane.setDisable(true);
    } else {
      // Enables help blurb for respective word difficulty
      wordDescription.setText(wordsDifficultyList.get(wordsDifficulty).getDescription());
      wordDescriptionPane.setVisible(true);
      wordDescriptionPane.setDisable(false);
    }
  }

  /** This method toggles the time help blurb */
  @FXML
  private void onGetTimeHelp() {
    AudioController.playPencilWrite();
    // Checks if help blurb is enabled/disabled and does the opposite
    if (timeDescriptionPane.isVisible()) {
      // Disables help blurb for respective accuracy difficulty
      timeDescriptionPane.setVisible(false);
      timeDescriptionPane.setDisable(true);
    } else {
      // Enables help blurb for respective time difficulty
      timeDescription.setText(timeDifficultyList.get(timeDifficulty).getDescription());
      timeDescriptionPane.setVisible(true);
      timeDescriptionPane.setDisable(false);
    }
  }

  /** This method toggles the confidence help blurb */
  @FXML
  private void onGetConfidenceHelp() {
    AudioController.playPencilWrite();
    // Checks if help blurb is enabled/disabled and does the opposite
    if (confidenceDescriptionPane.isVisible()) {
      // Disables help blurb for respective accuracy difficulty
      confidenceDescriptionPane.setVisible(false);
      confidenceDescriptionPane.setDisable(true);
    } else {
      // Enables help blurb for respective confidence difficulty
      confidenceDescription.setText(
          confidenceDifficultyList.get(confidenceDifficulty).getDescription());
      confidenceDescriptionPane.setVisible(true);
      confidenceDescriptionPane.setDisable(false);
    }
  }

  /**
   * This method saves the user's difficulties and then changes the scene to the main menu.
   *
   * @param e the action event that triggered this method
   * @throws IOException if the user profile does not exist
   */
  @FXML
  private void onGameMenu(ActionEvent e) throws IOException {
    AudioController.playButtonClick();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();

    // Saves the difficulties to the user profile
    currentUser.setAccuracyDifficulty(accuracyDifficultyList.get(accuracyDifficulty));
    currentUser.setWordsDifficulty(wordsDifficultyList.get(wordsDifficulty));
    currentUser.setTimeDifficulty(timeDifficultyList.get(timeDifficulty));
    currentUser.setConfidenceDifficulty(confidenceDifficultyList.get(confidenceDifficulty));

    // Create json file named as the username
    FileWriter fileWriter =
        new FileWriter("src/main/resources/UserProfiles/" + currentUser.getName() + ".json");

    // Write user details into the file
    Gson gson = new Gson();
    gson.toJson(currentUser, fileWriter);
    fileWriter.close();

    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }
}
