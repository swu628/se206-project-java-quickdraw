package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Random;
import nz.ac.auckland.se206.profile.User;

public class CategoryManager {
  public enum Difficulty {
    EASY,
    MEDIUM,
    HARD
  }

  private static ArrayList<String> easy = new ArrayList<>();
  private static ArrayList<String> medium = new ArrayList<>();
  private static ArrayList<String> hard = new ArrayList<>();

  private static Random rand = new Random();
  private static String word;

  public static void addEasy(String c) {
    easy.add(c);
  }

  public static void addMedium(String c) {
    medium.add(c);
  }

  public static void addHard(String c) {
    hard.add(c);
  }

  public static ArrayList<String> getEasyWords() {
    return easy;
  }

  public static ArrayList<String> getMediumWords() {
    return medium;
  }

  public static ArrayList<String> getHardWords() {
    return hard;
  }

  public static String getRandomWord(Difficulty diff) {

    User user = App.getCurrentUser();

    // Returns a random category that has not been previously returned unless all
    // categories classified as the desired difficulty has been returned.
    switch (diff) {
      case EASY:
        if (user.getNotPlayedEasyWords().isEmpty()) {
          return getRandomWord(easy);
        } else {
          return getRandomWord(user.getNotPlayedEasyWords());
        }
      case MEDIUM:
        if (user.getNotPlayedMediumWords().isEmpty()) {
          return getRandomWord(medium);
        } else {
          return getRandomWord(user.getNotPlayedMediumWords());
        }
      case HARD:
        if (user.getNotPlayedHardWords().isEmpty()) {
          return getRandomWord(hard);
        } else {
          return getRandomWord(user.getNotPlayedHardWords());
        }
    }
    return null;
  }

  public static String getRandomWord(ArrayList<String> words) {
    return words.get(rand.nextInt(words.size()));
  }

  public static void setWord(DifficultySettingsController.WordsDifficulty diff) {
    Difficulty[] difficulties = diff.getDifficulties();

    word = getRandomWord(difficulties[rand.nextInt(difficulties.length)]);
  }

  public static String getWord() {
    return word;
  }
}
