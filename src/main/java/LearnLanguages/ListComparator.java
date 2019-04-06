package LearnLanguages;

import java.util.Comparator;
import java.util.List;

public class ListComparator implements Comparator<List<List<String>>>
{
    public int compare(List<List<String>> firstList, List<List<String>> secondList)
    {
        String firstListString = firstList.get(0).get(0);
        String secondListString = secondList.get(0).get(0);

        return firstListString.compareTo(secondListString);
    }
}
