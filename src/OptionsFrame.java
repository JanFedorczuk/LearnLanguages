package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class OptionsFrame extends ComplexFrame
{
    private OptionsFrame optionsFrame = this;

    private int size = Constants.SIZE;

    private List<List<String>> wordAndItsContent;

    private JButton menuButton;
    private JButton cancelJButton;
    private JButton wordJButton;

    private JsonFilesManager jsonFilesManager;
    private WindowsManager windowsManager;

    public OptionsFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager,
                        List<List<String>> wordAndItsContent)
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

        GridBagConstraints gbc = new GridBagConstraints();

        size = getSizeForVisualPurposes();

        int buttonWidth  = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 4);
        int mainPanelHeight = (int) (size * 0.424);

        int fontSize     = (int) (size * Constants.FONT_SIZE_MULTIPLIER);

        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension mainPanelDimension = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension minorPanelDimension = new Dimension(mainPanelWidth, buttonHeight);

        Dimension buttonDimension = new Dimension(buttonWidth, buttonHeight);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);
        JPanel textPanel = createTextPanel(gbc, minorPanelDimension, font);
        JPanel buttonPanel = createButtonPanel(gbc, minorPanelDimension, buttonDimension, font);

        gbc.gridy = 0;
        gbc.gridx = 0;
        mainPanel.add(textPanel, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(buttonPanel, gbc);

        this.add(mainPanel);

        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation((Constants.DIMENSION.width / 2) - (int)(size *  Constants.buttonWidthMultiplier * 2),
                (Constants.DIMENSION.height / 2) - (int)(Constants.DIMENSION.height / 4));
        this.setResizable(false);

        addListenersToFrame();
    }

    private JPanel createTextPanel(GridBagConstraints gbc, Dimension minorPanelDimension, Font font)
    {
        JPanel textPanel = getNewPanel(minorPanelDimension, null);

        gbc.gridy = 0;

        JLabel textLabel = getNewLabel(Constants.CHOOSE_WHERE_TO_GO, font);

        textPanel.add(textLabel, gbc);

        return textPanel;
    }

    private JPanel createButtonPanel(GridBagConstraints gbc, Dimension minorPanelDimension, Dimension buttonDimension,
                                     Font font)
    {
        JPanel buttonPanel = getNewPanel(minorPanelDimension, null);

        String frameButtonName;
        if(windowsManager.getIfWordIsBeingEnteredFromMenu())
        {
            frameButtonName = Constants.MENU;
        }
        else
        {
            frameButtonName = Constants.LIST;
        }

        menuButton = getNewButton(   buttonDimension, frameButtonName , font);
        cancelJButton = getNewButton(buttonDimension, Constants.CANCEL, font);
        wordJButton = getNewButton(  buttonDimension, Constants.WORD  , font);

        gbc.gridx = 0;
        buttonPanel.add(menuButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(cancelJButton, gbc);

        gbc.gridx = 2;
        buttonPanel.add(wordJButton, gbc);

        return buttonPanel;
    }

    private void addListenersToFrame()
    {
        addActionListeners();
        addKeyListeners();

        addNavigationListeners();

        addWindowListener();
    }

    private void addActionListeners()
    {
        menuButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayMainMenuFrame();
            }
        });

        cancelJButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayAddOrChooseListOfWordsFrame();
            }
        });

        wordJButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                displayWordFrame();
            }
        });
    }

    private void addKeyListeners()
    {
        menuButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    displayMainMenuFrame();
                }
            }
        });

        cancelJButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    displayAddOrChooseListOfWordsFrame();
                }
            }
        });

        wordJButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    displayWordFrame();
                }
            }
        });
    }

    private void addNavigationListeners()
    {
        List<Component> buttons = new ArrayList<>();

        buttons.add(menuButton);
        buttons.add(cancelJButton);
        buttons.add(wordJButton);

        addNavigationKeyListenersToMainComponents(buttons, true, false);
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

    public void exitFrame()
    {
        optionsFrame.dispose();
        new AddOrChooseListOfWordsFrame(jsonFilesManager, windowsManager, wordAndItsContent);
    }

    private void displayMainMenuFrame()
    {
        optionsFrame.dispose();
        if(windowsManager.getIfWordIsBeingEnteredFromMenu())
        {
            new MainMenuFrame(jsonFilesManager);
        }
        else
        {
            new ListOfWordsFrame(jsonFilesManager, windowsManager);
        }
    }

    private void displayAddOrChooseListOfWordsFrame()
    {
        optionsFrame.dispose();
        new AddOrChooseListOfWordsFrame(jsonFilesManager, windowsManager, wordAndItsContent);
    }

    private void displayWordFrame()
    {
        optionsFrame.dispose();
        new WordFrame(jsonFilesManager, windowsManager, wordAndItsContent);
    }
}
