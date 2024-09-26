package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private static final String ALPHATHREE = "alpha3";
    private final Map<String, JSONObject> countryMap = new HashMap<String, JSONObject>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            int arrayLength = jsonArray.length();
            for (int i = 0; i < arrayLength; i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                String countryCode = countryObject.getString(ALPHATHREE);
                countryMap.put(countryCode, countryObject);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        ArrayList<String> countryLanguages = new ArrayList<>();
        JSONObject countryObject = this.countryMap.get(country);
        countryObject.keySet().forEach(key -> {
            if (!key.equals(ALPHATHREE) && !key.equals("alpha2") && !key.equals("id")) {
                countryLanguages.add(key);
            }
        });

        return countryLanguages;
    }

    @Override
    public List<String> getCountries() {
        ArrayList<String> countryCodes = new ArrayList<String>();
        this.countryMap.keySet().forEach(key -> {
            countryCodes.add(countryMap.get(key).getString(ALPHATHREE));
        });

        return countryCodes;
    }

    @Override
    public String translate(String country, String language) {
        return this.countryMap.get(country).getString(language);
    }
}
