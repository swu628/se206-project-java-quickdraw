package nz.ac.auckland.se206;

import java.util.HashMap;
import javafx.scene.Parent;

public class SceneManager {

  public enum AppScene {
    MAIN_MENU,
    REGISTER,
    GAME
  }

  private static HashMap<AppScene, Parent> sceneMap = new HashMap<>();

  public static void addUi(AppScene appScene, Parent sceneRoot) {
    sceneMap.put(appScene, sceneRoot);
  }

  public static Parent getUiRoot(AppScene appScene) {
    return sceneMap.get(appScene);
  }
}
