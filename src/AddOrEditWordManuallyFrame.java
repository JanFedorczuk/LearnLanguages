package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddOrEditWordManuallyFrame extends MultiComponentComplexFrame
{
    private AddOrEditWordManuallyFrame addOrEditWordManuallyFrame = this;

    private JTextField     wordTextField;

    private JToolBar   categoryToolBar  ;
    private JComboBox  categoryComboBox ;
    private JTextField categoryTextField;

    private JToolBar    elementToolBar ;
    private JComboBox   elementComboBox;
    private JTextArea   elementTextArea;

    private JButton addCategory;
    private JButton deleteCategory;
    private JButton moveCategoryUp;
    private JButton moveCategoryDown;
    private JButton modifyCategory;

    private JButton addElement;
    private JButton deleteElement;
    private JButton moveElementUp;
    private JButton moveElementDown;
    private JButton modifyElement;

    private JButton actionJButton;

    private List<List<String>> wordAndItsContent;

    private WindowAdapter windowAdapter;

    private JsonFilesManager jsonFilesManager;
    private WindowsManager   windowsManager;

    public AddOrEditWordManuallyFrame
            (JsonFilesManager jsonFilesManager, WindowsManager windowsManager, List<List<String>> wordAndItsContent)
    {
        this.wordAndItsContent = wordAndItsContent;
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

        buttonWidth      = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 1.75);
        int mainPanelHeight = (int) (size * 2.376);

        int fontSize = (int) (size * 0.052);

        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension mainPanelDimension     = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension wordPanelDimension     = new Dimension(mainPanelWidth, (int)Math.round(mainPanelHeight * 0.1178451));
        Dimension categoryPanelDimension = new Dimension(mainPanelWidth, (int)Math.round(mainPanelHeight * 0.2659932));
        Dimension elementsPanelDimension = new Dimension(mainPanelWidth, (int)Math.round(mainPanelHeight * 0.3535353));
        Dimension buttonPanelDimension   = new Dimension(mainPanelWidth, buttonHeight);

        Dimension buttonDimension            = new Dimension(buttonWidth, buttonHeight);
        Dimension toolBarButtonDimension     = new Dimension((int)(buttonWidth / 4), buttonHeight);
        Dimension elementScrollPaneDimension = new Dimension((int)buttonWidth * 5 / 4, buttonHeight * 3);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);

        JPanel wordPanel = getNewWordPanel(componentGbc, wordPanelDimension,
                buttonDimension, font);

        JPanel elementsPanel = getNewElementsPanel(componentGbc,elementsPanelDimension,
                buttonDimension, toolBarButtonDimension, elementScrollPaneDimension, font);

        JPanel categoryPanel = getNewCategoriesPanel(componentGbc, categoryPanelDimension,
                buttonDimension, toolBarButtonDimension, font);
        JPanel buttonPanel = getNewButtonPanel(componentGbc, buttonPanelDimension, buttonDimension, font);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;
        mainPanel.add(wordPanel, panelGbc);

        panelGbc.gridy = 1;
        mainPanel.add(categoryPanel, panelGbc);

        panelGbc.gridy = 2;
        mainPanel.add(elementsPanel, panelGbc);

        panelGbc.gridy = 3;
        mainPanel.add(buttonPanel, panelGbc);

        add(mainPanel);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - ((int)(buttonWidth * 0.875))),
                (Constants.DIMENSION.height / 2) - ((Constants.DIMENSION.height / 3)));

        this.setResizable(false);

        addListenersToFrame();

        setInitialStateAndAvailabilityOfUIComponents();
    }

    private JPanel getNewWordPanel(GridBagConstraints componentGbc, Dimension wordPanelDimension, Dimension buttonDimension,
                                   Font font)
    {
        JPanel wordPanel = getNewPanel(wordPanelDimension, null);

        JLabel wordLabel = getNewLabel(Constants.WORD, font);

        wordTextField = getNewTextField(buttonDimension, font, SwingConstants.CENTER);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;
        wordPanel.add(wordLabel, componentGbc);

        componentGbc.gridy = 1;
        componentGbc.weighty = 0;
        wordPanel.add(wordTextField, componentGbc);

        return wordPanel;
    }

    private JPanel getNewCategoriesPanel(GridBagConstraints componentGbc, Dimension categoryPanelDimension,
                                         Dimension buttonDimension, Dimension toolBarButtonDimension, Font font)
    {
        JPanel categoryPanel = getNewPanel(categoryPanelDimension, null);

        JLabel categoryLabel = getNewLabel(Constants.CATEGORY, font);

        categoryToolBar = getNewToolbar(buttonDimension, false, true);

        categoryComboBox = getNewComboBox(buttonDimension, font);

        addButtonsToCategoryToolbar(toolBarButtonDimension, font);

        categoryTextField = getNewTextField(buttonDimension, font, SwingConstants.CENTER);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;
        categoryPanel.add(categoryLabel, componentGbc);

        componentGbc.gridy = 1;
        categoryPanel.add(categoryToolBar, componentGbc);

        componentGbc.gridy = 2;
        categoryPanel.add(categoryComboBox, componentGbc);

        componentGbc.gridy = 3;
        componentGbc.weighty = 0;
        categoryPanel.add(categoryTextField, componentGbc);

        changeAvailabilityOfCategoryComboBox(false);
        changeAvailabilityOfCategoryTextField(false);

        return categoryPanel;
    }

    private JPanel getNewElementsPanel(GridBagConstraints componentGbc, Dimension elementsPanelDimension,
                                       Dimension buttonDimension, Dimension toolBarButtonDimension,
                                       Dimension scrollPaneDimension, Font font)
    {
        JPanel elementsPanel = getNewPanel(elementsPanelDimension, null);

        JLabel elementLabel = getNewLabel(Constants.ELEMENT, font);

        elementToolBar = getNewToolbar(buttonDimension, false, true);

        addButtonsToElementsToolBar(toolBarButtonDimension, font);

        elementComboBox = getNewComboBox(buttonDimension, font);

        elementTextArea = getNewJTextArea(font);

        JScrollPane elementScrollPane = getNewScrollPane(scrollPaneDimension, elementTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        elementTextArea.setFont(font);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;
        elementsPanel.add(elementLabel, componentGbc);

        componentGbc.gridy = 1;
        elementsPanel.add(elementToolBar, componentGbc);

        componentGbc.gridy = 2;
        elementsPanel.add(elementComboBox, componentGbc);

        componentGbc.gridy = 3;
        componentGbc.weighty = 0;
        elementsPanel.add(elementScrollPane, componentGbc);

        changeAvailabilityOfElementToolBar(false);
        changAvailabilityOfElementComboBox(false);
        changAvailabilityOfElementTextArea(false);

        return elementsPanel;
    }

    private JPanel getNewButtonPanel(GridBagConstraints componentGbc, Dimension buttonPanelDimension, Dimension buttonDimension,
                                     Font font)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        if(!windowsManager.getIfWordIsBeingModified())
        {
            actionJButton = getNewButton(buttonDimension, Constants.PREVIEW, font);
        }
        else
        {
            actionJButton = getNewButton(buttonDimension, Constants.REPLACE, font);
        }

        componentGbc.gridx = 0;
        componentGbc.gridy = 0;
        buttonPanel.add(actionJButton, componentGbc);

        return buttonPanel;
    }

    private void addButtonsToCategoryToolbar(Dimension toolBarButtonDimension, Font font)
    {
        addCategory      = getNewButton(toolBarButtonDimension, Constants.ADD,       font);
        deleteCategory   = getNewButton(toolBarButtonDimension, Constants.DELETE,    font);
        moveCategoryUp   = getNewButton(toolBarButtonDimension, Constants.MOVE_UP,   font);
        moveCategoryDown = getNewButton(toolBarButtonDimension, Constants.MOVE_DOWN, font);
        modifyCategory   = getNewButton(toolBarButtonDimension, Constants.MODIFY,    font);

        categoryToolBar.add(addCategory);
        categoryToolBar.add(deleteCategory);
        categoryToolBar.add(moveCategoryUp);
        categoryToolBar.add(moveCategoryDown);
        categoryToolBar.add(modifyCategory);
    }

    private void addButtonsToElementsToolBar(Dimension toolBarButtonDimension, Font font)
    {
        addElement      = getNewButton(toolBarButtonDimension, Constants.ADD,       font);
        deleteElement   = getNewButton(toolBarButtonDimension, Constants.DELETE,    font);
        moveElementUp   = getNewButton(toolBarButtonDimension, Constants.MOVE_UP,   font);
        moveElementDown = getNewButton(toolBarButtonDimension, Constants.MOVE_DOWN, font);
        modifyElement   = getNewButton(toolBarButtonDimension, Constants.MODIFY,    font);

        elementToolBar.add(addElement);
        elementToolBar.add(deleteElement);
        elementToolBar.add(moveElementUp);
        elementToolBar.add(moveElementDown);
        elementToolBar.add(modifyElement);
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
        addActionListenersToCategoryPanel();
        addActionListenersToElementPanel();
        addActionListenersToButtonPanel();
    }

    private void addActionListenersToCategoryPanel()
    {
        categoryComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                categoryComboBoxAction();
            }
        });

        addCategory.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addCategory();
            }
        });

        deleteCategory.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteCategory();
            }
        });

        moveCategoryUp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveCategoryUp();
            }
        });

        moveCategoryDown.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveCategoryDown();
            }
        });

        modifyCategory.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyCategory();
            }
        });
    }

    private void addActionListenersToElementPanel()
    {
        elementComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                elementComboBoxAction();
            }
        });

        addElement.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addElement();
            }
        });

        deleteElement.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                deleteElement();
            }
        });

        moveElementDown.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveElementDown();
            }
        });

        moveElementUp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                moveElementUp();
            }
        });

        modifyElement.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                modifyElement();
            }
        });
    }

    private void addActionListenersToButtonPanel()
    {
        actionJButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                actionButtonAction();
            }
        });
    }

    private void addKeyListeners()
    {
        addKeyListenersToCategoryPanel();
        addKeyListenersToElementPanel();
        addKeyListenersToButtonPanel();
    }

    private void addKeyListenersToCategoryPanel()
    {
        categoryComboBox.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    categoryComboBoxAction();
                }
            }
        });

        addCategory.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    addCategory();
                }
            }
        });

        deleteCategory.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    deleteCategory();
                }
            }
        });

        moveCategoryUp.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveCategoryUp();
                }
            }
        });

        moveCategoryDown.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveCategoryDown();
                }
            }
        });

        modifyCategory.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    modifyCategory();
                }
            }
        });


    }

    private void addKeyListenersToElementPanel()
    {
        elementComboBox.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    elementComboBoxAction();
                }
            }
        });

        addElement.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    addElement();
                }
            }
        });

        deleteElement.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    deleteElement();
                }
            }
        });

        moveElementUp.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveElementUp();
                }
            }
        });

        moveElementDown.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    moveElementDown();
                }
            }
        });

        modifyElement.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    modifyElement();
                }
            }
        });


    }

    private void addKeyListenersToButtonPanel()
    {
        actionJButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    actionButtonAction();
                }
            }
        });


    }

    private void addWindowListener()
    {
        if(windowsManager.getIfWordIsBeingModified())
        {
            windowAdapter = new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    addOrEditWordManuallyFrame.dispose();
                    new ListOfWordsFrame(jsonFilesManager, windowsManager);
                }
            };
        }
        else
        {
            windowAdapter = new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    addOrEditWordManuallyFrame.dispose();
                    new AddWordFrame(jsonFilesManager, windowsManager);
                }
            };
        }

        this.addWindowListener(windowAdapter);
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(wordTextField);
        components.add(moveCategoryUp);
        components.add(categoryComboBox);
        components.add(categoryTextField);
        components.add(moveElementUp);
        components.add(elementComboBox);
        components.add(elementTextArea);
        components.add(actionJButton);

        addNavigationKeyListenersToMainComponents(components, false, true);

        addNavigationKeyListenersToComboBox(categoryComboBox);
        addNavigationKeyListenersToComboBox(elementComboBox);

        List<Component> categoryButtons = new ArrayList<>();

        categoryButtons.add(addCategory);
        categoryButtons.add(deleteCategory);
        categoryButtons.add(moveCategoryUp);
        categoryButtons.add(moveCategoryDown);
        categoryButtons.add(modifyCategory);

        addHorizontalNavigationKeyBindingsToToolbarButtons(categoryButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(categoryButtons, components, 1, false);

        List<Component> elementButtons = new ArrayList<>();

        elementButtons.add(addElement);
        elementButtons.add(deleteElement);
        elementButtons.add(moveElementUp);
        elementButtons.add(moveElementDown);
        elementButtons.add(modifyElement);

        addHorizontalNavigationKeyBindingsToToolbarButtons(elementButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(elementButtons, components, 4, false);
    }

    private void categoryComboBoxAction()
    {
        try
        {
            categoryTextField.setText(wordAndItsContent.get(categoryComboBox.getSelectedIndex()).get(0));
            if(wordAndItsContent.get(categoryComboBox.getSelectedIndex()).size() >= 2)
            {
                changAvailabilityOfElementComboBox(true);
                changAvailabilityOfElementTextArea(true);

                changeStateOfElementComboBoxAndElementTextArea(true);
            }
            else
            {
                changAvailabilityOfElementComboBox(false);
                changAvailabilityOfElementTextArea(false);

                changeStateOfElementComboBoxAndElementTextArea(false);
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void addCategory()
    {
        addNewCategory(0);

        if(wordAndItsContent.size() == 1)
        {
            changeAvailabilityOfCategoryComboBox(true);
            changeAvailabilityOfCategoryTextField(true);

            changeAvailabilityOfElementToolBar(true);
        }

        List<String> categoriesList = createCategoriesList();

        refreshComboBoxState(categoryComboBox, categoriesList, wordAndItsContent.size() - 1);
    }

    private void deleteCategory()
    {
        try
        {
            int index = categoryComboBox.getSelectedIndex();
            wordAndItsContent.remove(index);

            List<String> categoriesList = createCategoriesList();
            refreshComboBoxState(categoryComboBox, categoriesList, -1);

            if(index < wordAndItsContent.size() && wordAndItsContent.size() != 0)
            {
                categoryComboBox.setSelectedIndex(index);
            }
            else if(index >= wordAndItsContent.size() && wordAndItsContent.size() != 0)
            {
                categoryComboBox.setSelectedIndex(index - 1);
            }
            else
            {
                changeAvailabilityOfCategoryComboBox(false);
                changeAvailabilityOfCategoryTextField(false);

                changeStateOfElementComboBoxAndElementTextArea(false);
                changeAvailabilityOfElementToolBar(false);

                changAvailabilityOfElementComboBox(false);
                changAvailabilityOfElementTextArea(false);
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void moveCategoryUp()
    {
        int index = categoryComboBox.getSelectedIndex();

        if(wordAndItsContent.size() != 0 && wordAndItsContent.size() != 1)
        {
            if(index != 0)
            {
                List<List<String>> copyOfWordAndItsContent = new ArrayList<>(wordAndItsContent);

                wordAndItsContent.set(index, copyOfWordAndItsContent.get(index - 1));
                wordAndItsContent.set(index - 1, copyOfWordAndItsContent.get(index));

                List<String> categoriesList = createCategoriesList();
                refreshComboBoxState(categoryComboBox, categoriesList, index - 1);

            }
        }
    }

    private void moveCategoryDown()
    {
        int index = categoryComboBox.getSelectedIndex();

        if(wordAndItsContent.size() != 0 && wordAndItsContent.size() != 1)
        {
            if(index != wordAndItsContent.size() - 1)
            {
                List<List<String>> newWordAndItsContent = new ArrayList<>(wordAndItsContent);

                wordAndItsContent.set(index, newWordAndItsContent.get(index + 1));
                wordAndItsContent.set(index + 1, newWordAndItsContent.get(index));

                List<String> categoriesList = createCategoriesList();
                refreshComboBoxState(categoryComboBox, categoriesList, index + 1);
            }
        }
    }

    private void modifyCategory()
    {
        try
        {
            if(categoryComboBox.getItemCount() != 0)
            {
                int index = categoryComboBox.getSelectedIndex();

                if(checkIfNameIsNotEmpty(categoryTextField.getText()))
                {
                    if(!checkIfCategoryExists(categoryTextField.getText()))
                    {
                        wordAndItsContent.get(categoryComboBox.getSelectedIndex()).set(0, categoryTextField.getText());

                        List<String> categoriesList = createCategoriesList();

                        refreshComboBoxState(categoryComboBox, categoriesList, index);
                    }
                    else
                    {
                        categoryTextField.setText(Constants.CATEGORY_ALREADY_EXISTS);
                    }
                }
                else
                {
                    categoryTextField.setText(Constants.CATEGORY_NAME_IS_EMPTY);
                }
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void elementComboBoxAction()
    {
        elementTextArea.setText(wordAndItsContent.get(categoryComboBox.getSelectedIndex()).get(
                elementComboBox.getSelectedIndex() + 1));
    }

    private void addElement()
    {
        int categoryIndex = categoryComboBox.getSelectedIndex();
        addNewElement(categoryIndex, 0);

        List <String> elementsList = createElementsList(categoryIndex);

        refreshComboBoxState(elementComboBox, elementsList,elementsList.size() - 1);

        changAvailabilityOfElementComboBox(true);
        changAvailabilityOfElementTextArea(true);
    }

    private void deleteElement()
    {
        try
        {
            int categoryIndex = categoryComboBox.getSelectedIndex();
            int elementIndex = elementComboBox.getSelectedIndex();

            if(wordAndItsContent.get(categoryIndex).size() > 1)
            {
                wordAndItsContent.get(categoryIndex).remove(elementIndex + 1);

                List <String> elementsList = createElementsList(categoryIndex);
                refreshComboBoxState(elementComboBox, elementsList, -1);

                if(elementIndex < elementsList.size() && elementsList.size() != 0)
                {
                    elementComboBox.setSelectedIndex(elementIndex);
                }
                else if(elementIndex >= elementsList.size() && elementsList.size() != 0)
                {
                    elementComboBox.setSelectedIndex(elementIndex - 1);
                }

                if(elementsList.size() == 0)
                {
                    changAvailabilityOfElementComboBox(false);
                    changAvailabilityOfElementTextArea(false);

                    changeStateOfElementComboBoxAndElementTextArea(false);
                }
            }
        }
        catch (Exception exception)
        {
        }
    }

    private void moveElementUp()
    {
        int categoryIndex = categoryComboBox.getSelectedIndex();
        int elementIndex = elementComboBox.getSelectedIndex();

        if(wordAndItsContent.get(categoryIndex).size() > 1)
        {
            if(elementIndex != 0)
            {
                List<String> copyOfWordAndItsContent = new ArrayList<>(wordAndItsContent.get(categoryIndex));

                wordAndItsContent.get(categoryIndex).set(elementIndex + 1,
                        copyOfWordAndItsContent.get(elementIndex));

                wordAndItsContent.get(categoryIndex).set(elementIndex,
                        copyOfWordAndItsContent.get(elementIndex + 1));

                List<String> elementsList = createElementsList(categoryIndex);
                refreshComboBoxState(elementComboBox, elementsList, elementIndex - 1);
            }
        }
    }

    private void moveElementDown()
    {
        int categoryIndex = categoryComboBox.getSelectedIndex();
        int elementIndex = elementComboBox.getSelectedIndex();

        if(wordAndItsContent.get(categoryIndex).size() > 1)
        {
            if(elementIndex != wordAndItsContent.get(categoryIndex).size() - 2)
            {
                List<String> copyOfWordAndItsContent = new ArrayList<>(wordAndItsContent.get(categoryIndex));

                wordAndItsContent.get(categoryIndex).set(elementIndex + 2,
                        copyOfWordAndItsContent.get(elementIndex + 1));

                wordAndItsContent.get(categoryIndex).set(elementIndex + 1,
                        copyOfWordAndItsContent.get(elementIndex + 2));

                List<String> elementsList = createElementsList(categoryIndex);
                refreshComboBoxState(elementComboBox, elementsList, elementIndex + 1);
            }
        }
    }

    private void modifyElement()
    {
        try
        {
            if(elementComboBox.getItemCount() != 0)
            {
                int categoryIndex = categoryComboBox.getSelectedIndex();
                int elementIndex = elementComboBox.getSelectedIndex();

                if(checkIfNameIsNotEmpty(elementTextArea.getText()))
                {
                    if(!checkIfElementExists(categoryIndex, elementTextArea.getText()))
                    {
                        wordAndItsContent.get(categoryComboBox.getSelectedIndex()).set(elementIndex + 1, elementTextArea.getText());

                        List<String> elementsList = createElementsList(categoryIndex);
                        refreshComboBoxState(elementComboBox, elementsList, elementIndex - 1);
                    }
                    else
                    {
                        elementTextArea.setText(Constants.ELEMENT_ALREADY_EXISTS);
                    }
                }
                else
                {
                    elementTextArea.setText(Constants.ELEMENT_NAME_IS_EMPTY);
                }
            }
        }
        catch (Exception exception)
        {
        }

    }

    private void actionButtonAction()
    {
        if(checkIfUserHasEnteredData())
        {
            if(!windowsManager.getIfWordIsBeingModified())
            {
                wordAndItsContent = returnProperWordAndItsContentList();

                addOrEditWordManuallyFrame.dispose();
                new WordFrame(jsonFilesManager, windowsManager, wordAndItsContent);
            }
            else
            {
                wordAndItsContent = returnProperWordAndItsContentList();

                jsonFilesManager.setContentOfGivenWord(wordAndItsContent);

                if(jsonFilesManager.checkIfWordDoNotExists(wordAndItsContent.get(0).get(0)))
                {
                    jsonFilesManager.replaceWordWithAnotherOne();
                    jsonFilesManager.setListOfWords();

                    new InformationDialog(Constants.INFORMATION, Constants.WORD_SUCCESSFULLY_MODIFIED,
                            Constants.CLICK_OK_TO_CONTINUE, null);
                }
                else
                {
                    new InformationDialog(Constants.INFORMATION,
                            Constants.WORD_ALREADY_EXISTS_FIRST_PART_OF_MEESAGE,
                            Constants.WORD_ALREADY_EXISTS_SECOND_PART_OF_MEESAGE, null);
                }

                wordAndItsContent.remove(0);
            }
        }
        else
        {
            new InformationDialog(Constants.INFORMATION,
                    Constants.GIVEN_DATA_IS_INCOMPLETE_FIRST_PART_OF_INFORMATION,
                    Constants.GIVEN_DATA_IS_INCOMPLETE_SECOND_PART_OF_INFORMATION, null);
        }
    }

    public void exitFrame()
    {
        if(windowsManager.getIfWordIsBeingModified())
        {
            addOrEditWordManuallyFrame.dispose();
            new ListOfWordsFrame(jsonFilesManager, windowsManager);
        }
        else
        {
            addOrEditWordManuallyFrame.dispose();
            new AddWordFrame(jsonFilesManager, windowsManager);
        }
    }

    private void addNewCategory(int index)
    {
        ArrayList<Boolean> booleans = new ArrayList<>();
        String string = Constants.NEW_CATEGORY + index;
        for(List<String> list: wordAndItsContent)
        {
            if(list.get(0).contains(string))
            {
                booleans.add(false);
            }
        }
        if(booleans.contains(false))
        {
            addNewCategory(index + 1);
        }
        else
        {
            List<String> List = new ArrayList<>();
            List.add(Constants.NEW_CATEGORY + index);
            wordAndItsContent.add(List);
        }
    }

    private void addNewElement(int categoryIndex, int elementNumber)
    {
        ArrayList<Boolean> booleans = new ArrayList<>();

        String elementName = Constants.NEW_ELEMENT + elementNumber;

        if(wordAndItsContent.get(categoryIndex).size() > 1)
        {
            for(int counter = 1; counter < wordAndItsContent.get(categoryIndex).size(); counter++)
            {
                if (wordAndItsContent.get(categoryIndex).get(counter).contains(elementName))
                {
                    booleans.add(false);
                }
            }
        }

        if(booleans.contains(false))
        {
            addNewElement(categoryIndex, elementNumber + 1);
        }
        else
        {
            wordAndItsContent.get(categoryIndex).add(elementName);
        }
    }

    private boolean checkIfCategoryExists(String categoryName)
    {
        List<Boolean> booleans = new ArrayList<>();
        for (List<String> list: wordAndItsContent)
        {
            if(list.get(0).equals(categoryName))
            {
                booleans.add(true);
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

    private boolean checkIfElementExists(int index, String elementName)
    {
        List<Boolean> booleans = new ArrayList<>();
        for (int counter = 1; counter < wordAndItsContent.get(index).size(); counter++)
        {
            if(wordAndItsContent.get(index).get(counter).equals(elementName))
            {
                booleans.add(true);
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

    private boolean checkIfNameIsNotEmpty(String name)
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

    private boolean checkIfUserHasEnteredData()
    {
        List<Boolean> booleanList = new ArrayList<>();

        if((!wordTextField.getText().replaceAll("\\s", "").equals("")
                && (wordAndItsContent.size() != 0)))
        {
            for(List<String> list: wordAndItsContent)
            {
                if(list.size() <= 1 )
                {
                    booleanList.add(false);
                }
            }
            if(booleanList.contains(false))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    private void changeAvailabilityOfCategoryComboBox(Boolean booleanValue)
    {
        categoryComboBox.setEnabled(booleanValue);
        categoryComboBox.setFocusable(booleanValue);

        categoryTextField.setEnabled(booleanValue);
        categoryTextField.setFocusable(booleanValue);
        categoryTextField.setText("");
    }

    private void changeAvailabilityOfCategoryTextField(Boolean booleanValue)
    {
        categoryTextField.setEnabled(booleanValue);
        categoryTextField.setFocusable(booleanValue);
        categoryTextField.setText("");
    }

    private void changeAvailabilityOfElementToolBar(Boolean booleanValue)
    {
        elementToolBar.         setEnabled(booleanValue);

        addElement.     setEnabled(booleanValue);
        deleteElement.  setEnabled(booleanValue);
        moveElementUp.  setEnabled(booleanValue);
        moveElementDown.setEnabled(booleanValue);
        modifyElement.  setEnabled(booleanValue);
    }

    private void changAvailabilityOfElementComboBox(Boolean booleanValue)
    {
        elementComboBox.setEnabled(booleanValue);
        elementComboBox.setFocusable(booleanValue);
    }

    private void changAvailabilityOfElementTextArea(Boolean booleanValue)
    {
        elementTextArea.setEnabled(booleanValue);
        elementTextArea.setFocusable(booleanValue);
    }

    private void changeStateOfElementComboBoxAndElementTextArea(Boolean booleanValue)
    {
        List <String> elementsList = new ArrayList<>();
        if(booleanValue)
        {
            elementsList.addAll(wordAndItsContent.get(categoryComboBox.getSelectedIndex()));
            elementsList.remove(0);

            refreshComboBoxState(elementComboBox, elementsList, elementsList.size() - 1);
        }
        else
        {
            elementComboBox.setModel(new DefaultComboBoxModel(elementsList.toArray()));
            elementTextArea.setText("");
        }
    }

    private void setInitialStateAndAvailabilityOfUIComponents()
    {
        if(wordAndItsContent.size() != 0)
        {
            wordTextField.setText(wordAndItsContent.get(0).get(0));
            wordAndItsContent.remove(0);

            changeAvailabilityOfCategoryComboBox(true);
            changeAvailabilityOfCategoryTextField(true);
            changeAvailabilityOfElementToolBar(true);

            List<String> newCategoriesList = createCategoriesList();
            refreshComboBoxState(categoryComboBox, newCategoriesList, newCategoriesList.size() - 1);

            if(wordAndItsContent.get(wordAndItsContent.size() - 1).size() >= 2)
            {
                changeStateOfElementComboBoxAndElementTextArea(true);
            }
        }
    }

    private void refreshComboBoxState(JComboBox jComboBox, List<String> list, int selectedIndex)
    {
        if(jComboBox.equals(categoryComboBox))
        {
            categoryComboBox.setModel(new DefaultComboBoxModel(list.toArray()));
            if(selectedIndex != -1)
            {
                categoryComboBox.setSelectedIndex(selectedIndex);
            }
            categoryComboBoxAction();
        }
        else
        {
            elementComboBox.setModel(new DefaultComboBoxModel(list.toArray()));
            if(selectedIndex != -1)
            {
                elementComboBox.setSelectedIndex(selectedIndex);
            }
            elementComboBoxAction();
        }
    }

    private List<String> createCategoriesList()
    {
        List<String> categoriesList = new ArrayList<>();
        for (List<String> list: wordAndItsContent)
        {
            categoriesList.add(list.get(0));
        }

        return categoriesList;
    }

    private List<String> createElementsList(int categoryIndex)
    {
        List <String> elementList = new ArrayList<>();
        elementList.addAll(wordAndItsContent.get(categoryIndex));
        elementList.remove(0);

        return elementList;
    }

    private List<List<String>> returnProperWordAndItsContentList()
    {
        List<List<String>> copyOfWordAndItsContent = new ArrayList<>();
        List<String> wordList = new ArrayList<>();

        wordList.add(wordTextField.getText());
        copyOfWordAndItsContent.add(wordList);
        copyOfWordAndItsContent.addAll(wordAndItsContent);

        wordAndItsContent.clear();

        return copyOfWordAndItsContent;
    }
}
