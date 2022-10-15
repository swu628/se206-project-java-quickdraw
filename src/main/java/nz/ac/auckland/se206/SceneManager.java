package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppScene {
    MAIN_MENU,
    REGISTER,
    GAME_MENU,
    STATISTICS,
    MODE_SELECT,
    DIFFICULTY_SETTINGS,
    GAME
  }

  private static HashMap<AppScene, Parent> sceneMap = new HashMap<>();

  /**
   * This method adds a specified scene to the scene map
   *
   * @param appScene the enum of the specificed scene
   * @param sceneRoot the Parent specified scene
   */
  public static void addUi(AppScene appScene, Parent sceneRoot) {
    sceneMap.put(appScene, sceneRoot);
  }

  /**
   * This method returns the Parent of the specified scene
   *
   * @param appScene the enum of the specified scene
   * @return the Parent specified scene
   */
  public static Parent getUiRoot(AppScene appScene) {
    return sceneMap.get(appScene);
  }
}
