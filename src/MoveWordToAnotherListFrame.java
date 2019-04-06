package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MoveWordToAnotherListFrame extends MultiComponentComplexFrame
{
    private MoveWordToAnotherListFrame moveWordToAnotherListFrame = this;

    int size;

    private JButton moveWordButton;

    private JButton previousWindowButton;

    private JComboBox startingListComboBox;
    private JComboBox wordComboBox;
    private JComboBox targetListComboBox;

    private JsonFilesManager jsonFilesManager;

    public MoveWordToAnotherListFrame(JsonFilesManager jsonFilesManager)
    {
        this.jsonFilesManager = jsonFilesManager;
        jsonFilesManager.setListOfLists();

        createUI();
        setUIOptions();
        displayUI();
    }

    private void createUI()
    {
        this.setLayout(new GridBagLayout());

        size = getSizeForVisualPurposes();

        buttonWidth      = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 2);
        int mainPanelHeight = (int) (size * 1.464);

        int fontSize = (int) (size * Constants.fontMultiplier);
        Font font    = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension buttonDimension      = new Dimension(buttonWidth   ,buttonHeight);
        Dimension toolBarButtonDimension = new Dimension((buttonWidth / 5), buttonHeight);

        Dimension mainPanelDimension   = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension arrowPanelDimensions = new Dimension(mainPanelWidth, buttonHeight);
        Dimension minorPanelDimension  = new Dimension(mainPanelWidth, (int)Math.round(size * 0.280104));
        Dimension buttonPanelDimension = new Dimension(mainPanelWidth, buttonHeight);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel         = getNewPanel(mainPanelDimension, null);

        JPanel arrowPanel = getArrowPanel(arrowPanelDimensions, toolBarButtonDimension, font,null);

        JPanel startingListPanel = createStaringListPanel(componentGbc, minorPanelDimension, buttonDimension, font);

        JPanel wordPanel         = createWordPanel(componentGbc, minorPanelDimension, buttonDimension, font);

        JPanel targetListPanel   = createTargetListPanel(componentGbc, minorPanelDimension, buttonDimension, font);

        JPanel buttonPanel       = createButtonPanel(buttonPanelDimension, buttonDimension, font);

        previousWindowButton = getPreviousWindowButton(arrowPanel);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;
        panelGbc.weighty = 0;

        mainPanel.add(arrowPanel, panelGbc);

        panelGbc.gridy = 1;
        panelGbc.weighty = 1;

        mainPanel.add(startingListPanel, panelGbc);

        panelGbc.gridy = 2;

        mainPanel.add(wordPanel, panelGbc);

        panelGbc.gridy = 3;

        mainPanel.add(targetListPanel, panelGbc);

        panelGbc.gridy = 4;
        mainPanel.add(buttonPanel, panelGbc);

        add(mainPanel);

        pack();

    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - buttonWidth),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 3)));

        this.setResizable(false);

        addListenersToFrame();

        setInitialAvailabilityAndStateOfFrameElements();
    }

    private JPanel createStaringListPanel(GridBagConstraints gbc, Dimension minorPanelDimension,
                                          Dimension buttonDimension, Font font)
    {
        JPanel startingListPanel = getNewPanel(minorPanelDimension, null);

        JLabel chooseStartingListLabel = getNewLabel(Constants.CHOOSE_STARTING_LIST, font);

        startingListComboBox = getNewComboBox(buttonDimension);

        gbc.gridy = 0;

        startingListPanel.add(chooseStartingListLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;

        startingListPanel.add(startingListComboBox, gbc);

        return startingListPanel;
    }

    private JPanel createWordPanel(GridBagConstraints gbc, Dimension minorPanelDimension,
                                          Dimension buttonDimension, Font font)
    {
        JPanel wordPanel = getNewPanel(minorPanelDimension, null);

        JLabel wordLabel = getNewLabel(Constants.CHOOSE_WORD_TO_MOVE, font);

        wordComboBox = getNewComboBox(buttonDimension);

        gbc.weighty = 1;
        gbc.gridy = 0;

        wordPanel.add(wordLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;

        wordPanel.add(wordComboBox, gbc);

        return wordPanel;
    }

    private JPanel createTargetListPanel(GridBagConstraints gbc, Dimension minorPanelDimension,
                                         Dimension buttonDimension, Font font)
    {
        JPanel targetListPanel = getNewPanel(minorPanelDimension, null);

        JLabel chooseTargetListLabel = getNewLabel(Constants.CHOOSE_TARGET_LIST, font);

        targetListComboBox = getNewComboBox(buttonDimension);

        gbc.gridy = 0;
        gbc.weighty = 1;

        targetListPanel.add(chooseTargetListLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;

        targetListPanel.add(targetListComboBox, gbc);

        return targetListPanel;
    }

    private JPanel createButtonPanel(Dimension minorPanelDimension, Dimension buttonDimension, Font font)
    {
        JPanel buttonPanel = getNewPanel(minorPanelDimension, null);

        moveWordButton = getNewButton(buttonDimension, Constants.MOVE_WORD, font);

        buttonPanel.add(moveWordButton);

        return buttonPanel;
    }

    private void addListenersToFrame()
    {
        addActionListeners();

        addKeyListenerToArrowPanel();

        addKeyListenerToMoveWordButton();

        addNavigationKeyListeners();

        addWindowListener();
    }

    private void addActionListeners()
    {
        addActionListenersToArrowPanel();
        addActionListenerToStartingListPanel();
        addActionListenerToWordPanel();
        addActionListenerToTargetListPanel();
        addActionListenerToButtonPanel();
    }

    private void addActionListenersToArrowPanel()
    {
        previousWindowButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                exitFrame();
            }
        });
    }

    private void addKeyListenerToMoveWordButton()
    {
        moveWordButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveWord();
                }
            }
        });
    }

    private void addKeyListenerToArrowPanel()
    {
        previousWindowButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    exitProgram();
                }
            }
        });
    }

    private void addActionListenerToStartingListPanel()
    {
        startingListComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                startingListComboBoxAction();
            }
        });
    }

    private void addActionListenerToWordPanel()
    {
        wordComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                chosenWordComboBoxAction();
            }
        });
    }

    private void addActionListenerToTargetListPanel()
    {
        targetListComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                targetListComboBoxAction();
            }
        });
    }

    private void addActionListenerToButtonPanel()
    {
        moveWordButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveWord();
            }
        });
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

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(startingListComboBox);
        components.add(wordComboBox);
        components.add(targetListComboBox);
        components.add(moveWordButton);

        addNavigationKeyListenersToMainComponents(components, false, true);

        addNavigationKeyListenersToComboBox(startingListComboBox);
        addNavigationKeyListenersToComboBox(wordComboBox);
        addNavigationKeyListenersToComboBox(targetListComboBox);

    }

    private void startingListComboBoxAction()
    {
        if(checkIfListComboBoxesIndexesAreTheSame())
        {
            changeSelectedIndexOfGivenComboBox(startingListComboBox, targetListComboBox);
        }

        changeStateOfChosenWordComboBox(startingListComboBox.getSelectedIndex());
    }

    private void chosenWordComboBoxAction()
    {

        jsonFilesManager.setCurrentWordIndex(wordComboBox.getSelectedIndex());
    }

    private void targetListComboBoxAction()
    {
        if(checkIfListComboBoxesIndexesAreTheSame())
        {
            changeSelectedIndexOfGivenComboBox(targetListComboBox, startingListComboBox);
        }

        changeStateOfChosenWordComboBox(startingListComboBox.getSelectedIndex());
    }

    private void moveWord()
    {
        if(jsonFilesManager.moveWordToAnotherList(false,
                jsonFilesManager.getListOfLists().get(targetListComboBox.getSelectedIndex())))
        {
            jsonFilesManager.removeWordFromJsonListFiles(jsonFilesManager.getCurrentListIndex(),
                    jsonFilesManager.getCurrentWordIndex());

            try
            {
                String firstPath = jsonFilesManager.getListOfLists().get(startingListComboBox.getSelectedIndex()) + "/" +
                        jsonFilesManager.getContentOfGivenWord().get(0).get(0);

                String secondPath = jsonFilesManager.getListOfLists().get(targetListComboBox.getSelectedIndex()) + "/" +
                        jsonFilesManager.getContentOfGivenWord().get(0).get(0);

                if(!jsonFilesManager.checkIfFolderExists(firstPath))
                {
                    jsonFilesManager.createFolder(firstPath);
                }

                if(!jsonFilesManager.checkIfFolderExists(secondPath))
                {
                    jsonFilesManager.createFolder(secondPath);
                }

                jsonFilesManager.moveFile(firstPath, secondPath);
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }

            new InformationDialog(Constants.INFORMATION, Constants.WORD_SUCCESSFULLY_MOVED,
                    Constants.CLICK_OK_TO_CONTINUE, null,null);
        }
        else
        {
            new InformationDialog(Constants.ERROR, Constants.GIVEN_WORD_IS_ALREADY_ON_THE_SELECTED_LIST,
                    Constants.PLEASE_WRITE_NEW_WORD_OR_MODIFY_THE_OLD_ONE, null,null);
        }

        changeStateOfChosenWordComboBox(jsonFilesManager.getCurrentListIndex());
    }

    public void exitFrame()
    {
        moveWordToAnotherListFrame.dispose();

        WindowsManager windowsManager = new WindowsManager(false,
                false, false);

        new ListOfWordsFrame(jsonFilesManager, windowsManager);
    }

    private void setInitialAvailabilityAndStateOfFrameElements()
    {
        changeStateOfStartingListComboBox();
        changeStateOfTargetListComboBox();

        startingListComboBox.setSelectedIndex(jsonFilesManager.getCurrentListIndex());
        startingListComboBox.requestFocus();
    }

    private void changeStateOfStartingListComboBox()
    {
        List<String> newList = new ArrayList<>();
        newList.addAll(jsonFilesManager.getListOfLists());

        startingListComboBox.setModel(new DefaultComboBoxModel(newList.toArray()));

        changeStateOfChosenWordComboBox(jsonFilesManager.getCurrentListIndex());
    }

    private void changeStateOfChosenWordComboBox(int listIndex)
    {
        jsonFilesManager.setCurrentListIndex(listIndex);
        jsonFilesManager.setCurrentListName(jsonFilesManager.getListOfLists().get(listIndex));
        jsonFilesManager.setListOfWords();

        List<String> newList = new ArrayList<>();
        newList.addAll(jsonFilesManager.getListOfWords());

        wordComboBox.setModel(new DefaultComboBoxModel(newList.toArray()));

        jsonFilesManager.setCurrentWordIndex(wordComboBox.getSelectedIndex());

        if(jsonFilesManager.getListOfWords().isEmpty())
        {
            changeAvailabilityOfChosenWordComboBoxAndMoveWordButton(false);
        }
        else
        {
            changeAvailabilityOfChosenWordComboBoxAndMoveWordButton(true);
        }
    }

    private void changeStateOfTargetListComboBox()
    {
        List<String> newList = new ArrayList<>();
        newList.addAll(jsonFilesManager.getListOfLists());

        targetListComboBox.setModel(new DefaultComboBoxModel(newList.toArray()));
    }

    private void changeAvailabilityOfChosenWordComboBoxAndMoveWordButton(Boolean value)
    {
        wordComboBox.setEnabled(value);
        moveWordButton.setEnabled(value);
    }

    private boolean checkIfListComboBoxesIndexesAreTheSame()
    {
        if(startingListComboBox.getSelectedIndex() == targetListComboBox.getSelectedIndex())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void changeSelectedIndexOfGivenComboBox(JComboBox jComboBox, JComboBox jComboBoxToBeChanged)
    {
        int newIndex = 0;

        if(jComboBox.getSelectedIndex() - 1 >= 0)
        {
            newIndex = jComboBox.getSelectedIndex() - 1;
        }
        else if(jComboBox.getSelectedIndex() + 1 <= jsonFilesManager.getListOfLists().size() - 1)
        {
            newIndex = jComboBox.getSelectedIndex() + 1;
        }

        jComboBoxToBeChanged.setSelectedIndex(newIndex);
    }
}
