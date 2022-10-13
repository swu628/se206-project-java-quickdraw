package nz.ac.auckland.se206;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class DefinitionController {
  @FXML private Label definitionTextLabel;

  public void initialize() {
    Font.loadFont(App.class.getResourceAsStream("/fonts/IndieFlower-Regular.ttf"), 100);
  }

  public void updateScene(String definitionText) {
    definitionTextLabel.setText(definitionText);
  }
}
