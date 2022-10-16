package nz.ac.auckland.se206.dict;

import java.util.List;
import org.json.JSONArray;

public class WordEntry {

  private List<String> definitions;
  private List<JSONArray> synonyms;
  private List<JSONArray> antonyms;

  /**
   * This constructor stores information on an entry of a word
   *
   * @param definitions is the list of definitions
   * @param synonyms is the list of JSONarrays of synonyms
   * @param antonyms is the list of JSONarrays of antonyms
   */
  public WordEntry(List<String> definitions, List<JSONArray> synonyms, List<JSONArray> antonyms) {
    this.definitions = definitions;
    this.synonyms = synonyms;
    this.antonyms = antonyms;
  }

  public List<String> getDefinitions() {
    return definitions;
  }

  public List<JSONArray> getSynonyms() {
    return synonyms;
  }

  public List<JSONArray> getAntonyms() {
    return antonyms;
  }
}
