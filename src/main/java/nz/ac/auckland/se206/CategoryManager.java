package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.Random;

import nz.ac.auckland.se206.profile.User;

public class CategoryManager {
	public enum Difficulty {
		EASY, MEDIUM, HARD
	}

	private static ArrayList<String> easy = new ArrayList<>();
	private static ArrayList<String> medium = new ArrayList<>();
	private static ArrayList<String> hard = new ArrayList<>();
	private static Random rand = new Random();
	private static String category;

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

	public static String getRandomCategory(Difficulty diff) {

		User user = App.getCurrentUser();

		// Returns a random category based on the desired difficulty
		switch (diff) {
		case EASY:
			System.out.println(user.getNotPlayedEasyWords());
			System.out.println(user.getNotPlayedEasyWords().size());
			return getRandomWord(user.getNotPlayedEasyWords());
		case MEDIUM:
			return getRandomWord(user.getNotPlayedMediumWords());
		case HARD:
			return getRandomWord(user.getNotPlayedHardWords());
		}
		return null;
	}

	public static String getRandomWord(ArrayList<String> words) {
		return words.get(rand.nextInt(words.size() + 1));
	}

	public static void setCategory(Difficulty diff) {
		category = getRandomCategory(diff);
	}

	public static String getCategory() {
		return category;
	}

}
