package LearnLanguages;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    private String currentListName;

    private List<Integer> indexesOfFilesWhichNamesShouldBeChanged = new ArrayList<>();

    public JsonFilesManager(String currentListName)
    {
        this.currentListName = currentListName;

    }

    public void createJsonFileAndAddJsonObjectWithJsonArrayToIt(String fileName)
    {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put(fileName, jsonArray);

        if((fileName == Constants.LIST_OF_LISTS) || (fileName == Constants.WORDS))
        {
            JSONArray orderJsonArray = new JSONArray();
            jsonObject.put(Constants.ORDER, orderJsonArray);
        }

        saveJsonFile(fileName, jsonObject);
    }

    public void createNewJsonListFiles()
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);

            for (Object stringObject : jsonArray)
            {
                if (!jsonFilesManager.checkIfJsonFileExists((String) stringObject, false))
                {
                    jsonFilesManager.createJsonFileAndAddJsonObjectWithJsonArrayToIt((String) stringObject);
                }
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean createFolder(String folderPath)
    {
        boolean success = (new File(Constants.PROGRAM_LOCATION + folderPath).mkdirs());

        if(success)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void saveJsonFile(String fileName, JSONObject jsonObject)
    {
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(Constants.PROGRAM_LOCATION + fileName +
                    Constants.JSON_EXTENSION);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,
                    StandardCharsets.UTF_8);

            outputStreamWriter.write(jsonObject.toString());

            outputStreamWriter.close();
            fileOutputStream.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean saveDataInJsonList_Words(String chosenList, String stringDate, boolean addDate)
    {
        try
        {
            if (jsonFilesManager.checkIfJsonListContainsGivenWord(chosenList + Constants.WORDS))
            {
                return false;
            }
            else
            {
                JSONParser jsonParser = new JSONParser();

                FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + chosenList +
                        Constants.WORDS + Constants.JSON_EXTENSION);

                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                Object object = jsonParser.parse(bufferedReader);
                JSONObject mainJsonObject = (JSONObject) object;
                JSONArray mainJsonArray = (JSONArray) mainJsonObject.get(chosenList + Constants.WORDS);

                mainJsonArray.add(contentOfGivenWord.get(0).get(0));
                mainJsonObject.put(chosenList + Constants.WORDS, mainJsonArray);

                if(addDate)
                {
                    mainJsonObject.put(Constants.ORDER, getJsonOrderArray(mainJsonObject, stringDate,
                            contentOfGivenWord.get(0).get(0)));
                }

                saveJsonFile(chosenList + Constants.WORDS, mainJsonObject);

                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                return true;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(contentOfGivenWord.get(0).get(0));
            jsonObject.put(chosenList + Constants.WORDS, jsonArray);

            if(addDate)
            {
                jsonObject.put(Constants.ORDER, getJsonOrderArray(jsonObject, stringDate,
                        contentOfGivenWord.get(0).get(0)));
            }

            saveJsonFile(chosenList + Constants.WORDS, jsonObject);

            return true;
        }

    }

    public boolean saveDataInJsonList_Content(String chosenList)
    {
        try
        {
            if (jsonFilesManager.checkIfJsonListContainsGivenWord(chosenList))
            {
                return false;
            }
            else
            {
                JSONParser jsonParser = new JSONParser();

                FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + chosenList +
                        Constants.JSON_EXTENSION);

                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                Object object = jsonParser.parse(bufferedReader);
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

                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                return true;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    public boolean saveDataInSoundFile(Path filePath, List<String> list)
    {
        try
        {
            JSONObject mainJsonObject = new JSONObject();
            JSONArray mainJsonArray = new JSONArray();

            mainJsonArray.add(list.get(1));
            mainJsonArray.add(list.get(2));
            mainJsonObject.put(Constants.DATA, mainJsonArray);

            saveJsonFile(filePath.toString(), mainJsonObject);

            return true;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    public boolean saveSoundFilesContent(String filePath, SoundFilesManager soundFilesManager)
    {
        try
        {
            if(!checkIfFolderExists(filePath))
            {
                createFolder(filePath);
            }

            for(List<String> list: soundFilesManager.getSoundFilesContent())
            {

                String newFilePath = filePath + "/" + list.get(0);

                if(checkIfJsonFileExists(newFilePath, true))
                {
                    deleteFile(newFilePath + Constants.JSON_EXTENSION);
                }

                saveDataInSoundFile(Paths.get(newFilePath), list);
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return true;
    }

    public boolean saveSoundFilesNamesAndSettings(String filePath, SoundFilesManager soundFilesManager, boolean bulkFileValue)
    {
        try
        {
            JSONObject mainJsonObject = new JSONObject();
            JSONArray namesJsonArray = new JSONArray();

            for(List<String> list: soundFilesManager.getSoundFilesContent())
            {
                namesJsonArray.add(list.get(0));
            }

            filePath = filePath + "/" + Constants.FILES_NAMES;

            if(checkIfJsonFileExists(filePath, true))
            {
                deleteFile(filePath + Constants.JSON_EXTENSION);
            }

            mainJsonObject.put(Constants.FILES_NAMES, namesJsonArray);
            mainJsonObject.put(Constants.BULK_FILE_VALUE, bulkFileValue);

            saveJsonFile(filePath, mainJsonObject);

            return true;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    public void replaceWordWithAnotherOne()
    {
        try
        {
            String wordToBeReplaced = getListOfWords().get(currentWordIndex);
            String replacementWord = contentOfGivenWord.get(0).get(0);

            replaceWordInJsonList_Words(wordToBeReplaced, replacementWord);
            replaceWordInJsonList_Content(replacementWord);

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void replaceWordInJsonList_Words(String wordToBeReplaced, String replacementWord)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + currentListName +
                    Constants.WORDS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonMainObject = (JSONObject) object;
            JSONArray jsonWordArray = (JSONArray) jsonMainObject.get(currentListName + Constants.WORDS);
            JSONArray jsonOrderArray = (JSONArray) jsonMainObject.get(Constants.ORDER);

            jsonWordArray.set(currentWordIndex, replacementWord);

            Object orderObject = getOrderObject(jsonOrderArray, wordToBeReplaced);
            JSONObject jsonOrderObject = (JSONObject) orderObject;
            String stringDate = (String) jsonOrderObject.get(wordToBeReplaced);
            jsonOrderObject.clear();

            jsonOrderObject.put(contentOfGivenWord.get(0).get(0), stringDate);

            saveJsonFile(currentListName + Constants.WORDS, jsonMainObject);

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void replaceWordInJsonList_Content(String replacementWord)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + currentListName +
                    Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonMainObject = (JSONObject) object;
            JSONArray jsonMainArray = (JSONArray) jsonMainObject.get(currentListName);

            JSONObject newWordObject = new JSONObject();
            JSONArray wordArray = new JSONArray();

            int counter = 0;

            for (List<String> list : contentOfGivenWord)
            {
                JSONObject jsonMinorObject = new JSONObject();
                JSONArray jsonMinorArray = new JSONArray();
                for (String string : list)
                {
                    jsonMinorArray.add(string);
                }

                String stringCounter = String.valueOf(counter);
                jsonMinorObject.put(stringCounter, jsonMinorArray);

                wordArray.add(jsonMinorObject);

                counter = counter + 1;
            }

            newWordObject.put(replacementWord, wordArray);

            jsonMainArray.set(currentWordIndex, newWordObject);

            saveJsonFile(currentListName, jsonMainObject);

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void deleteFile(String fileString)
    {
        try
        {
            if(fileString.contains(Constants.PROGRAM_LOCATION))
            {
                Files.delete(Paths.get(fileString));
            }
            else
            {
                Files.delete(Paths.get(Constants.PROGRAM_LOCATION + fileString));
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
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
            exception.printStackTrace();
        }
    }

    public void deleteFileAndAllItsContent(String filePath)
    {
        try
        {
            File mainFile = new File(Constants.PROGRAM_LOCATION + filePath);
            File[] arrayOfFiles = mainFile.listFiles();
            List<File> listOfFiles = new ArrayList<>(Arrays.asList(arrayOfFiles));

            for(File file: listOfFiles)
            {
                if(Files.isRegularFile(file.toPath()))
                {
                    try
                    {
                        Files.delete(file.toPath());
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }
                else if(Files.isDirectory(file.toPath()))
                {
                    File[] arrayOfInnerFiles = file.listFiles();

                    List listOfInnerFiles = new ArrayList<>(Arrays.asList(arrayOfInnerFiles));

                    deleteEverythingInGivenFile(listOfInnerFiles);

                    Files.delete(file.toPath());
                }
            }

            Files.delete(Paths.get(Constants.PROGRAM_LOCATION + filePath));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void deleteEverythingInGivenFile(List<File> listOfFiles)
    {
        try
        {
            for(File file: listOfFiles)
            {
                if (Files.isRegularFile(file.toPath()))
                {
                    try
                    {
                        Files.delete(file.toPath());
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }
                else if (Files.isDirectory(file.toPath()))
                {
                    File[] arrayOfInnerFiles = file.listFiles();
                    List listOfInnerFiles = new ArrayList<>(Arrays.asList(arrayOfInnerFiles));

                    deleteEverythingInGivenFile(listOfInnerFiles);

                    Files.delete(file.toPath());
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean renameFile(String firstFilePath, String secondFilePath)
    {
        try
        {
            boolean result = false;

            File firstFile = new File(firstFilePath);

            File secondFile = new File(secondFilePath);

            if(firstFile.renameTo(secondFile))
            {
                result = true;
            }

            return result;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    public boolean moveFile(String firstPath, String secondPath)
    {
        try
        {
            Files.move(Paths.get(Constants.PROGRAM_LOCATION + firstPath),
                    Paths.get(Constants.PROGRAM_LOCATION + secondPath), StandardCopyOption.REPLACE_EXISTING);

            return true;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    public void addNewListToJSONListsFile(String newListName, String stringDate, boolean addDate)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
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
                jsonObject.put(Constants.ORDER, getJsonOrderArray(jsonObject, stringDate,newListName));
            }

            saveJsonFile(Constants.LIST_OF_LISTS, jsonObject);

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            jsonFilesManager.createJsonFileAndAddJsonObjectWithJsonArrayToIt(Constants.LIST_OF_LISTS);
            addNewListToJSONListsFile(newListName, stringDate, addDate);
        }
    }

    public void removeListFromJSONListsFileAndDeleteJSONListFile(int listIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonListArray = (JSONArray) jsonObject.get(Constants.LIST_OF_LISTS);
            JSONArray jsonOrderArray = (JSONArray) jsonObject.get(Constants.ORDER);

            if (jsonListArray.size() != 0)
            {
                String listName = (String) jsonListArray.get(listIndex);

                try
                {
                    if (jsonFilesManager.checkIfJsonFileExists(listName, false))
                    {
                        deleteJsonFileContainingGivenList(listName);
                    }

                    if (jsonFilesManager.checkIfJsonFileExists(listName +
                            Constants.WORDS, false))
                    {
                        deleteJsonFileContainingGivenList(listName + Constants.WORDS);
                    }
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }

                jsonListArray.remove(listIndex);

                jsonOrderArray.remove(getOrderObject(jsonOrderArray, listOfLists.get(listIndex)));
                saveJsonFile(Constants.LIST_OF_LISTS, jsonObject);
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void removeFileNameFromJSONFile(String filePath, int objectIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + filePath + "/" +
                    Constants.FILES_NAMES + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonListArray = (JSONArray) jsonObject.get(Constants.FILES_NAMES);

            jsonListArray.remove(jsonListArray.get(objectIndex));

            jsonObject.put(Constants.FILES_NAMES, jsonListArray);

            saveJsonFile(filePath + "/" + Constants.FILES_NAMES, jsonObject);

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void moveListDownInJSONListFile(int listIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
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

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void moveListUpInJSONListFile(int listIndex)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
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

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean modifyListInJSONListsFileAndInJSONList(String previousFileName, String newListName, int selectedIndex)
    {
        try
        {
            JSONParser jsonListsParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonListsParser.parse(bufferedReader);
            JSONObject jsonListsObject = (JSONObject) object;
            JSONArray jsonListsArray = (JSONArray) jsonListsObject.get(Constants.LIST_OF_LISTS);
            JSONArray jsonListsOrderArray = (JSONArray) jsonListsObject.get(Constants.ORDER);

            if (!checkIfListAlreadyExists(newListName, jsonListsArray))
            {
                if (jsonFilesManager.checkIfJsonFileExists(previousFileName, false))
                {
                    try
                    {
                        FileInputStream secondFileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                                previousFileName + Constants.JSON_EXTENSION);

                        InputStreamReader secondInputStreamReader = new InputStreamReader(secondFileInputStream,
                                StandardCharsets.UTF_8);

                        BufferedReader secondBufferedReader = new BufferedReader(secondInputStreamReader);

                        JSONParser jsonListParser = new JSONParser();

                        Object listObject = jsonListParser.parse(secondBufferedReader);

                        JSONObject jsonListObject = (JSONObject) listObject;
                        JSONArray jsonListArray = (JSONArray) jsonListObject.get(previousFileName);

                        JSONObject jsonNewListObject = new JSONObject();
                        jsonNewListObject.put(newListName, jsonListArray);

                        saveJsonFile(newListName, jsonNewListObject);

                        secondFileInputStream.close();
                        secondInputStreamReader.close();
                        secondBufferedReader.close();

                        Path listPath = Paths.get(Constants.PROGRAM_LOCATION + previousFileName + Constants.JSON_EXTENSION);

                        Files.delete(listPath);

                        JSONParser jsonWordParser = new JSONParser();

                        FileInputStream thirdFileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                                previousFileName + Constants.WORDS + Constants.JSON_EXTENSION);

                        InputStreamReader thirdInputStreamReader = new InputStreamReader(thirdFileInputStream,
                                StandardCharsets.UTF_8);

                        BufferedReader thirdBufferedReader = new BufferedReader(thirdInputStreamReader);

                        Object wordObject = jsonWordParser.parse(thirdBufferedReader);
                        JSONObject jsonWordObject = (JSONObject) wordObject;
                        JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(previousFileName + Constants.WORDS);
                        JSONArray jsonWordOrderArray = (JSONArray) jsonWordObject.get(Constants.ORDER);

                        JSONObject jsonNewWordObject = new JSONObject();
                        jsonNewWordObject.put(newListName + Constants.WORDS, jsonWordArray);
                        jsonNewWordObject.put(Constants.ORDER, jsonWordOrderArray);

                        saveJsonFile(newListName + Constants.WORDS, jsonNewWordObject);

                        thirdFileInputStream.close();
                        thirdInputStreamReader.close();
                        thirdBufferedReader.close();

                        Path listWordsPath = Paths.get(Constants.PROGRAM_LOCATION + previousFileName + Constants.WORDS
                                + Constants.JSON_EXTENSION);

                        Files.delete(listWordsPath);
                    }
                    catch(Exception exception)
                    {
                        exception.printStackTrace();
                    }
                }

                jsonListsArray.set(selectedIndex, newListName);

                Object orderObject = getOrderObject(jsonListsOrderArray, previousFileName);
                JSONObject jsonOrderObject = (JSONObject) orderObject;
                String stringDate = (String) jsonOrderObject.get(previousFileName);
                jsonOrderObject.clear();
                jsonOrderObject.put(newListName, stringDate);

                saveJsonFile(Constants.LIST_OF_LISTS, jsonListsObject);

                jsonFilesManager.setListOfLists();

                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                return true;
            }
            else
            {
                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                return false;
            }


        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return false;
        }
    }

    public void removeWordFromJsonListFiles(int listIndex, int wordIndex)
    {
        try
        {
            jsonFilesManager.setListOfLists();

            JSONParser jsonListsParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + Constants.LIST_OF_LISTS +
                    Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonListsParser.parse(bufferedReader);

            JSONObject jsonListsObject = (JSONObject) object;
            JSONArray jsonListsArray = (JSONArray) jsonListsObject.get(Constants.LIST_OF_LISTS);

            if (jsonListsArray.size() != 0)
            {
                String listName = (String) jsonListsArray.get(listIndex);

                JSONParser wordParser = new JSONParser();

                FileInputStream secondFileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + listName +
                        Constants.WORDS + Constants.JSON_EXTENSION);

                InputStreamReader secondInputStreamReader = new InputStreamReader(secondFileInputStream, StandardCharsets.UTF_8);

                BufferedReader secondBufferedReader = new BufferedReader(secondInputStreamReader);

                Object wordObject = wordParser.parse(secondBufferedReader);
                JSONObject jsonWordObject = (JSONObject) wordObject;
                JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(listName + Constants.WORDS);
                JSONArray jsonOrderArray = (JSONArray) jsonWordObject.get(Constants.ORDER);

                String word = (String) jsonWordArray.get(wordIndex);

                jsonWordArray.remove(wordIndex);
                jsonOrderArray.remove(getOrderObject
                        (jsonOrderArray, listOfWords.get(wordIndex)));

                saveJsonFile(listName + Constants.WORDS, jsonWordObject);

                JSONParser listParser = new JSONParser();

                FileInputStream thirdFileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + listName +
                        Constants.JSON_EXTENSION);

                InputStreamReader thirdInputStreamReader = new InputStreamReader(thirdFileInputStream, StandardCharsets.UTF_8);

                BufferedReader thirdBufferedReader = new BufferedReader(thirdInputStreamReader);

                Object listObject = listParser.parse(thirdBufferedReader);
                JSONObject jsonListMainObject = (JSONObject) listObject;
                JSONArray jsonListMainArray = (JSONArray) jsonListMainObject.get(listName);

                JSONObject jsonListObject = (JSONObject) jsonListMainArray.get(wordIndex);
                JSONArray jsonListArray = (JSONArray) jsonListObject.get(word);

                jsonListMainArray.remove(jsonListObject);

                saveJsonFile(listName, jsonListMainObject);

                secondFileInputStream.close();
                secondInputStreamReader.close();
                secondBufferedReader.close();

                thirdFileInputStream.close();
                thirdInputStreamReader.close();
                thirdBufferedReader.close();
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
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

                FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                        listName + Constants.WORDS + Constants.JSON_EXTENSION);

                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

                BufferedReader inputBufferedReader = new BufferedReader(inputStreamReader);

                Object wordObject = jsonWordParser.parse(inputBufferedReader);
                JSONObject jsonWordObject = (JSONObject) wordObject;
                JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(listName + Constants.WORDS);

                jsonWordArray.set(wordIndex - 1, jsonWordArray.get(wordIndex));
                jsonWordArray.set(wordIndex, wordName);

                saveJsonFile(listName + Constants.WORDS, jsonWordObject);

                JSONParser jsonListParser = new JSONParser();

                FileInputStream secondFileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                        listName + Constants.JSON_EXTENSION);

                InputStreamReader secondInputStreamReader = new InputStreamReader(secondFileInputStream, StandardCharsets.UTF_8);

                BufferedReader secondInputBufferedReader = new BufferedReader(secondInputStreamReader);

                Object listObject = jsonListParser.parse(secondInputBufferedReader);
                JSONObject jsonListObject = (JSONObject) listObject;
                JSONArray jsonListArray = (JSONArray) jsonListObject.get(listName);

                JSONObject firstJsonWordObjectToBeSwapped = (JSONObject) jsonListArray.get(wordIndex);
                JSONObject secondJsonWordObjectToBeSwapped = (JSONObject) jsonListArray.get(wordIndex - 1);

                jsonListArray.set(wordIndex, secondJsonWordObjectToBeSwapped);
                jsonListArray.set(wordIndex - 1, firstJsonWordObjectToBeSwapped);

                saveJsonFile(listName, jsonListObject);

                jsonFilesManager.setListOfWords();

                fileInputStream.close();
                inputStreamReader.close();
                inputBufferedReader.close();

                secondFileInputStream.close();
                secondInputStreamReader.close();
                secondInputBufferedReader.close();
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void moveWordDownInJsonFile(int listIndex, int wordIndex)
    {
        try
        {
            String listName = listOfLists.get(listIndex);
            String wordName = listOfWords.get(wordIndex + 1);

            JSONParser jsonWordParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + listName +
                    Constants.WORDS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader inputBufferedReader = new BufferedReader(inputStreamReader);

            Object wordObject = jsonWordParser.parse(inputBufferedReader);
            JSONObject jsonWordObject = (JSONObject) wordObject;
            JSONArray jsonWordArray = (JSONArray) jsonWordObject.get(listName + Constants.WORDS);


            if (wordIndex != (jsonWordArray.size() - 1))
            {
                jsonWordArray.set(wordIndex + 1, jsonWordArray.get(wordIndex));
                jsonWordArray.set(wordIndex, wordName);

                saveJsonFile(listName + Constants.WORDS, jsonWordObject);

                JSONParser jsonListParser = new JSONParser();

                FileInputStream secondFileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                        listName + Constants.JSON_EXTENSION);

                InputStreamReader secondInputStreamReader = new InputStreamReader(secondFileInputStream, StandardCharsets.UTF_8);

                BufferedReader secondInputBufferedReader = new BufferedReader(secondInputStreamReader);

                Object listObject = jsonListParser.parse(secondInputBufferedReader);
                JSONObject jsonListObject = (JSONObject) listObject;
                JSONArray jsonListArray = (JSONArray) jsonListObject.get(listName);

                JSONObject firstJsonWordObjectToBeSwap = (JSONObject) jsonListArray.get(wordIndex);
                JSONObject secondJsonWordObjectToBeSwap = (JSONObject) jsonListArray.get(wordIndex + 1);

                jsonListArray.set(wordIndex, secondJsonWordObjectToBeSwap);
                jsonListArray.set(wordIndex + 1, firstJsonWordObjectToBeSwap);

                saveJsonFile(listName, jsonListObject);

                jsonFilesManager.setListOfWords();

                secondFileInputStream.close();
                secondInputStreamReader.close();
                secondInputBufferedReader.close();
            }

            fileInputStream.close();
            inputStreamReader.close();
            inputBufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean moveWordToAnotherList(Boolean saveMovedWordInFirstPlace, String secondListName)
    {
        try
        {
            fillListOfListUsingJsonFile();
            fillListOfWordsUsingJsonFile(currentListName);

            setContentOfGivenWord();

            checkIfJsonFileExists(secondListName, true);
            checkIfJsonFileExists(secondListName + Constants.WORDS, true);

            if (jsonFilesManager.checkIfJsonListContainsGivenWord(secondListName + Constants.WORDS))
            {
                return false;
            }
            else
            {
                String date = "";
                try
                {
                    JSONParser jsonWordParser = new JSONParser();

                    FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                            currentListName + Constants.WORDS + Constants.JSON_EXTENSION);

                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    Object wordObject = jsonWordParser.parse(bufferedReader);

                    JSONObject wordJsonObject = (JSONObject) wordObject;

                    JSONArray jsonWordOrderArray = (JSONArray) wordJsonObject.get(Constants.ORDER);
                    JSONArray jsonWordArray = (JSONArray) wordJsonObject.get(currentListName + Constants.WORDS);

                    JSONObject wordOrderObject =
                            (JSONObject) getOrderObject(jsonWordOrderArray, listOfWords.get(currentWordIndex));

                    date = (String) wordOrderObject.get(listOfWords.get(currentWordIndex));

                    fileInputStream.close();
                    inputStreamReader.close();
                    bufferedReader.close();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                }

                saveDataInJsonList_Words(secondListName, date, true);
                saveDataInJsonList_Content(secondListName);

                return true;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return true;
        }
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
            exception.printStackTrace();
            return false;
        }
    }

    public boolean checkIfJsonListOfWordsIsEmpty(String chosenList)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    currentListName + Constants.WORDS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);

            JSONObject mainJsonObject = (JSONObject) object;
            JSONArray mainJsonArray = (JSONArray) mainJsonObject.get(chosenList + Constants.WORDS);

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();

            if(mainJsonArray != null)
            {
                if(mainJsonArray.isEmpty())
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return true;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return true;
        }

    }

    public boolean checkIfJsonListContainsGivenWord(String listName)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + listName +
                    Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
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
                        exception.printStackTrace();
                    }
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();

                for (Object stringObject : mainJsonArray)
                {
                    String string = (String) stringObject;
                    if (string.equals(word))
                    {
                        booleanList.add(true);
                    }
                }
            }


            if (booleanList.contains(true))
            {
                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                return true;
            }
            else
            {
                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                return false;
            }

        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
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

    public boolean checkIfFolderExists(String filePath)
    {

        File file = new File(Constants.PROGRAM_LOCATION + filePath);

        if(file.exists() && Files.isDirectory(Paths.get(Constants.PROGRAM_LOCATION + filePath))
                || Files.isRegularFile(Paths.get(Constants.PROGRAM_LOCATION + filePath)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void addIndexesOfFileWhichNamesShouldBeChanged(int index, List<Integer> indexesOfFilesWhichNamesShouldBeChanged)
    {
        indexesOfFilesWhichNamesShouldBeChanged.add(index);
    }

    public boolean checkIfAnyListContainsFalse(List<List<Boolean>> comparisonList)
    {
        boolean value = false;

        for(List<Boolean> list: comparisonList)
        {
            if(list.contains(false))
            {
                value = true;
            }
        }

        return value;
    }

    public List<Boolean> getProperList(List<List<Boolean>> temporaryList)
    {
        List<Boolean> properList = new ArrayList<>();

        boolean isOver = false;

        for(List<Boolean> list: temporaryList)
        {
            if(list.get(1).equals(true))
            {
                properList = new ArrayList<>(list);
                isOver = true;
            }
        }

        if(isOver)
        {
            return properList;
        }
        else
        {
            for(List<Boolean> list: temporaryList)
            {
                if(list.get(0).equals(true))
                {
                    properList = new ArrayList<>(list);
                    isOver = true;
                }
            }
            if(isOver)
            {
                return properList;
            }
            else
            {
                properList.add(false);
                properList.add(false);
                properList.add(false);

                return properList;
            }
        }
    }

    public boolean getIfAllListsValuesAtIndexAreTrue(List<List<Boolean>> comparisonList, int index)
    {
        boolean value = true;

        for(List<Boolean> list: comparisonList)
        {
            if(!list.get(index))
            {
                value = false;
            }
        }

        return value;
    }

    public boolean checkIfFilesHaveIdenticalOrder(List<List<String>> soundFilesContent, List<String> filesNames)
    {
        boolean booleanValue = true;

        try
        {

            for(List<String> list: soundFilesContent)
            {
                if(soundFilesContent.indexOf(list) <= (filesNames.size() - 1))
                {
                    if((list.get(0).equals(filesNames.get(soundFilesContent.indexOf(list)))))
                    {
                        booleanValue = true;
                    }
                    else
                    {
                        booleanValue = false;
                        break;
                    }
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return booleanValue;
    }

    public void clearJsonArray(String fileName, String key)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + fileName +
                    Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;

            JSONArray jsonArray = (JSONArray) jsonObject.get(key);

            jsonArray.clear();

            saveJsonFile(key, jsonObject);

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
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


    public void setListOfWords()
    {
        if (checkIfJsonFileExists(currentListName + Constants.WORDS, false))
        {
            if (!checkIfJsonFileIsEmpty(currentListName + Constants.WORDS))
            {
                listOfWords.clear();
                listOfWords = fillListOfWordsUsingJsonFile(currentListName);
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

    public void setContentOfGivenWord()
    {
        String wordName = listOfWords.get(currentWordIndex);

        if (checkIfJsonFileExists(currentListName, false))
        {
            if (!checkIfJsonFileIsEmpty(currentListName))
            {

                contentOfGivenWord.clear();
                contentOfGivenWord = fillContentOfGivenWordUsingJsonFile(wordName, currentListName);
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

    private List<String> fillListOfListUsingJsonFile()
    {
        List<String> newList = new ArrayList<>();

        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION +
                    Constants.LIST_OF_LISTS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;

            Object array = jsonObject.get(Constants.LIST_OF_LISTS);
            JSONArray jsonArray = (JSONArray) array;

            for (Object stringObject : jsonArray)
            {
                String string = (String) stringObject;
                newList.add(string);
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            createJsonFileAndAddJsonObjectWithJsonArrayToIt(Constants.LIST_OF_LISTS);
            new InformationDialog(Constants.ERROR, Constants.FILE_IS_CORRUPTED, Constants.PLEASE_RESTART_PROGRAM,
                    null,null);
        }

        return newList;
    }

    private List<String> fillListOfWordsUsingJsonFile(String chosenList)
    {
        List<String> newList = new ArrayList<>(listOfWords);

        try
        {
            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + chosenList
                    + Constants.WORDS + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            JSONParser jsonParser = new JSONParser();

            Object object = jsonParser.parse(bufferedReader);

            JSONObject mainJsonObject = (JSONObject) object;

            Object arrayObject = mainJsonObject.get(chosenList + Constants.WORDS);
            JSONArray wordJsonArray = (JSONArray) arrayObject;

            for (Object stringObject : wordJsonArray)
            {
                String string = (String) stringObject;
                newList.add(string);
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            new InformationDialog(Constants.ERROR, Constants.FILE_IS_CORRUPTED, Constants.PLEASE_RESTART_PROGRAM,
                    null,null);
        }

        return newList;

    }

    public List<List<String>> fillContentOfGivenWordUsingJsonFile(String chosenWord, String chosenList)
    {
        List<List<String>> mainList = new ArrayList<>();

        try
        {
            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + chosenList +
                    Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object mainObject = new JSONParser().parse(bufferedReader);

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
                    exception.printStackTrace();
                }

            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            createJsonFileAndAddJsonObjectWithJsonArrayToIt(chosenList);
            new InformationDialog(Constants.ERROR, Constants.FILE_IS_CORRUPTED, Constants.PLEASE_RESTART_PROGRAM,
                    null,null);
        }

        return mainList;
    }

    public List<List<List<String>>> fillListOfWordsAndTheirContent()
    {
        List<List<List<String>>> listOfWordsAndTheirContent = new ArrayList<>();

        this.setListOfWords();

        for (String word : jsonFilesManager.getListOfWords())
        {
            listOfWordsAndTheirContent.add(fillContentOfGivenWordUsingJsonFile(word, currentListName));
        }

        return listOfWordsAndTheirContent;
    }

    public void setCurrentListIndex(int chosenListIndex)
    {

        currentListIndex = chosenListIndex;
    }

    public void setCurrentWordIndex(int chosenWordIndex)
    {

        currentWordIndex = chosenWordIndex;
    }

    public void setCurrentListName(String newListName)
    {

        currentListName = newListName;
    }

    public List<List<Boolean>> getFilesComparisonList(List<List<String>> soundFilesContent,
                                                      List<String> filesNames)
    {
        indexesOfFilesWhichNamesShouldBeChanged = new ArrayList<>();

        List<List<Boolean>> filesComparisonList = new ArrayList<>();

        try
        {
            for(List<String> list: soundFilesContent)
            {
                String firstFileName = list.get(0);
                String firstFileContent = list.get(1);
                String firstFileLanguage = list.get(2);

                List<Integer> indexList = new ArrayList<>();

                List<List<Boolean>> temporaryList = new ArrayList<>();

                if(!filesNames.isEmpty())
                {
                    for(String secondFileName: filesNames)
                    {
                        String filePath = jsonFilesManager.getCurrentListName() + "/" + jsonFilesManager.getListOfWords().get
                                (jsonFilesManager.getCurrentWordIndex()) + "/" + secondFileName;

                        List<String> soundFileContent = getSoundFileContent(filePath);

                        List<Boolean> valueList = new ArrayList<>();

                        if(!soundFileContent.isEmpty())
                        {
                            String secondFileContent = soundFileContent.get(0);
                            String secondFileLanguage = soundFileContent.get(1);

                            valueList.add(firstFileName.equals(secondFileName));
                            valueList.add(firstFileContent.equals(secondFileContent));
                            valueList.add(firstFileLanguage.equals(secondFileLanguage));
                        }
                        else
                        {
                            valueList.add(false);
                            valueList.add(false);
                            valueList.add(false);
                        }

                        if(!valueList.get(0) && valueList.get(1) && valueList.get(2))
                        {
                            indexList.add(filesNames.indexOf(secondFileName));
                        }
                        else
                        {
                            indexList.add(-1);
                        }

                        temporaryList.add(valueList);
                    }

                    for(int i = 0; i < filesNames.size(); i ++)
                    {
                        if(indexList.get(0) >= 0)
                        {

                            addIndexesOfFileWhichNamesShouldBeChanged((indexList.get(0)),
                                    indexesOfFilesWhichNamesShouldBeChanged);
                            break;
                        }
                        else
                        {
                            indexList.remove(0);
                        }
                    }

                    if(indexList.isEmpty())
                    {
                        addIndexesOfFileWhichNamesShouldBeChanged(-1, indexesOfFilesWhichNamesShouldBeChanged);
                    }
                    else
                    {

                    }

                    filesComparisonList.add(getProperList(temporaryList));
                }
                else
                {
                    List<Boolean> valueList = new ArrayList<>();

                    valueList.add(false);
                    valueList.add(false);
                    valueList.add(false);

                    addIndexesOfFileWhichNamesShouldBeChanged(-1, indexesOfFilesWhichNamesShouldBeChanged);

                    filesComparisonList.add(valueList);
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return filesComparisonList;
    }

    public List<List<String>> getListContainingElementsAndDates(String fileName, List<String> list)
    {
        try
        {
            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + fileName +
                    Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            List<List<String>> newList = new ArrayList<>();

            JSONParser jsonParser = new JSONParser();

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;

            JSONArray orderArray;

            if (jsonObject.isEmpty())
            {
                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

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

                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

                return newList;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return new ArrayList<>();
        }
    }

    public JSONArray getFilesNamesJsonArray(String filePath)
    {
        try
        {
            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + filePath + "/" +
                    Constants.FILES_NAMES + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            JSONParser jsonParser = new JSONParser();

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonFilesArray = new JSONArray();

            if (!jsonObject.isEmpty())
            {
                jsonFilesArray = (JSONArray) jsonObject.get(Constants.FILES_NAMES);
            }


            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();

            return jsonFilesArray;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return new JSONArray();
        }
    }

    public boolean getFilesBulkCreationValue(String filePath)
    {
        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + filePath + "/" +
                    Constants.FILES_NAMES + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;
            boolean jsonValue = false;
            if (!jsonObject.isEmpty())
            {
                jsonValue = (Boolean) jsonObject.get(Constants.BULK_FILE_VALUE);
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();

            return jsonValue;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            return false;
        }
    }

    public List<List<String>> getSoundFilesContent(String filePath)
    {
        List<List<String>> soundFilesData = new ArrayList<>();

        try
        {
            JSONParser jsonParser = new JSONParser();

            FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + filePath + "/" +
                    Constants.FILES_NAMES + Constants.JSON_EXTENSION);

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Object object = jsonParser.parse(bufferedReader);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray jsonFilesArray = new JSONArray();
            if (!jsonObject.isEmpty())
            {
                jsonFilesArray = (JSONArray) jsonObject.get(Constants.FILES_NAMES);
            }

            for(Object stringObject: jsonFilesArray)
            {
                String fileName = (String) stringObject;
                List<String> soundFileContent = getSoundFileContent(filePath + "/" + fileName);

                List<String> newSoundFileContent = new ArrayList<>();
                newSoundFileContent.add(fileName);
                newSoundFileContent.addAll(soundFileContent);

                soundFilesData.add(newSoundFileContent);
            }

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return soundFilesData;
    }

    public List<String> getSoundFileContent(String filePath)
    {
        List<String> soundFileContent = new ArrayList<>();

        try
        {
            JSONParser jsonParser = new JSONParser();

            if(checkIfFolderExists(filePath + Constants.JSON_EXTENSION))
            {
                FileInputStream fileInputStream = new FileInputStream(Constants.PROGRAM_LOCATION + filePath +
                        Constants.JSON_EXTENSION);

                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                Object object = jsonParser.parse(bufferedReader);
                JSONObject jsonObject = (JSONObject) object;
                JSONArray jsonFilesArray = (JSONArray) jsonObject.get(Constants.DATA);

                for(Object stringObject: jsonFilesArray)
                {
                    String dataString = (String) stringObject;
                    soundFileContent.add(dataString);
                }

                fileInputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
            }
            else
            {
                return soundFileContent;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return soundFileContent;
    }

    private Object getOrderObject(JSONArray jsonOrderArray, String elementName)
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
                exception.printStackTrace();
            }
        }

        return orderObjectToBeDeleted;
    }

    private JSONArray getJsonOrderArray(JSONObject mainJsonObject, String stringDate, String elementName)
    {
        JSONArray jsonOrderArray;

        jsonOrderArray = (JSONArray) mainJsonObject.get(Constants.ORDER);

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

    public String getNewListName(JSONArray jsonArray)
    {
        String newListName = null;

        int i = 0;
        while (newListName == null)
        {
            newListName = getNewListName(i, jsonArray);
            i++;
        }

        return newListName;
    }

    private String getNewListName(int number, JSONArray jsonArray)
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
            exception.printStackTrace();
            return null;
        }
    }

    public List<String> getListOfLists() {
        return listOfLists;
    }

    public List<String> getListOfWords() {
        return listOfWords;
    }

    public List<List<String>> getContentOfGivenWord()
    {
        return contentOfGivenWord;
    }

    public int getCurrentListIndex() {
        return currentListIndex;
    }

    public int getCurrentWordIndex() {
        return currentWordIndex;
    }

    public String getCurrentListName() {
        return currentListName;
    }

    public List<Integer> getIndexesOfFilesWhichNamesShouldBeChanged()
    {
        return indexesOfFilesWhichNamesShouldBeChanged;
    }
}
