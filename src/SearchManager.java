package LearnLanguages;

import java.util.ArrayList;
import java.util.List;

public class SearchManager
{
    private String phrase;
    private ListOfWordsFrame listOfWordsFrame;

    public SearchManager(String phrase, ListOfWordsFrame listOfWordsFrame)
    {
        this.phrase = phrase;
        this.listOfWordsFrame = listOfWordsFrame;
    }

    public List<Object> returnListOfFoundElementsAndTheirIndexes()
    {
        List<Object> listOfElementsAndTheirIndexes = returnListOfElementsAndTheirIndexes();

        List<String> elementsList = (List<String>) listOfElementsAndTheirIndexes.get(0);
        List<Pair> indexList = (List<Pair>) listOfElementsAndTheirIndexes.get(1);

        List<String> foundElementsList = new ArrayList<>();
        List<Pair> foundIndexes = new ArrayList<>();

        for(int i = 0; i <= elementsList.size() - 1; i++)
        {
            if(elementsList.get(i).equalsIgnoreCase(phrase) ||
                    elementsList.get(i).toLowerCase().contains(phrase.toLowerCase()))
            {
                foundElementsList.add(elementsList.get(i));
                foundIndexes.add(indexList.get(i));
            }
        }

        List<Object> listOfFoundElementsAndTheirIndexes = new ArrayList<>();

        if(!foundElementsList.isEmpty() && !foundIndexes.isEmpty())
        {
            listOfFoundElementsAndTheirIndexes.add(foundElementsList);
            listOfFoundElementsAndTheirIndexes.add(foundIndexes);
        }

        return listOfFoundElementsAndTheirIndexes;
    }

    private List<Object> returnListOfElementsAndTheirIndexes()
    {
        listOfWordsFrame.getJsonFilesManager().setListOfLists();

        List<String> listOfLists = listOfWordsFrame.getJsonFilesManager().getListOfLists();

        List<String> elementsList = new ArrayList<>();
        List<Pair> indexList = new ArrayList<>();

        for(int listIndex = 0; listIndex < listOfLists.size(); listIndex++)
        {
            indexList.add(new Pair(listIndex, -1));

            String listName = listOfWordsFrame.getJsonFilesManager().getListOfLists().get(listIndex);
            elementsList.add(listName);

            listOfWordsFrame.getJsonFilesManager().setCurrentListName(listName);
            listOfWordsFrame.getJsonFilesManager().setListOfWords();

            List<String> listOfWords = listOfWordsFrame.getJsonFilesManager().getListOfWords();

            for(int wordIndex = 0; wordIndex < listOfWords.size(); wordIndex++)
            {
                indexList.add(new Pair(listIndex, wordIndex));
                elementsList.add(listOfWords.get(wordIndex));
            }
        }

        List<Object> listOfElementsAndTheirIndexes = new ArrayList<>();

        listOfElementsAndTheirIndexes.add(elementsList);
        listOfElementsAndTheirIndexes.add(indexList);

        return listOfElementsAndTheirIndexes;
    }
}
