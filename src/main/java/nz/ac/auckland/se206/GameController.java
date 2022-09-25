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
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
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
  private static final int MAX_TIME = 60;

  @FXML private Canvas canvas;
  @FXML private Button clearButton;
  @FXML private Button penButton;
  @FXML private Button eraserButton;
  @FXML private Label categoryLabel;
  @FXML private Label preGameCategoryLabel;
  @FXML private AnchorPane preGamePane;
  @FXML private AnchorPane postGame;
  @FXML private Label postGameOutcomeLabel;
  @FXML private AnchorPane game;
  @FXML private Label timerLabel;
  @FXML private TextArea predictionsList;
  private GraphicsContext graphic;
  private DoodlePrediction model;
  private Thread timerThread;
  private Thread predictThread;
  private Boolean gameWon;
  private Boolean doPredict;

  // mouse coordinates
  private double currentX;
  private double currentY;

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

  public void updateScene() {
    // Chooses a random category for next game
    CategoryManager.setCategory(CategoryManager.Difficulty.EASY);
    preGameCategoryLabel.setText("Category: " + CategoryManager.getCategory());
    categoryLabel.setText("Category: " + CategoryManager.getCategory());

    onSwitchToPen();
    // Displays the pregame pane
    displayPreGame();
  }

  @FXML
  private void onResetGame() {
    // Clears the canvas
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    // Chooses a random category for next game
    CategoryManager.setCategory(CategoryManager.Difficulty.EASY);
    preGameCategoryLabel.setText("Category: " + CategoryManager.getCategory());
    categoryLabel.setText("Category: " + CategoryManager.getCategory());

    onSwitchToPen();
    // Shows the preGamePane whilst disabling all the other panes
    displayPreGame();
  }

  @FXML
  private void onStartDrawing() {
    // Sets initial conditions of game and shows game pane whilst disabling all the
    // other panes
    gameWon = false;
    doPredict = false;
    displayGame();

    // Creating timer task for tracking time player has left to draw
    Task<Void> timerTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            // Setting initial conditions for timer
            int timeLeft = MAX_TIME;
            double deltaTime = 0;
            long previousTimeMillis = System.currentTimeMillis();

            updateMessage("Time left: " + timeLeft + "s");
            // If there are less than 0 seconds or the game has been won, the timer will
            // stop
            while (timeLeft >= 0 && !gameWon) {
              // Calculating how many milliseconds has elapsed since last iteration of the
              // loop
              // And totaling the milliseconds elapsed
              long currentTimeMillis = System.currentTimeMillis();
              deltaTime += (currentTimeMillis - previousTimeMillis);
              previousTimeMillis = currentTimeMillis;
              // If 1000 milliseconds has elapsed, it has been 1 second, so timer is updated
              if (deltaTime >= 1000) {
                timeLeft--;
                updateMessage("Time left: " + timeLeft + "s");
                deltaTime -= 1000;
              }
            }

            final int timeTaken = 60 - timeLeft;

            Platform.runLater(
                () -> {
                  setPostGame(timeTaken);
                });

            return null;
          }
        };

    Task<Void> predictTask =
        new Task<Void>() {

          @Override
          protected Void call() throws Exception {
            // Setting initial conditions for prediction timer
            double deltaTime = 0;
            long previousTimeMillis = System.currentTimeMillis();

            // Setting intial conditions for predictior
            TextToSpeech textToSpeech = new TextToSpeech();
            String prevTopPred = "";
            String currentTopPred = "";

            // If timerThread is alive, then 60s has not passed
            while (!gameWon && timerThread.isAlive()) {
              // Calculating how many milliseconds has elapsed since last iteration of the
              // loop
              // And totaling the milliseconds elapsed
              long currentTimeMillis = System.currentTimeMillis();
              deltaTime += (currentTimeMillis - previousTimeMillis);
              previousTimeMillis = currentTimeMillis;
              // If 1000 milliseconds has elapsed, it has been 1 second, so the DL model is
              // queried
              if (deltaTime >= 1000) {
                if (doPredict) {
                  // Uses game thread to get current snapshot of canvas
                  // Returns to predictThread to query DL model
                  FutureTask<BufferedImage> snapshotTask =
                      new FutureTask<BufferedImage>((Callable) () -> getCurrentSnapshot());
                  Platform.runLater(snapshotTask);
                  java.util.List<Classifications.Classification> predictions =
                      model.getPredictions(snapshotTask.get(), 10);

                  // Creates a string which lists the top 10 DL predictions and updates the
                  // predictionList string
                  StringBuilder sb = new StringBuilder();
                  int i = 1;
                  for (Classifications.Classification c : predictions) {
                    StringBuilder stringBuilder = new StringBuilder(c.getClassName());

                    for (int j = 0; j < c.getClassName().length(); j++) {
                      if (stringBuilder.charAt(j) == '_') {
                        stringBuilder.setCharAt(j, ' ');
                      }
                    }

                    String currentPred = stringBuilder.toString();

                    if (i == 1) {
                      currentTopPred = currentPred;
                      sb.append(i).append(". ").append(currentPred).append(System.lineSeparator());
                    } else {
                      sb.append(i).append(". ").append(currentPred).append(System.lineSeparator());
                    }
                    if (currentPred.equals(CategoryManager.getCategory()) && i <= 3) {
                      gameWon = true;
                    }
                    i++;
                  }
                  updateMessage(sb.toString());
                  // If there has been a change to the top 1 prediciton, the text-to-speech will
                  // say
                  // what it sees
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
    timerLabel.textProperty().bind(timerTask.messageProperty());
    predictionsList.textProperty().bind(predictTask.messageProperty());

    timerThread = new Thread(timerTask);
    predictThread = new Thread(predictTask);
    timerThread.setDaemon(true);
    predictThread.setDaemon(true);

    timerThread.start();
    predictThread.start();
  }

  private void setPostGame(int timeTaken) {
    // Delegating text-to-speech to background thread to avoid GUI freeze
    Task<Void> ttsTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            TextToSpeech tts = new TextToSpeech();
            tts.speak(postGameOutcomeLabel.getText());

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
              if (user.getFastestWon() > timeTaken || user.getFastestWon() == -1) {
                user.setFastestWon(timeTaken);
              }
            } else {
              user.setGamesLost(user.getGamesLost() + 1);
            }

            // Updates the user's history of words encountered
            ArrayList<String> wordsHistory = user.getWordsHistory();

            wordsHistory.add(CategoryManager.getCategory());

            user.setWordsHistory(wordsHistory);

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

    if (gameWon) {
      postGameOutcomeLabel.setText("You won!");
    } else {
      postGameOutcomeLabel.setText("You lost!");
    }

    ttsThread.start();
    saveThread.start();

    // Sets the postGame pane to visible so save, play again and quit utilities are
    // available to the
    // player
    displayPostGame();
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    doPredict = false;
  }

  @FXML
  private void onSwitchToPen() {
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
          final double size = 5.0;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          graphic.setStroke(Color.BLACK);
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  @FXML
  private void onSwitchToEraser() {
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size
          final double size = 8;

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

  @FXML
  private void onSaveDrawing() throws IOException {
    // Opens a new file explorer window for user to save the image at their chosen
    // location
    // Will only save if the extension is a .jpg or .png
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters()
        .addAll(
            new FileChooser.ExtensionFilter("PNG Files", "*.png"),
            new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));
    File filePath = fc.showSaveDialog(null);
    if (filePath != null) {
      File file = new File(filePath.getAbsolutePath());
      String fileName = filePath.getName();
      String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      if (fileExtension.equals("jpg") || fileExtension.equals("png")) {
        ImageIO.write(getCurrentSnapshot(), fileExtension, file);
      }
    }
  }

  @FXML
  private void onGameMenu(ActionEvent e) {
    // Resets the game and switches scene to the main menu
    onResetGame();
    Button button = (Button) e.getSource();
    Scene currentScene = button.getScene();
    currentScene.setRoot(SceneManager.getUiRoot(SceneManager.AppScene.GAME_MENU));
  }

  @FXML
  private void onQuitGame() {
    Platform.exit();
  }

  private void displayPreGame() {
    // Shows the preGamePane whilst disabling all the other panes
    preGamePane.setDisable(false);
    preGamePane.setVisible(true);
    postGame.setDisable(true);
    postGame.setVisible(false);
    game.setDisable(true);
    game.setVisible(false);
  }

  private void displayGame() {
    // Shows the gamePane whilst disabling all the other panes
    preGamePane.setDisable(true);
    preGamePane.setVisible(false);
    postGame.setDisable(true);
    postGame.setVisible(false);
    game.setDisable(false);
    game.setVisible(true);
  }

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
}
