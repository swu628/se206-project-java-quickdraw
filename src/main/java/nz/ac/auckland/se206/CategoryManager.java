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

  /**
   * This method gets a random word that the player hasn't played yet from the specified difficulty.
   * If the player has played all words, it will just get any random word in that difficulty.
   *
   * @param diff the word difficiulty
   * @return the random word
   */
  private static String getRandomWord(Difficulty diff, boolean useUserWordHistory) {

    User user = App.getCurrentUser();

    // Returns a random category that has not been previously returned unless all
    // categories classified as the desired difficulty has been returned.
    switch (diff) {
      case EASY:
        if (user.getNotPlayedEasyWords().isEmpty() || useUserWordHistory) {
          return getRandomWord(easy);
        } else {
          return getRandomWord(user.getNotPlayedEasyWords());
        }
      case MEDIUM:
        if (user.getNotPlayedMediumWords().isEmpty() || useUserWordHistory) {
          return getRandomWord(medium);
        } else {
          return getRandomWord(user.getNotPlayedMediumWords());
        }
      case HARD:
        if (user.getNotPlayedHardWords().isEmpty() || useUserWordHistory) {
          return getRandomWord(hard);
        } else {
          return getRandomWord(user.getNotPlayedHardWords());
        }
    }
    return null;
  }

  private static String getRandomWord(ArrayList<String> words) {
    return words.get(rand.nextInt(words.size()));
  }

  /**
   * This method sets the word to a random word of a specificed difficulty
   *
   * @param diff the word difficiulty
   * @param useUserWordHistory include words from user's words history
   */
  public static void setWord(SettingsController.WordsDifficulty diff, boolean useUserWordHistory) {
    Difficulty[] difficulties = diff.getDifficulties();

    word = getRandomWord(difficulties[rand.nextInt(difficulties.length)], useUserWordHistory);
  }

  /**
   * This method returns the current word
   *
   * @return the word
   */
  public static String getWord() {
    return word;
  }
}
