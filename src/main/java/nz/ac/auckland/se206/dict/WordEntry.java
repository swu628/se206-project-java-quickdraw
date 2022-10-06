package nz.ac.auckland.se206.dict;

import java.util.List;
import org.json.JSONArray;

public class WordEntry {

  private String partOfSpeech;
  private List<String> definitions;
  private List<JSONArray> synonyms;
  private List<JSONArray> antonyms;

  public WordEntry(
      String partOfSpeech,
      List<String> definitions,
      List<JSONArray> synonyms,
      List<JSONArray> antonyms) {
    this.partOfSpeech = partOfSpeech;
    this.definitions = definitions;
    this.synonyms = synonyms;
    this.antonyms = antonyms;
  }

  public String getPartOfSpeech() {
    return partOfSpeech;
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
