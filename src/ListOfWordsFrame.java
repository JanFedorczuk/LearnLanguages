package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ListOfWordsFrame extends MultiComponentComplexFrame
{
    private ListOfWordsFrame listOfWordsFrame = this;

    private String listName;
    private JsonFilesManager jsonFilesManager;

    private JMenuBar menuBar = new JMenuBar();

    private JPanel toolBarPanel;

    private JButton addNewListButton;
    private JButton deleteListButton;
    private JButton modifyListButton;
    private JButton moveListUpButton;
    private JButton moveListDownButton;

    private JButton addNewWorButton;
    private JButton deleteWordButton;
    private JButton modifyWordButton;
    private JButton moveWordUpButton;
    private JButton moveWordDownButton;

    private JToolBar listToolbar;
    private JComboBox listComboBox;

    private JToolBar wordToolbar;
    private JScrollPane wordScrollPane;
    private JList wordList;

    public SearchManager searchManager;

    private WindowsManager windowsManager;

    public ListOfWordsFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager)
    {
        this.jsonFilesManager = jsonFilesManager;
        this.listName = jsonFilesManager.getChosenListName();
        this.windowsManager = windowsManager;

        createUI();
        setUIOptions();
        displayUI();
    }

    //////////

    private void createUI()
    {
        this.setLayout(new GridBagLayout());

        size = getSizeForVisualPurposes();

        buttonWidth  = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 1.5);
        int mainPanelHeight = (int) (size * 2.12);

        int fontSize = (int) (size * Constants.fontMultiplier);

        int wordListHeight = (int) (size * 1.248);

        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension mainPanelDimension  = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension listPanelDimension  = new Dimension(mainPanelWidth, (int)Math.round(size * 0.28));
        Dimension wordsPanelDimension = new Dimension(mainPanelWidth, (int)Math.round(size * 1.424));

        Dimension buttonDimension        = new Dimension(buttonWidth, buttonHeight);
        Dimension toolBarButtonDimension = new Dimension((buttonWidth / 5), buttonHeight);
        Dimension wordListDimension     = new Dimension(buttonWidth,wordListHeight);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel = getNewPanel(mainPanelDimension,null);

        JPanel listPanel = createListPanel(componentGbc, listPanelDimension, buttonDimension, toolBarButtonDimension,
                font);
        JPanel wordsPanel = createWordPanel(componentGbc, wordsPanelDimension, buttonDimension, toolBarButtonDimension,
                wordListDimension, font);
        menuBar = createMenuBar(font);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        this.setJMenuBar(menuBar);

        panelGbc.gridy = 1;
        mainPanel.add(listPanel, panelGbc);

        panelGbc.gridy = 2;
        mainPanel.add(wordsPanel, panelGbc);

        add(mainPanel);
        pack();

    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - (menuBar.getWidth() / 2)),
                (int)((Constants.DIMENSION.height / 2) - (Constants.DIMENSION.height / 2.5)));

        this.setResizable(false);

        addListenersToFrame();

        jsonFilesManager.setListOfLists();
        setInitialAvailabilityAndStateOfFrameElements(jsonFilesManager.getListOfLists());
    }

    //////////

    private JPanel createListPanel(GridBagConstraints gbc, Dimension listPanelDimension, Dimension buttonDimension,
                                   Dimension toolBarButtonDimension, Font font)
    {
        JPanel listPanel = getNewPanel(listPanelDimension, null);

        gbc.gridy = 0;

        listToolbar = getNewToolbar(buttonDimension, false, true);

        addButtonsToListToolbar(toolBarButtonDimension, font);
        toolBarPanel = getNewPanel(buttonDimension, null);

        toolBarPanel.add(listToolbar);

        listPanel.add(toolBarPanel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;

        listComboBox = getNewComboBox(buttonDimension);

        listPanel.add(listComboBox, gbc);

        return listPanel;
    }

    private JPanel createWordPanel(GridBagConstraints gbc, Dimension wordPanelDimension, Dimension buttonDimension,
                                   Dimension toolBarButtonDimension, Dimension wordListDimension, Font font)
    {
        JPanel wordsPanel = getNewPanel(wordPanelDimension, null);

        gbc.gridy = 0;
        gbc.weighty = 1;

        wordToolbar = getNewToolbar(buttonDimension, false,true);

        InputMap inputMap = wordToolbar.getInputMap();
        InputMap parentInputMap = inputMap.getParent();
        //parentInputMap.clear();

        addButtonsToWordToolbar(toolBarButtonDimension, font);

        //parentInputMap.remove(KeyStroke.getKeyStroke(Constants.UP));
        //parentInputMap.remove(KeyStroke.getKeyStroke(Constants.DOWN));

        wordsPanel.add(wordToolbar, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;

        wordList = getNewList(font, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.VERTICAL);
        wordList.setVisibleRowCount(-1);

        InputMap inputMap2 = wordList.getInputMap();
        InputMap parentInputMap2 = inputMap2.getParent();

        parentInputMap2.remove(KeyStroke.getKeyStroke(Constants.UP));
        parentInputMap2.remove(KeyStroke.getKeyStroke(Constants.DOWN));

        wordScrollPane = getNewScrollPane(wordListDimension, wordList);

        //removeNativeKeyListeners(wordList);

        wordsPanel.add(wordScrollPane, gbc);

        return wordsPanel;
    }

    private JMenuBar createMenuBar(Font font)
    {
        menuBar.setFont(font);
        addItemsToMenuBar(font);

        return menuBar;
    }

    //////////

    private void addButtonsToListToolbar(Dimension toolBarButtonDimension, Font font)
    {
        addNewListButton   = getNewButton  (toolBarButtonDimension, Constants.ADD,       font);
        deleteListButton   = getNewButton  (toolBarButtonDimension, Constants.DELETE,    font);
        moveListUpButton   = getNewButton  (toolBarButtonDimension, Constants.MOVE_UP,   font);
        moveListDownButton = getNewButton  (toolBarButtonDimension, Constants.MOVE_DOWN, font);
        modifyListButton   = getNewButton  (toolBarButtonDimension, Constants.MODIFY,    font);

        addNewListButton.setName("addNewListButton");
        deleteListButton.setName("deleteListButton");
        moveListUpButton.setName("moveListUpButton");
        moveListDownButton.setName("moveListDownButton");
        modifyListButton .setName("modifyListButton");

        listToolbar.add(addNewListButton);
        listToolbar.add(deleteListButton);
        listToolbar.add(moveListUpButton);
        listToolbar.add(moveListDownButton);
        listToolbar.add(modifyListButton);

        listToolbar.setFocusable(false);
    }

    private void addButtonsToWordToolbar(Dimension toolBarButtonDimension, Font font)
    {
        addNewWorButton   = getNewButton   (toolBarButtonDimension, Constants.ADD,       font);
        deleteWordButton   = getNewButton  (toolBarButtonDimension, Constants.DELETE,    font);
        moveWordUpButton   = getNewButton  (toolBarButtonDimension, Constants.MOVE_UP,   font);
        moveWordDownButton = getNewButton  (toolBarButtonDimension, Constants.MOVE_DOWN, font);
        modifyWordButton   = getNewButton  (toolBarButtonDimension, Constants.MODIFY,    font);

        wordToolbar.add(addNewWorButton);
        wordToolbar.add(deleteWordButton);
        wordToolbar.add(moveWordUpButton);
        wordToolbar.add(moveWordDownButton);
        wordToolbar.add(modifyWordButton);

    }

    private void addItemsToMenuBar(Font font)
    {
        JMenu listMenu     = getNewMenu(Constants.LISTS_MENU_NAME, font);
        JMenu worMenu      = getNewMenu(Constants.WORDS_MENU_NAME, font);
        JMenu searchMenu   = getNewMenu(Constants.SEARCH_MENU_NAME, font);
        JMenu sortMenu     = getNewMenu(Constants.SORT_MENU_NAME, font);
        JMenu settingsMenu = getNewMenu(Constants.SETTINGS_MENU_NAME, font);

        JMenuItem addList      = getNewMenuItem(Constants.ADD_LIST, font);
        JMenuItem removeList   = getNewMenuItem(Constants.DELETE_LIST, font);
        JMenuItem moveListUp   = getNewMenuItem(Constants.MOVE_LIST_UP, font);
        JMenuItem moveListDown = getNewMenuItem(Constants.MOVE_LIST_DOWN, font);
        JMenuItem modifyList   = getNewMenuItem(Constants.MODIFY_LIST, font);

        addList.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addNewList();
            }
        });

        removeList.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteList();
            }
        });

        moveListUp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveListUp();
            }
        });

        moveListDown.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveListDown();
            }
        });

        modifyList.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyList();
            }
        });

        listMenu.add(addList);
        listMenu.add(removeList);
        listMenu.add(moveListUp);
        listMenu.add(moveListDown);
        listMenu.add(modifyList);

        JMenuItem addNewWord            = getNewMenuItem(Constants.ADD_WORD, font);
        JMenuItem removeWord            = getNewMenuItem(Constants.REMOVE_WORD, font);
        JMenuItem moveWordUp            = getNewMenuItem(Constants.MOVE_WORD_UP, font);
        JMenuItem moveWordDown          = getNewMenuItem(Constants.MOVE_WORD_DOWN, font);
        JMenuItem modifyWord            = getNewMenuItem(Constants.MODIFY_WORD, font);
        JMenuItem moveWordToAnotherList = getNewMenuItem(Constants.MOVE_WORD_TO_ANOTHER_LIST, font);


        addNewWord.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addNewWord();
            }
        });

        removeWord.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteWord();
            }
        });

        moveWordDown.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveWordDown();
            }
        });

        moveWordUp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveWordUp();
            }
        });

        modifyWord.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyWord();
            }
        });

        moveWordToAnotherList.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveWordToAnotherList();
            }
        });

        worMenu.add(addNewWord);
        worMenu.add(removeWord);
        worMenu.add(moveWordUp);
        worMenu.add(moveWordDown);
        worMenu.add(modifyWord);
        worMenu.add(moveWordToAnotherList);

        JMenuItem find = getNewMenuItem(Constants.FIND, font);

        find.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                search();
            }
        });

        searchMenu.add(find);

        JMenuItem sort = getNewMenuItem(Constants.SORT, font);

        sort.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sort();
            }
        });

        sortMenu.add(sort);

        JMenuItem settings = getNewMenuItem(Constants.SETTINGS, font);

        settings.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                listOfWordsFrame.dispose();

                new SettingsFrame(windowsManager);
            }
        });

        settingsMenu.add(settings);

        menuBar.add(listMenu);
        menuBar.add(worMenu);
        menuBar.add(searchMenu);
        menuBar.add(sortMenu);
        menuBar.add(settingsMenu);
    }

    //////////

    private void addListenersToFrame()
    {
        addActionListeners();

        addKeyListenerToWordPanel();

        addMouseListenerToWordList();

        addNavigationKeyListeners();

        addWindowListener();
    }

    private void addActionListeners()
    {
        addActionListenerToListJPanel();
        addActionListenerToWordJPanel();
    }

    private void addActionListenerToListJPanel()
    {
        //for(Component button: listButtons)
        //{
        //    for(KeyListener keyListener: button.getKeyListeners())
        //    {
        //        button.removeKeyListener(keyListener);
        //    }
        //}
//
        //for(Component button: listButtons)
        //{
        //    for(InputMethodListener inputMethodListener: button.getInputMethodListeners())
        //    {
        //        button.removeInputMethodListener(inputMethodListener);
        //    }
        //}



        listComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                listComboBoxAction();
            }
        });

        addNewListButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addNewList();
            }
        });

        deleteListButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteList();
            }
        });

        modifyListButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyList();
            }
        });

        moveListUpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveListUp();
            }
        });

        moveListDownButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveListDown();
            }
        });
    }

    private void addActionListenerToWordJPanel()
    {
        addNewWorButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addNewWord();
            }
        });

        deleteWordButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteWord();
            }
        });

        modifyWordButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyWord();
            }
        });

        moveWordUpButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveWordUp();
            }
        });

        moveWordDownButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveWordDown();
            }
        });
    }

    private void addKeyListenerToWordPanel()
    {
        wordScrollPane.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    exitFrame();
                }

                if(e.getKeyCode() == KeyEvent.VK_UP && (e.getModifiers() & KeyEvent.SHIFT_MASK) == 0)
                {
                    if(wordList.getModel().getSize() > 0)
                    {
                        if(wordList.getSelectedIndex() == 0)
                        {
                            wordToolbar.requestFocus();
                        }
                        else
                        {
                            int selectedIndex = wordList.getSelectedIndex();
                            wordList.setSelectedIndex(selectedIndex - 1);
                            wordList.requestFocus();
                        }
                    }

                }

                if(e.getKeyCode() == KeyEvent.VK_DOWN && (e.getModifiers() & KeyEvent.SHIFT_MASK) == 0)
                {
                    if(wordList.getModel().getSize() > 0)
                    {
                        if(wordList.getSelectedIndex() == wordList.getModel().getSize() - 1)
                        {
                            //listToolbar.requestFocus();
                            moveListUpButton.requestFocus();
                        }
                        else
                        {
                            int selectedIndex = wordList.getSelectedIndex();
                            wordList.setSelectedIndex(selectedIndex + 1);
                            wordList.requestFocus();
                        }
                    }

                }
            }
        });

        wordList.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    exitFrame();
                }


                if(e.getKeyCode() == KeyEvent.VK_UP && (e.getModifiers() & KeyEvent.SHIFT_MASK) == 0)
                {
                    if(wordList.getModel().getSize() > 0)
                    {
                        if(wordList.getSelectedIndex() == 0)
                        {
                            wordToolbar.requestFocus();
                        }
                        else
                        {
                            int selectedIndex = wordList.getSelectedIndex();
                            wordList.setSelectedIndex(selectedIndex - 1);
                            wordList.requestFocus();
                        }
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_DOWN && (e.getModifiers() & KeyEvent.SHIFT_MASK) == 0)
                {
                    if(wordList.getModel().getSize() > 0)
                    {
                        if(wordList.getSelectedIndex() == wordList.getModel().getSize() - 1)
                        {
                            //listToolbar.requestFocus();
                            moveListUpButton.requestFocus();
                        }
                        else
                        {
                            int selectedIndex = wordList.getSelectedIndex();
                            wordList.setSelectedIndex(selectedIndex + 1);
                            wordList.requestFocus();
                        }
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    wordListKeyAction();
                }
            }
        });
    }

    private void addMouseListenerToWordList()
    {
        wordList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                jsonFilesManager.setCurrentWordIndex(wordList.getSelectedIndex());
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if(e.getClickCount() == 2)
                {
                    listName = jsonFilesManager.getListOfLists().get(listComboBox.getSelectedIndex());


                    jsonFilesManager.setCurrentWordIndex(wordList.getSelectedIndex());
                    jsonFilesManager.setChosenListName(listName);
                    jsonFilesManager.setContentOfGivenWord();

                    listOfWordsFrame.dispose();
                    windowsManager.setIfsWordIsdBeingBrowsed(true);
                    new WordFrame(jsonFilesManager, windowsManager, jsonFilesManager.getContentOfGivenWord());
                }
            }

        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(moveListUpButton);
        components.add(listComboBox);
        components.add(moveWordUpButton);
        components.add(wordScrollPane);

        addNavigationKeyListenersToMainComponents(components, false, true);

        addNavigationKeyListenersToComboBox(listComboBox);

        List<Component> listButtons = new ArrayList<>();

        listButtons.add(addNewListButton);
        listButtons.add(deleteListButton);
        listButtons.add(moveListUpButton);
        listButtons.add(moveListDownButton);
        listButtons.add(modifyListButton);

        addHorizontalNavigationKeyBindingsToToolbarButtons(listButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(listButtons, components, 0, false);

        List<Component> wordButtons = new ArrayList<>();

        wordButtons.add(addNewWorButton);
        wordButtons.add(deleteWordButton);
        wordButtons.add(moveWordUpButton);
        wordButtons.add(moveWordDownButton);
        wordButtons.add(modifyWordButton);

        addHorizontalNavigationKeyBindingsToToolbarButtons(wordButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(wordButtons, components, 2, false);
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

    //////////

    private void listComboBoxAction()
    {
        try
        {
            int chosenListIndex = listComboBox.getSelectedIndex();

            jsonFilesManager.setChosenListName(jsonFilesManager.getListOfLists().get(chosenListIndex));
            jsonFilesManager.setListOfWords();
            changeStateOfWordList();

            if(!jsonFilesManager.getListOfWords().isEmpty())
            {
                changeAvailabilityOfWordList(true);

                jsonFilesManager.setCurrentWordIndex(jsonFilesManager.getListOfWords().size() - 1);
                wordList.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
            }
            else
            {
                changeAvailabilityOfWordList(false);
            }

            jsonFilesManager.setCurrentListIndex(chosenListIndex);
        }
        catch (Exception exception)
        {
        }
    }

    private void addNewList()
    {
        try
        {
            jsonFilesManager.addNewListToJSONListsFile(null, null,true);
            jsonFilesManager.setListOfLists();

            if(jsonFilesManager.getListOfLists().size() == 1)
            {
                changeAvailabilityOfListComboBox(true);

                changeAvailabilityOfWordButtons(true);
                changeAvailabilityOfWordList(true);
            }

            changeStateOfListComboBox();

            jsonFilesManager.setCurrentListIndex(jsonFilesManager.getListOfLists().size() - 1);

            listComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
            listComboBox.requestFocus();
        }
        catch (Exception exception)
        {
        }
    }

    private void deleteList()
    {
        try
        {
            if(jsonFilesManager.getListOfLists().size() != 0)
            {
                int listIndex = listComboBox.getSelectedIndex();
                jsonFilesManager.removeListFromJSONListsFileAndDeleteJSONListFile(listIndex);

                jsonFilesManager.setListOfLists();
                changeStateOfListComboBox();

                if(listIndex >= jsonFilesManager.getListOfLists().size())
                {
                    listIndex = jsonFilesManager.getListOfLists().size() - 1;
                }

                jsonFilesManager.setCurrentListIndex(listIndex);

                listComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
                listComboBox.requestFocus();

                jsonFilesManager.setListOfWords();
                changeStateOfWordList();

                jsonFilesManager.setCurrentWordIndex(jsonFilesManager.getListOfWords().size() - 1);

                wordList.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
                wordList.requestFocus();

                if(jsonFilesManager.getListOfLists().size() == 0)
                {
                    changeAvailabilityOfListComboBox(false);

                    changeAvailabilityOfWordButtons(false);
                    changeAvailabilityOfWordList(false);
                }
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void moveListUp()
    {
        try
        {
            int listIndex = listComboBox.getSelectedIndex();

            if(listIndex != 0)
            {
                jsonFilesManager.moveListUpInJSONListFile(listIndex);

                changeStateOfListComboBox();

                jsonFilesManager.setCurrentListIndex(listIndex - 1);

                listComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
                listComboBox.requestFocus();
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void moveListDown()
    {
        int listIndex = listComboBox.getSelectedIndex();

        if(listIndex != jsonFilesManager.getListOfLists().size() - 1)
        {
            jsonFilesManager.moveListDownInJSONListFile(listIndex);

            changeStateOfListComboBox();

            jsonFilesManager.setCurrentListIndex(listIndex + 1);

            listComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
            listComboBox.requestFocus();
        }
    }

    private void modifyList()
    {
        windowsManager.setIfWordIsdBeingModified(true);
        new AddOrChooseListOfWordsFrame(jsonFilesManager, windowsManager, null);
        listOfWordsFrame.dispose();
    }

    //////////

    private void addNewWord()
    {
        listOfWordsFrame.dispose();

        windowsManager.setIfWordIsdBeingModified(false);
        windowsManager.setIfsWordIsdBeingBrowsed(false);

        AddWordFrame addWordFrame = new AddWordFrame(jsonFilesManager, windowsManager);
    }

    private void deleteWord()
    {
        try
        {
            if(jsonFilesManager.getListOfWords().size() != 0)
            {
                int listIndex = listComboBox.getSelectedIndex();
                int wordIndex = wordList.getSelectedIndex();

                jsonFilesManager.removeWordFromJsonListFiles(listIndex, wordIndex);

                jsonFilesManager.setListOfWords();
                changeStateOfWordList();

                //!!!
                jsonFilesManager.setCurrentWordIndex(jsonFilesManager.getListOfWords().size() - 1);
                wordList.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
                wordList.requestFocus();

                if(jsonFilesManager.getListOfWords().size() == 0)
                {
                    changeAvailabilityOfWordList(false);
                }
                else
                {
                    if(wordIndex > jsonFilesManager.getListOfWords().size() - 1)
                    {
                        wordList.setSelectedIndex(jsonFilesManager.getListOfWords().size() - 1);
                    }
                    else
                    {
                        wordList.setSelectedIndex(wordIndex);
                    }
                }
            }
            else
            {
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void moveWordUp()
    {
        int listIndex = listComboBox.getSelectedIndex();
        int wordIndex = wordList.getSelectedIndex();

        if((wordIndex != 0) && (wordIndex != -1))
        {
            jsonFilesManager.moveWordUpInJsonFile(listIndex, wordIndex);

            changeStateOfWordList();

            //wordList.setSelectedIndex(wordIndex - 1);
            //wordList.requestFocus();

            //!!!
            jsonFilesManager.setCurrentWordIndex(wordIndex- 1);
            wordList.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
            wordList.requestFocus();
        }
    }

    private void moveWordDown()
    {
        int listIndex = listComboBox.getSelectedIndex();
        int wordIndex = wordList.getSelectedIndex();

        //if((wordIndex != 0) && (wordIndex != -1))
        if(wordIndex != jsonFilesManager.getListOfWords().size() - 1)
        {
            jsonFilesManager.moveWordDownInJsonFile(listIndex, wordIndex);

            changeStateOfWordList();

            //wordList.setSelectedIndex(wordIndex + 1);
            //wordList.requestFocus();

            //!!!
            jsonFilesManager.setCurrentWordIndex(wordIndex + 1);
            wordList.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
            wordList.requestFocus();
        }
    }

    private void modifyWord()
    {
        if(wordList.getSelectedIndex() > -1)
        {
            listOfWordsFrame.dispose();

            jsonFilesManager.setCurrentWordIndex(wordList.getSelectedIndex());
            jsonFilesManager.setCurrentListIndex(listComboBox.getSelectedIndex());

            jsonFilesManager.setContentOfGivenWord();
            jsonFilesManager.setChosenListName(jsonFilesManager.getListOfLists().get(listComboBox.getSelectedIndex()));

            //AddOrEditWordManuallyFrame addOrEditWordManuallyFrame = new AddOrEditWordManuallyFrame
            //        (jsonFilesManager.getContentOfGivenWord(), jsonFilesManager,
            //                false);

            windowsManager.setIfWordIsdBeingModified(true);

            AddOrEditWordManuallyFrame addOrEditWordManuallyFrame = new AddOrEditWordManuallyFrame
                    (jsonFilesManager, windowsManager, jsonFilesManager.getContentOfGivenWord());
        }
    }

    //////////

    private void moveWordToAnotherList()
    {
        new MoveWordToAnotherListFrame(jsonFilesManager).setVisible(true);
        listOfWordsFrame.dispose();
    }

    private void wordListKeyAction()
    {
        listName = jsonFilesManager.getListOfLists().get(listComboBox.getSelectedIndex());

        jsonFilesManager.setCurrentWordIndex(wordList.getSelectedIndex());
        jsonFilesManager.setChosenListName(listName);
        jsonFilesManager.setContentOfGivenWord();

        listOfWordsFrame.dispose();
        windowsManager.setIfsWordIsdBeingBrowsed(true);
        new WordFrame(jsonFilesManager, windowsManager, jsonFilesManager.getContentOfGivenWord());
    }

    public void exitFrame()
    {
        listOfWordsFrame.dispose();
        new MainMenuFrame(jsonFilesManager);
    }

    private void sort()
    {
        if(jsonFilesManager.getListOfLists().size() == 0)
        {
            new InformationDialog(Constants.ERROR,
                    Constants.TO_USE_OPTION_THERE_HAS_TO_BE_AT_LEAST_ONE_LIST,
                    Constants.CLICK_OK_TO_CONTINUE, null);
        }
        else
        {
            listOfWordsFrame.dispose();
            new SortFrame(jsonFilesManager, this);
        }
    }

    private void search()
    {
        new SearchDialog(this);
        this.displayUI();
    }

    public void createNewSearchManager(String phrase)
    {
        searchManager = new SearchManager(phrase, this);

    }

    public SearchManager getSearchManager()
    {
        return searchManager;

    }

    public JsonFilesManager getJsonFilesManager()
    {
        return jsonFilesManager;

    }

    private void setInitialAvailabilityAndStateOfFrameElements(List<String> listOfLists)
    {
        if(!listOfLists.isEmpty())
        {
            changeStateOfListComboBox();

            listComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
            listComboBox.requestFocus();
        }
        else
        {
            changeAvailabilityOfListComboBox(false);
            changeAvailabilityOfWordButtons(false);
            changeAvailabilityOfWordList(false);
        }
    }

    private void changeAvailabilityOfWordButtons(Boolean booleanValue)
    {
        addNewWorButton.setEnabled(booleanValue);
        deleteWordButton.setEnabled(booleanValue);
        modifyWordButton.setEnabled(booleanValue);
        moveWordUpButton.setEnabled(booleanValue);
        moveWordDownButton.setEnabled(booleanValue);

        addNewWorButton.setFocusable(booleanValue);
        deleteWordButton.setFocusable(booleanValue);
        modifyWordButton.setFocusable(booleanValue);
        moveWordUpButton.setFocusable(booleanValue);
        moveWordDownButton.setFocusable(booleanValue);
    }

    private void changeAvailabilityOfWordList(Boolean booleanValue)
    {
        wordList.setFocusable(booleanValue);
        wordList.setEnabled(booleanValue);
        if(jsonFilesManager.getListOfWords().size() != 0)
        {
            wordList.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
            wordList.requestFocus();
        }
    }

    public void changeStateOfWordList()
    {
        DefaultListModel model = new DefaultListModel();

        if(jsonFilesManager.getListOfWords().size() != 0)
        {
            for (String word: jsonFilesManager.getListOfWords())
            {
                model.addElement(word);
            }

            wordList.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
        }

        wordList.setModel(model);
    }

    private void changeAvailabilityOfListComboBox(Boolean booleanValue)
    {
        listComboBox.setEnabled(booleanValue);
        listComboBox.setFocusable(booleanValue);
    }

    public void changeStateOfListComboBox()
    {
        List <String> newList = new ArrayList<>();

        if(jsonFilesManager.getListOfLists().size() != 0)
        {
            newList.addAll(jsonFilesManager.getListOfLists());

            listComboBox.setModel(new DefaultComboBoxModel(newList.toArray()));

            jsonFilesManager.setListOfWords();
            changeStateOfWordList();
        }
        else
        {
            listComboBox.setModel(new DefaultComboBoxModel(newList.toArray()));
        }
    }

    public void changeFocusOnElements(int listIndex, int wordIndex)
    {
        listComboBox.setSelectedIndex(listIndex);
        listComboBox.requestFocus();

        if(wordIndex == -1)
        {
            wordList.clearSelection();
        }
        else
        {
            wordList.setSelectedIndex(wordIndex);
            wordList.requestFocus();
        }
    }
}
