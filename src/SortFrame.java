package LearnLanguages;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SortFrame extends MultiComponentComplexFrame
{
    private JButton previousWindowButton;

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
        Dimension arrowPanelDimensions           = new Dimension(mainPanelWidth, buttonHeight);
        Dimension chooseWhatToSortPanelDimension = new Dimension(mainPanelWidth, (int)(mainPanelHeight * 0.3630573));
        Dimension chooseHowToSortPanelDimension  = new Dimension(mainPanelWidth, (int)(mainPanelHeight * 0.2229299));

        Dimension buttonPanelDimension           = new Dimension(mainPanelWidth, buttonHeight);
        Dimension toolBarButtonDimension = new Dimension((buttonWidth / 5), buttonHeight);

        Dimension buttonDimension = new Dimension(buttonWidth, buttonHeight);
        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        GridBagConstraints gbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel = getNewPanel(mainPanelDimension,null);

        JPanel arrowPanel = getArrowPanel(arrowPanelDimensions, toolBarButtonDimension, font,null);

        JPanel chooseWhatToSortPanel = createChooseWhatToSortPanel(gbc, chooseWhatToSortPanelDimension, buttonDimension, font);

        JPanel chooseHowToSortPanel = createChooseHowToSortPanel(gbc, chooseHowToSortPanelDimension, buttonDimension,
                font);

        JPanel buttonPanel = createButtonPanel(buttonPanelDimension,buttonDimension,
                font);

        previousWindowButton = getPreviousWindowButton(arrowPanel);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;
        panelGbc.weighty = 0;
        mainPanel.add(arrowPanel, panelGbc);

        panelGbc.gridy = 1;
        panelGbc.weighty = 1;
        mainPanel.add(chooseWhatToSortPanel, panelGbc);

        panelGbc.gridy = 2;
        mainPanel.add(chooseHowToSortPanel, panelGbc);

        panelGbc.gridy = 3;
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

        givenListWordsOrder.requestFocus();

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

        ButtonGroup buttonGroup = new ButtonGroup();

        buttonGroup.add(everyListWordsOrder);
        buttonGroup.add(givenListWordsOrder);
        buttonGroup.add(listsOrder);

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
        addActionListenerToSortButton();

        addActionListenersToArrowPanel();

        addKeyListener();

        addKeyListenerToArrowPanel();

        addNavigationKeyListeners();

        addFocusListenerToFirstButtonGroup();

        addWindowListener();
    }

    private void addKeyListener()
    {
        sortButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    sortAction();
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
                    exitFrame();
                }
            }
        });
    }

    private void addActionListenerToSortButton()
    {
        sortButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                sortAction();
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

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(givenListWordsOrder);
        components.add(listJComboBox);
        components.add(sortInReverseAlphabeticalOrder);
        components.add(sortButton);

        addNavigationKeyListenersToMainComponents(components, false, true);

        addNavigationKeyListenersToComboBox(listJComboBox);

        List<Component> chooseWhatToSortButtons = new ArrayList<>();

        chooseWhatToSortButtons.add(everyListWordsOrder);
        chooseWhatToSortButtons.add(givenListWordsOrder);
        chooseWhatToSortButtons.add(listsOrder);

        addHorizontalNavigationKeyBindingsToGroupOfButtons(chooseWhatToSortButtons);
        addVerticalNavigationKeyBindingsToGroupOfButtons(chooseWhatToSortButtons, components, 0, false);

        List<Component> chooseHowToSortButtons = new ArrayList<>();

        chooseHowToSortButtons.add(sortAlphabetically);
        chooseHowToSortButtons.add(sortInReverseAlphabeticalOrder);
        chooseHowToSortButtons.add(sortAccordingToTheDateOfAddition);

        addHorizontalNavigationKeyBindingsToGroupOfButtons(chooseHowToSortButtons);
        addVerticalNavigationKeyBindingsToGroupOfButtons(chooseHowToSortButtons, components, 2, false);
    }

    private void addFocusListenerToFirstButtonGroup()
    {
        listsOrder.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(listsOrder.isSelected())
                {
                    setListComboBoxAvailability(false);
                }
            }
        });

        givenListWordsOrder.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(givenListWordsOrder.isSelected())
                {
                    setListComboBoxAvailability(true);
                }
            }
        });

        everyListWordsOrder.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                if(everyListWordsOrder.isSelected())
                {
                    setListComboBoxAvailability(false);
                }
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
                exitProgram();
            }
        });
    }

    private void sortAction()
    {
        jsonFilesManager.setCurrentListName
                (jsonFilesManager.getListOfLists().get(listJComboBox.getSelectedIndex()));

        SortingManager sortingManager = new SortingManager(returnNameOfElementToBeSorted(),
                returnNameOfList(), returnNameOfTypeOfSorting(), jsonFilesManager, listOfWordsFrame);
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
