package LearnLanguages;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.*;

public class SortingManager
{
    private String objectToBeSorted;
    private String listName;
    private String typeOfSorting;

    private Boolean isThereAnyWord = false;

    private JsonFilesManager jsonFilesManager;
    private ListOfWordsFrame listOfWordsFrame;

    public SortingManager(String objectToBeSorted, String listName, String typeOfSorting,
                          JsonFilesManager jsonFilesManager, ListOfWordsFrame listOfWordsFrame)
    {
        this.objectToBeSorted = objectToBeSorted;
        this.listName = listName;
        this.typeOfSorting = typeOfSorting;
        this.jsonFilesManager = jsonFilesManager;
        this.listOfWordsFrame = listOfWordsFrame;

        if(objectToBeSorted.equals(Constants.ALL_LISTS))
        {
            sortLists();
        }
        else if(objectToBeSorted.equals(Constants.WORDS_OF_GIVEN_LIST))
        {
            sortGivenListWords();
        }
        else if(objectToBeSorted.equals(Constants.WORDS_OF_ALL_LISTS))
        {
            sortEveryListWords();
        }

        jsonFilesManager.setListOfLists();
        jsonFilesManager.setListOfWords();

        listOfWordsFrame.changeStateOfListComboBox();
    }

    private void sortLists()
    {
        jsonFilesManager.setListOfLists();

        if(checkIfTypeOfSortingIsAlphabetical())
        {
            Collections.sort(jsonFilesManager.getListOfLists(), String.CASE_INSENSITIVE_ORDER);
        }

        if(checkIfTypeOfSortingIsReserveAlphabetical())
        {
            Collections.sort(jsonFilesManager.getListOfLists(), String.CASE_INSENSITIVE_ORDER);
            Collections.reverse(jsonFilesManager.getListOfLists());
        }

        if(checkIfTypeOfSortingIsAccordingToDateOfAddition())
        {
            List<List<String>> list = jsonFilesManager.returnListContainingElementsAndDates
                    (Constants.LIST_OF_LISTS, jsonFilesManager.getListOfLists());

            List<String> elementsList = list.get(0);
            List<String> datesList = list.get(1);

            Collections.sort(datesList, new Comparator<String>()
            {
                public int compare(String firstDate, String secondDate)
                {
                    return Integer.compare(elementsList.indexOf(firstDate), elementsList.indexOf(secondDate));
                }
            });

            jsonFilesManager.getListOfLists().clear();
            jsonFilesManager.getListOfLists().addAll(elementsList);
        }

        saveLists();

        if(!jsonFilesManager.getListOfLists().isEmpty())
        {
            new InformationDialog(Constants.INFORMATION, Constants.SORTING_WAS_SUCCESSFUL,
                    Constants.CLICK_OK_TO_CONTINUE, null);
        }
        else
        {
            new InformationDialog(Constants.INFORMATION, Constants.THERE_IS_NOTHING_TO_SORT,
                    Constants.CLICK_OK_TO_CONTINUE, null);
        }
    }

    private void sortGivenListWords()
    {
        List<List<List<String>>> listOfWordsAndTheirContent = jsonFilesManager.fillListOfWordsAndTheirContent();

        if(checkIfTypeOfSortingIsAlphabetical())
        {
            listOfWordsAndTheirContent.sort(new ListComparator());
        }

        if(checkIfTypeOfSortingIsReserveAlphabetical())
        {
            listOfWordsAndTheirContent.sort(new ListComparator().reversed());
        }

        if(checkIfTypeOfSortingIsAccordingToDateOfAddition())
        {
            String listWithWordsName = listName + Constants.WORDS_SUFFIX;

            List<List<String>> list = jsonFilesManager.returnListContainingElementsAndDates
                    (listWithWordsName, jsonFilesManager.getListOfWords());

            List<String> elementsList = list.get(0);
            List<String> datesList = list.get(1);

            Collections.sort(datesList, new Comparator<String>()
            {
                public int compare(String firstDate, String secondDate)
                {
                    return Integer.compare(elementsList.indexOf(firstDate), elementsList.indexOf(secondDate));
                }
            });

            for(String elementString : elementsList)
            {
                int elementIndex = elementsList.indexOf(elementString);

                listOfWordsAndTheirContent.get(elementIndex).get(0).set(0, elementString);
            }
        }

        if(!jsonFilesManager.getListOfWords().isEmpty())
        {
            new InformationDialog(Constants.INFORMATION, Constants.SORTING_WAS_SUCCESSFUL,
                    Constants.CLICK_OK_TO_CONTINUE, null);
        }
        else
        {
            new InformationDialog(Constants.INFORMATION, Constants.THERE_IS_NOTHING_TO_SORT,
                    Constants.CLICK_OK_TO_CONTINUE, null);
        }

        saveWordsOfGivenList(listOfWordsAndTheirContent);
    }

    private void sortEveryListWords()
    {
        List<List<Object>> listContainingListNamesAndTheirWordsWithContent = new ArrayList<>();

        jsonFilesManager.setListOfLists();

        if(checkIfTypeOfSortingIsAlphabetical())
        {
            listContainingListNamesAndTheirWordsWithContent = fillListContainingListsNamesAndTheirWordsWithContent
                    (listContainingListNamesAndTheirWordsWithContent, true, false);
        }

        if(checkIfTypeOfSortingIsReserveAlphabetical())
        {
            listContainingListNamesAndTheirWordsWithContent =fillListContainingListsNamesAndTheirWordsWithContent
                    (listContainingListNamesAndTheirWordsWithContent, true,true);
        }

        if(checkIfTypeOfSortingIsAccordingToDateOfAddition())
        {
            for(String listName: jsonFilesManager.getListOfLists())
            {
                jsonFilesManager.setChosenListName(listName);

                List<List<List<String>>> listOfWordsAndTheirContent = jsonFilesManager.fillListOfWordsAndTheirContent();

                String listWithWordsName = jsonFilesManager.getChosenListName() + Constants.WORDS_SUFFIX;

                List<List<String>> list = jsonFilesManager.returnListContainingElementsAndDates
                        (listWithWordsName, jsonFilesManager.getListOfWords());

                List<String> elementsList = list.get(0);
                List<String> datesList = list.get(1);
                Collections.sort(datesList, new Comparator<String>()
                {
                    public int compare(String firstDate, String secondDate)
                    {
                        return Integer.compare(elementsList.indexOf(firstDate), elementsList.indexOf(secondDate));
                    }
                });

                List<List<List<String>>> copyOfListOfWordsAndTheirContent
                        = new ArrayList<>(listOfWordsAndTheirContent);

                for(String elementString : elementsList)
                {
                    int elementIndex = elementsList.indexOf(elementString);

                    int listIndex = 0;

                    for(int i = 0; i < copyOfListOfWordsAndTheirContent.size(); i++)
                    {
                        String element = copyOfListOfWordsAndTheirContent.get(i).get(0).get(0);

                        if(elementString.equals(element))
                        {
                            listIndex = i;
                        }
                    }

                    listOfWordsAndTheirContent.set(elementIndex, copyOfListOfWordsAndTheirContent.get(listIndex));
                }

                List<Object> insideList = new ArrayList<>();

                insideList.add(listOfWordsAndTheirContent);
                insideList.add(jsonFilesManager.getChosenListName());

                listContainingListNamesAndTheirWordsWithContent.add(insideList);
            }
        }

        saveWordsOfEveryList(listContainingListNamesAndTheirWordsWithContent);

        if(isThereAnyWord)
        {
            new InformationDialog(Constants.INFORMATION, Constants.SORTING_WAS_SUCCESSFUL,
                    Constants.CLICK_OK_TO_CONTINUE, null);
        }
        else
        {
            new InformationDialog(Constants.INFORMATION, Constants.THERE_IS_NOTHING_TO_SORT,
                    Constants.CLICK_OK_TO_CONTINUE, null);
        }
    }

    private boolean checkIfTypeOfSortingIsAlphabetical()
    {
        if(typeOfSorting == Constants.ALPHABETICAL)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkIfTypeOfSortingIsReserveAlphabetical()
    {
        if(typeOfSorting == Constants.REVERSE_ALPHABETICAL)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkIfTypeOfSortingIsAccordingToDateOfAddition()
    {
        if(typeOfSorting == Constants.DATE_OF_ADDITION)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void saveLists()
    {
        jsonFilesManager.clearJsonArray(Constants.LIST_OF_LISTS, Constants.LIST_OF_LISTS);

        for(int i = 0; i < jsonFilesManager.getListOfLists().size(); i++)
        {
            jsonFilesManager.addNewListToJSONListsFile
                    (jsonFilesManager.getListOfLists().get(i), null, false);
        }
    }

    private void saveWordsOfGivenList(List<List<List<String>>> listOfWordsAndTheirContent)
    {
        jsonFilesManager.deleteJsonFileContainingGivenList(jsonFilesManager.getChosenListName());

        String fileName = jsonFilesManager.getChosenListName() + jsonFilesManager.getWordsSuffix();

        jsonFilesManager.clearJsonArray
                (fileName, fileName);

        jsonFilesManager.createJsonFileAndAddJsonObjectWithJsonArrayToIt(jsonFilesManager.getChosenListName());

        for(List<List<String>> list : listOfWordsAndTheirContent)
        {
            jsonFilesManager.setContentOfGivenWord(list);

            jsonFilesManager.saveDataInJsonList_Content(jsonFilesManager.getChosenListName());

            jsonFilesManager.saveDataInJsonList_Words(jsonFilesManager.getChosenListName(), null,
                    false);
        }
    }

    private void saveWordsOfEveryList(List<List<Object>> listContainingListNamesAndTheirWordsWithContent)
    {
        for(List<Object> list: listContainingListNamesAndTheirWordsWithContent)
        {
            List<List<List<String>>> listOfWordsAndTheirContent = (List<List<List<String>>>) list.get(0);
            String listName = (String) list.get(1);

            jsonFilesManager.setChosenListName(listName);

            jsonFilesManager.deleteJsonFileContainingGivenList(jsonFilesManager.getChosenListName());

            String listWithWordsName = listName + Constants.WORDS_SUFFIX;
            jsonFilesManager.clearJsonArray(listWithWordsName, listWithWordsName);


            jsonFilesManager.createJsonFileAndAddJsonObjectWithJsonArrayToIt(jsonFilesManager.getChosenListName());

            for(List<List<String>> minorList : listOfWordsAndTheirContent)
            {
                jsonFilesManager.setContentOfGivenWord(minorList);

                jsonFilesManager.saveDataInJsonList_Content(jsonFilesManager.getChosenListName());
                jsonFilesManager.saveDataInJsonList_Words
                        (jsonFilesManager.getChosenListName(), null, false);
            }
        }
    }

    private List<List<Object>> fillListContainingListsNamesAndTheirWordsWithContent
            (List<List<Object>> listContainingListNamesAndTheirWordsWithContent, Boolean sort, Boolean isReversed)
    {
        for(String listName : jsonFilesManager.getListOfLists())
        {
            jsonFilesManager.setChosenListName(listName);

            List<List<List<String>>> list = new ArrayList<>();
            list = jsonFilesManager.fillListOfWordsAndTheirContent();

            if(list.size() != 0)
            {
                isThereAnyWord = true;
            }

            if(sort)
            {
                if(isReversed)
                {
                    list.sort(new ListComparator().reversed());
                }
                else
                {
                    list.sort(new ListComparator());
                }
            }

            List<Object> insideList = new ArrayList<>();

            insideList.add(list);
            insideList.add(jsonFilesManager.getChosenListName());

            listContainingListNamesAndTheirWordsWithContent.add(insideList);
        }

        return listContainingListNamesAndTheirWordsWithContent;
    }
}
