package nz.ac.auckland.se206;

import java.util.Objects;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioController {
  private static MediaPlayer mediaPlayer;
  private static Media buttonClickSound =
      new Media(
          Objects.requireNonNull(
                  AudioController.class.getResource("/sounds/mixkit-page-turn-single-1104.wav"))
              .toExternalForm());
  // Carefree Kevin MacLeod (incompetech.com)
  // Licensed under Creative Commons: By Attribution 4.0 License
  // http://creativecommons.org/licenses/by/4.0/
  // Music promoted on https://www.chosic.com/free-music/all/
  private static Media backgroundMusic =
      new Media(
          Objects.requireNonNull(AudioController.class.getResource("/sounds/Carefree.mp3"))
              .toExternalForm());

  public static void playButtonClick() {
    Thread buttonClickThread =
        new Thread(
            () -> {
              mediaPlayer = new MediaPlayer(buttonClickSound);
              // Plays the button click sound
              mediaPlayer.setVolume(2);
              mediaPlayer.play();
            });

    buttonClickThread.start();
  }

  public static void playBackgroundMusic() {
    Thread backgroundMusicThread =
        new Thread(
            () -> {
              mediaPlayer = new MediaPlayer(backgroundMusic);
              // Keeps the song looping
              mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
              mediaPlayer.setVolume(0.3);
              mediaPlayer.play();
            });

    backgroundMusicThread.start();
  }
}
