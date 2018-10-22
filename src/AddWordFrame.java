package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddWordFrame extends ComplexFrame
{
    private AddWordFrame addWordFrame = this;

    private JButton addWordManuallyButton = new JButton(Constants.MANUALLY);
    private JButton addWordAutomaticallyButton = new JButton(Constants.AUTOMATICALLY);

    private JsonFilesManager jsonFilesManager;
    private WindowsManager windowsManager;

    public AddWordFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager)
    {
        this.jsonFilesManager = jsonFilesManager;
        this.windowsManager = windowsManager;

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

        int mainPanelWidth = (int) (buttonWidth * 2.75);
        int mainPanelHeight = (int) (size * 0.56);

        Dimension mainPanelDimension        = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension selectionPanelDimension   = new Dimension(mainPanelWidth, buttonHeight);
        Dimension informationPanelDimension = new Dimension(mainPanelWidth, buttonHeight * 2);

        Dimension buttonDimension = new Dimension(buttonWidth, buttonHeight);

        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        GridBagConstraints gbc = getGridBagConstraints(GridBagConstraints.CENTER);

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);
        JPanel informationPanel = getNewInformationPanel(gbc, informationPanelDimension, font);
        JPanel selectionPanel = getNewSelectionPanel(gbc, selectionPanelDimension, buttonDimension, font);

        gbc.gridy = 0;
        mainPanel.add(informationPanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(selectionPanel, gbc);

        add(mainPanel, gbc);
        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.PROGRAM_NAME);

        this.setLocation(((Constants.DIMENSION.width / 2) - ((int) (buttonWidth * 2.75 / 2))),
                (Constants.DIMENSION.height / 2) - ((int) (Constants.DIMENSION.height / 3.5)));

        this.setResizable(false);

        addListenersToFrame();
    }

    private JPanel getNewInformationPanel(GridBagConstraints gbc, Dimension informationPanelDimension, Font font)
    {
        JPanel informationPanel = getNewPanel(informationPanelDimension, null);

        JLabel addingWordChoiceLabelFirstPart = getNewLabel(Constants.ADD_WORD_FIRST_PART_OF_INFORMATION, font);
        JLabel addingWordChoiceLabelSecondPart = getNewLabel(Constants.ADD_WORD_SECOND_PART_OF_INFORMATION, font);

        gbc.gridy = 0;
        gbc.gridx = 0;
        informationPanel.add(addingWordChoiceLabelFirstPart, gbc);

        gbc.gridy = 1;
        informationPanel.add(addingWordChoiceLabelSecondPart, gbc);

        return informationPanel;
    }

    private JPanel getNewSelectionPanel(GridBagConstraints gbc, Dimension selectionPanelDimension,
                                        Dimension buttonDimension, Font font)
    {
        JPanel selectionPanel = getNewPanel(selectionPanelDimension, null);

        addWordManuallyButton = getNewButton     (buttonDimension, Constants.MANUALLY,      font);
        addWordAutomaticallyButton = getNewButton(buttonDimension, Constants.AUTOMATICALLY, font);

        gbc.gridy = 0;
        gbc.gridx = 0;
        selectionPanel.add(addWordManuallyButton, gbc);

        gbc.gridx = 1;
        selectionPanel.add(addWordAutomaticallyButton, gbc);

        return selectionPanel;
    }


    private void addListenersToFrame()
    {

        addActionListenersToSelectionPanel();
        addKeyListenersToSelectionPanel();

        addNavigationKeyListeners();

        addWindowListener();

    }

    private void addActionListenersToSelectionPanel()
    {
        addWordManuallyButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addWordManually();
            }
        });

        addWordAutomaticallyButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addWordAutomatically();
            }
        });

    }

    private void addKeyListenersToSelectionPanel()
    {
        addWordManuallyButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    addWordManually();
                }
            }
        });

        addWordAutomaticallyButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    addWordAutomatically();
                }
            }
        });
    }

    private void addNavigationKeyListeners()
    {
        List<Component> components = new ArrayList<>();

        components.add(addWordManuallyButton);
        components.add(addWordAutomaticallyButton);

        addNavigationKeyListenersToMainComponents(components, true, false);
    }

    private void addWindowListener()
    {
        if(windowsManager.getIfWordIsBeingEnteredFromMenu())
        {
            this.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    addWordFrame.dispose();
                    new MainMenuFrame(jsonFilesManager).setVisible(true);
                }
            });
        }
        else
        {
            this.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent e)
                {
                    addWordFrame.dispose();
                    new ListOfWordsFrame(jsonFilesManager, windowsManager).setVisible(true);
                }
            });
        }
    }

    private void addWordManually()
    {
        addWordFrame.dispose();
        new AddOrEditWordManuallyFrame(jsonFilesManager, windowsManager, new ArrayList<>());
    }

    private void addWordAutomatically()
    {
        addWordFrame.dispose();
        new AddWordAutomaticallyFrame(jsonFilesManager, windowsManager);
    }

    public void exitFrame()
    {
        addWordFrame.dispose();

        if(windowsManager.getIfWordIsBeingEnteredFromMenu())
        {
            new MainMenuFrame(jsonFilesManager);
        }
        else
        {
            new ListOfWordsFrame(jsonFilesManager, windowsManager);
        }

    }
}


