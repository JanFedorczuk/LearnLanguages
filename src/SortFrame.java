package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SortFrame extends ComplexFrame
{
    private JRadioButton listsOrder         ;
    private JRadioButton givenListWordsOrder;
    private JRadioButton everyListWordsOrder;

    private JRadioButton sortAlphabetically              ;
    private JRadioButton sortInReverseAlphabeticalOrder  ;
    private JRadioButton sortAccordingToTheDateOfAddition;

    private JComboBox listJComboBox;

    private JButton sortButton;

    private JsonFilesManager jsonFilesManager;
    private ListOfWordsFrame listOfWordsFrame;

    public SortFrame(JsonFilesManager jsonFilesManager, ListOfWordsFrame listOfWordsFrame)
    {
        jsonFilesManager.setListOfLists();

        this.listOfWordsFrame = listOfWordsFrame;
        this.jsonFilesManager = jsonFilesManager;

        createUI();
        setUIOptions();
        displayUI();
    }

    private void createUI()
    {
        this.setLayout(new GridBagLayout());

        size = getSizeForVisualPurposes();

        buttonWidth  = (int) (size * Constants.buttonWidthMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 2.5);
        int mainPanelHeight = (int) (size * 1.256);

        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);
        int fontSize     = (int) (size * Constants.fontMultiplier);

        Dimension mainPanelDimension             = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension chooseWhatToSortPanelDimension = new Dimension(mainPanelWidth, (int)(mainPanelHeight * 0.3630573));
        Dimension chooseHowToSortPanelDimension  = new Dimension(mainPanelWidth, (int)(mainPanelHeight * 0.2229299));

        Dimension buttonPanelDimension           = new Dimension(mainPanelWidth, buttonHeight);

        Dimension buttonDimension = new Dimension(buttonWidth, buttonHeight);
        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        GridBagConstraints gbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel = getNewPanel(mainPanelDimension,null);

        JPanel chooseWhatToSortPanel = createChooseWhatToSortPanel(gbc, chooseWhatToSortPanelDimension, buttonDimension, font);

        JPanel chooseHowToSortPanel = createChooseHowToSortPanel(gbc, chooseHowToSortPanelDimension, buttonDimension,
                font);

        JPanel buttonPanel = createButtonPanel(buttonPanelDimension,buttonDimension,
                font);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;

        mainPanel.add(chooseWhatToSortPanel, panelGbc);

        panelGbc.gridy = 1;

        mainPanel.add(chooseHowToSortPanel, panelGbc);

        panelGbc.gridy = 2;

        mainPanel.add(buttonPanel, panelGbc);

        add(mainPanel);

        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - ((int)(buttonWidth * 1.25))),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 3)));

        this.setResizable(false);

        addListenersToFrame();
    }

    private JPanel createChooseWhatToSortPanel(GridBagConstraints gbc, Dimension chooseWhatToSortPanelDimension,
                                               Dimension buttonDimension, Font font)
    {
        JPanel chooseWhatToSortPanel = getNewPanel(chooseWhatToSortPanelDimension, null);

        JLabel chooseWhatToSortLabel = getNewLabel(Constants.CHOOSE_WHAT_TO_SORT, font);


        listsOrder          = getNewRadioButton(buttonDimension, Constants.LISTS, font);
        givenListWordsOrder = getNewRadioButton(buttonDimension, Constants.LIST_WORDS, font);
        everyListWordsOrder = getNewRadioButton(buttonDimension, Constants.ALL_WORDS, font);

        givenListWordsOrder.setSelected(true);

        JPanel radioButtonsFirstPanel = getNewPanel(
                (new Dimension((int)chooseWhatToSortPanelDimension.getWidth(), (int)buttonDimension.getHeight())),
                null);

        radioButtonsFirstPanel.add(everyListWordsOrder);
        radioButtonsFirstPanel.add(givenListWordsOrder);
        radioButtonsFirstPanel.add(listsOrder);

        JPanel comboBoxPanel = getNewPanel(buttonDimension, null);

        listJComboBox = getNewComboBox(buttonDimension, font);

        setListComboBoxContent();

        comboBoxPanel.add(listJComboBox);

        gbc.gridy = 0;

        chooseWhatToSortPanel.add(chooseWhatToSortLabel, gbc);

        gbc.gridy = 1;

        chooseWhatToSortPanel.add(radioButtonsFirstPanel, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;

        chooseWhatToSortPanel.add(comboBoxPanel, gbc);

        return chooseWhatToSortPanel;
    }

    private JPanel createChooseHowToSortPanel(GridBagConstraints gbc, Dimension chooseHowToSortPanelDimension,
                                              Dimension buttonDimension, Font font)
    {
        JPanel chooseHowToSortPanel = getNewPanel(chooseHowToSortPanelDimension, null);

        JLabel choseHowToSortLabel= getNewLabel(Constants.CHOOSE_HOW_TO_SORT, font);

        sortAlphabetically               = getNewRadioButton(buttonDimension, Constants.A_Z, font);
        sortInReverseAlphabeticalOrder   = getNewRadioButton(buttonDimension, Constants.Z_A, font);
        sortAccordingToTheDateOfAddition =
                getNewRadioButton(buttonDimension, Constants.ACCORDING_TO_DATE_OF_ADDITION, font);

        sortInReverseAlphabeticalOrder.setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(sortAlphabetically);
        buttonGroup.add(sortInReverseAlphabeticalOrder);
        buttonGroup.add(sortAccordingToTheDateOfAddition);

        JPanel radioButtonsSecondPanel = new JPanel(new GridBagLayout());

        radioButtonsSecondPanel.add(sortAlphabetically);
        radioButtonsSecondPanel.add(sortInReverseAlphabeticalOrder);
        radioButtonsSecondPanel.add(sortAccordingToTheDateOfAddition);

        gbc.weighty = 1;
        gbc.gridy = 0;

        chooseHowToSortPanel.add(choseHowToSortLabel, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0;

        chooseHowToSortPanel.add(radioButtonsSecondPanel, gbc);

        return chooseHowToSortPanel;
    }

    private JPanel createButtonPanel(Dimension buttonPanelDimension, Dimension buttonDimension, Font font)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        sortButton = getNewButton(buttonDimension, Constants.SORT, font);

        buttonPanel.add(sortButton);

        return buttonPanel;
    }

    private void addListenersToFrame()
    {
        addActionListenerToChooseWhatToSortButton();
        addActionListenerToSortButton();

        addNavigationKeyListeners();

        addMouseListeners();

        addWindowListener();
    }

    private void addActionListenerToSortButton()
    {
        sortButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jsonFilesManager.setChosenListName
                        (jsonFilesManager.getListOfLists().get(listJComboBox.getSelectedIndex()));

                SortingManager sortingManager = new SortingManager(returnNameOfElementToBeSorted(),
                        returnNameOfList(), returnNameOfTypeOfSorting(), jsonFilesManager, listOfWordsFrame);
            }
        });
    }

    private void addActionListenerToChooseWhatToSortButton()
    {
        listsOrder.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setListComboBoxAvailability(false);
            }
        });

        givenListWordsOrder.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setListComboBoxAvailability(true);
            }
        });

        everyListWordsOrder.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setListComboBoxAvailability(false);
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(givenListWordsOrder);
        components.add(listJComboBox);
        components.add(sortInReverseAlphabeticalOrder);
        components.add(sortButton);

        addNavigationKeyListenersToMainComponents(components, false, true);

        List<Component> chooseWhatToSortButtons = new ArrayList<>();

        chooseWhatToSortButtons.add(everyListWordsOrder);
        chooseWhatToSortButtons.add(givenListWordsOrder);
        chooseWhatToSortButtons.add(listsOrder);

        addHorizontalNavigationKeyBindingsToToolbarButtons(chooseWhatToSortButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(chooseWhatToSortButtons, components, 0, false);

        List<Component> chooseHowToSortButtons = new ArrayList<>();

        chooseHowToSortButtons.add(sortAccordingToTheDateOfAddition);
        chooseHowToSortButtons.add(sortAlphabetically);
        chooseHowToSortButtons.add(sortInReverseAlphabeticalOrder);

        addHorizontalNavigationKeyBindingsToToolbarButtons(chooseWhatToSortButtons);
        addVerticalNavigationKeyBindingsToToolbarButtons(chooseWhatToSortButtons, components, 0, false);
    }

    private void addMouseListeners()
    {
        List<JRadioButton> sortRadioButtons = new ArrayList<>();

        sortRadioButtons.add(listsOrder);
        sortRadioButtons.add(givenListWordsOrder);
        sortRadioButtons.add(everyListWordsOrder);

        List<JRadioButton> typeOfSortingRadioButtons = new ArrayList<>();

        typeOfSortingRadioButtons.add(sortAlphabetically);
        typeOfSortingRadioButtons.add(sortInReverseAlphabeticalOrder);
        typeOfSortingRadioButtons.add(sortAccordingToTheDateOfAddition);


        addMouseListenersToRadioButtons(sortRadioButtons);
        addMouseListenersToRadioButtons(typeOfSortingRadioButtons);
    }

    private void addMouseListenersToRadioButtons(List<JRadioButton> radioButtons)
    {
        for(JRadioButton radioButton: radioButtons)
        {
            radioButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    int index = radioButtons.indexOf(radioButton);
                    int nextIndex;
                    int previousIndex;

                    if(index == 0)
                    {
                        nextIndex = index + 1;
                        previousIndex = radioButtons.size() - 1;
                    }
                    else if(index == (radioButtons.size() - 1))
                    {
                        nextIndex = 0;
                        previousIndex = index - 1;
                    }
                    else
                    {
                        nextIndex = index + 1;
                        previousIndex = index - 1;
                    }

                    if(radioButton.isSelected())
                    {
                        radioButtons.get(nextIndex).setSelected(false);
                        radioButtons.get(previousIndex).setSelected(false);
                    }
                    else
                    {
                        radioButton.setSelected(true);
                    }
                }
            });
        }
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

    private void setListComboBoxContent()
    {
        List<String> newList = new ArrayList<>();
        newList.addAll(jsonFilesManager.getListOfLists());

        listJComboBox.setModel(new DefaultComboBoxModel(newList.toArray()));
    }

    private void setListComboBoxAvailability(Boolean value)
    {
        listJComboBox.setEnabled(value);
    }

    public void exitFrame()
    {
        this.dispose();
        new ListOfWordsFrame(jsonFilesManager, new WindowsManager(false,
                false,false));
    }

    private String returnNameOfElementToBeSorted()
    {
        if (listsOrder.isSelected())
        {
            return Constants.ALL_LISTS;
        }
        else if(givenListWordsOrder.isSelected())
        {
            return Constants.WORDS_OF_GIVEN_LIST;
        }
        else
        {
            return Constants.WORDS_OF_ALL_LISTS;
        }
    }

    private String returnNameOfTypeOfSorting()
    {
        if (sortAlphabetically.isSelected())
        {
            return Constants.ALPHABETICAL;
        }
        else if(sortInReverseAlphabeticalOrder.isSelected())
        {
            return Constants.REVERSE_ALPHABETICAL;
        }
        else
        {
            return Constants.DATE_OF_ADDITION;
        }
    }

    private String returnNameOfList()
    {
        if(listJComboBox.isEnabled())
        {
            return jsonFilesManager.getListOfLists().get(listJComboBox.getSelectedIndex());
        }
        else
        {
            return  null;
        }
    }

}
