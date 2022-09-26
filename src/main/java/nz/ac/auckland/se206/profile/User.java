package nz.ac.auckland.se206.profile;

import java.util.ArrayList;
import java.util.Random;
import nz.ac.auckland.se206.CategoryManager;

public class User {
  private final int[] COLOUR;
  private String username;
  private int gamesWon;
  private int gamesLost;
  private ArrayList<String> wordsHistory;

  private ArrayList<String> notPlayedEasyWords = CategoryManager.getEasyWords();
  private ArrayList<String> notPlayedMediumWords = CategoryManager.getMediumWords();
  private ArrayList<String> notPlayedHardWords = CategoryManager.getHardWords();

  private int fastestWon;

  public User(String username) {
    this.username = username;
    // This sets the default statistics of the user
    gamesWon = 0;
    gamesLost = 0;
    wordsHistory = new ArrayList<>();
    // The default fastest won time is set to -1 as there is no game that has been
    // won yet
    fastestWon = -1;
    Random rand = new Random(System.currentTimeMillis());
    // Sets a random colour to be used for user icon
    COLOUR = new int[] {rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)};
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
        .append(COLOUR[0])
        .append(",")
        .append(COLOUR[1])
        .append(",")
        .append(COLOUR[2])
        .append(")");
    return sb.toString();
  }
}
