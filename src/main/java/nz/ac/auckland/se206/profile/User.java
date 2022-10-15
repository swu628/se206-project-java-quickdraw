package nz.ac.auckland.se206.profile;

import java.util.ArrayList;
import java.util.Random;
import nz.ac.auckland.se206.CategoryManager;
import nz.ac.auckland.se206.SettingsController;

public class User {
  private final int[] userProfileColour;
  private String username;
  private int gamesWon;
  private int gamesLost;
  private ArrayList<String> wordsHistory;
  private ArrayList<Integer> timeTakenHistory;
  private ArrayList<String> notPlayedEasyWords = CategoryManager.getEasyWords();
  private ArrayList<String> notPlayedMediumWords = CategoryManager.getMediumWords();
  private ArrayList<String> notPlayedHardWords = CategoryManager.getHardWords();
  private int fastestWon;
  private SettingsController.AccuracyDifficulty accuracyDifficulty;
  private SettingsController.WordsDifficulty wordsDifficulty;
  private SettingsController.TimeDifficulty timeDifficulty;
  private SettingsController.ConfidenceDifficulty confidenceDifficulty;
  private String underThirty;
  private String underTwenty;
  private String underTen;
  private String zenBadge;
  private String consecutiveWins;
  private int numberOfZenPlayed;
  private ArrayList<String> wonOrLostHistory;

  public User(String username) {
    this.username = username;
    // This sets the default statistics of the user
    gamesWon = 0;
    gamesLost = 0;
    wordsHistory = new ArrayList<>();
    timeTakenHistory = new ArrayList<>();
    wonOrLostHistory = new ArrayList<>();
    // The default fastest won time is set to -1 as there is no game that has been
    // won yet
    fastestWon = -1;
    Random rand = new Random(System.currentTimeMillis());
    // Sets a random colour to be used for user icon
    userProfileColour = new int[] {rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)};

    // Sets the default difficulties
    accuracyDifficulty = SettingsController.AccuracyDifficulty.EASY;
    wordsDifficulty = SettingsController.WordsDifficulty.EASY;
    timeDifficulty = SettingsController.TimeDifficulty.EASY;
    confidenceDifficulty = SettingsController.ConfidenceDifficulty.EASY;

    numberOfZenPlayed = 0;
  }

  public String getName() {
    return username;
  }

  public int getGamesWon() {
    return gamesWon;
  }

  public int getGamesLost() {
    return gamesLost;
  }

  public ArrayList<String> getWordsHistory() {
    return wordsHistory;
  }

  public int getFastestWon() {
    return fastestWon;
  }

  public void setGamesWon(int gamesWon) {
    this.gamesWon = gamesWon;
  }

  public void setGamesLost(int gamesLost) {
    this.gamesLost = gamesLost;
  }

  public void setWordsHistory(ArrayList<String> wordsHistory) {
    this.wordsHistory = wordsHistory;
  }

  public void setFastestWon(int fastestWon) {
    this.fastestWon = fastestWon;
  }

  public void setName(String username) {
    this.username = username;
  }

  public ArrayList<String> getNotPlayedEasyWords() {
    if (wordsHistory.size() != 0) {
      // Remove the already played easy words from the easy category
      for (int i = 0; i < wordsHistory.size(); i++) {
        for (int j = 0; j < notPlayedEasyWords.size(); j++) {
          if (wordsHistory.get(i).equals(notPlayedEasyWords.get(j))) {
            notPlayedEasyWords.remove(j);
          }
        }
      }
    }
    return notPlayedEasyWords;
  }

  public ArrayList<String> getNotPlayedMediumWords() {
    if (wordsHistory.size() != 0) {
      // Remove the already played medium words from the medium category
      for (int i = 0; i < wordsHistory.size(); i++) {
        for (int j = 0; j < notPlayedMediumWords.size(); j++) {
          if (wordsHistory.get(i).equals(notPlayedMediumWords.get(j))) {
            notPlayedMediumWords.remove(j);
          }
        }
      }
    }
    return notPlayedMediumWords;
  }

  public ArrayList<String> getNotPlayedHardWords() {
    if (wordsHistory.size() != 0) {
      // Remove the already played hard words from the hard category
      for (int i = 0; i < wordsHistory.size(); i++) {
        for (int j = 0; j < notPlayedHardWords.size(); j++) {
          if (wordsHistory.get(i).equals(notPlayedHardWords.get(j))) {
            notPlayedHardWords.remove(j);
          }
        }
      }
    }
    return notPlayedHardWords;
  }

  public String getColour() {
    // Returns the string "rgb(r,g,b)"
    StringBuilder sb = new StringBuilder();
    sb.append("rgb(")
        .append(userProfileColour[0])
        .append(",")
        .append(userProfileColour[1])
        .append(",")
        .append(userProfileColour[2])
        .append(")");
    return sb.toString();
  }

  public SettingsController.AccuracyDifficulty getAccuracyDifficulty() {
    return accuracyDifficulty;
  }

  public void setAccuracyDifficulty(SettingsController.AccuracyDifficulty accuracyDifficulty) {
    this.accuracyDifficulty = accuracyDifficulty;
  }

  public SettingsController.WordsDifficulty getWordsDifficulty() {
    return wordsDifficulty;
  }

  public void setWordsDifficulty(SettingsController.WordsDifficulty wordsDifficulty) {
    this.wordsDifficulty = wordsDifficulty;
  }

  public SettingsController.TimeDifficulty getTimeDifficulty() {
    return timeDifficulty;
  }

  public void setTimeDifficulty(SettingsController.TimeDifficulty timeDifficulty) {
    this.timeDifficulty = timeDifficulty;
  }

  public SettingsController.ConfidenceDifficulty getConfidenceDifficulty() {
    return confidenceDifficulty;
  }

  public void setConfidenceDifficulty(
      SettingsController.ConfidenceDifficulty confidenceDifficulty) {
    this.confidenceDifficulty = confidenceDifficulty;
  }

  public ArrayList<Integer> getTimeTakenHistory() {
    return timeTakenHistory;
  }

  public void setTimeTakenHistory(ArrayList<Integer> timeTakenHistory) {
    this.timeTakenHistory = timeTakenHistory;
  }

  public String getUnderThirtyBadge() {
    return underThirty;
  }

  public void setUnderThirtyBadge(String underThirty) {
    this.underThirty = underThirty;
  }

  public String getUnderTwentyBadge() {
    return underTwenty;
  }

  public void setUnderTwentyBadge(String underTwenty) {
    this.underTwenty = underTwenty;
  }

  public String getUnderTenBadge() {
    return underTen;
  }

  public void setUnderTenBadge(String underTen) {
    this.underTen = underTen;
  }

  public String getZenBadge() {
    return zenBadge;
  }

  public void setZenBadge(String zenBadge) {
    this.zenBadge = zenBadge;
  }

  public String getConsecutiveWinsBadge() {
    return consecutiveWins;
  }

  public void setConsecutiveWinsBadge(String consecutiveWins) {
    this.consecutiveWins = consecutiveWins;
  }

  public int getNumberOfZenPlayed() {
    return numberOfZenPlayed;
  }

  public void setNumberOfZenPlayed() {
    this.numberOfZenPlayed++;
  }

  public ArrayList<String> getWonOrLostHistory() {
    return wonOrLostHistory;
  }

  public void setWonOrLostHistory(ArrayList<String> wonOrLostHistory) {
    this.wonOrLostHistory = wonOrLostHistory;
  }
}
