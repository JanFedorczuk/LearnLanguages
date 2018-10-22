package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddOrChooseListOfWordsFrame extends MultiComponentComplexFrame
{
    private AddOrChooseListOfWordsFrame addOrChooseListOfWordsFrame = this;

    private JButton addNewListButton;
    private JButton deleteListButton;
    private JButton modifyListButton;
    private JButton moveListUpButton;
    private JButton moveListDownButton;

    private JButton deleteWordButton;
    private JButton moveWordUpButton;
    private JButton moveWordDownButton;

    private JToolBar wordToolbar;
    private JComboBox wordComboBox;

    private JToolBar listToolbar;
    private JComboBox listComboBox;
    private JTextField listTextField;
    private JButton saveDataButton;

    private JsonFilesManager jsonFilesManager;
    private WindowsManager windowsManager;
    private List<List<String>> wordAndItsContent;

    public AddOrChooseListOfWordsFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager,
                                        List<List<String>> wordAndItsContent)
    {
        this.wordAndItsContent = wordAndItsContent;

        if(wordAndItsContent != null)
        {
            jsonFilesManager.setContentOfGivenWord(wordAndItsContent);
        }
        this.jsonFilesManager = jsonFilesManager;
        this.windowsManager = windowsManager;

        createUI();
        setUIOptions();
        displayUI();

    }

    private void createUI()
    {
        this.setLayout(new GridBagLayout());

        size = getSizeForVisualPurposes();

        buttonWidth  = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 1.5);
        int mainPanelHeight = (int) (size * 1.608);

        int fontSize     = (int) (size * Constants.fontMultiplier);

        int wordToolbarButtonWidth = (buttonWidth / 5 * 4);

        Dimension mainPanelDimension   = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension listPanelDimension   = new Dimension(mainPanelWidth, (int)(size * 0.632));
        Dimension wordPanelDimension   = new Dimension(mainPanelWidth, (int)(size * 0.456));
        Dimension buttonPanelDimension = new Dimension(mainPanelWidth, buttonHeight);

        Dimension buttonDimension            = new Dimension(buttonWidth, buttonHeight);
        Dimension wordToolbarDimension       = new Dimension(wordToolbarButtonWidth, buttonHeight);
        Dimension listToolBarButtonDimension = new Dimension((buttonWidth / 5), buttonHeight);
        Dimension wordToolBarButtonDimension = new Dimension((wordToolbarButtonWidth / 3), buttonHeight);

        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);

        JPanel listPanel = createListPanel(componentGbc, listPanelDimension, listToolBarButtonDimension,
                buttonDimension, font);

        JPanel wordPanel = createWordPanel(componentGbc, wordPanelDimension, wordToolbarDimension,
                wordToolBarButtonDimension, buttonDimension, font);

        JPanel buttonPanel = createButtonPanel(componentGbc, buttonPanelDimension, buttonDimension, font);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);
        
        panelGbc.gridy = 0;
        mainPanel.add(listPanel, panelGbc);

        panelGbc.gridy = 1;
        mainPanel.add(wordPanel, panelGbc);

        panelGbc.gridy = 2;
        mainPanel.add(buttonPanel, panelGbc);

        add(mainPanel);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation((Constants.DIMENSION.width / 2) - (int)(size * Constants.buttonWidthMultiplier * 0.75),
                (Constants.DIMENSION.height / 2) - (int)(Constants.DIMENSION.height/ 3));
        this.setResizable(false);

        jsonFilesManager.setListOfLists();

        addListenersToFrame();

        setInitialAvailabilityAndStateOfFrameElements();
    }

    private JPanel createListPanel(GridBagConstraints gbc, Dimension listPanelDimension,
                                   Dimension listToolBarButtonDimension, Dimension buttonDimension, Font font)
    {
        JPanel listPanel = getNewPanel(listPanelDimension, null);

        gbc.gridy = 0;

        JLabel writeCategoryName = getNewLabel(Constants.LIST, font);
        listPanel.add(writeCategoryName, gbc);

        gbc.gridy = 1;

        listToolbar = getNewToolbar(buttonDimension, false, true);
        addButtonsToListToolbar(listToolBarButtonDimension, font);

        listPanel.add(listToolbar, gbc);

        gbc.gridy = 2;

        listComboBox = getNewComboBox(buttonDimension);
        listPanel.add(listComboBox, gbc);

        gbc.gridy = 3;
        gbc.weighty = 0;

        listTextField = getNewTextField(buttonDimension, font, SwingConstants.CENTER);
        listPanel.add(listTextField, gbc);

        return listPanel;
    }

    private JPanel createWordPanel(GridBagConstraints gbc, Dimension wordPanelDimension,
                                   Dimension wordToolbarDimension, Dimension wordToolBarButtonDimension,
                                   Dimension buttonDimension, Font font)
    {
        JPanel wordPanel = getNewPanel(wordPanelDimension, null);

        gbc.gridy = 0;
        gbc.weighty = 1;

        JLabel wordNameJLabel = getNewLabel(Constants.WORD + Constants.COLON, font);
        wordPanel.add(wordNameJLabel, gbc);

        gbc.gridy = 1;

        wordToolbar = getNewToolbar(wordToolbarDimension, false, true);
        addButtonsToWordToolbar(wordToolBarButtonDimension, font);
        wordPanel.add(wordToolbar, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;

        wordComboBox = getNewComboBox(buttonDimension);

        wordPanel.add(wordComboBox, gbc);

        return wordPanel;
    }

    private JPanel createButtonPanel(GridBagConstraints gbc, Dimension buttonPanelDimension,
                                     Dimension buttonDimension, Font font)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        gbc.gridy = 0;

        saveDataButton = getNewButton(buttonDimension, Constants.SAVE_DATA, font);

        buttonPanel.add(saveDataButton, gbc);

        if(windowsManager.getIfWordIsBeingModified())
        {
            saveDataButton.setEnabled(false);
            saveDataButton.setFocusable(false);
        }

        return buttonPanel;
    }

    private void addButtonsToListToolbar(Dimension listToolbarButtonDimension, Font font)
    {
        addNewListButton   = getNewButton(listToolbarButtonDimension, Constants.ADD,       font);
        deleteListButton   = getNewButton(listToolbarButtonDimension, Constants.DELETE,    font);
        moveListUpButton   = getNewButton(listToolbarButtonDimension, Constants.MOVE_UP,   font);
        moveListDownButton = getNewButton(listToolbarButtonDimension, Constants.MOVE_DOWN, font);
        modifyListButton   = getNewButton(listToolbarButtonDimension, Constants.MODIFY,    font);

        listToolbar.add(addNewListButton);
        listToolbar.add(deleteListButton);
        listToolbar.add(moveListUpButton);
        listToolbar.add(moveListDownButton);
        listToolbar.add(modifyListButton);
    }

    private void addButtonsToWordToolbar(Dimension wordToolBarButtonDimension, Font font)
    {
        deleteWordButton   = getNewButton(wordToolBarButtonDimension, Constants.DELETE,    font);
        moveWordUpButton   = getNewButton(wordToolBarButtonDimension, Constants.MOVE_UP,   font);
        moveWordDownButton = getNewButton(wordToolBarButtonDimension, Constants.MOVE_DOWN, font);

        wordToolbar.add(deleteWordButton);
        wordToolbar.add(moveWordUpButton);
        wordToolbar.add(moveWordDownButton);
    }

    private void addListenersToFrame()
    {
        addActionListeners();
        addKeyListeners();

        addNavigationKeyListeners();
        addWindowListener();
    }

    private void addActionListeners()
    {
        addActionListenersToListPanel();
        addActionListenersToWordPanel();
        addActionListenerToButtonPanel();
    }

    private void addActionListenersToListPanel()
    {
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
        modifyListButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyList();
            }
        });
    }

    private void addActionListenersToWordPanel()
    {
        deleteWordButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteWord();
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

    private void addActionListenerToButtonPanel()
    {
        saveDataButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                saveData();
            }
        });
    }

    private void addKeyListeners()
    {
        addKeyListenersToListPanel();
        addKeyListenersToWordPanel();
        addKeyListenerToButtonPanel();
    }

    private void addKeyListenersToListPanel()
    {
        addNewListButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    addNewList();
                    addNewListButton.requestFocus();
                }
            }
        });

        deleteListButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    deleteList();
                    deleteListButton.requestFocus();
                }
            }
        });

        moveListUpButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveListUp();
                    moveListUpButton.requestFocus();
                }
            }
        });

        moveListDownButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveListDown();
                    moveListDownButton.requestFocus();
                }
            }
        });

        modifyListButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    modifyList();
                    modifyListButton.requestFocus();
                }
            }
        });
    }

    private void addKeyListenersToWordPanel()
    {
        deleteWordButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    deleteWord();
                    deleteWordButton.requestFocus();
                }
            }
        });

        moveWordUpButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveWordUp();
                    moveWordUpButton.requestFocus();
                }
            }
        });

        moveWordDownButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveWordDown();
                    moveWordDownButton.requestFocus();
                }
            }
        });
    }

    private void addKeyListenerToButtonPanel()
    {
        saveDataButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    saveData();
                }
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(moveListUpButton);
        components.add(listComboBox);
        components.add(listTextField);
        components.add(moveWordUpButton);
        components.add(wordComboBox);
        components.add(saveDataButton);

        addNavigationKeyListenersToMainComponents(components, false, true);

        addNavigationKeyListenersToComboBox(listComboBox);
        addNavigationKeyListenersToComboBox(wordComboBox);

        List<Component> listButtons = new ArrayList<>();

        listButtons.add(addNewListButton);
        listButtons.add(deleteListButton);
        listButtons.add(moveListUpButton);
        listButtons.add(moveListDownButton);
        listButtons.add(modifyListButton);

        addHorizontalNavigationKeyBindingsToToolbarButtons(listButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(listButtons, components, 0, false);

        List<Component> wordButtons = new ArrayList<>();

        wordButtons.add(deleteWordButton);
        wordButtons.add(moveWordUpButton);
        wordButtons.add(moveWordDownButton);

        addHorizontalNavigationKeyBindingsToToolbarButtons(wordButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(wordButtons, components, 3, false);
    }

    private void addWindowListener()
    {
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                addOrChooseListOfWordsFrame.dispose();

                if(windowsManager.getIfWordIsBeingModified())
                {
                    new ListOfWordsFrame(jsonFilesManager, windowsManager);
                }
                else
                {
                    new OptionsFrame(jsonFilesManager, windowsManager, wordAndItsContent);
                }
            }
        });
    }

    private void listComboBoxAction()
    {
        try
        {
            String chosenListName = jsonFilesManager.getListOfLists().get(listComboBox.getSelectedIndex());

            listTextField.setText(chosenListName);

            jsonFilesManager.setCurrentListIndex(listComboBox.getSelectedIndex());
            jsonFilesManager.setChosenListName(chosenListName);

            jsonFilesManager.setListOfWords();

            if(jsonFilesManager.checkIfJsonFileExists(chosenListName, false))
            {
                if(jsonFilesManager.checkIfListOfWordsIsEmpty(chosenListName))
                {
                    changeAvailabilityAndStateOfGivenElements(false, wordComboBox, null,
                            jsonFilesManager.getListOfWords());
                }
                else
                {
                    changeAvailabilityAndStateOfGivenElements(true, wordComboBox, null,
                            jsonFilesManager.getListOfWords());

                    jsonFilesManager.setCurrentWordIndex(jsonFilesManager.getListOfWords().size() - 1);

                    wordComboBox.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
                }
            }
            else
            {
                changeAvailabilityAndStateOfGivenElements(false, wordComboBox, null,
                        jsonFilesManager.getListOfWords());

                jsonFilesManager.setCurrentWordIndex(jsonFilesManager.getListOfWords().size() - 1);

                updateSelectedIndexAndFocusOfComboBox(wordComboBox, jsonFilesManager.getCurrentWordIndex());
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void addNewList()
    {
        jsonFilesManager.addNewListToJSONListsFile(null, null, true);
        jsonFilesManager.setListOfLists();

        if(jsonFilesManager.getListOfLists().size() == 1)
        {
            changeAvailabilityOfGivenElements(true, listComboBox, listTextField);
        }

        changeStateOfGivenElements
                (true, listComboBox, listTextField, jsonFilesManager.getListOfLists());

        jsonFilesManager.setCurrentListIndex(jsonFilesManager.getListOfLists().size() - 1);

        updateSelectedIndexAndFocusOfComboBox(listComboBox, jsonFilesManager.getCurrentListIndex());
    }

    private void deleteList()
    {
        try
        {
            int listIndex = listComboBox.getSelectedIndex();
            jsonFilesManager.removeListFromJSONListsFileAndDeleteJSONListFile(listIndex);
            jsonFilesManager.setListOfLists();

            if(jsonFilesManager.getListOfLists().size() == 0)
            {
                changeAvailabilityAndStateOfGivenElements(false, listComboBox, listTextField,
                        jsonFilesManager.getListOfLists());

                changeAvailabilityAndStateOfGivenElements(false, wordComboBox, null,
                        jsonFilesManager.getListOfWords());
            }
            else
            {
                changeAvailabilityAndStateOfGivenElements(true, listComboBox, listTextField,
                        jsonFilesManager.getListOfLists());

                if(listIndex >= jsonFilesManager.getListOfLists().size())
                {
                    listIndex = jsonFilesManager.getListOfLists().size() - 1;
                }
                jsonFilesManager.setCurrentListIndex(listIndex);

                updateSelectedIndexAndFocusOfComboBox(listComboBox, jsonFilesManager.getCurrentListIndex());

                if(jsonFilesManager.getListOfWords().size() != 0)
                {
                    changeAvailabilityAndStateOfGivenElements(true, wordComboBox, null,
                            jsonFilesManager.getListOfWords());

                    jsonFilesManager.setCurrentWordIndex(jsonFilesManager.getListOfWords().size() - 1);

                    wordComboBox.setSelectedIndex(jsonFilesManager.getCurrentWordIndex());
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

            if(listIndex > 0)
            {
                jsonFilesManager.moveListUpInJSONListFile(listIndex);

                changeAvailabilityAndStateOfGivenElements(true, listComboBox,
                        listTextField, jsonFilesManager.getListOfLists());

                jsonFilesManager.setCurrentListIndex(listIndex - 1);

                updateSelectedIndexAndFocusOfComboBox(listComboBox, jsonFilesManager.getCurrentListIndex());
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

            changeStateOfGivenElements(true, listComboBox,
                    listTextField, jsonFilesManager.getListOfLists());

            jsonFilesManager.setCurrentListIndex(listIndex + 1);

            updateSelectedIndexAndFocusOfComboBox(listComboBox, jsonFilesManager.getCurrentListIndex());
        }
    }

    private void modifyList()
    {
        try
        {
            String previousFileName = jsonFilesManager.getListOfLists().get(listComboBox.getSelectedIndex());

            String newListName = listTextField.getText();
            int selectedIndex = listComboBox.getSelectedIndex();

            if(jsonFilesManager.modifyListInJSONListsFileAndInJSONList(previousFileName, newListName, selectedIndex))
            {
                changeAvailabilityAndStateOfGivenElements(true, listComboBox, listTextField,
                        jsonFilesManager.getListOfLists());

                jsonFilesManager.setCurrentListIndex(selectedIndex);
                listComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
            }
            else
            {
                listTextField.setText(Constants.CATEGORY_ALREADY_EXISTS);
            }
        }
        catch (Exception exception)
        {
        }

    }

    private void deleteWord()
    {
        try
        {
            if(jsonFilesManager.getListOfWords().size() != 0)
            {
                int listIndex = listComboBox.getSelectedIndex();
                int wordIndex = wordComboBox.getSelectedIndex();

                jsonFilesManager.removeWordFromJsonListFiles(listIndex, wordIndex);

                jsonFilesManager.setListOfWords();

                if(jsonFilesManager.getListOfWords().size() == 0)
                {
                    changeAvailabilityAndStateOfGivenElements(false, wordComboBox, null,
                            jsonFilesManager.getListOfWords());

                    jsonFilesManager.setCurrentWordIndex(0);
                }
                else
                {
                    int index = wordComboBox.getSelectedIndex();
                    if(jsonFilesManager.getListOfWords().size() -1 < wordComboBox.getSelectedIndex())
                    {
                        index = jsonFilesManager.getListOfWords().size() -1;
                    }

                    changeAvailabilityAndStateOfGivenElements(true, wordComboBox, null,
                            jsonFilesManager.getListOfWords());

                    jsonFilesManager.setCurrentWordIndex(index);

                    updateSelectedIndexAndFocusOfComboBox(wordComboBox, jsonFilesManager.getCurrentWordIndex());
                }
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void moveWordUp()
    {
        try
        {
            int listIndex = listComboBox.getSelectedIndex();
            int wordIndex = wordComboBox.getSelectedIndex();

            if(wordIndex > 0)
            {
                jsonFilesManager.moveWordUpInJsonFile(listIndex, wordIndex);

                changeStateOfGivenElements(true, wordComboBox,
                        null, jsonFilesManager.getListOfWords());

                jsonFilesManager.setCurrentWordIndex(wordIndex - 1);

                updateSelectedIndexAndFocusOfComboBox(wordComboBox, jsonFilesManager.getCurrentWordIndex());
            }
        }
        catch (Exception exception)
        {
        }

    }

    private void moveWordDown()
    {
        try
        {
            int listIndex = listComboBox.getSelectedIndex();
            int wordIndex = wordComboBox.getSelectedIndex();

            if(wordIndex != (jsonFilesManager.getListOfWords().size() - 1))
            {
                jsonFilesManager.moveWordDownInJsonFile(listIndex, wordIndex);

                changeStateOfGivenElements(true, wordComboBox,
                        null, jsonFilesManager.getListOfWords());

                jsonFilesManager.setCurrentWordIndex(wordIndex + 1);

                updateSelectedIndexAndFocusOfComboBox(wordComboBox, jsonFilesManager.getCurrentWordIndex());
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void saveData()
    {
        try
        {
            jsonFilesManager.createNewJsonListFiles();

            String chosenList = jsonFilesManager.getListOfLists().get(listComboBox.getSelectedIndex());

            if(jsonFilesManager.saveDataInJsonList_Content(chosenList) &&
                jsonFilesManager.saveDataInJsonList_Words(chosenList, null, true))
            {
                changeAvailabilityAndStateOfGivenElements
                        (true, wordComboBox, null, jsonFilesManager.getListOfWords());

                jsonFilesManager.setCurrentWordIndex(jsonFilesManager.getListOfWords().size() - 1);

                updateSelectedIndexAndFocusOfComboBox(wordComboBox, jsonFilesManager.getCurrentWordIndex());

                new InformationDialog(Constants.INFORMATION, Constants.WORD_SUCCESSFULLY_SAVE_IN_GIVEN_LIST,
                        Constants.CLICK_OK_TO_CONTINUE, null);
            }
            else
            {
                new InformationDialog(Constants.ERROR, Constants.GIVEN_WORD_IS_ALREADY_ON_THE_SELECTED_LIST,
                        Constants.PLEASE_WRITE_NEW_WORD_OR_MODIFY_THE_OLD_ONE, null);
            }

        }
        catch (Exception exception)
        {
            new InformationDialog(Constants.INFORMATION,
                    Constants.NO_LIST_EXISTS,
                    Constants.PLEASE_CREATE_A_LIST, null);
        }
    }

    public void exitFrame()
    {
        addOrChooseListOfWordsFrame.dispose();

        if(windowsManager.getIfWordIsBeingModified())
        {
            new ListOfWordsFrame(jsonFilesManager, windowsManager);
        }
        else
        {
            new OptionsFrame(jsonFilesManager, windowsManager, wordAndItsContent);
        }
    }

    private void changeAvailabilityOfGivenElements(Boolean booleanValue, JComboBox comboBox, JTextField textField)
    {
        comboBox.setEnabled(booleanValue);
        comboBox.setFocusable(booleanValue);

        if(textField != null)
        {
            textField.setEnabled(booleanValue);
            textField.setFocusable(booleanValue);
        }
    }

    private void changeStateOfGivenElements(Boolean booleanValue, JComboBox comboBox, JTextField textField,
                                            List<String> list)
    {
        if(booleanValue)
        {
            if(list.equals(jsonFilesManager.getListOfLists()))
            {
                list.clear();
                jsonFilesManager.setListOfLists();

                list = new ArrayList<>(jsonFilesManager.getListOfLists());
            }
            else if(list.equals(jsonFilesManager.getListOfWords()))
            {
                list.clear();
                jsonFilesManager.setListOfWords();

                list = new ArrayList<>(jsonFilesManager.getListOfWords());
            }

            List <String> newList = new ArrayList<>(list);

            comboBox.setModel(new DefaultComboBoxModel(newList.toArray()));
        }
        else
        {
            List <String> newList = new ArrayList<>();
            comboBox.setModel(new DefaultComboBoxModel(newList.toArray()));
            if(textField != null)
            {
                textField.setText("");
            }

        }
    }

    private void changeAvailabilityAndStateOfGivenElements(Boolean booleanValue, JComboBox comboBox,
                                                           JTextField textField, List<String> list)
    {
        changeAvailabilityOfGivenElements(booleanValue, comboBox, textField);
        changeStateOfGivenElements(booleanValue, comboBox, textField, list);
    }

    private void setInitialAvailabilityAndStateOfFrameElements()
    {
        if(!jsonFilesManager.getListOfLists().isEmpty())
        {
            changeAvailabilityAndStateOfGivenElements(true, listComboBox, listTextField, jsonFilesManager.getListOfLists());

            listComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
            listComboBox.requestFocus();
        }
        else
        {
            changeAvailabilityOfGivenElements(false, listComboBox, listTextField);
            changeAvailabilityOfGivenElements(false, wordComboBox, null);
        }
    }

    private void updateSelectedIndexAndFocusOfComboBox(JComboBox comboBox, int index)
    {
        comboBox.setSelectedIndex(index);
        comboBox.requestFocus();
    }
}

