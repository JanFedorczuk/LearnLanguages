package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddWordAutomaticallyFrame extends ComplexFrame
{
    private AddWordAutomaticallyFrame addWordAutomaticallyFrame = this;

    private JButton previousWindowButton;

    private JButton     translateWordButton   ;
    private JButton     chooseCategoriesButton;

    private JLabel      informationLabel;

    private JList       categoriesList;
    private JTextField  wordToBeTranslatedTextField;
    private JScrollPane scrollPane;

    private String word;

    private List<List<String>> wordAndItsContent;

    private JsonFilesManager jsonFilesManager;
    private WindowsManager windowsManager;
    private SoundFilesManager soundFilesManager;

    private int previousIndex = -1;

    public AddWordAutomaticallyFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager,
                                     SoundFilesManager soundFilesManager)
    {
        this.jsonFilesManager = jsonFilesManager;
        this.windowsManager = windowsManager;
        this.soundFilesManager = soundFilesManager;

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

        int mainPanelWidth  = (buttonWidth * 2);
        int mainPanelHeight = (int) (size * 1.696);

        int fontSize     = (int) (size * Constants.fontMultiplier);

        Font boldFont  = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);
        Font plainFont = new Font(Constants.FONT_NAME, Font.PLAIN, fontSize);

        Dimension mainPanelDimension        = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension arrowPanelDimensions   = new Dimension(mainPanelWidth, buttonHeight);
        Dimension informationPanelDimension = new Dimension(mainPanelWidth, (int)Math.round(size * 0.104));
        Dimension translationPanelDimension = new Dimension(mainPanelWidth, (int)Math.round(size * 0.456));
        Dimension categoryPanelDimension    = new Dimension(mainPanelWidth, (int)Math.round(size * 0.72));

        Dimension scrollPaneDimension       = new Dimension((int)(buttonWidth * 1.5), (int)(buttonHeight * 5));
        Dimension toolBarButtonDimension     = new Dimension((int)(buttonWidth / 5), buttonHeight);
        Dimension chooseCategoriesDimension = new Dimension((int)(buttonWidth * 1.5), buttonHeight);

        Dimension buttonDimension = new Dimension(buttonWidth,buttonHeight);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);
        
        JPanel mainPanel = getNewPanel(mainPanelDimension, null);

        JPanel arrowPanel = getArrowPanel(arrowPanelDimensions, toolBarButtonDimension, plainFont,null);

        JPanel translationPanel = getNewTranslationPanel(componentGbc, translationPanelDimension, buttonDimension,
                boldFont, plainFont);

        JPanel informationPanel = getNewInformationPanel(componentGbc, informationPanelDimension, boldFont);

        JPanel categoryPanel    = getNewCategoryPanel(componentGbc, categoryPanelDimension, scrollPaneDimension,
                chooseCategoriesDimension, boldFont);

        previousWindowButton = getPreviousWindowButton(arrowPanel);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;
        panelGbc.weighty = 0;
        mainPanel.add(arrowPanel, panelGbc);

        panelGbc.gridy = 1;
        panelGbc.weighty = 1;
        mainPanel.add(translationPanel, panelGbc);

        panelGbc.gridy = 2;
        mainPanel.add(informationPanel, panelGbc);

        panelGbc.gridy = 3;
        mainPanel.add(categoryPanel, panelGbc);

        add(mainPanel);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - buttonWidth),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 3)));

        this.setResizable(false);

        this.changeCategoriesListAndButtonStateAndAvailability(false);

        addListenersToFrame();

        wordToBeTranslatedTextField.requestFocus();
    }

    private JPanel getNewTranslationPanel(GridBagConstraints componentGbc, Dimension translationPanelDimension,
                                          Dimension buttonDimension, Font boldFont, Font plainFont)
    {
        JPanel translationPanel = getNewPanel(translationPanelDimension, null);

        JLabel writeDesiredWordLabel = getNewLabel(Constants.WORD, boldFont);

        wordToBeTranslatedTextField = getNewTextField(buttonDimension, plainFont, SwingConstants.CENTER);

        translateWordButton = getNewButton(buttonDimension, Constants.TRANSLATE, boldFont);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;
        translationPanel.add(writeDesiredWordLabel, componentGbc);

        componentGbc.gridy = 1;
        componentGbc.weighty = 1;
        translationPanel.add(wordToBeTranslatedTextField, componentGbc);

        componentGbc.gridy = 2;
        componentGbc.weighty = 0;
        translationPanel.add(translateWordButton, componentGbc);

        return translationPanel;
    }

    private JPanel getNewInformationPanel(GridBagConstraints componentGbc, Dimension informationPanelDimension,
                                          Font boldFont)
    {
        JPanel informationPanel = getNewPanel(informationPanelDimension, null);

        informationLabel = getNewLabel("", boldFont);

        componentGbc.gridy = 0;
        informationPanel.add(informationLabel, componentGbc);

        return informationPanel;
    }

    private JPanel getNewCategoryPanel(GridBagConstraints componentGbc, Dimension categoryPanelDimension,
                                          Dimension scrollPaneDimension, Dimension buttonDimension, Font boldFont)
    {
        JPanel categoryPanel = getNewPanel(categoryPanelDimension, null);

        categoriesList = getNewList(boldFont, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.VERTICAL);

        scrollPane = getNewScrollPane(scrollPaneDimension, categoriesList);

        chooseCategoriesButton = getNewButton(buttonDimension, Constants.CHOOSE_CATEGORY, boldFont);

        componentGbc.gridy = 0;
        componentGbc.weighty = 1;
        categoryPanel.add(scrollPane, componentGbc);

        componentGbc.gridy = 1;
        componentGbc.weighty = 0;
        categoryPanel.add(chooseCategoriesButton, componentGbc);

        return categoryPanel;
    }

    private void addListenersToFrame()
    {
        addActionListeners();

        addKeyListeners();

        addNavigationKeyListeners();

        addWindowListener();

        addMouseListener();
    }

    private void addActionListeners()
    {
        addActionListenersToArrowPanel();
        addActionListenerToTranslationPanel();
        addActionListenerToCategoryPanel();
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

    private void addActionListenerToTranslationPanel()
    {
        translateWordButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        informationLabel.setText(Constants.TRANSLATING);
                        translateWord();
                    }
                }).start();
            }
        });
    }

    private void addActionListenerToCategoryPanel()
    {
        chooseCategoriesButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                chooseCategories();
            }
        });
    }

    private void addKeyListeners()
    {
        addKeyListenerToArrowPanel();
        addKeyListenerToTranslationPanel();
        addKeyListenersToCategoryPanel();
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

    private void addKeyListenerToTranslationPanel()
    {
        translateWordButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    translateWord();
                }
            }
        });
    }

    private void addKeyListenersToCategoryPanel()
    {
        scrollPane.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                categoriesList.requestFocus();
            }
        });

        scrollPane.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_UP)
                {
                    int[] indices = categoriesList.getSelectedIndices();

                    if(categoriesList.getSelectedIndex() == 0 && previousIndex == 0 && indices.length == 1)
                    {
                        translateWordButton.requestFocus();
                    }
                    else if(categoriesList.getSelectedIndex() == 0)
                    {
                        previousIndex = 0;
                    }
                    else
                    {
                        previousIndex = -1;
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    int[] indices = categoriesList.getSelectedIndices();

                    if(categoriesList.getSelectedIndex() == categoriesList.getModel().getSize() - 1
                            && previousIndex == categoriesList.getModel().getSize() - 1 && indices.length == 1)
                    {
                        chooseCategoriesButton.requestFocus();
                    }
                    else if(categoriesList.getSelectedIndex() == categoriesList.getModel().getSize() - 1)
                    {
                        previousIndex = categoriesList.getModel().getSize() - 1;
                    }
                    else
                    {
                        previousIndex = -1;
                    }
                }
            }
        });

        categoriesList.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_UP)
                {
                    int[] indices = categoriesList.getSelectedIndices();

                    if(categoriesList.getSelectedIndex() == 0 && previousIndex == 0 && indices.length == 1)
                    {
                        translateWordButton.requestFocus();
                    }
                    else if(categoriesList.getSelectedIndex() == 0)
                    {
                        previousIndex = 0;
                        categoriesList.requestFocus();
                    }
                    else
                    {
                        previousIndex = -1;
                    }
                }

                if(e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    int[] indices = categoriesList.getSelectedIndices();

                    if(categoriesList.getSelectedIndex() == categoriesList.getModel().getSize() - 1
                            && previousIndex == categoriesList.getModel().getSize() - 1 && indices.length == 1)
                    {
                        chooseCategoriesButton.requestFocus();
                    }
                    else if(categoriesList.getSelectedIndex() == categoriesList.getModel().getSize() - 1)
                    {
                        previousIndex = categoriesList.getModel().getSize() - 1;
                        categoriesList.requestFocus();
                    }
                    else
                    {
                        previousIndex = -1;
                    }
                }
            }
        });

        chooseCategoriesButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    chooseCategories();
                }
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(wordToBeTranslatedTextField);
        components.add(translateWordButton);
        components.add(scrollPane);
        components.add(chooseCategoriesButton);

        addNavigationKeyListenersToMainComponents(components, false, true);
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

    private void addMouseListener()
    {
        categoriesList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if(categoriesList.getSelectedIndex() == 0)
                {
                    previousIndex = 0;
                }
                else if(categoriesList.getSelectedIndex() == categoriesList.getModel().getSize() - 1)
                {
                    previousIndex = categoriesList.getModel().getSize() - 1;
                }
            }
        });
    }

    private void translateWord()
    {

        word = wordToBeTranslatedTextField.getText();

        SettingsManager settingsManager = new SettingsManager();

        Translator translator = new Translator(word,
                settingsManager.getLanguageToWhichWordIsTranslated());

        if(translator.checkIfWordExists())
        {
            wordAndItsContent = translator.translateWordAndReturnWordAndItsContent();
            categoriesList.setModel(createNewModel(wordAndItsContent));

        }
        List<Object> list = translator.getTranslatorRunnable().getList();
        if(list.get(1) != null)
        {
            setInformationLabel((Boolean) list.get(0), (Exception) list.get(1));
        }
        else
        {
            setInformationLabel((Boolean) list.get(0), null);
        }
    }

    private void chooseCategories()
    {
        if(!categoriesList.getSelectedValuesList().isEmpty())
        {
            List<List<String>> newWordAndItsContent = new ArrayList<>();
            newWordAndItsContent.add(wordAndItsContent.get(0));
            for (Object categoryObject: categoriesList.getSelectedValuesList())
            {
                String category = (String) categoryObject;
                category = category + Constants.COLON;
                for(List<String> list: wordAndItsContent)
                {
                    if(list.get(0).equals(category))
                    {
                        newWordAndItsContent.add(list);
                    }
                }
            }
            addWordAutomaticallyFrame.dispose();
            new WordFrame(jsonFilesManager, windowsManager, soundFilesManager, newWordAndItsContent);
        }
        else
        {
            new InformationDialog(Constants.ERROR, Constants.NO_CATEGORY_CHOSEN,
                    Constants.PLEASE_CHOSE_AT_LEAST_ONE_CATEGORY, null,null);
        }
    }

    public void exitFrame()
    {
        addWordAutomaticallyFrame.dispose();
        new AddWordFrame(jsonFilesManager, windowsManager);
    }

    private void changeCategoriesListAndButtonStateAndAvailability(Boolean booleanValue)
    {
        if(!booleanValue)
        {
            DefaultListModel model = new DefaultListModel();
            categoriesList.setModel(model);
        }

        scrollPane.setEnabled(booleanValue);
        categoriesList.setEnabled(booleanValue);
        chooseCategoriesButton.setEnabled(booleanValue);

    }

    private void setInformationLabelText(String text, Boolean success)
    {
        String color;

        if(success)
        {
            color = Constants.GREEN;
        }
        else
        {
            color = Constants.RED;
        }

        informationLabel.setText(Constants.FONT_TAG_FIRST_PART + color + Constants.FONT_TAG_SECOND_PART + text +
                Constants.FONT_TAG_THIRD_PART);

    }

    private void setInformationLabel(Boolean value, Exception exception)
    {
        if(value)
        {
            setInformationLabelText(Constants.WORD_HAS_BEEN_SUCCESSFULLY_TRANSLATED, true);
            changeCategoriesListAndButtonStateAndAvailability(true);
        }
        else if(!value && (exception.getClass().getCanonicalName().equals
                (Constants.JAVA_LANG_ARRAY_INDEX_OUT_OF_BOUND_EXCEPTION)) ||
                (exception.getClass().getCanonicalName().equals(Constants.JAVA_IO_FILE_NOT_FOUND_EXCEPTION)))
        {
            setInformationLabelText(Constants.THE_WORD_DOES_NOT_EXIST, false);
            changeCategoriesListAndButtonStateAndAvailability(false);
        }
        else if(!value)
        {
            setInformationLabelText(Constants.NO_INTERNET_CONNECTION, false);
            changeCategoriesListAndButtonStateAndAvailability(false);
        }
    }

    private DefaultListModel createNewModel(List<List<String>> wordAndItsContent)
    {
        DefaultListModel model = new DefaultListModel();

        List<List<String>> copyOfWordAndItsContent = new ArrayList<>(wordAndItsContent);
        for (int counter = 1; counter < copyOfWordAndItsContent.size(); counter++)
        {
            model.addElement(copyOfWordAndItsContent.get(counter).get(0).replaceAll
                    (Constants.COLON, ""));
        }

        return model;
    }
}
