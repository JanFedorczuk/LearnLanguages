package LearnLanguages;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoundFilesManager
{
    private SoundFilesManager soundFilesManager = this;

    private List<List<String>> soundFilesContent = new ArrayList<>();

    private JsonFilesManager jsonFilesManager;

    private Thread thread;

    private Object exceptionObject = null;

    private boolean createOneFile;

    private boolean doFilesHaveIdenticalOrder;

    private List<List<Boolean>> comparisonList;

    private List<Integer> indexesOfFilesWhichNamesShouldBeChanged;

    public SoundFilesManager(JsonFilesManager jsonFilesManager)
    {

        this.jsonFilesManager = jsonFilesManager;
    }

    public void startANewThread(ProgressDialog progressDialog, JFrame frame)
    {
        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String folderPath = "";

                try
                {
                    thread.sleep(10);

                    folderPath = jsonFilesManager.getCurrentListName() + "/" +
                            jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex()) + "/" +
                            Constants.SOUND_FILES;

                    thread.sleep(10);

                    if(!jsonFilesManager.checkIfFolderExists(folderPath))
                    {
                        jsonFilesManager.createFolder(folderPath);
                    }

                    thread.sleep(10);

                    progressDialog.setProgressBarMaximum(getSoundFilesContent().size());

                    thread.sleep(10);

                    boolean savedCreationOption = jsonFilesManager.getFilesBulkCreationValue(
                            jsonFilesManager.getCurrentListName() + "/" +
                                     jsonFilesManager.getListOfWords().get
                                     (jsonFilesManager.getCurrentWordIndex()) + "/");

                    List<String> filesNames = jsonFilesManager.getFilesNamesJsonArray(jsonFilesManager.
                            getCurrentListName() + "/" + jsonFilesManager.getListOfWords().get(jsonFilesManager.
                            getCurrentWordIndex()) + "/");

                    List<List<String>> filesNamesAndItsFutureNames = new ArrayList<>();
                    List<String> currentFilesNames = new ArrayList<>();
                    List<String> futureFilesNames = new ArrayList<>();

                    thread.sleep(10);

                    for(List<String> list: soundFilesContent)
                    {
                        String text = list.get(1);
                        String language = list.get(2);
                        String languageCode = getLanguage(language);

                        thread.sleep(10);

                        int index = soundFilesContent.indexOf(list);

                        boolean anyFileHasDifferentContent =
                                !jsonFilesManager.getIfAllListsValuesAtIndexAreTrue(comparisonList, 1);
                        boolean anyFileHasDifferentLanguage =
                                !jsonFilesManager.getIfAllListsValuesAtIndexAreTrue(comparisonList, 2);

                        thread.sleep(10);

                        if((!comparisonList.get(index).get(1)) || (!comparisonList.get(index).get(2)) ||
                                (!createOneFile && savedCreationOption) ||
                                ((anyFileHasDifferentContent || anyFileHasDifferentLanguage ||
                                        !doFilesHaveIdenticalOrder) && (createOneFile && savedCreationOption)) ||
                                ((index == indexesOfFilesWhichNamesShouldBeChanged.get(index)) &&
                                        (createOneFile || savedCreationOption)))
                        {

                            thread.sleep(10);

                            String fileAddress = createNewMp3FileAddress(text, languageCode);

                            thread.sleep(10);

                            createSoundFile(fileAddress, folderPath, list.get(0));

                            thread.sleep(10);

                            progressDialog.setProgressLabelText((getSoundFilesContent().indexOf(list) + 1) + " z " +
                                    getSoundFilesContent().size());

                            progressDialog.setProgressBarValue(getSoundFilesContent().indexOf(list) + 1);

                            progressDialog.repaint();

                            thread.sleep(10);
                        }
                        else
                        {
                            thread.sleep(10);

                            if(comparisonList.get(index).get(0).equals(false) &&
                                    comparisonList.get(index).get(0).equals(false) &&
                                    comparisonList.get(index).get(0).equals(false))
                            {
                                thread.sleep(10);

                                String pathString = jsonFilesManager.getCurrentListName() + "/" +
                                        jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex()) + "/";

                                String wavFilePath = pathString + "/" + Constants.SOUND_FILES + "/" +
                                        filesNames.get(indexesOfFilesWhichNamesShouldBeChanged.get(index));

                                String newWavFilePath = pathString + "/" + Constants.SOUND_FILES + "/" +
                                        soundFilesContent.get(index).get(0);

                                thread.sleep(10);

                                if(jsonFilesManager.checkIfFolderExists(wavFilePath + Constants.WAV_SUFFIX))
                                {
                                    thread.sleep(10);

                                    currentFilesNames.add(Constants.PROGRAM_LOCATION + wavFilePath + Constants.FILE_NAME_TO_BE_CHANGED_SOON + Constants.WAV_SUFFIX);
                                    futureFilesNames.add(Constants.PROGRAM_LOCATION + newWavFilePath + Constants.WAV_SUFFIX);

                                    thread.sleep(10);

                                    jsonFilesManager.renameFile(Constants.PROGRAM_LOCATION + wavFilePath + Constants.WAV_SUFFIX,
                                            Constants.PROGRAM_LOCATION + wavFilePath + Constants.FILE_NAME_TO_BE_CHANGED_SOON + Constants.WAV_SUFFIX);

                                }
                            }
                        }
                    }

                    thread.sleep(10);

                    filesNamesAndItsFutureNames.add(currentFilesNames);
                    filesNamesAndItsFutureNames.add(futureFilesNames);

                    thread.sleep(10);

                    if(filesNamesAndItsFutureNames.get(0).size() != 0 &&
                            filesNamesAndItsFutureNames.get(1).size() != 0)
                    {
                        thread.sleep(10);

                        for(int i = 0; i < filesNamesAndItsFutureNames.get(0).size(); i++)
                        {
                            thread.sleep(10);

                            jsonFilesManager.renameFile(filesNamesAndItsFutureNames.get(0).get(i),
                                    filesNamesAndItsFutureNames.get(1).get(i));
                        }
                    }

                    thread.sleep(10);

                    String pathStringForJsonFiles = jsonFilesManager.getCurrentListName() + "/" +
                            jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex()) + "/";

                    String pathStringForSoundFiles = jsonFilesManager.getCurrentListName() + "/" +
                            jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex()) + "/" +
                            Constants.SOUND_FILES;

                    thread.sleep(10);

                    File folder = new File(Constants.PROGRAM_LOCATION + folderPath);
                    File[] arrayOfFiles = folder.listFiles();
                    ArrayList<File> listOfFilesInFolder = new ArrayList<>(Arrays.asList(arrayOfFiles));

                    deleteUnnecessaryWavFiles(listOfFilesInFolder);

                    if(getIfFileCreationOptionIsCreateBulkFiles())
                    {
                        progressDialog.setProgressLabelText(Constants.CREATE_BULK_FILE);

                        thread.sleep(10);
                        mergeWavFiles(pathStringForSoundFiles);
                    }

                    thread.sleep(10);

                    jsonFilesManager.saveSoundFilesContent(pathStringForJsonFiles, soundFilesManager);

                    thread.sleep(10);

                    jsonFilesManager.saveSoundFilesNamesAndSettings
                            (pathStringForJsonFiles, soundFilesManager, createOneFile);

                    thread.sleep(10);

                    progressDialog.dispose();

                    new InformationDialog(Constants.INFORMATION, Constants.SOUND_FILES_STATE_HAS_BEEN_CHANGED,
                            Constants.CLICK_OK_TO_CONTINUE, null, frame);

                }
                catch (Exception exception)
                {
                    exception.printStackTrace();

                    if(exceptionObject == null)
                    {
                        exceptionObject = exception;
                    }

                    thread.interrupt();

                    if(jsonFilesManager.checkIfFolderExists(folderPath))
                    {
                        jsonFilesManager.deleteFileAndAllItsContent(folderPath);
                    }

                    progressDialog.dispose();


                    if(exceptionObject.getClass().getCanonicalName().equals
                            (Constants.JAVA_LANG_INTERRUPTED_EXCEPTION))
                    {
                        new InformationDialog(Constants.INFORMATION, Constants.ADDING_FILES_CANCELLED,
                                Constants.CLICK_OK_TO_CONTINUE, null,null);
                    }
                    else
                    {
                        new InformationDialog(Constants.INFORMATION, Constants.NO_INTERNET_CONNECTION,
                                Constants.CLICK_OK_TO_CONTINUE, null,null);
                    }

                    exceptionObject = null;
                }
            }
        });

        thread.start();
    }

    public void interruptThread()
    {

        thread.interrupt();
    }

    public String sendPostRequestAndReturnId(String text, String language)
    {
        String id = "";
        try
        {
            Thread.sleep(10);

            URL url = new URL(Constants.SOUND_OF_TEXT_API_ADDRESS_HTTPS);

            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestProperty(Constants.CONTENT_TYPE, Constants.HEADER);
            httpsURLConnection.setRequestMethod(Constants.POST);

            JSONObject data   = new JSONObject();
            JSONObject body = new JSONObject();

            data.put(Constants.TEXT, text);
            data.put(Constants.VOICE, language);

            body.put(Constants.ENGINE, Constants.GOOGLE);
            body.put(Constants.DATA, data);

            Thread.sleep(10);

            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(body.toString().getBytes(Constants.ENCODING));
            outputStream.flush();
            outputStream.close();

            int httpResult = httpsURLConnection.getResponseCode();

            Thread.sleep(10);

            if (httpResult == HttpURLConnection.HTTP_OK)
            {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpsURLConnection.getInputStream(), Constants.ENCODING));

                String line = null;
                StringBuilder stringBuilder = new StringBuilder();

                Thread.sleep(10);

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                id = getData(stringBuilder.toString());

                bufferedReader.close();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            if(exceptionObject == null)
            {
                exceptionObject = exception;
            }

            Thread.currentThread().interrupt();
        }

        return id;
    }

    public String sendGetRequestAndReturnFileAddress(String id)
    {
        String address = "";

        try
        {
            Thread.sleep(10);

            URL url = new URL(Constants.SOUND_OF_TEXT_API_ADDRESS_HTTP + id);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(Constants.GET);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Thread.sleep(10);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.flush();
            outputStream.close();

            int httpResult = httpURLConnection.getResponseCode();

            Thread.sleep(10);

            if (httpResult == httpURLConnection.HTTP_OK)
            {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), Constants.ENCODING));

                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                Thread.sleep(10);

                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line);
                }

                address = stringBuilder.toString();

                bufferedReader.close();
            }
            else
            {
                Thread.sleep(10);

                String urlAddress = httpURLConnection.getHeaderField(Constants.LOCATION);

                do
                {
                    address = sendAdditionalGetRequest(urlAddress, httpURLConnection);

                    Thread.sleep(5000);
                }
                while (address.equals(Constants.PENDING) || address.equals("") || address.equals(" "));
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            if(exceptionObject == null)
            {
                exceptionObject = exception;
            }
            Thread.currentThread().interrupt();
        }

        return address;
    }

    public String sendAdditionalGetRequest(String urlAddress, HttpURLConnection httpURLConnection)
    {
        String address = "";

        try
        {
            URL newURL = new URL(urlAddress);
            httpURLConnection = (HttpURLConnection) newURL.openConnection();
            httpURLConnection.setRequestMethod(Constants.GET);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            int newResult = httpURLConnection.getResponseCode();

            BufferedReader newReader;

            if (newResult == httpURLConnection.HTTP_OK)
            {
                newReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), Constants.ENCODING));

                StringBuilder newStringBuilder = new StringBuilder();
                String newLine;

                while ((newLine = newReader.readLine()) != null)
                {
                    newStringBuilder.append(newLine);
                    address = getData(newStringBuilder.toString());
                }

                newReader.close();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return address;
    }

    public String createNewMp3FileAddress(String text, String language)
    {
        String address = sendGetRequestAndReturnFileAddress(sendPostRequestAndReturnId(text, language));

        return address;
    }

    public void mergeWavFiles(String folderPath)
    {
        try
        {
            boolean hasMergingStarted = false;

            if(!jsonFilesManager.checkIfFolderExists(folderPath))
            {
                jsonFilesManager.createFolder(folderPath);
            }

            File folder;

            File[] arrayOfFiles;

            List<File> listOfFilesInFolder = new ArrayList<>();

            List<File> workingListOfFiles = new ArrayList<>();

            do
            {
                if(!hasMergingStarted)
                {
                    folder = new File(Constants.PROGRAM_LOCATION + folderPath);
                    arrayOfFiles = folder.listFiles();
                    listOfFilesInFolder = new ArrayList<>(Arrays.asList(arrayOfFiles));

                    removeFoldersFromList(listOfFilesInFolder);

                    workingListOfFiles = createListOfSortedFiles(listOfFilesInFolder);
                }
                else
                {
                    workingListOfFiles = new ArrayList<>(listOfFilesInFolder);
                }

                if(listOfFilesInFolder.size() != 1)
                {
                    List<File> temporaryListOfFiles = new ArrayList<>();

                    int listSize = workingListOfFiles.size();

                    for (int i = 0; i < listSize - 1;)
                    {
                        if((i) <= (listSize - 1))
                        {
                            AudioInputStream firstAudioInputStream = AudioSystem.getAudioInputStream((workingListOfFiles.get(0)));
                            AudioInputStream secondAudioInputStream = AudioSystem.getAudioInputStream(workingListOfFiles.get(1));

                            AudioInputStream appendedFiles = new AudioInputStream(
                                    new SequenceInputStream(firstAudioInputStream, secondAudioInputStream),
                                    firstAudioInputStream.getFormat(), firstAudioInputStream.getFrameLength() +
                                    secondAudioInputStream.getFrameLength());

                            String newFileName;

                            int number = - 1;

                            do
                            {
                                newFileName = getNewNameForWavFile(Constants.FILE, number);
                                number = number + 1;
                            }
                            while(jsonFilesManager.checkIfFolderExists(folderPath + "/" + newFileName +
                                    Constants.WAV_SUFFIX));

                            AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE,
                                    new File(Constants.PROGRAM_LOCATION + folderPath + "/" + newFileName +
                                            Constants.WAV_SUFFIX));

                            firstAudioInputStream.close();
                            secondAudioInputStream.close();

                            Files.delete(workingListOfFiles.get(0).toPath());
                            Files.delete(workingListOfFiles.get(1).toPath());

                            workingListOfFiles.remove(0);

                            workingListOfFiles.remove(0);

                            temporaryListOfFiles.add(new File(Constants.PROGRAM_LOCATION + folderPath + "/" +
                                    newFileName + Constants.WAV_SUFFIX));

                            if(workingListOfFiles.size() == 1)
                            {
                                temporaryListOfFiles.add(workingListOfFiles.get(0));
                            }

                            i = i + 2;
                        }
                    }

                    listOfFilesInFolder = new ArrayList<>(temporaryListOfFiles);
                }

                hasMergingStarted = true;
            }
            while(listOfFilesInFolder.size() != 1);

           if(listOfFilesInFolder.size() == 1)
           {
               String wordName = jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex());

               jsonFilesManager.renameFile(listOfFilesInFolder.get(0).getAbsolutePath(),
                       Constants.PROGRAM_LOCATION + jsonFilesManager.getCurrentListName() + "/" + wordName + "/" +
                       Constants.SOUND_FILES + "/" + wordName + Constants.WAV_SUFFIX);
           }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void deleteUnnecessaryWavFiles(List<File> listOfFilesInFolder)
    {
        List<File> listOfSortedFilesInFolder = new ArrayList<>();

        for(List<String> list: soundFilesContent)
        {
            for (File file: listOfFilesInFolder)
            {
                if (list.get(0).equals(getFileName(file)))
                {
                    listOfSortedFilesInFolder.add(file);
                }
            }
        }

        for(File file: listOfFilesInFolder)
        {
            try
            {
                if(!listOfSortedFilesInFolder.contains(file))
                {
                    jsonFilesManager.deleteFile(file.toString());
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }

    public List<File> createListOfSortedFiles(List<File> listOfFilesInFolder)
    {
        List<File> listOfSortedFilesInFolder = new ArrayList<>();

        for(List<String> list: soundFilesContent)
        {
            for (File file: listOfFilesInFolder)
            {
                if (list.get(0).equals(getFileName(file)))
                {
                    listOfSortedFilesInFolder.add(file);
                }
            }
        }

        return  listOfSortedFilesInFolder;
    }

    public void removeFoldersFromList(List<File> listOfFilesInFolder)
    {
        List<Integer> indexesOfElementsToBeRemoved = new ArrayList<>();

        for (int i = 0; i <= listOfFilesInFolder.size() - 1; i++)
        {
            if (listOfFilesInFolder.get(i).isDirectory())
            {
                indexesOfElementsToBeRemoved.add(i);
            }
        }

        for(int integer: indexesOfElementsToBeRemoved)
        {
            listOfFilesInFolder.remove(integer);
        }
    }

    public void createSoundFile(String urlString, String folderPath, String fileName)
    {
        try
        {
            URL url = new URL(urlString);

            File dstFile = new File(Constants.PROGRAM_LOCATION + folderPath +  "/" +
                    fileName + Constants.WAV_SUFFIX);

            MpegAudioFileReader mpegAudioFileReader = new MpegAudioFileReader();

            AudioInputStream firstAudioInputStream = mpegAudioFileReader.getAudioInputStream(url);

            AudioFormat baseFormat = firstAudioInputStream.getFormat();

            AudioFormat audioFormat = new AudioFormat(44100,16, 2, true,
                    true);

            AudioFileFormat audioFileFormat = new AudioFileFormat
                    (AudioFileFormat.Type.WAVE, audioFormat,AudioSystem.NOT_SPECIFIED);

            AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(),
                    16, baseFormat.getChannels(), baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(), false);

            AudioInputStream mainAudioInputStream = AudioSystem.getAudioInputStream(targetFormat, firstAudioInputStream);


            if (AudioSystem.isFileTypeSupported(audioFileFormat.getType(),
                    mainAudioInputStream))
            {
                AudioSystem.write(mainAudioInputStream, audioFileFormat.getType(), dstFile);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void addNewFileContent(int index)
    {
        List<Boolean> booleans = new ArrayList<>();
        String string = Constants.FILE + index;
        if(soundFilesContent.size() != 0)
        {
            for(List<String> list: soundFilesContent)
            {
                if(list.get(0).contains(string))
                {
                    booleans.add(false);
                }
            }
            if(booleans.contains(false))
            {
                addNewFileContent(index + 1);
            }
            else
            {
                List<String> list = new ArrayList<>();
                list.add(Constants.FILE + index);
                list.add("");
                list.add("");
                soundFilesContent.add(list);
            }
        }
        else
        {
            List<String> list = new ArrayList<>();
            list.add(Constants.FILE + index);
            list.add("");
            list.add("");
            soundFilesContent.add(list);
        }
    }

    public void deleteFileContent(int index)
    {

        soundFilesContent.remove(index);
    }

    public void moveFileContentUp(int index)
    {
        List<List<String>> copyOfWordAndItsContent = new ArrayList<>(soundFilesContent);

        soundFilesContent.set(index, copyOfWordAndItsContent.get(index - 1));
        soundFilesContent.set(index - 1, copyOfWordAndItsContent.get(index));
    }

    public void moveFileContentDown(int index)
    {
        List<List<String>> newWordAndItsContent = new ArrayList<>(soundFilesContent);

        soundFilesContent.set(index, newWordAndItsContent.get(index + 1));
        soundFilesContent.set(index + 1, newWordAndItsContent.get(index));
    }

    public void modifyFileName(int index, String text)
    {

        soundFilesContent.get(index).set(0, text);


    }

    public void modifyFileContent(int index, String text)
    {

        soundFilesContent.get(index).set(1, text);
    }

    public void modifyFileLanguage(int index, String text)
    {

        soundFilesContent.get(index).set(2, text);
    }

    private String getData(String dataString)
    {
        String data = dataString;

        data = data.replaceAll(Constants.BACKSLASH_AND_QUOTATION_MARK, "'");

        Pattern pattern = Pattern.compile(Constants.ALL_BETWEEN_APOSTROPHES);
        Matcher matcher = pattern.matcher(data);

        while (matcher.find())
        {
            data = matcher.group(0);
        }

        data = data.replaceAll(Constants.APOSTROPHE, "");

        return data;
    }

    public String getLanguage(String language)
    {
        if(language.equals(Constants.ENGLISH_LANGUAGE))
        {
            return Constants.ENGLISH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.POLISH_LANGUAGE))
        {
            return Constants.POLISH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.RUSSIAN_LANGUAGE))
        {
            return Constants.RUSSIAN_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.FRENCH_LANGUAGE))
        {
            return Constants.FRENCH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.ITALIAN_LANGUAGE))
        {
            return Constants.ITALIAN_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.SPANISH_LANGUAGE))
        {
            return Constants.SPANISH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.PORTUGAL_LANGUAGE))
        {
            return Constants.PORTUGAL_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.MODERN_GREEK_LANGUAGE))
        {
            return Constants.MODERN_GREEK_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.ANCIENT_GREEK_LANGUAGE))
        {
            return Constants.MODERN_GREEK_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.LATIN_LANGUAGE))
        {
            return Constants.LATIN_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.GERMAN_LANGUAGE))
        {
            return Constants.GERMAN_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.CZECH_LANGUAGE))
        {
            return Constants.CZECH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.DUTCH_LANGUAGE))
        {
            return Constants.DUTCH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.HUNGARIAN_LANGUAGE))
        {
            return Constants.HUNGARIAN_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.TURKISH_LANGUAGE))
        {
            return Constants.TURKISH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.FINNISH_LANGUAGE))
        {
            return Constants.FINNISH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.SWEDISH_LANGUAGE))
        {
            return Constants.SWEDISH_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.AFRIKAANS_LANGUAGE))
        {
            return Constants.AFRIKAANS_LANGUAGE_API_CODE;
        }
        else if(language.equals(Constants.CROATIAN_LANGUAGE))
        {
            return Constants.CROATIAN_LANGUAGE_API_CODE;
        }
        else
        {
            return null;
        }
    }

    public List<List<String>> getSoundFilesContent()
    {
        return soundFilesContent;
    }

    public int getSoundFilesContentSize()
    {

        return soundFilesContent.size();
    }

    private String getNewNameForWavFile(String name, int number)
    {
        name = name + getNewNumber(number);

        return name;
    }

    private int getNewNumber(int number)
    {
        number = number + 1;

        return number;
    }

    public boolean getIfFileCreationOptionIsCreateBulkFiles()
    {

        return createOneFile;
    }

    public String getFileName(File file)
    {
        String fileName = "";

        Pattern pattern;
        String replacement;

        if(file.getAbsolutePath().contains("/"))
        {
            pattern = Pattern.compile((Constants.LINUX_FILE_PATTERN));
            replacement = "/";

        }
        else
        {
            pattern = Pattern.compile((Constants.WINDOWS_FILE_PATTERN));
            replacement = "\\";
        }

        Matcher matcher = pattern.matcher(file.getAbsolutePath());
        while(matcher.find())
        {
            fileName = matcher.group(0);
            fileName = fileName.replace(replacement, "").replace(Constants.WAV_SUFFIX, "");
        }

        return fileName;
    }

    public void setFilesCreationOption(Boolean booleanValue)
    {

        createOneFile = booleanValue;
    }

    public void setSoundFilesContent(List<List<String>> loadedFilesContent)
    {
        this.soundFilesContent = loadedFilesContent;
    }

    public void setIndexesOfFilesWhichNamesShouldBeChanged(List<Integer> indexesOfFilesWhichNamesShouldBeChanged)
    {
        this.indexesOfFilesWhichNamesShouldBeChanged = indexesOfFilesWhichNamesShouldBeChanged;
    }

    public void setComparisonList(List<List<Boolean>> comparisonList)
    {

        this.comparisonList = comparisonList;
    }

    public void setIfFilesHaveIdenticalOrder(Boolean booleanValue)
    {

        this.doFilesHaveIdenticalOrder = booleanValue;
    }
}
