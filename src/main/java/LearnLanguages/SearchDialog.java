package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SearchDialog extends ComplexDialog
{
    private SearchDialog searchDialog;
    private ListOfWordsFrame listOfWordsFrame;

    private JTextField phraseTextField;
    private JButton findPhrase;
    private JButton findNext;
    private JButton findPrevious;
    private JTextField searchResultTextField;

    private List<Object> listOfFoundElementsAndTheirIndexes;

    int elementIndex = 0;

    public SearchDialog(ListOfWordsFrame listOfWordsFrame)
    {
        this.listOfWordsFrame = listOfWordsFrame;

        this.listOfWordsFrame = listOfWordsFrame;
        createUI();
        setUIOptions();

        this.setModalityType(ModalityType.APPLICATION_MODAL);

        displayUI();
    }

    private void createUI()
    {
        this.setLayout(new GridBagLayout());

        size = getSizeForVisualPurposes();

        buttonWidth      = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);
        int fontSize     = (int) (size * Constants.fontMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 2.5);
        int mainPanelHeight = (int) (size * 0.904);

        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension buttonDimension      = new Dimension(buttonWidth, buttonHeight);
        Dimension smallButtonDimension = new Dimension(buttonWidth / 2, buttonHeight);

        Dimension mainPanelDimension   = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension phrasePanelDimension = new Dimension(mainPanelWidth, (int)Math.round(size * 0.28));
        Dimension searchPanelDimension = new Dimension(mainPanelWidth, buttonHeight);
        Dimension resultPanelDimension = new Dimension(mainPanelWidth, buttonHeight);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);

        JPanel phrasePanel = createPhrasePanel(componentGbc, phrasePanelDimension, buttonDimension, font);
        JPanel searchPanel = createSearchPanel(componentGbc, searchPanelDimension, buttonDimension,
                smallButtonDimension, font);
        JPanel resultPanel = createResultPanel(componentGbc, resultPanelDimension, buttonDimension, font);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;
        panelGbc.gridx = 0;

        mainPanel.add(phrasePanel, panelGbc);

        panelGbc.gridy = 1;
        panelGbc.gridx = 0;

        mainPanel.add(searchPanel, panelGbc);

        panelGbc.gridy = 2;
        panelGbc.gridx = 0;

        mainPanel.add(resultPanel, panelGbc);

        add(mainPanel);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) + (int)(buttonWidth * 1.1)),
                (int)((Constants.DIMENSION.height / 2) - (Constants.DIMENSION.height / 2.5)));


        this.setResizable(false);

        addListenersToFrame();
    }

    private JPanel createPhrasePanel(GridBagConstraints componentGbc, Dimension phrasePanelDimension,
                                   Dimension buttonDimension, Font font)
    {
        JPanel phrasePanel = getNewPanel(phrasePanelDimension, null);

        JLabel enterPhrase = getNewLabel(Constants.ENTER_SEARCHED_PHRASE, font);

        phraseTextField = getNewTextField(buttonDimension, font, SwingConstants.CENTER);

        phrasePanel.add(enterPhrase, componentGbc);

        componentGbc.gridy = 1;
        componentGbc.gridx = 0;

        componentGbc.weighty = 0;

        phrasePanel.add(phraseTextField, componentGbc);

        return phrasePanel;
    }

    private JPanel createSearchPanel(GridBagConstraints componentGbc, Dimension searchPanelDimension,
                                   Dimension buttonDimension, Dimension smallButtonDimension, Font font)
    {
        JPanel searchPanel = getNewPanel(searchPanelDimension, null);

        findPrevious = getNewButton(smallButtonDimension, Constants.PREVIOUS, font);

        findPhrase = getNewButton(buttonDimension, Constants.SEARCH, font);

        findNext = getNewButton(smallButtonDimension, Constants.NEXT, font);

        componentGbc.gridy = 0;
        componentGbc.gridx = 0;

        searchPanel.add(findPrevious, componentGbc);

        componentGbc.gridy = 0;
        componentGbc.gridx = 1;

        searchPanel.add(findPhrase, componentGbc);

        componentGbc.gridy = 0;
        componentGbc.gridx = 2;

        searchPanel.add(findNext, componentGbc);

        return searchPanel;
    }

    private JPanel createResultPanel(GridBagConstraints componentGbc, Dimension resultPanelDimension,
                                   Dimension buttonDimension, Font font)
    {
        JPanel resultPanel = getNewPanel(resultPanelDimension, null);

        searchResultTextField = getNewTextField(buttonDimension, font, SwingConstants.CENTER);
        searchResultTextField.setEditable(false);

        componentGbc.gridy = 0;
        componentGbc.gridx = 0;

        resultPanel.add(searchResultTextField, componentGbc);

        return resultPanel;
    }

    private void addListenersToFrame()
    {
        addActionListenersToButtonPanel();

        addKeyListenersToButtonPanel();

        addNavigationKeyListeners();

        addWindowListener();
    }

    private void addKeyListenersToButtonPanel()
    {
        findPhrase.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    findPhrase();
                }
            }
        });

        findNext.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    findElement(true);
                }
            }
        });

        findPrevious.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    findElement(false);
                }
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> verticalComponents = new ArrayList<>();

        verticalComponents.add(phraseTextField);
        verticalComponents.add(findPhrase);

        List<Component> horizontalComponents = new ArrayList<>();

        horizontalComponents.add(findPrevious);
        horizontalComponents.add(findPhrase);
        horizontalComponents.add(findNext);

        addNavigationKeyListenersToMainElements(verticalComponents, false,
                true);

        addNavigationKeyListenersToMainElements(horizontalComponents, false,
                false);
    }

    private void addActionListenersToButtonPanel()
    {
        findPhrase.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                findPhrase();
            }
        });

        findNext.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                findElement(true);;
            }
        });

        findPrevious.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                findElement(false);
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
                exitDialog();
            }
        });
    }

    private void findPhrase()
    {
        elementIndex = 0;

        if(!phraseTextField.getText().equals(""))
        {
            listOfWordsFrame.createNewSearchManager(phraseTextField.getText());

            listOfFoundElementsAndTheirIndexes =
                    listOfWordsFrame.getSearchManager().returnListOfFoundElementsAndTheirIndexes();

            if(!listOfFoundElementsAndTheirIndexes.isEmpty())
            {
                focusOnFoundElement();
            }
            else
            {
                searchResultTextField.setText(Constants.NO_RECORDS_FOUND);
            }
        }
    }

    private void findElement(Boolean findNextElement)
    {
        if(listOfFoundElementsAndTheirIndexes != null)
        {
            if(listOfFoundElementsAndTheirIndexes.size() != 0)
            {
                List<String> elementList = (List<String>) listOfFoundElementsAndTheirIndexes.get(0);

                if(elementList.size() != 0)
                {
                    if(findNextElement)
                    {
                        if(elementIndex == (elementList.size() - 1))
                        {
                            elementIndex = 0;
                        }
                        else
                        {
                            elementIndex = elementIndex + 1;
                        }
                    }
                    else
                    {
                        if(elementIndex == 0)
                        {
                            elementIndex = (elementList.size() - 1);
                        }
                        else
                        {
                            elementIndex = elementIndex - 1;
                        }
                    }

                    focusOnFoundElement();
                }
            }
        }
    }

    private void focusOnFoundElement()
    {
        List<Pair> indexList = (List<Pair>) listOfFoundElementsAndTheirIndexes.get(1);

        int wordIndex = indexList.get(elementIndex).getSecondIndex();
        int listIndex = indexList.get(elementIndex).getFirstIndex();

        listOfWordsFrame.getJsonFilesManager().setCurrentListIndex(listIndex);
        listOfWordsFrame.changeFocusOnElements(listIndex, wordIndex);

        searchResultTextField.setText(elementIndex + 1 + Constants.OUT_OF + indexList.size());
    }

    public void exitDialog()
    {
        this.dispose();
    }
}
