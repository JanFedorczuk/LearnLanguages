package LearnLanguages;

import org.jsoup.Jsoup;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator
{
    private String word;
    private String languageToBeTranslated;

    private List<List<String>> wordAndItsContent = new ArrayList<>();

    private TranslatorRunnable translatorRunnable = new TranslatorRunnable();

    public Translator(String word, String languageToBeTranslated)
    {
        this.word = word;
        this.languageToBeTranslated = languageToBeTranslated;
    }

    public List<List<String>> translateWordAndReturnWordAndItsContent()
    {
        String dataString;
        List<String> dataList;

        dataString = translatorRunnable.getDataString();
        dataList = parseStringContainingHTMLAndReturnDataList(dataString);
        wordAndItsContent = fillWordAndItsContent(dataList);

        return wordAndItsContent;
    }

    private List<String> parseStringContainingHTMLAndReturnDataList(String dataString)
    {
        String[] stringPartsStartingFrom = dataString.split("\\(" + languageToBeTranslated + "\\)");

        String[] stringPartsEndingAt = stringPartsStartingFrom[1].split(Constants.SOURCES);

        dataString = stringPartsEndingAt[0];

        Pattern dataPattern = Pattern.compile(Constants.FIRST_PATTERN_REGEX);
        Matcher dataMatcher = dataPattern.matcher(dataString);
        List<String> dataList = new ArrayList<>();

        while (dataMatcher.find())
        {
            String dataMatcherString = Jsoup.parse(dataMatcher.group(0)).text();
            dataList.add(dataMatcherString);
        }

        return dataList;
    }

    private List<List<String>> fillWordAndItsContent(List<String> dataList)
    {
        List<String> stringList = new ArrayList<>();

        int counter = 0;
        while (counter < dataList.size())
        {
            if(dataList.get(counter).contains(Constants.MEANINGS))
            {
                for (int decreasingCounter = counter - 1; decreasingCounter >= 0; decreasingCounter--)
                {
                    dataList.remove(decreasingCounter);
                }
            }
            counter++;
        }

        Pattern categoryPattern = Pattern.compile(Constants.SECOND_PATTERN_REGEX);

        for (counter = 0; counter < dataList.size(); counter++)
        {
            String firstCharacters = dataList.get(counter).substring(0, 5);
            firstCharacters = firstCharacters.replaceAll(Constants.FIRST_REGEX_FOR_MATCHING, "");
            try
            {
                int number = Integer.parseInt(firstCharacters);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();

                firstCharacters = "";
            }

            if (firstCharacters == "")
            {
                Matcher categoryMatcher = categoryPattern.matcher(dataList.get(counter));
                if (categoryMatcher.find())
                {
                    stringList.add(categoryMatcher.group(0));
                    String newString = dataList.get(counter).replace(categoryMatcher.group(0), "").
                            replace(Constants.SECOND_REGEX_FOR_MATCHING, "");

                    if (!newString.isEmpty())
                    {
                        String helpString = newString;
                        Pattern bracketWithNumbersPattern = Pattern.compile(Constants.THIRD_PATTERN_REGEX);
                        Matcher bracketWithNumbersMatcher = bracketWithNumbersPattern.matcher(helpString);
                        while (bracketWithNumbersMatcher.find())
                        {
                            helpString = helpString.replace(bracketWithNumbersMatcher.group(0), "");
                        }

                        if (!helpString.isEmpty())
                        {
                            stringList.add(newString);
                        }
                    }
                }
                else
                {
                    String newString = dataList.get(counter);
                    String helpString = newString;
                    Pattern bracketWithNumbersPattern = Pattern.compile(Constants.THIRD_PATTERN_REGEX);
                    Matcher bracketWithNumbersMatcher = bracketWithNumbersPattern.matcher(helpString);

                    while (bracketWithNumbersMatcher.find())
                    {
                        helpString = helpString.replace(bracketWithNumbersMatcher.group(0), "");
                    }

                    if (!helpString.isEmpty())
                    {
                        stringList.add(newString);
                    }
                }
            }

            else
            {
                String newString = dataList.get(counter);
                String helpString = newString;
                Pattern bracketWithNumbersPattern = Pattern.compile(Constants.THIRD_PATTERN_REGEX);
                Matcher bracketWithNumbersMatcher = bracketWithNumbersPattern.matcher(helpString);

                while (bracketWithNumbersMatcher.find())
                {
                    helpString = helpString.replace(bracketWithNumbersMatcher.group(0), "");
                }

                if (!helpString.isEmpty())
                {
                    stringList.add(newString.replaceAll(Constants.COLON, Constants.SEMICOLON));
                }
            }
        }

        List<String> garbageList = new ArrayList<>();

        for (counter = 0; counter < stringList.size(); counter++)
        {
            if (counter < stringList.size() - 1)
            {
                Matcher categoryMatcher = categoryPattern.matcher(stringList.get(counter));
                if (categoryMatcher.find())
                {
                    Matcher matcherCheckingNextElementForBeingCategory = categoryPattern.matcher(stringList.get(counter + 1));
                    if (matcherCheckingNextElementForBeingCategory.find())
                    {
                        garbageList.add(stringList.get(counter));
                    }
                }
            }
            else
            {
                Matcher categoryMatcher = categoryPattern.matcher(stringList.get(counter));

                if (categoryMatcher.find())
                {
                    garbageList.add(stringList.get(counter));
                }
            }
        }

        stringList.removeAll(garbageList);

        List<String> categoryList = new ArrayList<>();

        categoryList.add(word);
        wordAndItsContent.add(new ArrayList<>(categoryList));

        for (counter = 0; counter < stringList.size(); counter++)
        {
            if (counter < stringList.size() - 1)
            {
                Matcher categoryMatcher = categoryPattern.matcher(stringList.get(counter));
                if (categoryMatcher.find())
                {
                    if (counter != 0)
                    {
                        wordAndItsContent.add(new ArrayList<>(categoryList));
                        categoryList.clear();
                    }
                    categoryList = new ArrayList<>();
                    categoryList.add(categoryMatcher.group(0));
                }
                else
                {
                    categoryList.add(stringList.get(counter));
                }
            }
            else
            {
                categoryList.add(stringList.get(counter));
                wordAndItsContent.add(new ArrayList<>(categoryList));
            }
        }

        String decodedWord = returnDecodedWord();
        wordAndItsContent.get(0).set(0, decodedWord);

        return wordAndItsContent;
    }

    private String returnDecodedWord()
    {
        String wordToBeDecoded = "";
        try
        {
            wordToBeDecoded = URLDecoder.decode(wordAndItsContent.get(0).get(0), Constants.ENCODING);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return wordToBeDecoded;
    }

    public Boolean checkIfWordExists()
    {
        translatorRunnable = new TranslatorRunnable();
        translatorRunnable.setValues(word, languageToBeTranslated);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(translatorRunnable);
        executor.shutdown();

        try
        {
            future.get(Constants.RUNNABLE_TIMEOUT, TimeUnit.SECONDS);
        }
        catch (InterruptedException interruptedException)
        {
            interruptedException.printStackTrace();
        }
        catch (ExecutionException executionException)
        {
            executionException.printStackTrace();
        }
        catch (TimeoutException timeOutException)
        {
            timeOutException.printStackTrace();
        }

        return translatorRunnable.getBooleanValue();
    }

    public TranslatorRunnable getTranslatorRunnable()
    {
        return translatorRunnable;
    }
}