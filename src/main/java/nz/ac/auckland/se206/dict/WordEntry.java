package nz.ac.auckland.se206.dict;

import java.util.List;

public class WordEntry {

  private String partOfSpeech;
  private List<String> definitions;
  private List<String> synonyms;
  private List<String> antonyms;

  public WordEntry(
      String partOfSpeech, List<String> definitions, List<String> synonyms, List<String> antonyms) {
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

  public List<String> getSynonyms() {
    return synonyms;
  }

  public List<String> getAntonyms() {
    return antonyms;
  }
}
