package nz.ac.auckland.se206.profile;

import java.util.ArrayList;

public class User {
  private String username;

  private int gamesWon;
  private int gamesLost;
  private ArrayList<String> wordsHistory;

  private int fastestWon;

  public User(String username) {
    this.username = username;
    gamesWon = 0;
    gamesLost = 0;
    wordsHistory = new ArrayList<>();
    fastestWon = -1;
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
}
