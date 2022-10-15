package nz.ac.auckland.se206;

import java.util.Objects;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioController {
  private static MediaPlayer buttonClickMediaPlayer;
  private static MediaPlayer pencilWriteMediaPlayer;
  private static MediaPlayer backgroundMusicMediaPlayer;
  private static double musicVolumeScale = 1;
  private static double sfxVolumeScale = 1;
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

  private static Media pencilWriteSound =
      new Media(
          Objects.requireNonNull(AudioController.class.getResource("/sounds/write_pencil.mp3"))
              .toExternalForm());

  public static void playButtonClick() {
    Thread buttonClickThread =
        new Thread(
            () -> {
              pencilWriteMediaPlayer = new MediaPlayer(buttonClickSound);
              // Plays the button click sound
              pencilWriteMediaPlayer.setVolume(2 * sfxVolumeScale);
              pencilWriteMediaPlayer.play();
            });

    buttonClickThread.start();
  }

  public static void playPencilWrite() {
    Thread pencilWriteThread =
        new Thread(
            () -> {
              buttonClickMediaPlayer = new MediaPlayer(pencilWriteSound);
              // Plays the button click sound
              buttonClickMediaPlayer.setVolume(0.6 * sfxVolumeScale);
              buttonClickMediaPlayer.play();
            });

    pencilWriteThread.start();
  }

  public static void playBackgroundMusic() {
    Thread backgroundMusicThread =
        new Thread(
            () -> {
              backgroundMusicMediaPlayer = new MediaPlayer(backgroundMusic);
              // Keeps the song looping
              backgroundMusicMediaPlayer.setOnEndOfMedia(
                  () -> {
                    backgroundMusicMediaPlayer.stop();
                    backgroundMusicMediaPlayer.seek(Duration.ZERO);
                    backgroundMusicMediaPlayer.play();
                  });
              backgroundMusicMediaPlayer.setVolume(0.3 * musicVolumeScale);
              backgroundMusicMediaPlayer.play();
            });

    backgroundMusicThread.start();
  }

  /**
   * This method returns the music volume scale.
   *
   * @return music volume scale
   */
  public static double getMusicVolumeScale() {
    return musicVolumeScale;
  }

  /**
   * This method sets the music volume scale.
   *
   * @param musicVolumeScale music volume scale
   */
  public static void setMusicVolumeScale(double musicVolumeScale) {
    AudioController.musicVolumeScale = musicVolumeScale;
    backgroundMusicMediaPlayer.setVolume(0.3 * musicVolumeScale);
  }

  /**
   * This method returns the sfx volume scale.
   *
   * @return sfx volume scale
   */
  public static double getSfxVolumeScale() {
    return sfxVolumeScale;
  }

  /**
   * This method sets the sfx volume scale.
   *
   * @param sfxVolumeScale sfx volume scale
   */
  public static void setSfxVolumeScale(double sfxVolumeScale) {
    AudioController.sfxVolumeScale = sfxVolumeScale;
  }
}
