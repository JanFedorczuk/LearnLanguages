package LearnLanguages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager
{
    private String languageFromWhichWordIsTranslated = "";
    private String languageToWhichWordIsTranslated = "";

    public SettingsManager()
    {
        loadSettings();
    }

    private String returnLanguage(JSONObject jsonObject, String key)
    {
        String firstString = (String) jsonObject.get(key);

        return firstString;
    }

    private boolean loadSettings()
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            FileReader fileReader = new FileReader(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX +
                    Constants.JSON_EXTENSION);

            Object object = jsonParser.parse(fileReader);

            JSONObject jsonObject = (JSONObject) object;

            languageFromWhichWordIsTranslated = returnLanguage(jsonObject, Constants.KEY_FOR_FIRST_LANGUAGE);
            languageToWhichWordIsTranslated = returnLanguage(jsonObject,   Constants.KEY_FOR_SECOND_LANGUAGE);

            fileReader.close();

            return true;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            setDefaultSettings();
            loadSettings();

            return false;
        }
    }

    private boolean updateValueInJsonFile(String key, String newLanguage)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileReader fileReader = new FileReader(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX +
                    Constants.JSON_EXTENSION);

            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            jsonObject.put(key, newLanguage);

            try
            {
                FileWriter fileWriter = new FileWriter(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX
                        + Constants.JSON_EXTENSION);

                fileWriter.write(jsonObject.toString());
                fileWriter.close();

                fileReader.close();
                return true;
            }
            catch (Exception exception)
            {
                fileReader.close();
                exception.printStackTrace();
                return false;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    private void setDefaultSettings()
    {
        JSONObject jsonMainObject = new JSONObject();
        JSONArray jsonMainArray = new JSONArray();

        JSONObject jsonObjectForFirstLanguage = new JSONObject();
        JSONArray jsonArrayForFirstLanguage = new JSONArray();

        jsonMainObject.put(Constants.KEY_FOR_FIRST_LANGUAGE,  Constants.POLISH_LANGUAGE);
        jsonMainObject.put(Constants.KEY_FOR_SECOND_LANGUAGE, Constants.ENGLISH_LANGUAGE);

        try
        {
            FileWriter fileWriter = new FileWriter(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX
                    + Constants.JSON_EXTENSION);
            fileWriter.write(jsonMainObject.toString());
            fileWriter.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public String getLanguageFromWhichWordIsTranslated()
    {
        return  languageFromWhichWordIsTranslated;
    }

    public String getLanguageToWhichWordIsTranslated()
    {
        return languageToWhichWordIsTranslated;
    }

    public List<String> getLanguagesList()
    {
        List<String> list = new ArrayList<>();

        list.add(Constants.ENGLISH_LANGUAGE);
        list.add(Constants.RUSSIAN_LANGUAGE);
        list.add(Constants.FRENCH_LANGUAGE);
        list.add(Constants.ITALIAN_LANGUAGE);
        list.add(Constants.SPANISH_LANGUAGE);
        list.add(Constants.PORTUGAL_LANGUAGE);
        list.add(Constants.ANCIENT_GREEK_LANGUAGE);
        list.add(Constants.MODERN_GREEK_LANGUAGE);
        list.add(Constants.LATIN_LANGUAGE);
        list.add(Constants.GERMAN_LANGUAGE);
        list.add(Constants.CZECH_LANGUAGE);
        list.add(Constants.LITHUANIAN_LANGUAGE);
        list.add(Constants.DUTCH_LANGUAGE);
        list.add(Constants.HUNGARIAN_LANGUAGE);
        list.add(Constants.TURKISH_LANGUAGE);
        list.add(Constants.FINNISH_LANGUAGE);
        list.add(Constants.SWEDISH_LANGUAGE);
        list.add(Constants.ESTONIAN_LANGUAGE);
        list.add(Constants.AFRIKAANS_LANGUAGE);
        list.add(Constants.CROATIAN_LANGUAGE);

        return list;
    }

    public void setLanguageFromWhichWordIsTranslated(String newLanguage)
    {
        languageFromWhichWordIsTranslated = newLanguage;

        if(!updateValueInJsonFile(Constants.KEY_FOR_FIRST_LANGUAGE, newLanguage))
        {
            setDefaultSettings();
        }
    }

    public void setLanguageToWhichWordIsTranslated(String newLanguage)
    {
        languageToWhichWordIsTranslated = newLanguage;

        if(!updateValueInJsonFile(Constants.KEY_FOR_SECOND_LANGUAGE, newLanguage))
        {
            setDefaultSettings();
        }
    }
}
