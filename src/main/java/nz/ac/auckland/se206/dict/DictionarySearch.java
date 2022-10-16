package nz.ac.auckland.se206.dict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class DictionarySearch {

  private static final String API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

  /**
   * This method uses a dictionary API to search and store information on a given word
   *
   * @param word is the word being searched using the dictionary API
   * @return WordInfo is the information of the word storing word entries
   * @throws IOException when url requested is invalid
   * @throws WordNotFoundException when word doesn't exist in the dictionary
   */
  public static WordInfo searchWordInfo(String word) throws IOException, WordNotFoundException {

    // Gets dictionary API
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().url(API_URL + word).build();
    Response response = client.newCall(request).execute();
    ResponseBody responseBody = response.body();

    String jsonString = responseBody.string();

    // Checks if word has no definition
    try {
      JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
      String title = jsonObj.getString("title");
      String subMessage = jsonObj.getString("message");
      throw new WordNotFoundException(word, title, subMessage);
    } catch (ClassCastException ignored) {
      // Ignored as this doesn't matter
    }

    JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString).nextValue();
    List<WordEntry> entries = new ArrayList<WordEntry>();

    // Checks through every entry for the word
    for (int e = 0; e < jsonArray.length(); e++) {
      JSONObject jsonEntryObj = jsonArray.getJSONObject(e);
      JSONArray jsonMeanings = jsonEntryObj.getJSONArray("meanings");

      // Creates lists to store word definitions, synonyms and antonyms
      List<String> definitions = new ArrayList<String>();
      List<JSONArray> synonyms = new ArrayList<JSONArray>();
      List<JSONArray> antonyms = new ArrayList<JSONArray>();

      // Checks through every meaning of each entry
      for (int m = 0; m < jsonMeanings.length(); m++) {
        JSONObject jsonMeaningObj = jsonMeanings.getJSONObject(m);

        // Checks every definition within each meaning
        JSONArray jsonDefinitions = jsonMeaningObj.getJSONArray("definitions");
        for (int d = 0; d < jsonDefinitions.length(); d++) {
          JSONObject jsonDefinitionObj = jsonDefinitions.getJSONObject(d);

          // Stores the definition, synonyms and antonyms if available
          String definition = jsonDefinitionObj.getString("definition");
          if (!definition.isEmpty()) {
            definitions.add(definition);
          }
          JSONArray synonym = jsonDefinitionObj.getJSONArray("synonyms");
          if (!synonym.isEmpty()) {
            synonyms.add(synonym);
          }
          JSONArray antonym = jsonDefinitionObj.getJSONArray("antonyms");
          if (!antonym.isEmpty()) {
            antonyms.add(antonym);
          }
        }
      }

      // Creates a word entry for the current entry and adds into the list of entries
      WordEntry wordEntry = new WordEntry(definitions, synonyms, antonyms);
      entries.add(wordEntry);
    }

    return new WordInfo(word, entries);
  }
}
