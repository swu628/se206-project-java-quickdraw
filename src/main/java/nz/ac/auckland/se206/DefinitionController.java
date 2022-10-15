package nz.ac.auckland.se206;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class DefinitionController {
  @FXML private Label definitionTextLabel;

  /**
   * This method is called upon first load of the fxml. It sets the font to the custom IndieFlower
   * font.
   */
  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  /**
   * This method updates the scene by displaying the current definition of the current word
   *
   * @param definitionText
   */
  public void updateScene(String definitionText) {
    definitionTextLabel.setText(definitionText);
  }
}
