package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class WordFrame extends ComplexFrame
{
    private WordFrame wordFrame = this;

    private JPanel mainPanel;

    private JButton getPreviousWordButton;
    private JButton getNextWordButton    ;
    private JButton editWordContentButton;
    private JButton exitWordJFameButton  ;

    private boolean isActionListenerAdded = false;

    private JsonFilesManager jsonFilesManager;
    private WindowsManager windowsManager;
    private List<List<String>> wordAndItsContent;

    public WordFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager,
                     List<List<String>> wordAndItsContent)
    {
        this.jsonFilesManager = jsonFilesManager;
        this.windowsManager = windowsManager;
        this.wordAndItsContent = wordAndItsContent;

        createUI();
        setUIOptions();
        displayUI();
    }

    private void createUI()
    {
        this.setLayout(new GridBagLayout());

        int size = getSizeForVisualPurposes();

        buttonWidth      = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);
        int fontSize     = (int) (size * Constants.fontMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 4.632);
        int mainPanelHeight = (int) (size * 2.816);

        int scrollWordPanelSide = (int)Math.round(mainPanelHeight * 0.8522727);

        Dimension mainPanelDimension       = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension scrollWordPanelDimension = new Dimension(scrollWordPanelSide, scrollWordPanelSide);
        Dimension buttonPanelDimension     = new Dimension(mainPanelWidth, buttonHeight);

        Dimension buttonDimension          = new Dimension(buttonWidth, buttonHeight);

        Font buttonFont = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        GridBagConstraints gbc = getGridBagConstraints(GridBagConstraints.CENTER);

        mainPanel                  = getNewPanel(mainPanelDimension, null);
        JScrollPane scrollWordPane = getNewScrollPane(fontSize, gbc, scrollWordPanelDimension);
        JPanel buttonPanel         = createButtonPanel(gbc, buttonDimension, buttonFont, buttonPanelDimension);

        gbc.gridy = 0;
        mainPanel.add(scrollWordPane, gbc);

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, gbc);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - (int)(buttonWidth * 2.25)),
                ((Constants.DIMENSION.height / 2) - (int)(Constants.DIMENSION.height / 2.15)));

        this.setResizable(false);

        addListenersToFrame();
    }

    private JPanel createWordPanel(int fontSize, GridBagConstraints gbc)
    {
        JPanel wordPanel = getNewPanel(null, Color.WHITE);

        JLabel wordNameLabel = getNewLabel(wordAndItsContent.get(0).get(0),
                new Font(Constants.FONT_NAME, Font.BOLD, (int)(fontSize * 2.5)));

        wordPanel.add(wordNameLabel);

        List<List<String>> newWordAndItsContent = returnWordAndItsContentForVisualPurposes(wordAndItsContent);
        newWordAndItsContent.remove(0);

        int i = 1;
        for(List<String> list: newWordAndItsContent)
        {
            gbc.gridy = i;
            gbc.gridx = 0;
            JLabel categorySeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                    new Font(Constants.FONT_NAME, Font.BOLD, (int)(fontSize * 1.5)));

            wordPanel.add(categorySeparationLabel, gbc);
            i++;

            gbc.gridy = i;

            JLabel categoryNameLabel = getNewLabel(list.get(0),
                    new Font(Constants.FONT_NAME, Font.BOLD, (int)(fontSize * 1.5)));

            wordPanel.add(categoryNameLabel, gbc);
            i++;

            JLabel categoryFromElementSeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                    new Font(Constants.FONT_NAME, Font.BOLD, fontSize / 2));

            gbc.gridy = i;
            wordPanel.add(categoryFromElementSeparationLabel, gbc);
            i++;

            for(int counter = 1; counter < list.size(); counter++)
            {
                gbc.gridy = i;

                JLabel elementLabel = getNewLabel(list.get(counter),
                        (new Font(Constants.FONT_NAME, Font.BOLD, fontSize)));

                wordPanel.add(elementLabel, gbc);
                i++;

                gbc.gridy = i;

                JLabel elementSeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                        new Font(Constants.FONT_NAME, Font.BOLD, fontSize / 2));

                wordPanel.add(elementSeparationLabel, gbc);
                i++;
            }
        }

        gbc.gridy = i;

        JLabel wordSeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                new Font(Constants.FONT_NAME, Font.BOLD, fontSize));

        wordPanel.add(wordSeparationLabel, gbc);

        return wordPanel;
    }

    private JPanel createButtonPanel(GridBagConstraints gbc, Dimension buttonDimension, Font buttonFont,
                                     Dimension buttonPanelDimension)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        if(!windowsManager.getIfWordIsBeingBrowsed())
        {
            gbc.gridy = 0;
            gbc.gridx = 0;
            editWordContentButton = getNewButton(buttonDimension, Constants.EDIT, buttonFont);

            buttonPanel.add(editWordContentButton, gbc);

            gbc.gridy = 0;
            gbc.gridx = 1;
            exitWordJFameButton = getNewButton(buttonDimension, Constants.EXIT, buttonFont);

            buttonPanel.add(exitWordJFameButton, gbc);
        }
        else
        {
            gbc.gridy = 0;
            gbc.gridx = 0;
            getPreviousWordButton = getNewButton(buttonDimension, Constants.PREVIOUS_WORD, buttonFont);

            buttonPanel.add(getPreviousWordButton, gbc);

            gbc.gridy = 0;
            gbc.gridx = 1;
            getNextWordButton = getNewButton(buttonDimension, Constants.NEXT_WORD, buttonFont);

            buttonPanel.add(getNextWordButton, gbc);
        }

        return buttonPanel;
    }

    private JScrollPane getNewScrollPane(int fontSize, GridBagConstraints gbc, Dimension dimension)
    {
        JScrollPane scrollWordPane = new JScrollPane(createWordPanel(fontSize, gbc));
        scrollWordPane.setPreferredSize(dimension);

        return scrollWordPane;
    }

    private void addListenersToFrame()
    {
        if(!isActionListenerAdded)
        {
            addActionListeners();
            addKeyListeners();

            addWindowListener();

            addNavigationKeyListeners();

            isActionListenerAdded = true;
        }
    }

    private void addActionListeners()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            getPreviousWordButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    getPreviousWord();
                }
            });

            getNextWordButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    getNextWord();
                }
            });
        }
        else
        {
            editWordContentButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    editWordContent();
                }
            });

            exitWordJFameButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    exitFrame();
                }
            });
        }
    }

    private void addKeyListeners()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            getPreviousWordButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_LEFT )
                    {
                        getPreviousWord();
                    }

                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                        exitFrame();
                    }
                }
            });


            getNextWordButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_RIGHT )
                    {
                        getNextWord();
                    }

                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                        exitFrame();
                    }
                }
            });
        }
        else
        {
            editWordContentButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        editWordContent();
                    }
                }
            });

            exitWordJFameButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        exitFrame();
                    }
                }
            });
        }

        wordFrame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(windowsManager.getIfWordIsBeingBrowsed())
                {
                    int newWordIndex = 0;
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                    {
                        if(jsonFilesManager.getCurrentWordIndex() != jsonFilesManager.getListOfWords().size() - 1)
                        {
                            newWordIndex = jsonFilesManager.getCurrentWordIndex() + 1;
                        }
                    }
                    if(e.getKeyCode() == KeyEvent.VK_LEFT)
                    {
                        if(jsonFilesManager.getCurrentWordIndex() != 0)
                        {
                            newWordIndex = jsonFilesManager.getCurrentWordIndex() - 1;
                        }
                        else
                        {
                            newWordIndex = jsonFilesManager.getListOfWords().size() - 1;
                        }
                    }

                    jsonFilesManager.setCurrentWordIndex(newWordIndex);
                    jsonFilesManager.setContentOfGivenWord();
                    wordAndItsContent = jsonFilesManager.getContentOfGivenWord();

                    wordFrame.remove(mainPanel);

                    createUI();
                    setUIOptions();
                    repaint();
                }
                else
                {
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT)
                    {
                        exitWordJFameButton.requestFocus();
                    }
                    if(e.getKeyCode() == KeyEvent.VK_LEFT)
                    {
                        editWordContentButton.requestFocus();
                    }
                }
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            components.add(getPreviousWordButton);
            components.add(getNextWordButton);
        }
        else
        {
            components.add(editWordContentButton);
            components.add(exitWordJFameButton);
        }

        addNavigationKeyListenersToMainComponents(components, true, false);

    }

    private void addWindowListener()
    {
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exitFrame();
            }
        });
    }

    private void editWordContent()
    {
        wordFrame.dispose();

        new AddOrEditWordManuallyFrame(jsonFilesManager, windowsManager, wordAndItsContent).
                setVisible(true);
    }

    private void getNextWord()
    {
        int newWordIndex;
        if(jsonFilesManager.getCurrentWordIndex() != jsonFilesManager.getListOfWords().size() - 1)
        {
            newWordIndex = jsonFilesManager.getCurrentWordIndex() + 1;
            getNextWordButton.setFocusable(false);

        }
        else
        {
            newWordIndex = 0;
            getNextWordButton.setFocusable(false);
        }

        jsonFilesManager.setCurrentWordIndex(newWordIndex);
        jsonFilesManager.setContentOfGivenWord();
        wordAndItsContent = jsonFilesManager.getContentOfGivenWord();


        wordFrame.remove(mainPanel);

        createUI();
        setUIOptions();
        repaint();
    }

    private void getPreviousWord()
    {
        int newWordIndex;
        if(jsonFilesManager.getCurrentWordIndex() != 0)
        {
            newWordIndex = jsonFilesManager.getCurrentWordIndex() - 1;
            getNextWordButton.setFocusable(false);

        }
        else
        {
            newWordIndex = jsonFilesManager.getListOfWords().size() - 1;
            getPreviousWordButton.setFocusable(false);
        }

        jsonFilesManager.setCurrentWordIndex(newWordIndex);
        jsonFilesManager.setContentOfGivenWord();
        wordAndItsContent = jsonFilesManager.getContentOfGivenWord();

        wordFrame.remove(mainPanel);

        createUI();
        setUIOptions();
        repaint();
    }

    public void exitFrame()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            wordFrame.dispose();
            new ListOfWordsFrame(jsonFilesManager, windowsManager);
        }
        else
        {
            wordFrame.dispose();

            new AddOrChooseListOfWordsFrame(jsonFilesManager, windowsManager, wordAndItsContent);
        }
    }

    private List<List<String>> returnWordAndItsContentForVisualPurposes(List<List<String>> wordAndItsContent)
    {
        List<List<String>> newListOfStringLists = new ArrayList<>();
        for(List<String> List: wordAndItsContent)
        {
            List<String> newStringList = new ArrayList<>();
            for (String string: List)
            {
                if((string.length() >= Constants.NUMBER_OF_CHARACTERS_IN_LINE) && (string.contains(" ")))
                {
                    while(string.length() >= Constants.NUMBER_OF_CHARACTERS_IN_LINE)
                    {
                        for(int counter = (Constants.NUMBER_OF_CHARACTERS_IN_LINE - 1); counter > 0; counter--)
                        {
                            char charAt = string.charAt(counter);
                            if(Character.isWhitespace(charAt))
                            {
                                String firstPartOfTheNewString = string.substring(0, counter);
                                string = string.substring(counter, string.length());
                                newStringList.add(firstPartOfTheNewString);
                                if(string.length() < Constants.NUMBER_OF_CHARACTERS_IN_LINE)
                                {
                                    newStringList.add(string);
                                }
                                counter = 0;
                            }
                        }
                    }
                }
                else
                {
                    newStringList.add(string);
                }
            }
            newListOfStringLists.add(newStringList);
        }
        return newListOfStringLists;
    }
}
