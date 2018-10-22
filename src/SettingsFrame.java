package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SettingsFrame extends MultiComponentComplexFrame
{
    private SettingsFrame settingsFrame = this;

    private JComboBox languageJComboBox;
    private JButton saveSettingsButton;

    private SettingsManager settingsManager = new SettingsManager();
    private WindowsManager windowsManager;

    public SettingsFrame(WindowsManager windowsManager)
    {
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

        int mainPanelWidth  = (int) (buttonWidth * 1.75);
        int mainPanelHeight = (int) (size * 0.832);

        int fontSize     = (int) (size * Constants.fontMultiplier);
        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension buttonDimension = new Dimension(buttonWidth,buttonHeight);

        Dimension mainPanelDimension        = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension informationPanelDimension = new Dimension(mainPanelWidth, buttonHeight * 2);
        Dimension minorPanelDimension       = new Dimension(mainPanelWidth, buttonHeight);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);
        JPanel informationPanel = createInformationPanel(gbc, informationPanelDimension, font);
        JPanel comboBoxPanel = createComboBoxPanelPanel(gbc, minorPanelDimension, buttonDimension, font);
        JPanel buttonPanel = createButtonPanel(gbc, minorPanelDimension, buttonDimension, font);

        gbc.gridy = 0;
        gbc.gridx = 0;
        mainPanel.add(informationPanel, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(comboBoxPanel, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        pack();

    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - ((int) (size * Constants.buttonWidthMultiplier * 0.875))),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 3)));
        this.setResizable(false);

        addListenersToFrame();
    }

    private JPanel createInformationPanel(GridBagConstraints gbc, Dimension informationPanelDimension, Font font)
    {
        JPanel informationPanel = getNewPanel(informationPanelDimension, null);

        JLabel chooseLanguageToWhichWordIsTranslatedFirstPart = getNewLabel(Constants.FIRST_PART_OF_INFORMATION,
                font);

        JLabel chooseLanguageToWhichWordIsTranslatedSecondPart = getNewLabel(Constants.SECOND_PART_OF_INFORMATION,
                font);

        gbc.gridy = 0;
        gbc.gridx = 0;

        informationPanel.add(chooseLanguageToWhichWordIsTranslatedFirstPart, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;

        informationPanel.add(chooseLanguageToWhichWordIsTranslatedSecondPart, gbc);

        return informationPanel;
    }

    private JPanel createComboBoxPanelPanel(GridBagConstraints gbc, Dimension informationPanelDimension,
                                            Dimension buttonDimension, Font font)
    {
        JPanel comboBoxPanel = getNewPanel(informationPanelDimension, null);

        languageJComboBox = getNewComboBox(buttonDimension);

        DefaultComboBoxModel defaultComboBoxModel =
                new DefaultComboBoxModel(settingsManager.getLanguagesList().toArray());
        languageJComboBox.setModel(defaultComboBoxModel);

        gbc.gridy = 0;
        gbc.gridx = 0;

        comboBoxPanel.add(languageJComboBox, gbc);

        return comboBoxPanel;
    }

    private JPanel createButtonPanel(GridBagConstraints gbc, Dimension buttonPanelDimension,
                                     Dimension buttonDimension, Font font)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        saveSettingsButton = getNewButton(buttonDimension, Constants.SAVE_SETTINGS, font);

        gbc.gridy = 0;
        gbc.gridx = 0;

        buttonPanel.add(saveSettingsButton, gbc);

        return buttonPanel;
    }

    private void addListenersToFrame()
    {
        addWindowListener();

        addActionListenerToButtonPanel();

        addKeyListenerToButtonPanel();

        addNavigationKeyListeners();
    }

    private void addActionListenerToButtonPanel()
    {
        saveSettingsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeSettings();
            }
        });
    }

    private void addKeyListenerToButtonPanel()
    {
        saveSettingsButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    changeSettings();
                }
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(languageJComboBox);
        components.add(saveSettingsButton);

        addNavigationKeyListenersToMainComponents(components, true, true);

        addNavigationKeyListenersToComboBox(languageJComboBox);
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

    private void changeSettings()
    {
        String language = settingsManager.getLanguagesList().get(languageJComboBox.getSelectedIndex());
        settingsManager.setLanguageToWhichWordIsTranslated(language);

        settingsFrame.dispose();

        new InformationDialog(Constants.INFORMATION, Constants.SETTINGS_SAVED_SUCCESSFULLY,
                Constants.CLICK_OK_TO_CONTINUE, settingsFrame);
    }

    public void exitFrame()
    {
        settingsFrame.dispose();

        if(windowsManager.getIfWordIsBeingEnteredFromMenu())
        {
            new MainMenuFrame(new JsonFilesManager(null));
        }
        else
        {

            new ListOfWordsFrame(new JsonFilesManager(null), windowsManager);
        }
    }

    public WindowsManager getWindowsManager()
    {
        return windowsManager;
    }
}
