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
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import nz.ac.auckland.se206.profile.User;

public class DifficultySettingsController {
  public enum AccuracyDifficulty {
    EASY(
        3,
        "During EASY difficulty, the player wins the game if the word to draw is within the top 3 guesses"),
    MEDIUM(
        2,
        "During MEDIUM difficulty, the player wins the game if the word to draw is within the top 2 guesses"),
    HARD(
        1,
        "During HARD difficulty, the player wins the game if the word to draw is within the top 1 guess");

    private final int TOP_NUM_GUESSES;
    private final String DESCRIPTION;

    AccuracyDifficulty(int numGuesses, String description) {
      TOP_NUM_GUESSES = numGuesses;
      DESCRIPTION = description;
    }

    public int getNumGuesses() {
      return TOP_NUM_GUESSES;
    }

    public String getDescription() {
      return DESCRIPTION;
    }
  }

  public enum WordsDifficulty {
    EASY(
        new CategoryManager.Difficulty[] {CategoryManager.Difficulty.EASY},
        "During EASY difficulty, only words classified as EASY are selected"),
    MEDIUM(
        new CategoryManager.Difficulty[] {
          CategoryManager.Difficulty.EASY, CategoryManager.Difficulty.MEDIUM
        },
        "During MEDIUM difficulty, only words classified as EASY or MEDIUM are selected"),
    HARD(
        new CategoryManager.Difficulty[] {
          CategoryManager.Difficulty.EASY,
          CategoryManager.Difficulty.MEDIUM,
          CategoryManager.Difficulty.HARD
        },
        "During HARD difficulty, only words classified as EASY, MEDIUM or HARD are selected"),
    MASTER(
        new CategoryManager.Difficulty[] {CategoryManager.Difficulty.HARD},
        "During MASTER difficulty, only words classified as HARD are selected");

    private final CategoryManager.Difficulty[] DIFFICULTIES;
    private final String DESCRIPTION;

    WordsDifficulty(CategoryManager.Difficulty[] difficulties, String description) {
      DIFFICULTIES = difficulties;
      DESCRIPTION = description;
    }

    public CategoryManager.Difficulty[] getDifficulties() {
      return DIFFICULTIES;
    }

    public String getDescription() {
      return DESCRIPTION;
    }
  }

  public enum TimeDifficulty {
    EASY(60, "During EASY difficulty, the player gets 60 seconds to draw the selected word"),
    MEDIUM(45, "During MEDIUM difficulty, the player gets 45 seconds to draw the selected word"),
    HARD(30, "During HARD difficulty, the player gets 30 seconds to draw the selected word"),
    MASTER(15, "During MASTER difficulty, the player gets 15 seconds to draw the selected word");

    private final int MAX_TIME;
    private final String DESCRIPTION;

    TimeDifficulty(int maxTime, String description) {
      MAX_TIME = maxTime;
      DESCRIPTION = description;
    }

    public int getMaxTime() {
      return MAX_TIME;
    }

    public String getDescription() {
      return DESCRIPTION;
    }
  }

  public enum ConfidenceDifficulty {
    EASY(
        1.0,
        "During EASY difficulty, the confidence level of the correct prediction must be at least 1%"),
    MEDIUM(
        10.0,
        "During MEDIUM difficulty, the confidence level of the correct prediction must be at least 10%"),
    HARD(
        25.0,
        "During HARD difficulty, the confidence level of the correct prediction must be at least 25%"),
    MASTER(
        50.0,
        "During MASTER difficulty, the confidence level of the correct prediction must be at least 50%");

    private final double MIN_CONFIDENCE;
    private final String DESCRIPTION;

    ConfidenceDifficulty(double minConfidence, String description) {
      MIN_CONFIDENCE = minConfidence;
      DESCRIPTION = description;
    }

    public double getMinConfidence() {
      return MIN_CONFIDENCE;
    }

    public String getDescription() {
      return DESCRIPTION;
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
  @FXML private TextArea settingDescription;
  @FXML private AnchorPane settingDescriptionPane;

  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);

    // Creates arrays which store each difficulty so that the user can scroll through them
    accuracyDifficultyList = new ArrayList<>(Arrays.asList(AccuracyDifficulty.values()));
    wordsDifficultyList = new ArrayList<>(Arrays.asList(WordsDifficulty.values()));
    timeDifficultyList = new ArrayList<>(Arrays.asList(TimeDifficulty.values()));
    confidenceDifficultyList = new ArrayList<>(Arrays.asList(ConfidenceDifficulty.values()));
  }

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

    settingDescriptionPane.setVisible(false);
    settingDescriptionPane.setDisable(true);
  }

  @FXML
  private void onToPreviousAccuracyDifficulty() {
    // Changes current accuracy difficulty to previous accuracy difficulty in list
    accuracyDifficulty -= 1;
    accuracySettingsLabel.setText(accuracyDifficultyList.get(accuracyDifficulty).toString());
    enableAccuracySelectRight();

    if (accuracyDifficulty == 0) {
      disableAccuracySelectLeft();
    }
  }

  @FXML
  private void onToNextAccuracyDifficulty() {
    // Changes current accuracy difficulty to next accuracy difficulty in list
    accuracyDifficulty += 1;
    accuracySettingsLabel.setText(accuracyDifficultyList.get(accuracyDifficulty).toString());
    enableAccuracySelectLeft();

    if (accuracyDifficulty == accuracyDifficultyList.size() - 1) {
      disableAccuracySelectRight();
    }
  }

  @FXML
  private void onToPreviousWordDifficulty() {
    // Changes current word difficulty to previous word difficulty in list
    wordsDifficulty -= 1;
    wordsSettingsLabel.setText(wordsDifficultyList.get(wordsDifficulty).toString());
    enableWordsSelectRight();

    if (wordsDifficulty == 0) {
      disableWordsSelectLeft();
    }
  }

  @FXML
  private void onToNextWordDifficulty() {
    // Changes current word difficulty to next word difficulty in list
    wordsDifficulty += 1;
    wordsSettingsLabel.setText(wordsDifficultyList.get(wordsDifficulty).toString());
    enableWordsSelectLeft();

    if (wordsDifficulty == wordsDifficultyList.size() - 1) {
      disableWordsSelectRight();
    }
  }

  @FXML
  private void onToPreviousTimeDifficulty() {
    // Changes current time difficulty to previous time difficulty in list
    timeDifficulty -= 1;
    timeSettingsLabel.setText(timeDifficultyList.get(timeDifficulty).toString());
    enableTimeSelectRight();

    if (timeDifficulty == 0) {
      disableTimeSelectLeft();
    }
  }

  @FXML
  private void onToNextTimeDifficulty() {
    // Changes current time difficulty to next time difficulty in list
    timeDifficulty += 1;
    timeSettingsLabel.setText(timeDifficultyList.get(timeDifficulty).toString());
    enableTimeSelectLeft();

    if (timeDifficulty == timeDifficultyList.size() - 1) {
      disableTimeSelectRight();
    }
  }

  @FXML
  private void onToPreviousConfidenceDifficulty() {
    // Changes current confidence difficulty to previous confidence difficulty in list
    confidenceDifficulty -= 1;
    confidenceSettingsLabel.setText(confidenceDifficultyList.get(confidenceDifficulty).toString());
    enableConfidenceSelectRight();

    if (confidenceDifficulty == 0) {
      disableConfidenceSelectLeft();
    }
  }

  @FXML
  private void onToNextConfidenceDifficulty() {
    // Changes current confidence difficulty to next confidence difficulty in list
    confidenceDifficulty += 1;
    confidenceSettingsLabel.setText(confidenceDifficultyList.get(confidenceDifficulty).toString());
    enableConfidenceSelectLeft();

    if (confidenceDifficulty == confidenceDifficultyList.size() - 1) {
      disableConfidenceSelectRight();
    }
  }

  private void enableAccuracySelectLeft() {
    accuracySelectLeft.setDisable(false);
    accuracySelectLeft.setVisible(true);
  }

  private void disableAccuracySelectLeft() {
    accuracySelectLeft.setDisable(true);
    accuracySelectLeft.setVisible(false);
  }

  private void enableAccuracySelectRight() {
    accuracySelectRight.setDisable(false);
    accuracySelectRight.setVisible(true);
  }

  private void disableAccuracySelectRight() {
    accuracySelectRight.setDisable(true);
    accuracySelectRight.setVisible(false);
  }

  private void enableWordsSelectLeft() {
    wordsSelectLeft.setDisable(false);
    wordsSelectLeft.setVisible(true);
  }

  private void disableWordsSelectLeft() {
    wordsSelectLeft.setDisable(true);
    wordsSelectLeft.setVisible(false);
  }

  private void enableWordsSelectRight() {
    wordsSelectRight.setDisable(false);
    wordsSelectRight.setVisible(true);
  }

  private void disableWordsSelectRight() {
    wordsSelectRight.setDisable(true);
    wordsSelectRight.setVisible(false);
  }

  private void enableTimeSelectLeft() {
    timeSelectLeft.setDisable(false);
    timeSelectLeft.setVisible(true);
  }

  private void disableTimeSelectLeft() {
    timeSelectLeft.setDisable(true);
    timeSelectLeft.setVisible(false);
  }

  private void enableTimeSelectRight() {
    timeSelectRight.setDisable(false);
    timeSelectRight.setVisible(true);
  }

  private void disableTimeSelectRight() {
    timeSelectRight.setDisable(true);
    timeSelectRight.setVisible(false);
  }

  private void enableConfidenceSelectLeft() {
    confidenceSelectLeft.setDisable(false);
    confidenceSelectLeft.setVisible(true);
  }

  private void disableConfidenceSelectLeft() {
    confidenceSelectLeft.setDisable(true);
    confidenceSelectLeft.setVisible(false);
  }

  private void enableConfidenceSelectRight() {
    confidenceSelectRight.setDisable(false);
    confidenceSelectRight.setVisible(true);
  }

  private void disableConfidenceSelectRight() {
    confidenceSelectRight.setDisable(true);
    confidenceSelectRight.setVisible(false);
  }

  @FXML
  private void onGetAccuracyHelp() {
    // Enables help blurb for respective accuracy difficulty
    settingDescription.setText(accuracyDifficultyList.get(accuracyDifficulty).getDescription());
    settingDescriptionPane.setVisible(true);
    settingDescriptionPane.setDisable(false);
  }

  @FXML
  private void onGetWordsHelp() {
    // Enables help blurb for respective word difficulty
    settingDescription.setText(wordsDifficultyList.get(wordsDifficulty).getDescription());
    settingDescriptionPane.setVisible(true);
    settingDescriptionPane.setDisable(false);
  }

  @FXML
  private void onGetTimeHelp() {
    // Enables help blurb for respective time difficulty
    settingDescription.setText(timeDifficultyList.get(timeDifficulty).getDescription());
    settingDescriptionPane.setVisible(true);
    settingDescriptionPane.setDisable(false);
  }

  @FXML
  private void onGetConfidenceHelp() {
    // Enables help blurb for respective confidence difficulty
    settingDescription.setText(confidenceDifficultyList.get(confidenceDifficulty).getDescription());
    settingDescriptionPane.setVisible(true);
    settingDescriptionPane.setDisable(false);
  }

  @FXML
  private void onGameMenu(ActionEvent e) throws IOException {
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
