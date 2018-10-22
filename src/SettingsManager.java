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
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;

            languageFromWhichWordIsTranslated = returnLanguage(jsonObject, Constants.KEY_FOR_FIRST_LANGUAGE);
            languageToWhichWordIsTranslated = returnLanguage(jsonObject,   Constants.KEY_FOR_SECOND_LANGUAGE);

            return true;
        }
        catch (Exception exception)
        {
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
            JSONObject jsonObject = (JSONObject) jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX + Constants.JSON_EXTENSION));
            jsonObject.put(key, newLanguage);

            try (FileWriter file = new FileWriter(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX
                    + Constants.JSON_EXTENSION))
            {
                file.write(jsonObject.toString());
                file.close();
                return true;
            }
            catch (Exception exception)
            {
                return false;
            }
        }
        catch (Exception exception)
        {
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

        try (FileWriter file = new FileWriter(Constants.PROGRAM_LOCATION + Constants.SETTINGS_SUFFIX
                + Constants.JSON_EXTENSION))
        {
            file.write(jsonMainObject.toString());
            file.close();
        }
        catch (Exception exception)
        {
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
