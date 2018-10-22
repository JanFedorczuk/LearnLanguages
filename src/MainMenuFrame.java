package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MainMenuFrame extends MultiComponentComplexFrame
{
    private JButton showLists     ;
    private JButton addWord       ;
    private JButton changeSettings;
    private JButton exitGame      ;

    private JsonFilesManager jsonFilesManager;

    public MainMenuFrame(JsonFilesManager jsonFilesManager)
    {
        this.jsonFilesManager = jsonFilesManager;

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
        int fontSize     = (int) (size * Constants.fontMultiplier);

        int mainMenuWidth  =  (int) (buttonWidth * 1.75);
        int mainMenuHeight =  (int) (size * 1.248);

        Dimension mainPanelDimension = new Dimension(mainMenuWidth, mainMenuHeight);
        Dimension buttonDimension    = new Dimension(buttonWidth,buttonHeight);

        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);

        showLists      = getNewButton(buttonDimension, Constants.SHOW_LISTS, font);
        addWord        = getNewButton(buttonDimension, Constants.ADD_WORD,   font);
        changeSettings = getNewButton(buttonDimension, Constants.SETTINGS,   font);
        exitGame       = getNewButton(buttonDimension, Constants.EXIT ,      font);

        GridBagConstraints gbc = getGridBagConstraints(GridBagConstraints.CENTER);

        gbc.gridy = 0;
        mainPanel.add(showLists, gbc);

        gbc.gridy = 1;
        mainPanel.add(addWord, gbc);

        gbc.gridy = 2;
        mainPanel.add(changeSettings, gbc);
        
        gbc.gridy = 3;
        mainPanel.add(exitGame, gbc);

        this.add(mainPanel);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);
        this.setLocation(((Constants.DIMENSION.width / 2) - (int)(buttonWidth * 1.75 / 2)),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 3.5)));
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addListenersToFrame();
    }

    private void addListenersToFrame()
    {
        addActionListenersToMainPanel();

        addKeyListenersToMainPanel();

        addNavigationKeyListeners();
    }

    private void addKeyListenersToMainPanel()
    {
        showLists.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    showLists();
                }
            }
        });
        addWord.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    addWord();
                }
            }
        });
        changeSettings.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    changeSettings();
                }
            }
        });
        exitGame.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    exitFrame();
                }
            }
        });
    }

    private void addActionListenersToMainPanel()
    {
        showLists.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showLists();
            }
        });

        addWord.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addWord();
            }
        });

        changeSettings.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeSettings();
            }
        });

        exitGame.addActionListener(new ActionListener()
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

        components.add(showLists);
        components.add(addWord);
        components.add(changeSettings);
        components.add(exitGame);

        addNavigationKeyListenersToMainComponents(components, false, true);
    }

    private void showLists()
    {
        this.dispose();
        WindowsManager windowsManager = new WindowsManager(false,
                false, false);
        new ListOfWordsFrame(jsonFilesManager, windowsManager);
    }

    private void addWord()
    {
        this.dispose();
        WindowsManager windowsManager = new WindowsManager(true,
                false, false);
        new AddWordFrame(jsonFilesManager,  windowsManager);
    }

    private void changeSettings()
    {
        this.dispose();
        new SettingsFrame(new WindowsManager(true,
            false, false));
    }

    public void exitFrame()
    {

        System.exit(0);
    }
}
