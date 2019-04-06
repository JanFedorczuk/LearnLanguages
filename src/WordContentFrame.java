package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WordContentFrame extends ComplexFrame
{
    private JTextArea wordsContentTextArea;
    private JScrollPane wordsContentScrollPane;

    private List<List<String>> wordAndItsContent;

    public WordContentFrame(List<List<String>> wordAndItsContent)
    {
        this.wordAndItsContent = wordAndItsContent;

        createUI();
        setUIOptions();
        displayUI();
    }

    private void createUI()
    {
        size = getSizeForVisualPurposes();

        int fontSize     = (int) (size * Constants.fontMultiplier);

        int mainPanelWidth  = (int) (size * 2.4);
        int mainPanelHeight = (int) (size * 2.4);

        Font font  = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension mainPanelDimension        = new Dimension(mainPanelWidth, mainPanelHeight);

        JPanel mainPanel = getNewPanel(mainPanelDimension,null);

        wordsContentTextArea = getNewJTextArea(font, true, true);
        wordsContentTextArea.setEditable(false);

        wordsContentScrollPane = getNewScrollPane(mainPanelDimension, wordsContentTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        fillTextArea();

        mainPanel.add(wordsContentScrollPane);
        add(mainPanel);

        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(Constants.DIMENSION.width - (int)(size * 2.4), 0);

        this.setResizable(false);
    }

    private void fillTextArea()
    {
        for(List<String> stringList: wordAndItsContent)
        {
            for(String string: stringList)
            {
                wordsContentTextArea.append(string + "\n");
            }
            
            wordsContentTextArea.append("\n");
        }
    }
}
