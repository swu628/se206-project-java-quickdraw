package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Random;

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

  public static void addEasy(String c) {
    easy.add(c);
  }

  public static void addMedium(String c) {
    medium.add(c);
  }

  public static void addHard(String c) {
    hard.add(c);
  }

  public static String getRandomCategory(Difficulty diff) {
    // Returns a random category based on the desired difficulty
    switch (diff) {
      case EASY:
        return easy.get(rand.nextInt(easy.size() + 1));
      case MEDIUM:
        return medium.get(rand.nextInt(medium.size() + 1));
      case HARD:
        return hard.get(rand.nextInt(hard.size() + 1));
    }
    return null;
  }
}
