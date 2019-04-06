package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddSoundFilesFrame extends MultiComponentComplexFrame
{
    private JFrame addSoundFilesFrame = this;

    private JButton previousWindowButton;

    private JButton addNewFile;
    private JButton deleteFile;
    private JButton moveFileUp;
    private JButton moveFileDown;
    private JButton modifyFile;

    private JTextField fileNameTextField;

    private JToolBar fileOptionsToolBar;

    private JComboBox filesNamesComboBox;

    private JTextArea fileContentTextArea;

    private JRadioButton firstLanguageRadioButton;
    private JRadioButton secondLanguageRadioButton;

    private JRadioButton filesCreationRadioButton;

    private JButton okButton;

    private List<List<String>> wordAndItsContent;

    private JsonFilesManager jsonFilesManager;
    private WindowsManager windowsManager;
    private SoundFilesManager soundFilesManager;
    private WordContentFrame wordContentFrame;

    public AddSoundFilesFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager,
                              SoundFilesManager soundFilesManager, List<List<String>> wordAndItsContent)
    {
        this.jsonFilesManager = jsonFilesManager;
        this.windowsManager = windowsManager;
        this.wordAndItsContent = wordAndItsContent;
        this.soundFilesManager = soundFilesManager;

        createUI();
        setUIOptions();
        displayUI();

        wordContentFrame = new WordContentFrame(wordAndItsContent);
    }

    private void createUI()
    {
        this.setLayout(new GridBagLayout());
        int size = getSizeForVisualPurposes();

        buttonWidth      = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);
        int fontSize     = (int) (size * Constants.fontMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 1.75);
        int mainPanelHeight = (int) (size * 1.912);

        Font font  = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Font toolbarButtonFont = new Font(Constants.FONT_NAME, Font.PLAIN, fontSize);

        Dimension mainPanelDimension        = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension arrowPanelDimensions   = new Dimension(mainPanelWidth, buttonHeight);
        Dimension fileOptionsPanelDimension = new Dimension(mainPanelWidth, (int)Math.round(size * 0.632));
        Dimension fileContentPanelDimension = new Dimension(mainPanelWidth, (int)Math.round(size * 0.696));
        Dimension buttonPanelDimension      = new Dimension(mainPanelWidth, (int)Math.round(size * 0.28));
        Dimension radioButtonPanelDimension = new Dimension(mainPanelWidth, buttonHeight);

        Dimension buttonDimension            = new Dimension(buttonWidth, buttonHeight);
        Dimension toolBarButtonDimension     = new Dimension((int)(buttonWidth / 5), buttonHeight);
        Dimension fileContentScrollPaneDimension = new Dimension(mainPanelWidth, buttonHeight * 5);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel = getNewPanel(mainPanelDimension,null);

        JPanel arrowPanel = getArrowPanel(arrowPanelDimensions, toolBarButtonDimension, font,null);

        JPanel fileOptionsPanel = getNewFileOptionsPanel(componentGbc, fileOptionsPanelDimension, buttonDimension,
                toolBarButtonDimension, font, toolbarButtonFont);

        JPanel fileContentPanel = getNewFileContentPanel(componentGbc, fileContentPanelDimension,
                fileContentScrollPaneDimension, radioButtonPanelDimension, buttonDimension, font);

        JPanel buttonPanel = getButtonPanel(componentGbc, buttonPanelDimension, buttonDimension, font);

        previousWindowButton = getPreviousWindowButton(arrowPanel);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;
        mainPanel.add(arrowPanel, panelGbc);

        panelGbc.gridy = 1;
        mainPanel.add(fileOptionsPanel, panelGbc);

        panelGbc.gridy = 2;
        mainPanel.add(fileContentPanel, panelGbc);

        panelGbc.gridy = 3;
        mainPanel.add(buttonPanel, panelGbc);

        add(mainPanel);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - (int)(buttonWidth * 0.875)),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 3)));

        this.setResizable(false);

        addListenersToFrame();

        setInitialStateAndAvailabilityOfUIComponents();
    }

    private JPanel getNewFileOptionsPanel(GridBagConstraints componentGbc, Dimension fileOptionsPanelDimension,
                                          Dimension buttonDimension, Dimension toolBarButtonDimension,
                                          Font font, Font toolbarButtonFont)
    {
        JPanel fileOptionsPanel = getNewPanel(fileOptionsPanelDimension, null);

        JLabel informationLabel = getNewLabel(Constants.MP3_FILE, font);

        fileOptionsToolBar = getNewToolbar(buttonDimension, false, true);

        addButtonsToFileOptionsToolbar(toolBarButtonDimension, toolbarButtonFont);

        filesNamesComboBox = getNewComboBox(buttonDimension, font);

        fileNameTextField = getNewTextField(buttonDimension, font, SwingConstants.CENTER);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;
        fileOptionsPanel.add(informationLabel, componentGbc);

        componentGbc.gridy = 1;
        fileOptionsPanel.add(fileOptionsToolBar, componentGbc);

        componentGbc.gridy = 2;
        fileOptionsPanel.add(filesNamesComboBox, componentGbc);

        componentGbc.gridy = 3;
        componentGbc.weighty = 0;
        fileOptionsPanel.add(fileNameTextField, componentGbc);

        return fileOptionsPanel;
    }

    private JPanel getNewFileContentPanel(GridBagConstraints componentGbc, Dimension fileContentPanelDimension,
                                          Dimension fileContentScrollPaneDimension, Dimension buttonPanelDimension,
                                          Dimension buttonDimension, Font font)
    {
        JPanel fileContentPanel = getNewPanel(fileContentPanelDimension,null);

        fileContentTextArea = getNewJTextArea(font, true, true);
        fileContentTextArea.setDocument(new TextFieldLimit(100));

        JScrollPane fileContentScrollPane = getNewScrollPane(fileContentScrollPaneDimension, fileContentTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        firstLanguageRadioButton  = getNewRadioButton(buttonDimension, getLanguage(0), font);
        secondLanguageRadioButton = getNewRadioButton(buttonDimension, getLanguage(1), font);

        if(checkIfLanguageIsForbidden(getLanguage(1)))
        {
            changeAvailabilityOfNonTextComponent(secondLanguageRadioButton, false);
        }

        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(firstLanguageRadioButton);
        buttonGroup.add(secondLanguageRadioButton);

        JPanel radioButtonsPanel = getNewPanel(buttonPanelDimension, null);

        radioButtonsPanel.add(firstLanguageRadioButton);
        radioButtonsPanel.add(secondLanguageRadioButton);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;

        fileContentPanel.add(fileContentScrollPane, componentGbc);

        componentGbc.gridy = 1;
        componentGbc.weighty = 0;

        fileContentPanel.add(radioButtonsPanel, componentGbc);

        return fileContentPanel;
    }

    private JPanel getButtonPanel(GridBagConstraints componentGbc, Dimension buttonPanelDimension,
                                  Dimension buttonDimension, Font font)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        filesCreationRadioButton = getNewRadioButton(buttonDimension, Constants.CREATE_BULK_FILE, font);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;

        filesCreationRadioButton.setToolTipText(Constants.SELECT_IF_YOU_WANT_TO_CREATE_ONE_FILE);

        buttonPanel.add(filesCreationRadioButton, componentGbc);

        okButton = getNewButton(buttonDimension, Constants.OK, font);

        componentGbc.gridy = 1;
        componentGbc.weighty = 0;

        buttonPanel.add(okButton, componentGbc);

        return buttonPanel;
    }

    private void addButtonsToFileOptionsToolbar(Dimension toolBarButtonDimension, Font font)
    {
        addNewFile   = getNewButton(toolBarButtonDimension, Constants.ADD,       font);
        deleteFile   = getNewButton(toolBarButtonDimension, Constants.DELETE,    font);
        moveFileUp   = getNewButton(toolBarButtonDimension, Constants.MOVE_UP,   font);
        moveFileDown = getNewButton(toolBarButtonDimension, Constants.MOVE_DOWN, font);
        modifyFile   = getNewButton(toolBarButtonDimension, Constants.MODIFY,    font);

        addNewFile.setToolTipText(Constants.ADD_SOUND);
        deleteFile.setToolTipText(Constants.DELETE_SOUND);
        moveFileUp.setToolTipText(Constants.MOVE_FILE_UP);
        moveFileDown.setToolTipText(Constants.MOVE_FILE_DOWN);
        modifyFile.setToolTipText(Constants.MODIFY_SOUND_FILES_TIP);

        fileOptionsToolBar.add(addNewFile);
        fileOptionsToolBar.add(deleteFile);
        fileOptionsToolBar.add(moveFileUp);
        fileOptionsToolBar.add(moveFileDown);
        fileOptionsToolBar.add(modifyFile);
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
        addActionListenersToArrowPanel();
        addActionListenersToFileOptionsPanel();
        addActionListenersToFileButtonPanel();
    }

    private void addKeyListeners()
    {
        addKeyListenerToArrowPanel();
        addKeyListenersToFileOptionsPanel();
        addKeyListenerToOkButton();
    }

    private void addKeyListenerToArrowPanel()
    {
        previousWindowButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    exitFrame();
                }
            }
        });
    }

    private void addKeyListenersToFileOptionsPanel()
    {
        addNewFile.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    addFileContent();
                    addNewFile.requestFocus();
                }
            }
        });

        deleteFile.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    deleteFileContent();
                    deleteFile.requestFocus();
                }
            }
        });

        moveFileUp.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveFileUpContent();
                    moveFileUp.requestFocus();
                }
            }
        });

        moveFileDown.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveFileDownContent();
                    moveFileDown.requestFocus();
                }
            }
        });

        modifyFile.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    modifyFileContent();
                    modifyFile.requestFocus();
                }
            }
        });
    }

    private void addKeyListenerToOkButton()
    {
        okButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    buttonAction();
                }
            }
        });
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

    private void addActionListenersToFileOptionsPanel()
    {
        addNewFile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addFileContent();
            }
        });

        deleteFile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteFileContent();
            }
        });

        moveFileUp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveFileUpContent();
            }
        });

        moveFileDown.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveFileDownContent();
            }
        });

        modifyFile.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyFileContent();
            }
        });

        filesNamesComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                filesNamesComboBoxAction();
            }
        });
    }

    private void addActionListenersToFileButtonPanel()
    {
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                buttonAction();
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(moveFileUp);
        components.add(filesNamesComboBox);
        components.add(fileNameTextField);
        components.add(fileContentTextArea);
        components.add(firstLanguageRadioButton);
        components.add(filesCreationRadioButton);
        components.add(okButton);

        addNavigationKeyListenersToMainComponents(components, false, true);

        addNavigationKeyListenersToComboBox(filesNamesComboBox);

        List<Component> filesOptionsButtons = new ArrayList<>();

        filesOptionsButtons.add(addNewFile);
        filesOptionsButtons.add(deleteFile);
        filesOptionsButtons.add(moveFileUp);
        filesOptionsButtons.add(moveFileDown);
        filesOptionsButtons.add(modifyFile);

        addHorizontalNavigationKeyBindingsToGroupOfButtons(filesOptionsButtons);
        addVerticalNavigationKeyBindingsToGroupOfButtons(filesOptionsButtons, components, 0,
                true);

        List<Component> chooseLanguages = new ArrayList<>();

        chooseLanguages.add(firstLanguageRadioButton);
        chooseLanguages.add(secondLanguageRadioButton);

        addHorizontalNavigationKeyBindingsToGroupOfButtons(chooseLanguages);
        addVerticalNavigationKeyBindingsToGroupOfButtons(chooseLanguages, components, 4,
                false);
    }

    private void addWindowListener()
    {
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exitProgram();
            }
        });

        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    exitFrame();
                }
            }
        });
    }

    private void filesNamesComboBoxAction()
    {
        if(soundFilesManager.getSoundFilesContentSize() != 0)
        {
            int index = filesNamesComboBox.getSelectedIndex();

            fileNameTextField.setText(soundFilesManager.getSoundFilesContent().get(index).get(0));

            fileContentTextArea.setText(soundFilesManager.getSoundFilesContent().get(index).get(1));

            setSelectedLanguage(soundFilesManager.getSoundFilesContent().get(index).get(2));
        }
    }

    private void addFileContent()
    {
        soundFilesManager.addNewFileContent(0);

        if(soundFilesManager.getSoundFilesContentSize() >= 1)
        {
            changeAvailabilityOfNonTextComponent(filesNamesComboBox, true);
            changeAvailabilityOfTextComponent(fileNameTextField,true);

            changeAvailabilityOfTextComponent(fileContentTextArea,true);
            changeAvailabilityOfNonTextComponent(firstLanguageRadioButton,true);
            if(!checkIfLanguageIsForbidden(getLanguage(1)))
            {
                changeAvailabilityOfNonTextComponent(secondLanguageRadioButton, true);
            }
            changeAvailabilityOfNonTextComponent(filesCreationRadioButton,true);
            changeAvailabilityOfNonTextComponent(okButton,true);

            firstLanguageRadioButton.setSelected(true);
        }

        List<String> fileNamesList = createFileNamesList();

        refreshComboBoxState(fileNamesList, soundFilesManager.getSoundFilesContentSize() - 1);
    }

    private void deleteFileContent()
    {
        int index = filesNamesComboBox.getSelectedIndex();

        try
        {
            List<List<String>> soundFilesContent = soundFilesManager.getSoundFilesContent();

            List<String> list = soundFilesContent.get(index);

            String fileName = list.get(0);

            String folderPath = jsonFilesManager.getCurrentListName() + "/" +
                    jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex());

            jsonFilesManager.deleteFile(folderPath + "/" + fileName + Constants.JSON_EXTENSION);

            if(filesNamesComboBox.getItemCount() == 1)
            {
                jsonFilesManager.deleteFileAndAllItsContent(folderPath);
            }
            else if(filesNamesComboBox.getItemCount() > 1)
            {
                jsonFilesManager.deleteFile(folderPath + "/" + Constants.SOUND_FILES + "/" + fileName +
                        Constants.WAV_SUFFIX);

                jsonFilesManager.removeFileNameFromJSONFile(folderPath, index);

            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        soundFilesManager.deleteFileContent(index);

        List<String> fileNamesList = createFileNamesList();
        refreshComboBoxState(fileNamesList, 0);

        if(index < soundFilesManager.getSoundFilesContentSize() &&
                soundFilesManager.getSoundFilesContentSize() != 0)
        {
            filesNamesComboBox.setSelectedIndex(index);
        }
        else if(index >= soundFilesManager.getSoundFilesContentSize() &&
                soundFilesManager.getSoundFilesContentSize() != 0)
        {
            filesNamesComboBox.setSelectedIndex(index - 1);
        }
        else
        {
            changeAvailabilityOfNonTextComponent(filesNamesComboBox, false);
            changeAvailabilityOfTextComponent(fileNameTextField,false);

            changeAvailabilityOfTextComponent(fileContentTextArea,false);
            changeAvailabilityOfNonTextComponent(firstLanguageRadioButton,false);

            changeAvailabilityOfNonTextComponent(secondLanguageRadioButton,false);

            changeAvailabilityOfNonTextComponent(filesCreationRadioButton,false);
            changeAvailabilityOfNonTextComponent(okButton,false);
        }
    }

    private void moveFileUpContent()
    {
        int index = filesNamesComboBox.getSelectedIndex();

        if(soundFilesManager.getSoundFilesContentSize() != 0 && soundFilesManager.getSoundFilesContentSize() != 1)
        {
            if(index != 0)
            {
                soundFilesManager.moveFileContentUp(index);

                List<String> fileNamesList = createFileNamesList();
                refreshComboBoxState(fileNamesList, index - 1);
            }
        }
    }

    private void moveFileDownContent()
    {
        int index = filesNamesComboBox.getSelectedIndex();

        if(soundFilesManager.getSoundFilesContentSize() != 0 && soundFilesManager.getSoundFilesContentSize() != 1)
        {
            if(index != soundFilesManager.getSoundFilesContentSize() - 1)
            {
                soundFilesManager.moveFileContentDown(index);

                List<String> fileNamesList = createFileNamesList();
                refreshComboBoxState(fileNamesList, index + 1);
            }
        }
    }

    private void modifyFileContent()
    {
        try
        {
            if(filesNamesComboBox.getItemCount() != 0)
            {
                int index = filesNamesComboBox.getSelectedIndex();

                if(checkIfFileNameIsNotEmpty(fileNameTextField.getText()))
                {
                    if(!checkIfFileNameExists(fileNameTextField.getText(), filesNamesComboBox.getSelectedIndex()))
                    {
                        if(!fileContentTextArea.getText().replaceAll("\\s", "").equals(""))
                        {
                            soundFilesManager.modifyFileName(filesNamesComboBox.getSelectedIndex(),
                                    fileNameTextField.getText());

                            soundFilesManager.modifyFileContent(index, fileContentTextArea.getText());

                            soundFilesManager.modifyFileLanguage(index, getSelectedLanguage());

                            soundFilesManager.setFilesCreationOption(getIfBulkFileCreationOptionsIsChosen());

                            List<String> fileNamesList = createFileNamesList();

                            refreshComboBoxState(fileNamesList, index);
                        }
                    }
                }
                else
                {
                    fileNameTextField.setText(Constants.CATEGORY_NAME_IS_EMPTY);
                    fileNameTextField.repaint();
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void buttonAction()
    {
        if(checkIfDataIsCorrect())
        {
            if(windowsManager.getIfWordIsBeingModified())
            {
                if(!soundFilesManager.getSoundFilesContent().isEmpty())
                {
                    List<String> filesNames = jsonFilesManager.getFilesNamesJsonArray(jsonFilesManager.
                            getCurrentListName() + "/" + jsonFilesManager.getListOfWords().get(jsonFilesManager.
                            getCurrentWordIndex()) + "/");

                    List<List<String>> soundFilesContent = soundFilesManager.getSoundFilesContent();

                    boolean newCreationOption = soundFilesManager.getIfFileCreationOptionIsCreateBulkFiles();
                    boolean oldCreationOption = jsonFilesManager.getFilesBulkCreationValue(
                            jsonFilesManager.getCurrentListName() + "/" + jsonFilesManager.getListOfWords().get
                                    (jsonFilesManager.getCurrentWordIndex()) + "/");

                    boolean doFilesHaveIdenticalOrder = jsonFilesManager.checkIfFilesHaveIdenticalOrder
                            (soundFilesContent, filesNames);

                    List<List<Boolean>> comparisonList = jsonFilesManager.getFilesComparisonList
                            (soundFilesContent, filesNames);

                    boolean doesAnyListContainFalse = jsonFilesManager.checkIfAnyListContainsFalse(comparisonList);

                    if((doesAnyListContainFalse) || (newCreationOption != oldCreationOption) ||
                            (!doFilesHaveIdenticalOrder))
                    {
                        ProgressDialog progressDialog = new ProgressDialog(jsonFilesManager, soundFilesManager, windowsManager);
                        soundFilesManager.setComparisonList(comparisonList);
                        soundFilesManager.setIfFilesHaveIdenticalOrder(doFilesHaveIdenticalOrder);
                        soundFilesManager.setIndexesOfFilesWhichNamesShouldBeChanged
                                (jsonFilesManager.getIndexesOfFilesWhichNamesShouldBeChanged());
                        progressDialog.startNewThread(this);
                    }
                    else
                    {
                        new InformationDialog(Constants.INFORMATION, Constants.FILES_ARE_THE_SAME,
                                Constants.TO_REPLACE_THEM_MODIFY_THEM, null,null);
                    }
                }
            }
            else
            {
                new InformationDialog(Constants.INFORMATION, Constants.GIVEN_DATA_IS_CORRECT,
                        Constants.GIVEN_DATA_WILL_BE_USED_TO_CREATE_MP3_FILES_FIRST_PART_OF_INFORMATION,
                        Constants.GIVEN_DATA_WILL_BE_USED_TO_CREATE_MP3_FILES_SECOND_PART_OF_INFORMATION,
                        null);

                exitFrame();
            }
        }
        else
        {
            new InformationDialog(Constants.ERROR, Constants.GIVEN_DATA_IS_INCOMPLETE_FIRST_PART_OF_INFORMATION,
                    Constants.GIVEN_DATA_IS_INCOMPLETE_SECOND_PART_OF_INFORMATION, null,
                    null);
        }
    }

    public void exitFrame()
    {
        addSoundFilesFrame.dispose();
        wordContentFrame.dispose();

        if(windowsManager.getIfWordIsBeingModified())
        {
            new AddOrEditWordManuallyFrame(jsonFilesManager, windowsManager, soundFilesManager, wordAndItsContent);
        }
        else
        {
            new AddOrChooseListOfWordsFrame(jsonFilesManager, windowsManager, soundFilesManager, wordAndItsContent);
        }
    }

    public void exitWordContentFrame()
    {
        wordContentFrame.dispose();
    }

    private String getSelectedLanguage()
    {
        if(firstLanguageRadioButton.isSelected())
        {
            return firstLanguageRadioButton.getText();
        }
        else
        {
            return secondLanguageRadioButton.getText();
        }
    }

    private String getLanguage(int number)
    {
        SettingsManager settingsManager = new SettingsManager();

        String language = "";

        if(number == 0)
        {
            language = settingsManager.getLanguageFromWhichWordIsTranslated();
        }
        else
        {
            language = settingsManager.getLanguageToWhichWordIsTranslated();
        }

        return language;
    }

    public boolean getIfBulkFileCreationOptionsIsChosen()
    {
        if(filesCreationRadioButton.isSelected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void setSelectedLanguage(String language)
    {
        if(language.equals(getLanguage(0)))
        {
            secondLanguageRadioButton.setSelected(false);
            firstLanguageRadioButton.setSelected(true);
        }
        else if(language.equals(getLanguage(1)))
        {
            firstLanguageRadioButton.setSelected(false);
            secondLanguageRadioButton.setSelected(true);
        }
    }

    private void setInitialStateAndAvailabilityOfUIComponents()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            Path pathStringForJsonFiles = Paths.get(jsonFilesManager.getCurrentListName() + "/" +
                    jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex()) + "/");

            soundFilesManager.setSoundFilesContent
                    (jsonFilesManager.getSoundFilesContent(pathStringForJsonFiles.toString()));
        }

        if(soundFilesManager.getSoundFilesContent().size() != 0)
        {
            List<String> fileNamesList = createFileNamesList();

            refreshComboBoxState(fileNamesList, 0);

            filesNamesComboBoxAction();

            changeAvailabilityOfNonTextComponent(firstLanguageRadioButton,true);

            boolean savedCreationOption = jsonFilesManager.getFilesBulkCreationValue(
                    jsonFilesManager.getCurrentListName() + "/" +
                            jsonFilesManager.getListOfWords().get
                            (jsonFilesManager.getCurrentWordIndex()) + "/");

            filesCreationRadioButton.setSelected(savedCreationOption);
        }
        else
        {
            changeAvailabilityOfNonTextComponent(filesNamesComboBox, false);
            changeAvailabilityOfTextComponent(fileNameTextField,false);

            changeAvailabilityOfTextComponent(fileContentTextArea,false);
            changeAvailabilityOfNonTextComponent(firstLanguageRadioButton,false);
            changeAvailabilityOfNonTextComponent(secondLanguageRadioButton,false);
            changeAvailabilityOfNonTextComponent(filesCreationRadioButton,false);
            changeAvailabilityOfNonTextComponent(okButton,false);
        }
    }

    private void changeAvailabilityOfTextComponent(JComponent component, Boolean booleanValue)
    {
        component.setEnabled(booleanValue);
        component.setFocusable(booleanValue);

        if(!booleanValue)
        {
            if(component instanceof JTextArea)
            {
                ((JTextArea) component).setText("");
            }
            else if(component instanceof JTextField)
            {
                ((JTextField) component).setText("");
            }
        }
    }

    private void changeAvailabilityOfNonTextComponent(JComponent component, Boolean booleanValue)
    {
        if(component instanceof JRadioButton)
        {
            ((JRadioButton) component).setSelected(booleanValue);
        }

        component.setEnabled(booleanValue);
        component.setFocusable(booleanValue);
    }

    private void refreshComboBoxState(List<String> list, int selectedIndex)
    {
        try
        {
            filesNamesComboBox.setModel(new DefaultComboBoxModel(list.toArray()));
            if(selectedIndex != -1)
            {
                filesNamesComboBox.setSelectedIndex(selectedIndex);
            }
            filesNamesComboBoxAction();
        }
        catch (Exception exception)
        {
           exception.printStackTrace();
        }
    }

    private List<String> createFileNamesList()
    {
        List<String> fileNamesList = new ArrayList<>();
        for (List<String> list: soundFilesManager.getSoundFilesContent())
        {
            fileNamesList.add(list.get(0));
        }

        return fileNamesList;
    }

    private boolean checkIfLanguageIsForbidden(String language)
    {
        if(language.equals(Constants.ESTONIAN_LANGUAGE) || language.equals(Constants.LITHUANIAN_LANGUAGE))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkIfFileNameIsNotEmpty(String name)
    {
        if(!name.replaceAll("\\s", "").equals(""))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkIfFileNameExists(String categoryName, int listIndex)
    {
        List<Boolean> booleans = new ArrayList<>();
        for (List<String> list: soundFilesManager.getSoundFilesContent())
        {
            if(!soundFilesManager.getSoundFilesContent().get(listIndex).equals(list))
            {
                if(list.get(0).equals(categoryName))
                {
                    booleans.add(true);
                }
            }
        }
        if(booleans.contains(true))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean checkIfDataIsCorrect()
    {
        boolean booleanValue = true;

        for(List<String> list: soundFilesManager.getSoundFilesContent())
        {
            for(String string: list)
            {
                if(string == "")
                {
                    booleanValue = false;
                }
            }
        }

        return booleanValue;
    }
}
