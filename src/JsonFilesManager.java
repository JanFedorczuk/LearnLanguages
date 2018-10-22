package LearnLanguages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonFilesManager 
{
    private JsonFilesManager jsonFilesManager = this;
    
    private List<String> listOfLists = new ArrayList<>();
    private List<String> listOfWords = new ArrayList<>();
    private List<List<String>> contentOfGivenWord = new ArrayList<>();
    
    private int currentWordIndex;
    private int currentListIndex;

    private String chosenListName;

    public JsonFilesManager(String chosenListName)
    {
        this.chosenListName = chosenListName;

    }

    public boolean checkIfJsonFileExists(String fileName, boolean createFile)
    {
        if(new File(Constants.PROGRAM_LOCATION + fileName + Constants.JSON_EXTENSION).exists())
        {
            return true;
        }
        else if(createFile)
        {
            createJsonFileAndAddJsonObjectWithJsonArrayToIt(fileName);
            return false;
        }
        else
        {
            return false;
        }
    }

    public boolean checkIfJsonFileIsEmpty(String fileName)
    {
        try
        {
            if(new File(Constants.PROGRAM_LOCATION + fileName + Constants.JSON_EXTENSION).length() == 0)
            {
                createJsonFileAndAddJsonObjectWithJsonArrayToIt(fileName);
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    public boolean checkIfListOfWordsIsEmpty(String chosenList)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(new FileReader(Constants.PROGRAM_LOCATION + chosenList +
                    Constants.JSON_EXTENSION));

            JSONObject mainJsonObject = (JSONObject) object;
            JSONArray mainJsonArray = (JSONArray) mainJsonObject.get(chosenList);

            if (mainJsonArray.isEmpty())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception exception)
        {
            return false;
        }

    }

    public boolean checkIfJsonListContainsTheGivenWord(String listName)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader
                            (Constants.PROGRAM_LOCATION + listName + Constants.JSON_EXTENSION));
            JSONObject mainJsonObject = (JSONObject) object;
            JSONArray mainJsonArray = (JSONArray) mainJsonObject.get(listName);

            String word = contentOfGivenWord.get(0).get(0);
            List<Boolean> booleanList = new ArrayList<>();
            try
            {
                for (Object wordObject : mainJsonArray)
                {
                    JSONObject jsonWordObject = (JSONObject) wordObject;
                    try
                    {
                        JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(word);
                        if (jsonWordArray != null)
                        {
                            booleanList.add(true);
                        }
                    }
                    catch (Exception exception)
                    {
                    }
                }
            }
            catch (Exception exception)
            {
                for (Object stringObject : mainJsonArray)
                {
                    String string = (String) stringObject;
                    if (string.equals(word)) {
                        booleanList.add(true);
                    }
                }
            }


            if (booleanList.contains(true))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    public void saveJsonFile(String fileName, JSONObject jsonObject)
    {
        try (FileWriter file =
                     new FileWriter(Constants.PROGRAM_LOCATION + fileName + Constants.JSON_EXTENSION))
        {
            file.write(jsonObject.toString());
            file.close();
        }
        catch (Exception exception)
        {
        }
    }

    public void createJsonFileAndAddJsonObjectWithJsonArrayToIt(String fileName)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put(fileName, jsonArray);

        if((fileName == Constants.LIST_OF_LISTS) || (fileName.contains(Constants.WORDS)))
        {
            JSONArray orderJsonArray = new JSONArray();
            jsonObject.put(Constants.ORDER, orderJsonArray);
        }

        try(FileWriter file = new FileWriter(Constants.PROGRAM_LOCATION + fileName + Constants.JSON_EXTENSION))
        {
            file.write(jsonObject.toString());
            file.close();
        }
        catch (Exception exception)
        {
        }
    }

    public void createNewJsonListFiles()
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);

            for (Object stringObject : jsonArray)
            {
                String filename = (String) stringObject;
                if (!jsonFilesManager.checkIfJsonFileExists(filename, false))
                {
                    jsonFilesManager.createJsonFileAndAddJsonObjectWithJsonArrayToIt(filename);
                }
            }
        }
        catch (Exception exception)
        {
        }
    }

    public Boolean saveDataInJsonList_Words(String chosenList, String stringDate, boolean addDate)
    {
        try
        {
            if (jsonFilesManager.checkIfJsonListContainsTheGivenWord(chosenList + Constants.WORDS))
            {
                return false;
            }
            else
            {
                JSONParser jsonParser = new JSONParser();
                Object object = jsonParser.parse
                        (new FileReader
                                (Constants.PROGRAM_LOCATION + chosenList + Constants.WORDS + Constants.JSON_EXTENSION));
                JSONObject mainJsonObject = (JSONObject) object;
                JSONArray mainJsonArray = (JSONArray) mainJsonObject.get(chosenList + Constants.WORDS);

                mainJsonArray.add(contentOfGivenWord.get(0).get(0));
                mainJsonObject.put(chosenList + Constants.WORDS, mainJsonArray);

                if(addDate)
                {
                    mainJsonObject.put(Constants.ORDER, returnJsonOrderArray(mainJsonObject, stringDate,
                            contentOfGivenWord.get(0).get(0)));
                }

                saveJsonFile(chosenList + Constants.WORDS, mainJsonObject);

                return true;
            }
        }
        catch (Exception exception)
        {

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(contentOfGivenWord.get(0).get(0));
            jsonObject.put(chosenList + Constants.WORDS, jsonArray);

            if(addDate)
            {
                jsonObject.put(Constants.ORDER, returnJsonOrderArray(jsonObject, stringDate,
                        contentOfGivenWord.get(0).get(0)));
            }

            saveJsonFile(chosenList + Constants.WORDS, jsonObject);

            return true;
        }

    }

    public Boolean saveDataInJsonList_Content(String chosenList)
    {
        try
        {
            if (jsonFilesManager.checkIfJsonListContainsTheGivenWord(chosenList))
            {
                return false;
            }
            else
            {
                JSONParser jsonParser = new JSONParser();
                Object object = jsonParser.parse
                        (new FileReader
                                (Constants.PROGRAM_LOCATION + chosenList + Constants.JSON_EXTENSION));
                JSONObject mainJsonObject = (JSONObject) object;
                JSONArray mainJsonArray = (JSONArray) mainJsonObject.get(chosenList);

                JSONObject wordObject = new JSONObject();
                JSONArray wordArray = new JSONArray();

                int counter = 0;

                String wordName = contentOfGivenWord.get(0).get(0);

                for (List<String> list : contentOfGivenWord)
                {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (String string : list)
                    {
                        jsonArray.add(string);
                    }

                    String stringCounter = String.valueOf(counter);
                    jsonObject.put(stringCounter, jsonArray);

                    wordArray.add(jsonObject);

                    counter = counter + 1;
                }

                wordObject.put(wordName, wordArray);

                mainJsonArray.add(wordObject);
                mainJsonObject.put(chosenList, mainJsonArray);

                saveJsonFile(chosenList, mainJsonObject);

                return true;
            }
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    public void replaceWordWithAnotherOne()
    {
        try
        {
            String previousWord = getListOfWords().get(currentWordIndex);
            String wordName = contentOfGivenWord.get(0).get(0);

            JSONParser jsonWordParser = new JSONParser();
            Object wordObject = jsonWordParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + chosenListName + Constants.WORDS + Constants.JSON_EXTENSION));
            JSONObject jsonWordObject = (JSONObject) wordObject;
            JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(chosenListName + Constants.WORDS);

            JSONArray jsonOrderArray = (JSONArray) jsonWordObject.get(Constants.ORDER);

            JSONArray copyOfJsonWordArray = new JSONArray();
            copyOfJsonWordArray.addAll(jsonWordArray);

            jsonWordArray.set(currentWordIndex, contentOfGivenWord.get(0).get(0));
            jsonWordObject.put(chosenListName + Constants.WORDS, jsonWordArray);

            Object orderObjectToBeDeleted = returnObjectToBeDeleted(jsonOrderArray, previousWord);
            JSONObject jsonOrderObjectToBeDeleted = (JSONObject) orderObjectToBeDeleted;
            String stringDate = (String) jsonOrderObjectToBeDeleted.get(previousWord);
            jsonOrderObjectToBeDeleted.clear();

            jsonOrderObjectToBeDeleted.put(contentOfGivenWord.get(0).get(0), stringDate);

            saveJsonFile(chosenListName + Constants.WORDS, jsonWordObject);

            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + chosenListName + Constants.JSON_EXTENSION));
            JSONObject jsonMainObject = (JSONObject) object;
            JSONArray jsonMainArray = (JSONArray) jsonMainObject.get(chosenListName);

            JSONArray copyOfJsonMainArray = new JSONArray();
            copyOfJsonMainArray.addAll(jsonMainArray);

            JSONObject newWordObject = new JSONObject();
            JSONArray wordArray = new JSONArray();

            int counter = 0;

            for (List<String> list : contentOfGivenWord) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (String string : list) {
                    jsonArray.add(string);
                }

                String stringCounter = String.valueOf(counter);
                jsonObject.put(stringCounter, jsonArray);

                wordArray.add(jsonObject);

                counter = counter + 1;
            }

            newWordObject.put(wordName, wordArray);

            jsonMainArray.set(currentWordIndex, newWordObject);
            jsonMainObject.put(chosenListName, jsonMainArray);

            saveJsonFile(chosenListName, jsonMainObject);

        }
        catch (Exception exception)
        {
        }
    }

    public boolean checkIfWordDoNotExists(String newWord)
    {
        List<Boolean> booleanList = new ArrayList<>();

        for (String wordName: listOfWords)
        {
            if(newWord.equals(wordName))
            {
                booleanList.add(false);
            }
        }

        if(booleanList.contains(false))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setListOfLists()
    {
        if (checkIfJsonFileExists(Constants.LIST_OF_LISTS, false))
        {
            if (!checkIfJsonFileIsEmpty(Constants.LIST_OF_LISTS))
            {
                listOfLists.clear();
                listOfLists = fillListOfListUsingJsonFile();
            }
            else
            {
                listOfLists = new ArrayList<>();
            }
        }
        else
        {
            listOfLists = new ArrayList<>();
        }
    }

    private List<String> fillListOfListUsingJsonFile()
    {
        List<String> newList = new ArrayList<>();

        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;

            Object array = jsonObject.get(Constants.LIST_OF_LISTS);
            JSONArray jsonArray = (JSONArray) array;

            for (Object stringObject : jsonArray)
            {
                String string = (String) stringObject;
                newList.add(string);
            }
        }
        catch (Exception exception)
        {
            createJsonFileAndAddJsonObjectWithJsonArrayToIt(Constants.LIST_OF_LISTS);
            new InformationDialog(Constants.ERROR, Constants.FILE_IS_CORRUPTED, Constants.PLEASE_RESTART_PROGRAM, null);

        }

        return newList;

    }

    public void setListOfWords()
    {
        if (checkIfJsonFileExists(chosenListName + Constants.WORDS, false))
        {
            if (!checkIfJsonFileIsEmpty(chosenListName + Constants.WORDS))
            {
                listOfWords.clear();
                String chosenList = chosenListName + Constants.WORDS;
                listOfWords = fillListOfWordsUsingJsonFile(chosenList);
            }
            else
            {
                listOfWords = new ArrayList<>();
            }

        }
        else
        {
            listOfWords = new ArrayList<>();
        }
    }

    private List<String> fillListOfWordsUsingJsonFile(String chosenList)
    {
        List<String> newList = new ArrayList<>(listOfWords);
        try
        {

            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + chosenList + Constants.JSON_EXTENSION));
            JSONObject mainJsonObject = (JSONObject) object;

            Object array = mainJsonObject.get(chosenList);
            JSONArray mainJsonArray = (JSONArray) array;

            for (Object stringObject : mainJsonArray)
            {
                String string = (String) stringObject;
                newList.add(string);
            }
        }
        catch (Exception exception)
        {
            createJsonFileAndAddJsonObjectWithJsonArrayToIt(chosenList);
            new InformationDialog(Constants.ERROR, Constants.FILE_IS_CORRUPTED, Constants.PLEASE_RESTART_PROGRAM, null);
        }

        return newList;

    }

    public void setContentOfGivenWord()
    {
        String wordName = listOfWords.get(currentWordIndex);

        if (checkIfJsonFileExists(chosenListName, false))
        {
            if (!checkIfJsonFileIsEmpty(chosenListName))
            {

                contentOfGivenWord.clear();
                contentOfGivenWord = fillContentOfGivenWordUsingJsonFile(wordName, chosenListName);
            }
            else
            {
                contentOfGivenWord = new ArrayList<>();
            }

        }
        else
        {
            contentOfGivenWord = new ArrayList<>();
        }
    }

    public void setContentOfGivenWord(List<List<String>> list)
    {

        contentOfGivenWord = list;
    }

    public List<List<String>> fillContentOfGivenWordUsingJsonFile(String chosenWord, String chosenList)
    {
        List<List<String>> mainList = new ArrayList<>();
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object mainObject = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + chosenList + Constants.JSON_EXTENSION));
            JSONObject mainJsonObject = (JSONObject) mainObject;
            JSONArray mainJsonArray = (JSONArray) mainJsonObject.get(chosenList);

            for (Object object : mainJsonArray)
            {
                JSONObject jsonWordObject = (JSONObject) object;
                try
                {
                    int counter = 0;
                    JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(chosenWord);
                    if (jsonWordArray != null)
                    {
                        for (Object elementObject : jsonWordArray)
                        {
                            JSONObject elementJsonObject = (JSONObject) elementObject;

                            String stringCounter = String.valueOf(counter);

                            JSONArray elementArray = (JSONArray) elementJsonObject.get(stringCounter);

                            List<String> elementList = new ArrayList<>();
                            for (Object stringObject : elementArray)
                            {
                                String string = (String) stringObject;
                                elementList.add(string);
                            }
                            mainList.add(elementList);
                            counter = counter + 1;
                        }
                    }
                }
                catch (Exception exception)
                {
                }

            }
        }
        catch (Exception exception)
        {
        }

        return mainList;
    }

    public void setCurrentListIndex(int chosenListIndex)
    {

        currentListIndex = chosenListIndex;
    }

    public void setCurrentWordIndex(int chosenWordIndex)
    {

        currentWordIndex = chosenWordIndex;
    }

    public void setChosenListName(String newListName)
    {

        chosenListName = newListName;
    }

    public List<List<List<String>>> fillListOfWordsAndTheirContent()
    {
        List<List<List<String>>> listOfWordsAndTheirContent = new ArrayList<>();

        this.setListOfWords();

        for (String word : jsonFilesManager.getListOfWords())
        {
            listOfWordsAndTheirContent.add(fillContentOfGivenWordUsingJsonFile(word, chosenListName));
        }

        return listOfWordsAndTheirContent;
    }

    public List<String> getListOfLists() {
        return listOfLists;
    }

    public List<String> getListOfWords() {
        return listOfWords;
    }

    public List<List<String>> getContentOfGivenWord() {
        return contentOfGivenWord;
    }

    public int getCurrentListIndex() {
        return currentListIndex;
    }

    public int getCurrentWordIndex() {
        return currentWordIndex;
    }

    public String getChosenListName() {
        return chosenListName;
    }

    public String getWordsSuffix()
{
        return Constants.WORDS;
    }


    public String getNewListName(JSONArray jsonArray)
    {
        String newListName = null;

        int i = 0;
        while (newListName == null)
        {
            newListName = getNewNameForList(i, jsonArray);
            i++;
        }

        return newListName;
    }

    private String getNewNameForList(int number, JSONArray jsonArray)
    {
        try
        {
            String newListName = Constants.NEW_LIST + number;

            if (jsonArray.size() > 0)
            {
                if (checkIfListAlreadyExists(newListName, jsonArray))
                {
                    return null;
                }
                else
                {
                    return newListName;
                }
            }
            else
            {
                return newListName;
            }
        }
        catch (Exception exception)
        {
            return null;
        }
    }

    private boolean checkIfListAlreadyExists(String text, JSONArray jsonArray)
    {
        List<Boolean> booleans = new ArrayList<>();
        for (Object stringObject : jsonArray)
        {
            String string = (String) stringObject;
            if (string.equals(text))
            {
                booleans.add(true);
            }
        }

        if (booleans.contains(true))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void deleteJsonFileContainingGivenList(String listName)
    {
        try
        {
            Path listPath = Paths.get(Constants.PROGRAM_LOCATION + listName + Constants.JSON_EXTENSION);
            Files.delete(listPath);
        }
        catch (Exception exception)
        {
        }
    }

    public void addNewListToJSONListsFile(String newListName, String stringDate, boolean addDate)
    {

        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);

            if (newListName == null)
            {
                newListName = getNewListName(jsonArray);
            }

            jsonArray.add(newListName);

            jsonObject.put(Constants.LIST_OF_LISTS, jsonArray);

            if (addDate)
            {
                jsonObject.put(Constants.ORDER, returnJsonOrderArray(jsonObject, stringDate,newListName));
            }

            saveJsonFile(Constants.LIST_OF_LISTS, jsonObject);
        }
        catch (Exception exception)
        {
            jsonFilesManager.createJsonFileAndAddJsonObjectWithJsonArrayToIt(Constants.LIST_OF_LISTS);
            addNewListToJSONListsFile(newListName, stringDate, true);
        }
    }

    public void removeListFromJSONListsFileAndDeleteJSONListFile(int listIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);
            JSONArray jsonOrderArray = (JSONArray) jsonObject.get(Constants.ORDER);

            if (jsonArray.size() != 0)
            {
                String listName = (String) jsonArray.get(listIndex);

                try
                {
                    if (jsonFilesManager.checkIfJsonFileExists(listName, false))
                    {
                        deleteJsonFileContainingGivenList(listName);
                    }

                    if (jsonFilesManager.checkIfJsonFileExists(listName + Constants.WORDS, false))
                    {
                        deleteJsonFileContainingGivenList(listName + Constants.WORDS);
                    }
                }
                catch (Exception exception)
                {
                }

                jsonArray.remove(listIndex);

                jsonOrderArray.remove(returnObjectToBeDeleted
                        (jsonOrderArray, listOfLists.get(listIndex)));
                saveJsonFile(Constants.LIST_OF_LISTS, jsonObject);
            }
        }
        catch (Exception exception)
        {
        }
    }

    public void moveListDownInJSONListFile(int listIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);

            if (listIndex != (jsonArray.size() - 1))
            {
                String listName = (String) jsonArray.get(listIndex + 1);
                jsonArray.set(listIndex + 1, jsonArray.get(listIndex));
                jsonArray.set(listIndex, listName);

                saveJsonFile(Constants.LIST_OF_LISTS, jsonObject);

                jsonFilesManager.setListOfLists();
            }

        }
        catch (Exception exception)
        {
        }
    }

    public void moveListUpInJSONListFile(int listIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);

            if (listIndex > 0)
            {
                String listName = (String) jsonArray.get(listIndex - 1);
                jsonArray.set(listIndex - 1, jsonArray.get(listIndex));
                jsonArray.set(listIndex, listName);

                saveJsonFile(Constants.LIST_OF_LISTS, jsonObject);

                jsonFilesManager.setListOfLists();
            }

        }
        catch (Exception exception)
        {
        }
    }

    public boolean modifyListInJSONListsFileAndInJSONList(String previousFileName, String newListName, int selectedIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);
            JSONArray jsonOrderArray = (JSONArray) jsonObject.get(Constants.ORDER);

            if (!checkIfListAlreadyExists(newListName, jsonArray))
            {
                if (jsonFilesManager.checkIfJsonFileExists(previousFileName, false))
                {
                    try
                    {
                        JSONParser jsonListParser = new JSONParser();
                        Object listObject = jsonListParser.parse
                                (new FileReader(Constants.PROGRAM_LOCATION + previousFileName + Constants.JSON_EXTENSION));
                        JSONObject jsonListObject = (JSONObject) listObject;
                        JSONArray jsonListArray = (JSONArray) jsonListObject.get(previousFileName);


                        JSONObject jsonNewListObject = new JSONObject();
                        jsonNewListObject.put(newListName, jsonListArray);

                        saveJsonFile(newListName, jsonNewListObject);

                        JSONParser jsonWordParser = new JSONParser();
                        Object wordObject = jsonWordParser.parse
                                (new FileReader(Constants.PROGRAM_LOCATION + previousFileName + Constants.WORDS
                                        + Constants.JSON_EXTENSION));
                        JSONObject jsonWordObject = (JSONObject) wordObject;
                        JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(previousFileName + Constants.WORDS);
                        JSONArray jsonOrderArray2 = (JSONArray) jsonWordObject.get(Constants.ORDER);


                        JSONObject jsonNewWordObject = new JSONObject();
                        jsonNewWordObject.put(newListName + Constants.WORDS, jsonWordArray);
                        jsonNewWordObject.put(Constants.ORDER, jsonOrderArray2);


                        saveJsonFile(newListName + Constants.WORDS, jsonNewWordObject);

                        Path listPath = Paths.get(Constants.PROGRAM_LOCATION + previousFileName + Constants.JSON_EXTENSION);
                        Path listWordsPath = Paths.get(Constants.PROGRAM_LOCATION + previousFileName + Constants.WORDS
                                + Constants.JSON_EXTENSION);

                        Files.delete(listPath);
                        Files.delete(listWordsPath);


                    }
                    catch(Exception exception)
                    {
                        deleteJsonFileContainingGivenList(previousFileName);
                        deleteJsonFileContainingGivenList(previousFileName + Constants.WORDS);
                    }
                }

                jsonArray.set(selectedIndex, newListName);

                Object orderObjectToBeDeleted = returnObjectToBeDeleted(jsonOrderArray, previousFileName);
                JSONObject jsonOrderObjectToBeDeleted = (JSONObject) orderObjectToBeDeleted;
                String stringDate = (String) jsonOrderObjectToBeDeleted.get(previousFileName);
                jsonOrderObjectToBeDeleted.clear();

                jsonOrderObjectToBeDeleted.put(newListName, stringDate);

                saveJsonFile(Constants.LIST_OF_LISTS, jsonObject);

                jsonFilesManager.setListOfLists();

                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    public void removeWordFromJsonListFiles(int listIndex, int wordIndex)
    {
        try
        {
            jsonFilesManager.setListOfLists();

            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);

            if (jsonArray.size() != 0)
            {
                String listName = (String) jsonArray.get(listIndex);

                JSONParser wordParser = new JSONParser();
                Object wordObject = wordParser.parse(new FileReader(Constants.PROGRAM_LOCATION + listName +
                        Constants.WORDS + Constants.JSON_EXTENSION));
                JSONObject jsonWordObject = (JSONObject) wordObject;
                JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(listName + Constants.WORDS);
                JSONArray jsonOrderArray = (JSONArray) jsonWordObject.get(Constants.ORDER);

                String word = (String) jsonWordArray.get(wordIndex);

                jsonWordArray.remove(wordIndex);

                jsonOrderArray.remove(returnObjectToBeDeleted
                        (jsonOrderArray, listOfWords.get(wordIndex)));

                saveJsonFile(listName + Constants.WORDS, jsonWordObject);

                JSONParser listParser = new JSONParser();
                Object listObject = listParser.parse
                        (new FileReader(Constants.PROGRAM_LOCATION + listName + Constants.JSON_EXTENSION));
                JSONObject jsonListMainObject = (JSONObject) listObject;
                JSONArray jsonListMainArray = (JSONArray) jsonListMainObject.get(listName);

                JSONObject jsonListObject = (JSONObject) jsonListMainArray.get(wordIndex);
                JSONArray jsonListArray = (JSONArray) jsonListObject.get(word);


                jsonListMainArray.remove(jsonListObject);

                saveJsonFile(listName, jsonListMainObject);

            }
        }
        catch (Exception exception)
        {
        }
    }

    public void moveWordUpInJsonFile(int listIndex, int wordIndex)
    {
        try
        {
            if (wordIndex != 0)
            {
                String listName = listOfLists.get(listIndex);
                String wordName = listOfWords.get(wordIndex - 1);

                JSONParser jsonWordParser = new JSONParser();
                Object wordObject = jsonWordParser.parse
                        (new FileReader(Constants.PROGRAM_LOCATION + listName + Constants.WORDS + Constants.JSON_EXTENSION));
                JSONObject jsonWordObject = (JSONObject) wordObject;
                JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(listName + Constants.WORDS);

                jsonWordArray.set(wordIndex - 1, jsonWordArray.get(wordIndex));
                jsonWordArray.set(wordIndex, wordName);

                saveJsonFile(listName + Constants.WORDS, jsonWordObject);

                JSONParser jsonParser = new JSONParser();
                Object object = jsonParser.parse
                        (new FileReader(Constants.PROGRAM_LOCATION + listName + Constants.JSON_EXTENSION));
                JSONObject jsonMainObject = (JSONObject) object;
                JSONArray jsonMainArray = (JSONArray) jsonMainObject.get(listName);

                JSONObject firstJsonWordObjectToBeSwap = (JSONObject) jsonMainArray.get(wordIndex);
                JSONObject secondJsonWordObjectToBeSwap = (JSONObject) jsonMainArray.get(wordIndex - 1);

                jsonMainArray.set(wordIndex, secondJsonWordObjectToBeSwap);
                jsonMainArray.set(wordIndex - 1, firstJsonWordObjectToBeSwap);

                saveJsonFile(listName, jsonMainObject);

                jsonFilesManager.setListOfWords();
            }

        }
        catch (Exception exception)
        {
        }
    }

    public void moveWordDownInJsonFile(int listIndex, int wordIndex)
    {
        try
        {
            String listName = listOfLists.get(listIndex);
            String wordName = listOfWords.get(wordIndex + 1);

            JSONParser jsonWordParser = new JSONParser();
            Object wordObject = jsonWordParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + listName + Constants.WORDS + Constants.JSON_EXTENSION));
            JSONObject jsonWordObject = (JSONObject) wordObject;
            JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(listName + Constants.WORDS);


            if (wordIndex != (jsonWordArray.size() - 1))
            {
                jsonWordArray.set(wordIndex + 1, jsonWordArray.get(wordIndex));
                jsonWordArray.set(wordIndex, wordName);

                saveJsonFile(listName + Constants.WORDS, jsonWordObject);

                JSONParser jsonParser = new JSONParser();
                Object object = jsonParser.parse
                        (new FileReader(Constants.PROGRAM_LOCATION + listName + Constants.JSON_EXTENSION));
                JSONObject jsonMainObject = (JSONObject) object;
                JSONArray jsonMainArray = (JSONArray) jsonMainObject.get(listName);

                JSONObject firstJsonWordObjectToBeSwap = (JSONObject) jsonMainArray.get(wordIndex);
                JSONObject secondJsonWordObjectToBeSwap = (JSONObject) jsonMainArray.get(wordIndex + 1);

                jsonMainArray.set(wordIndex, secondJsonWordObjectToBeSwap);
                jsonMainArray.set(wordIndex + 1, firstJsonWordObjectToBeSwap);

                saveJsonFile(listName, jsonMainObject);

                jsonFilesManager.setListOfWords();
            }
        }
        catch (Exception exception)
        {
        }
    }

    public boolean moveWordToAnotherList(Boolean saveMovedWordInFirstPlace, String secondListName)
    {
        try
        {
            fillListOfListUsingJsonFile();
            fillListOfWordsUsingJsonFile(chosenListName + Constants.WORDS);

            setContentOfGivenWord();

            checkIfJsonFileExists(secondListName, true);
            checkIfJsonFileExists(secondListName + Constants.WORDS, true);

            if (jsonFilesManager.checkIfJsonListContainsTheGivenWord(secondListName + Constants.WORDS))
            {
                return false;
            }
            else
            {
                String date = "";
                try
                {
                    JSONParser jsonParser = new JSONParser();
                    Object object = jsonParser.parse
                            (new FileReader
                                    (Constants.PROGRAM_LOCATION + chosenListName + Constants.WORDS + Constants.JSON_EXTENSION));
                    JSONObject mainJsonObject = (JSONObject) object;

                    JSONArray jsonOrderArray = (JSONArray) mainJsonObject.get(Constants.ORDER);
                    JSONArray jsonWordArray = (JSONArray) mainJsonObject.get(chosenListName + Constants.WORDS);

                    JSONObject objectToBeDeleted =
                            (JSONObject) returnObjectToBeDeleted(jsonOrderArray, listOfWords.get(currentWordIndex));


                    date = (String) objectToBeDeleted.get(listOfWords.get(currentWordIndex));
                }
                catch (Exception exception)
                {
                }

                saveDataInJsonList_Words(secondListName, date, true);
                saveDataInJsonList_Content(secondListName);

                return true;
            }
        }
        catch (Exception exception)
        {
            return true;
        }
    }

    public List<List<String>> returnListContainingElementsAndDates(String fileName, List<String> list)
    {
        try
        {
            List<List<String>> newList = new ArrayList<>();

            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + fileName + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;

            JSONArray orderArray;

            if (jsonObject.isEmpty())
            {
                return new ArrayList<>();
            }
            else
            {
                orderArray = (JSONArray) jsonObject.get(Constants.ORDER);

                List<String> elementList = new ArrayList<>();
                List<String> dateList = new ArrayList<>();

                for (Object object1 : orderArray)
                {
                    JSONObject orderJsonObject = (JSONObject) object1;
                    for (String string : list)
                    {
                        String dateString = (String) orderJsonObject.get(string);

                        if (dateString != null)
                        {
                            elementList.add(string);
                            dateList.add(dateString);
                        }
                    }
                }

                newList.add(elementList);
                newList.add(dateList);

                return newList;
            }
        }
        catch (Exception exception)
        {
            return new ArrayList<>();
        }
    }

    private Object returnObjectToBeDeleted(JSONArray jsonOrderArray, String elementName)
    {
        Object orderObjectToBeDeleted = new Object();

        for (Object orderObject : jsonOrderArray)
        {
            JSONObject jsonOrderObject = (JSONObject) orderObject;
            try
            {
                String string = (String) jsonOrderObject.get(elementName);
                if (string != null)
                {
                    orderObjectToBeDeleted = orderObject;
                }
            }
            catch (Exception exception)
            {
            }
        }

        return orderObjectToBeDeleted;
    }

    public void clearJsonArray(String fileName, String key)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse
                    (new FileReader(Constants.PROGRAM_LOCATION + fileName + Constants.JSON_EXTENSION));
            JSONObject jsonObject = (JSONObject) object;

            JSONArray jsonArray = (JSONArray) jsonObject.get(key);

            jsonArray.clear();

            saveJsonFile(key, jsonObject);
        }
        catch (Exception exception)
        {
        }
    }

    private JSONArray returnJsonOrderArray(JSONObject jsonObject, String stringDate, String elementName)
    {
        JSONArray jsonOrderArray;

        jsonOrderArray = (JSONArray) jsonObject.get(Constants.ORDER);

        if(jsonOrderArray == null)
        {
            jsonOrderArray = new JSONArray();
        }

        if (stringDate == null)
        {
            DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
            Date today = Calendar.getInstance().getTime();
            stringDate = dateFormat.format(today);
        }

        JSONObject orderObject = new JSONObject();
        orderObject.put(elementName, stringDate);
        jsonOrderArray.add(orderObject);

        return jsonOrderArray;
    }
}
