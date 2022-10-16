package nz.ac.auckland.se206;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import com.google.gson.Gson;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.dict.DictionarySearch;
import nz.ac.auckland.se206.dict.WordEntry;
import nz.ac.auckland.se206.dict.WordInfo;
import nz.ac.auckland.se206.dict.WordNotFoundException;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profile.User;
import nz.ac.auckland.se206.speech.TextToSpeech;

/**
 * This is the controller of the canvas. You are free to modify this class and the corresponding
 * FXML file as you see fit. For example, you might no longer need the "Predict" button because the
 * DL model should be automatically queried in the background every second.
 *
 * <p>!! IMPORTANT !!
 *
 * <p>Although we added the scale of the image, you need to be careful when changing the size of the
 * drawable canvas and the brush size. If you make the brush too big or too small with respect to
 * the canvas size, the ML model will not work correctly. So be careful. If you make some changes in
 * the canvas and brush sizes, make sure that the prediction works fine.
 */
public class GameController {
  @FXML private Canvas canvas;
  @FXML private Button clearButton;
  @FXML private Button penButton;
  @FXML private Button eraserButton;
  @FXML private ImageView toolImage;
  @FXML private Label wordLabel;
  @FXML private Button getDefWindowButton;
  @FXML private Label preGameWordLabel;
  @FXML private Button startDrawButton;
  @FXML private AnchorPane preGamePane;
  @FXML private Button nextDefButton;
  @FXML private Button prevDefButton;
  @FXML private Button hintButton;
  @FXML private Label hintLabel;
  @FXML private Label hintStringLabel;
  @FXML private AnchorPane postGame;
  @FXML private Label postGameOutcomeLabel;
  @FXML private Label postGameHiddenWordLabel;
  @FXML private AnchorPane game;
  @FXML private Label timerLabel;
  @FXML private TextArea predictionsList;
  @FXML private Button exitButton;
  @FXML private Button saveButton;
  @FXML private ColorPicker colourPicker;
  @FXML private Label predDirectionLabel;
  private GraphicsContext graphic;
  private DoodlePrediction model;
  private Thread timerThread;
  private volatile Thread predictThread;
  private volatile int timeLeft;
  private volatile Boolean gameWon;
  private volatile Boolean doPredict;
  // mouse coordinates
  private double currentX;
  private double currentY;
  private Color colour;
  private String btnClicked;
  private boolean hiddenWordMode;
  private boolean isExitBtnClicked;
  private String currentWord;
  private String wordText;
  private int entryIndex;
  private int definitionIndex;
  private int hintIndex;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() throws ModelException, IOException {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
    graphic = canvas.getGraphicsContext2D();
    onSwitchToPen();
    model = new DoodlePrediction();
  }

  public void updateScene(boolean hiddenWordMode) {
    this.hiddenWordMode = hiddenWordMode;
    onResetGame();
  }

  /**
   * This method searches the definition from the given word input. Keeps track of current
   * definition to avoid reuse.
   *
   * @param word that is being searched in the dictionary
   * @param direction false for previous, true for next
   * @return string with the definition of the word
   */
  private String getDefinition(String word, boolean direction, boolean firstDefinition) {
    try {
      // Searches definitions for the given word in a dictionary
      WordInfo wordResult = DictionarySearch.searchWordInfo(word);
      List<WordEntry> entries = wordResult.getWordEntries();

      if (direction && !firstDefinition) {
        // Gets index position of next definition of the given word
        if (definitionIndex < entries.get(entryIndex).getDefinitions().size() - 1) {
          // Increases definition index as there are more definitions
          definitionIndex++;
        } else if (entryIndex < entries.size() - 1) {
          // Increases entry index as entry has run out of definitions
          entryIndex++;
          definitionIndex = 0;
        }

        if (entryIndex == entries.size() - 1
            && definitionIndex == entries.get(entryIndex).getDefinitions().size() - 1) {
          // We have run out of definitions, enable prev def button, disable next def button
          Platform.runLater(
              () -> {
                nextDefButton.setDisable(true);
                prevDefButton.setDisable(false);
              });
        } else {
          // Enable next def button and prev def button
          Platform.runLater(
              () -> {
                nextDefButton.setDisable(false);
                prevDefButton.setDisable(false);
              });
        }

      } else if (!firstDefinition) {
        // Gets index position of next definition of the given word
        if (definitionIndex > 0) {
          // Decreases definition index as there are more definitions
          definitionIndex--;
        } else if (entryIndex > 0) {
          // Decreases entry index as entry has run out of definitions
          entryIndex--;
          definitionIndex = entries.get(entryIndex).getDefinitions().size() - 1;
        }

        if (entryIndex == 0 && definitionIndex == 0) {
          // We have run out of definitions, disable prev def button, enable next def button
          Platform.runLater(
              () -> {
                nextDefButton.setDisable(false);
                prevDefButton.setDisable(true);
              });
        } else {
          // Enable next def button and prev def button
          Platform.runLater(
              () -> {
                nextDefButton.setDisable(false);
                prevDefButton.setDisable(false);
              });
        }
      } else {
        if (entryIndex == entries.size() - 1
            && definitionIndex == entries.get(entryIndex).getDefinitions().size() - 1) {
          // We have run out of definitions, enable prev def button, disable next def button
          Platform.runLater(
              () -> {
                nextDefButton.setDisable(true);
                prevDefButton.setDisable(true);
              });
        }
      }

      return entries.get(entryIndex).getDefinitions().get(definitionIndex);
    } catch (IOException e) {
      e.printStackTrace();
      return "Word not found";
    } catch (WordNotFoundException e) {
      return "Word not found";
    }
  }

  /**
   * This method resets the game by clearing the canvas and then setting up the labels for the next
   * game.
   */
  @FXML
  private void onResetGame() {
    // Clears the canvas
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    User currentUser = App.getCurrentUser();

    // Chooses a random category for next game
    // if hidden word or zen mode, the game can reuse words from words history
    Button button = (Button) ModeSelectController.getActionEvent().getSource();
    btnClicked = button.getText();
    CategoryManager.setWord(
        App.getCurrentUser().getWordsDifficulty(), hiddenWordMode || btnClicked.equals("Zen mode"));

    currentWord = CategoryManager.getWord();

    entryIndex = 0;
    definitionIndex = 0;
    hintIndex = 1;
    hintButton.setVisible(false);
    hintLabel.setVisible(false);
    hintStringLabel.setVisible(false);

    // Checks if hidden word mode is selected
    if (hiddenWordMode) {
      // Setting pregame background
      preGamePane.setStyle("-fx-background-image: url('/images/background-hiddenWord.png')");
      // Setting the pregame word label
      preGameWordLabel.setLayoutY(178);
      preGameWordLabel.setStyle("-fx-font-size: 35px");
      startDrawButton.setLayoutY(615);
      // Enabling the hidden word buttons
      nextDefButton.setVisible(true);
      prevDefButton.setDisable(true);
      prevDefButton.setVisible(true);
      hintLabel.setText("");
      hintStringLabel.setText("");
      hintButton.setText("Get hint");
      hintButton.setVisible(true);
      hintButton.setDisable(false);
      wordText = getDefinition(currentWord, true, true);
      // Checks if the current word has a definition
      while (wordText.equals("Word not found")) {
        CategoryManager.setWord(App.getCurrentUser().getWordsDifficulty(), true);
        currentWord = CategoryManager.getWord();
        wordText = getDefinition(currentWord, true, true);
      }
      wordLabel.setVisible(false);
      getDefWindowButton.setVisible(true);
    } else {
      // Setting pregame background
      preGamePane.setStyle("-fx-background-image: url('/images/background-nonHidden.png')");
      // Setting the pregame word label
      preGameWordLabel.setLayoutY(71);
      preGameWordLabel.setStyle("-fx-font-size: 45px");
      startDrawButton.setLayoutY(395);
      // disabling the hidden word mode buttons
      nextDefButton.setVisible(false);
      nextDefButton.setDisable(true);
      prevDefButton.setVisible(false);
      prevDefButton.setDisable(true);
      getDefWindowButton.setVisible(false);
      wordText = "Draw: " + currentWord;
      wordLabel.setVisible(true);
    }

    nextDefButton.setDisable(false);
    preGameWordLabel.setText(wordText);

    wordLabel.setText(wordText);
    predDirectionLabel.setText("");

    // Set pen colour to black
    if (colourPicker.isVisible()) {
      colourPicker.setValue(Color.BLACK);
    }

    // Set visibility of time label
    btnClicked = button.getText();

    onSwitchToPen();
    // Shows the preGamePane whilst disabling all the other panes
    AudioController.playButtonClick();
    displayPreGame();
  }

  /**
   * This method starts the game according to the user's specified difficulty settings and game
   * mode.
   *
   * @throws IOException if the current user profile doesn't exist
   */
  @FXML
  private void onStartDrawing() throws IOException {
    User currentUser = App.getCurrentUser();
    // Sets initial conditions of game and shows game pane whilst disabling all the
    // other panes
    gameWon = false;
    doPredict = false;
    timeLeft = currentUser.getTimeDifficulty().getMaxTime();
    int maxGuessNum = currentUser.getAccuracyDifficulty().getNumGuesses();
    double minConfidence = currentUser.getConfidenceDifficulty().getMinConfidence();
    onSwitchToPen();
    onClear();
    AudioController.playButtonClick();
    displayGame();

    // Checks if zen mode is selected
    if (btnClicked.equals("Zen mode")) {
      timerLabel.setVisible(false);
      exitButton.setVisible(true);
      saveButton.setVisible(true);
      colourPicker.setVisible(true);
      isExitBtnClicked = false;
      colour = Color.BLACK;

      // Increment the total number of zen mode played
      currentUser.setNumberOfZenPlayed();

      getPredictTask(maxGuessNum, minConfidence, currentUser);
    } else {
      timerLabel.setVisible(true);
      exitButton.setVisible(false);
      saveButton.setVisible(false);
      colourPicker.setVisible(false);
      colour = Color.BLACK;

      getTimerTask();
      getPredictTask(maxGuessNum, minConfidence, currentUser);
    }
  }

  /** This method will call the timer task; it will generate a count down timer */
  private void getTimerTask() {
    // Creating timer task for tracking time player has left to draw
    Task<Void> timerTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Setting initial conditions for timer
            double deltaTime = 0;
            long previousTimeMillis = System.currentTimeMillis();

            updateMessage("Time left: " + timeLeft + "s");
            // If there are less than 0 seconds or the game has been won, the timer will
            // stop
            while (timeLeft >= 0 && !gameWon) {
              // Calculating how many milliseconds has elapsed since last iteration of the
              // loop, and totaling the milliseconds elapsed
              long currentTimeMillis = System.currentTimeMillis();
              deltaTime += (currentTimeMillis - previousTimeMillis);
              previousTimeMillis = currentTimeMillis;
              // If 1000 milliseconds has elapsed, it has been 1 second, so timer is updated
              if (deltaTime >= 1000) {
                timeLeft--;
                if (timeLeft >= 0) {
                  updateMessage("Time left: " + timeLeft + "s");
                }
                deltaTime -= 1000;
              }
            }

            final int timeTaken = 60 - timeLeft;

            while (predictThread.isAlive()) {
              // Waiting for predict thread to die before going to post game scene.
            }

            Platform.runLater(
                () -> {
                  setPostGame(timeTaken);
                });

            return null;
          }
        };

    // Setting intitial conditions for the timer and prediction tasks and threads
    timerLabel.textProperty().bind(timerTask.messageProperty());
    timerThread = new Thread(timerTask);
    timerThread.setDaemon(true);
    timerThread.start();
  }

  /**
   * This is a helper method which will be used in the prediction task
   *
   * @return either true or false based on the mode chosen
   */
  private boolean getStatement() {
    boolean statement;

    // Checking if we should continue to predict
    if (btnClicked.equals("Zen mode")) {
      // In zen mode, stop if exit is pressed
      if (!isExitBtnClicked) {
        statement = true;
      } else {
        statement = false;
      }
    } else {
      // In normal mode, stop if time left is < 0 or game is won
      if (timeLeft >= 0 && !gameWon) {
        statement = true;
      } else {
        statement = false;
      }
    }
    return statement;
  }

  /**
   * This method will call the prediction task; it will generate the prediction list
   *
   * @param maxGuessNum depends on the difficulty chosen
   * @param minConfidence depends on the difficulty chosen
   * @param currentUser is the current account logged in
   */
  private void getPredictTask(int maxGuessNum, double minConfidence, User currentUser) {
    Task<Void> predictTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Setting initial conditions for prediction timer
            double deltaTime = 0;
            long previousTimeMillis = System.currentTimeMillis();

            // Setting initial conditions for prediction
            TextToSpeech textToSpeech = new TextToSpeech();
            boolean givenWarning = false;
            String prevTopPred = "";
            String currentTopPred = "";
            int prevPredPos = Integer.MAX_VALUE;

            while (getStatement()) {
              // Calculating how many milliseconds has elapsed since last iteration of the
              // loop, and totaling the milliseconds elapsed
              long currentTimeMillis = System.currentTimeMillis();
              deltaTime += (currentTimeMillis - previousTimeMillis);
              previousTimeMillis = currentTimeMillis;
              // If 1000 milliseconds has elapsed, it has been 1 second, so the DL model is
              // queried
              if (deltaTime >= 1000) {
                if (timeLeft <= 10 && !givenWarning) {
                  textToSpeech.speak("Less than 10 seconds remaining");
                  givenWarning = true;
                }
                if (doPredict) {
                  // Uses game thread to get current snapshot of canvas
                  // Returns to predictThread to query DL model
                  FutureTask<BufferedImage> snapshotTask =
                      new FutureTask<BufferedImage>((Callable) () -> getCurrentSnapshot());
                  Platform.runLater(snapshotTask);
                  List<Classifications.Classification> predictions =
                      model.getPredictions(snapshotTask.get(), 50);
                  // Creates a string which lists the top 10 DL predictions and updates the
                  // predictionList string
                  StringBuilder sb = new StringBuilder();
                  int currentPos = 1;
                  boolean wordFound = false;
                  for (Classifications.Classification c : predictions) {
                    StringBuilder stringBuilder = new StringBuilder(c.getClassName());

                    for (int j = 0; j < c.getClassName().length(); j++) {
                      if (stringBuilder.charAt(j) == '_') {
                        stringBuilder.setCharAt(j, ' ');
                      }
                    }

                    String currentPred = stringBuilder.toString();

                    // Getting confidence of each prediction
                    double percentage = c.getProbability() * 100;

                    if (currentPos == 1) {
                      currentTopPred = currentPred;
                      sb.append(currentPred)
                          .append(" - ")
                          .append(String.format("%.2f", percentage))
                          .append("%")
                          .append(System.lineSeparator());
                    } else if (currentPos <= 10) {
                      sb.append(currentPred)
                          .append(" - ")
                          .append(String.format("%.2f", percentage))
                          .append("%")
                          .append(System.lineSeparator());
                    }

                    // Checks if current prediction word is correct
                    if (currentPred.equals(CategoryManager.getWord())) {
                      wordFound = true;

                      if (currentPos <= maxGuessNum && percentage >= minConfidence) {
                        gameWon = true;
                      }

                      if (currentPos <= 10) {
                        Platform.runLater(
                            () -> {
                              predDirectionLabel.setText("in top 10");
                            });
                      } else {
                        // Checks if prediction is getting further or closer to top 10
                        if (currentPos < prevPredPos) {
                          Platform.runLater(
                              () -> {
                                predDirectionLabel.setText("getting CLOSER");
                              });

                        } else if (currentPos > prevPredPos) {
                          Platform.runLater(
                              () -> {
                                predDirectionLabel.setText("getting FURTHER");
                              });
                        }
                      }
                      prevPredPos = currentPos;
                    }
                    currentPos++;
                  }
                  if (!wordFound) {
                    Platform.runLater(
                        () -> {
                          predDirectionLabel.setText("getting FURTHER");
                        });
                  }
                  updateMessage(sb.toString());
                  // If there has been a change to the top 1 prediction, the text-to-speech will
                  // say what it sees
                  if (!prevTopPred.equals(currentTopPred)) {
                    prevTopPred = currentTopPred;
                    textToSpeech.speak("I see " + currentTopPred);
                  }
                }
                deltaTime -= 1000;
              }
            }

            return null;
          }
        };

    // Setting intitial conditions for the timer and prediction tasks and threads
    predictionsList.textProperty().bind(predictTask.messageProperty());
    predictThread = new Thread(predictTask);
    predictThread.setDaemon(true);
    predictThread.start();
  }

  /**
   * This method changes the scene to the post game scene. It will make text to speech say whether
   * the user won/loss. It will update the user's game statistics.
   *
   * @param timeTaken the time taken to finish the game
   */
  private void setPostGame(int timeTaken) {
    // Delegating text-to-speech to background thread to avoid GUI freeze
    Task<Void> ttsTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            TextToSpeech tts = new TextToSpeech();
            tts.speak(postGameOutcomeLabel.getText());
            tts.speak(postGameHiddenWordLabel.getText());

            return null;
          }
        };

    Task<Void> saveUserDataTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            User user = App.getCurrentUser();

            // Updates user total games won/lost statistic
            // If game won, checks if it is a new fastest won and updates if true
            if (gameWon) {
              user.setGamesWon(user.getGamesWon() + 1);
              ArrayList<String> wonOrLostHistory = user.getWonOrLostHistory();
              wonOrLostHistory.add("won");
              user.setWonOrLostHistory(wonOrLostHistory);
              if (user.getFastestWon() > timeTaken || user.getFastestWon() == -1) {
                user.setFastestWon(timeTaken);
              }
            } else {
              user.setGamesLost(user.getGamesLost() + 1);
              ArrayList<String> wonOrLostHistory = user.getWonOrLostHistory();
              wonOrLostHistory.add("lost");
              user.setWonOrLostHistory(wonOrLostHistory);
            }

            // Updates the user's history of words encountered
            ArrayList<String> wordsHistory = user.getWordsHistory();
            wordsHistory.add(CategoryManager.getWord());
            user.setWordsHistory(wordsHistory);

            // Updates the user's history of time taken
            ArrayList<Integer> timeTakenHistory = user.getTimeTakenHistory();
            timeTakenHistory.add(timeTaken);
            user.setTimeTakenHistory(timeTakenHistory);

            // Create json file named as the username
            FileWriter fileWriter =
                new FileWriter("src/main/resources/UserProfiles/" + user.getName() + ".json");

            // Write user details into the file
            Gson gson = new Gson();
            gson.toJson(user, fileWriter);
            fileWriter.close();

            return null;
          }
        };

    Thread ttsThread = new Thread(ttsTask);
    ttsThread.setDaemon(true);

    Thread saveThread = new Thread(saveUserDataTask);

    // Setting the post game labels
    String hiddenText = "";
    postGameOutcomeLabel.setLayoutY(162);
    if (hiddenWordMode) {
      hiddenText = "The word was: " + currentWord;
      // Moving game outcome label up to accomodate for hidden word label
      postGameOutcomeLabel.setLayoutY(107);
    }
    // Setting game outcome text
    postGameHiddenWordLabel.setText(hiddenText);
    if (gameWon) {
      postGameOutcomeLabel.setText("You won!");
    } else {
      postGameOutcomeLabel.setText("You lost!");
    }

    ttsThread.start();

    if (!hiddenWordMode) {
      saveThread.start();
    }

    // Sets the postGame pane to visible so save, play again and quit utilities are
    // available to the player
    displayPostGame();
  }

  /**
   * This method is called when the "Next meaning" button is pressed. Updates to a new definition
   * for the current word
   */
  @FXML
  private void onNextDef() {
    nextDefButton.setDisable(true);

    // Changes the definition of the current word being displayed
    Task<Object> getDefTask =
        new Task<>() {
          @Override
          protected Object call() throws Exception {
            // Getting definition of word
            String definition = getDefinition(currentWord, true, false);

            AudioController.playPencilWrite();

            // Sets the labels to the word's definitions
            Platform.runLater(
                () -> {
                  wordText = definition;
                  preGameWordLabel.setText(wordText);
                  wordLabel.setText(wordText);
                });

            return null;
          }
        };

    Thread getDefThread = new Thread(getDefTask);
    getDefThread.start();
  }

  /**
   * This method is called when the "Previous meaning" button is pressed. Updates to a new
   * definition for the current word
   */
  @FXML
  private void onPrevDef() {
    prevDefButton.setDisable(true);

    // Changes the definition of the current word being displayed
    Task<Object> getDefTask =
        new Task<>() {
          @Override
          protected Object call() throws Exception {
            // Getting definition of word
            String definition = getDefinition(currentWord, false, false);

            AudioController.playPencilWrite();

            // Sets the labels to the word's definitions
            Platform.runLater(
                () -> {
                  wordText = definition;
                  preGameWordLabel.setText(wordText);
                  wordLabel.setText(wordText);
                });

            return null;
          }
        };

    Thread getDefThread = new Thread(getDefTask);
    getDefThread.start();
  }

  /**
   * This method gets a hint by showing the next letter of the word currently displayed as the hint
   */
  @FXML
  private void onGetHint() {
    // Makes labels visible
    hintLabel.setVisible(true);
    hintStringLabel.setVisible(true);

    // Checks if the current character of the word is a space
    if (currentWord.charAt(hintIndex - 1) == ' ') {
      hintIndex++;
    }
    String hintText = currentWord.substring(0, hintIndex++);

    AudioController.playPencilWrite();
    // Checks if the whole word is shown
    if (hintIndex <= currentWord.length()) {
      hintLabel.setText("The word starts with:");
      hintStringLabel.setText(hintText);
      hintButton.setText("Get another hint");
    } else {
      hintLabel.setText("The word is:");
      hintStringLabel.setText(hintText);
      hintButton.setDisable(true);
      hintButton.setText("No more hints");
    }
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.setFill(Color.rgb(243, 243, 243));
    graphic.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    doPredict = false;
  }

  /** This method will switch to the pen */
  @FXML
  private void onSwitchToPen() {
    // Change tool and cursor image
    toolImage.setImage(new Image("/images/pen.png"));
    canvas.setCursor(new ImageCursor(new Image("/images/penCursor.png")));

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
          doPredict = true;
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size
          final double size = 7.0;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(colour);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  /** This method will switch to the eraser */
  @FXML
  private void onSwitchToEraser() {
    // Change tool and cursor image
    toolImage.setImage(new Image("/images/eraser.png"));
    Image eraserImage = new Image("/images/eraserCursor.png");
    canvas.setCursor(
        new ImageCursor(
            eraserImage, eraserImage.getHeight() * 0.125, eraserImage.getWidth() * 0.125));

    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size
          final double size = 12;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.rgb(243, 243, 243));
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  /**
   * This method will open a new window that displays the current word's definition
   *
   * @throws IOException if definition fxml doesn't exist
   */
  @FXML
  private void onGetDefWindow() throws IOException {
    // Creates a new window containing the word definition
    getDefWindowButton.setDisable(true);

    // Setting scene to hold definition
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/definition.fxml"));
    Parent parent = loader.load();
    DefinitionController definitionController = loader.getController();
    definitionController.updateScene(wordText);

    // Setting game icons and scene
    Stage stage = new Stage();
    Scene scene = new Scene(parent);
    stage.getIcons().add(new Image("/images/icon.png"));
    stage.setResizable(false);
    stage.setScene(scene);
    AudioController.playButtonClick();
    // Popping up window with definition
    stage.show();

    // Enabling get definition button on window close
    stage.setOnCloseRequest(
        new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
            getDefWindowButton.setDisable(false);
          }
        });
  }

  /**
   * This method will save the user's drawing without colour
   *
   * @throws IOException if file path specified doesn't exist
   */
  @FXML
  private void onSaveDrawing() throws IOException {
    // Opens a new file explorer window for user to save the image at their chosen
    // location. Will only save if the extension is a .jpg
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
    File filePath = fc.showSaveDialog(null);
    // Saves file if file path exists
    if (filePath != null) {
      File file = new File(filePath.getAbsolutePath());
      String fileName = filePath.getName();
      String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      if (fileExtension.equals("png")) {
        ImageIO.write(getCurrentSnapshot(), fileExtension, file);
      }
    }
  }

  /**
   * This method will save the user's drawing with colour
   *
   * @throws IOException if file path specified doesn't exist
   */
  @FXML
  private void onSaveDrawingColour() throws IOException {
    // Opens a new file explorer window for user to save the image at their chosen
    // location. Will only save if the extension is a .jpg
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
    File filePath = fc.showSaveDialog(null);
    if (filePath != null) {
      File file = new File(filePath.getAbsolutePath());
      String fileName = filePath.getName();
      String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      if (fileExtension.equals("png")) {
        ImageIO.write(getCurrentSnapshotColour(), fileExtension, file);
      }
    }
  }

  @FXML
  private void onColourPick() {
    colour = colourPicker.getValue();
  }

  /**
   * This method will change the scene to the game menu
   *
   * @param e the action event that triggered this method
   */
  @FXML
  private void onGameMenu(ActionEvent e) {
    // Plays button click sound
    AudioController.playButtonClick();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    // Sets the scene to game menu
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));

    if (button.getText().equals("Exit")) {
      isExitBtnClicked = true;
    }
  }

  /** This method quits the game */
  @FXML
  private void onQuitGame() {
    Platform.exit();
  }

  /** This method displays the pregame scene */
  private void displayPreGame() {
    // Shows the preGamePane whilst disabling all the other panes
    preGamePane.setDisable(false);
    preGamePane.setVisible(true);
    postGame.setDisable(true);
    postGame.setVisible(false);
    game.setDisable(true);
    game.setVisible(false);
  }

  /** This method displays the game scene */
  private void displayGame() {
    // Shows the gamePane whilst disabling all the other panes
    preGamePane.setDisable(true);
    preGamePane.setVisible(false);
    postGame.setDisable(true);
    postGame.setVisible(false);
    game.setDisable(false);
    game.setVisible(true);
  }

  /** This method displays the post game scene */
  private void displayPostGame() {
    // Shows the postGamePane whilst disabling all the other panes
    preGamePane.setDisable(true);
    preGamePane.setVisible(false);
    game.setDisable(true);
    game.setVisible(false);
    postGame.setDisable(false);
    postGame.setVisible(true);
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();

    return imageBinary;
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  private BufferedImage getCurrentSnapshotColour() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    return image;
  }
}
