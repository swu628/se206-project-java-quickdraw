package nz.ac.auckland.se206;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nz.ac.auckland.se206.profile.User;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  private static User currentUser;
  private static MainMenuController mainMenuController;
  private static GameMenuController gameMenuController;
  private static StatisticsController statisticsController;
  private static SettingsController settingsController;
  private static GameController gameController;
  private static ModeSelectController modeSelectController;

  public static void main(final String[] args) {
    launch();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
    Parent parent = loader.load();

    switch (fxml) {
      case "mainMenu":
        mainMenuController = loader.getController();
        break;
      case "gameMenu":
        gameMenuController = loader.getController();
        break;
      case "statistics":
        statisticsController = loader.getController();
        break;
      case "settings":
        settingsController = loader.getController();
        break;
      case "game":
        gameController = loader.getController();
        break;
      case "modeSelect":
        modeSelectController = loader.getController();
        break;
    }

    return parent;
  }

  public static void setCurrentUser(User user) {
    currentUser = user;
  }

  public static User getCurrentUser() {
    return currentUser;
  }

  public static GameMenuController getGameMenuController() {
    return gameMenuController;
  }

  public static StatisticsController getStatisticsController() {
    return statisticsController;
  }

  public static SettingsController getSettingsController() {
    return settingsController;
  }

  public static GameController getGameController() {
    return gameController;
  }

  /**
   * Gets the main menu fxml controller
   *
   * @return main menu controller
   */
  public static MainMenuController getMainMenuController() {
    return mainMenuController;
  }

  public static ModeSelectController getModeSelectController() {
    return modeSelectController;
  }

  /**
   * This method loads all the categories in the category_difficulty csv
   *
   * @throws IOException when category_difficulty file doesn't exist
   */
  private void loadCategories() throws IOException {
    // Loads all the categories from category_difficulty.csv and splits them into
    // lists of Easy,
    // Medium and Hard
    Scanner s = new Scanner(new File("src/main/resources/category_difficulty.csv"));

    while (s.hasNext()) {
      String[] splitRow = s.nextLine().split(",");

      switch (splitRow[1].trim()) {
        case "E":
          CategoryManager.addEasy(splitRow[0].trim());
          break;
        case "M":
          CategoryManager.addMedium(splitRow[0].trim());
          break;
        case "H":
          CategoryManager.addHard(splitRow[0].trim());
          break;
      }
    }
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   * @throws IOException If "src/main/resources/fxml/canvas.fxml" is not found.
   */
  @Override
  public void start(final Stage stage) throws IOException {
    // Sets initial conditions for the game
    loadCategories();
    SceneManager.addUi(SceneManager.AppScene.MAIN_MENU, loadFxml("mainMenu"));
    SceneManager.addUi(SceneManager.AppScene.REGISTER, loadFxml("register"));
    SceneManager.addUi(SceneManager.AppScene.GAME_MENU, loadFxml("gameMenu"));
    SceneManager.addUi(SceneManager.AppScene.STATISTICS, loadFxml("statistics"));
    SceneManager.addUi(SceneManager.AppScene.SETTINGS, loadFxml("settings"));
    SceneManager.addUi(SceneManager.AppScene.GAME, loadFxml("game"));
    SceneManager.addUi(SceneManager.AppScene.MODE_SELECT, loadFxml("modeSelect"));

    // Sets application icon
    stage.getIcons().add(new Image("/images/icon.png"));

    final Scene scene =
        new Scene(SceneManager.getUiRoot(SceneManager.AppScene.MAIN_MENU), 960, 720);

    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

    AudioController.playBackgroundMusic();
  }
}
