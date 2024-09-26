package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    private static final int PARTSLENGTH = 3;
    private final Map<String, String> countryCodeMap = new HashMap<String, String>();
    private final Map<String, String> codeCountryMap = new HashMap<String, String>();
    private final Map<String, String> twoLetterToThree = new HashMap<String, String>();
    private final Map<String, String> threeLetterToTwo = new HashMap<String, String>();

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */

    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            for (String line : lines) {
                String[] parts = line.split("\t");
                if (parts.length >= PARTSLENGTH) {
                    String lowerCaseTwoLetterCode = parts[1].toLowerCase();
                    String lowerCaseThreeLetterCode = parts[2].toLowerCase();
                    countryCodeMap.put(parts[0], lowerCaseThreeLetterCode);
                    codeCountryMap.put(lowerCaseThreeLetterCode, parts[0]);
                    twoLetterToThree.put(lowerCaseTwoLetterCode, lowerCaseThreeLetterCode);
                    threeLetterToTwo.put(lowerCaseThreeLetterCode, lowerCaseTwoLetterCode);
                }

            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        return this.codeCountryMap.get(code);
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        return this.countryCodeMap.get(country);
    }

    /**
     * Returns the 3-letter code of the country for the given 2-letter country code.
     * @param code the alpha2 of the country
     * @return the 3-letter code of the country
     */
    public String fromTwoLetter(String code) {
        return this.twoLetterToThree.get(code);
    }

    /**
     * Returns the 2-letter code of the country for the given 3-letter country code.
     * @param code the alpha3 of the country
     * @return the 2-letter code of the country
     */
    public String fromThreeLetter(String code) {
        return this.threeLetterToTwo.get(code);
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return this.countryCodeMap.size() - 1;
    }
}
