package org.translation;

import java.util.*;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    private static final String STOPWORD = "quit";

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String country = promptForCountry(translator);
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

            if (STOPWORD.equals(country)) {
                break;
            }

            String countryCodeTwoLetters = languageCodeConverter.fromLanguage(country);
            String countryCodeThreeLetters = countryCodeConverter.fromTwoLetter(countryCodeTwoLetters);
            String language = promptForLanguage(translator, countryCodeThreeLetters);
            if (STOPWORD.equals(language)) {
                break;
            }

            String languageCodeTwoLetters = languageCodeConverter.fromLanguage(language);
            System.out.println(country + " in " + language + " is " + translator.translate(countryCodeThreeLetters,
                                                                                           languageCodeTwoLetters));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if ("quit".equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        List<String> countryNames = new ArrayList<>();
        List<String> countryCodes = translator.getCountries();
        LanguageCodeConverter converter = new LanguageCodeConverter();
        countryCodes.forEach(countryCode -> {
            String countryName = converter.fromLanguageCode(countryCode);
            if (countryName != null) {
                countryNames.add(converter.fromLanguageCode(countryCode));
            }
        });

        Collections.sort(countryNames);

        countryNames.forEach(countryName -> {
            System.out.println(countryName);
        });

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        LanguageCodeConverter converter = new LanguageCodeConverter();
        List<String> languageCodes = translator.getCountryLanguages(country);
        List<String> languageNames = new ArrayList<>();
        languageCodes.forEach(languageCode -> {
            languageNames.add(converter.fromLanguageCode(languageCode));
        });

        languageNames.forEach(languageName -> {
            System.out.println(languageName);
        });

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
