package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InformationDialog extends ComplexDialog
{
    private InformationDialog informationDialog = this;

    private String title;
    private String firstPartOfMessage;
    private String secondPartOfMessage;
    private String thirdPartOfMessage;

    private JButton okButton = new JButton();

    private JFrame frame;

    public InformationDialog(String title, String firstPartOfMessage, String secondPartOfMessage,
                             String thirdPartOfMessage, JFrame frame)
    {
        this.title = title;
        this.firstPartOfMessage = firstPartOfMessage;
        this.secondPartOfMessage = secondPartOfMessage;
        this.thirdPartOfMessage = thirdPartOfMessage;
        this.frame = frame;

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

        int mainPanelWidth  = (int) (buttonWidth * 2.75);

        int mainPanelHeight;
        Dimension textPanelDimension;

        if(thirdPartOfMessage != null)
        {
            mainPanelHeight = (int) (size * 0.632);
            textPanelDimension = new Dimension(mainPanelWidth, buttonHeight * 3);
        }
        else
        {
            mainPanelHeight = (int) (size * 0.528);
            textPanelDimension = new Dimension(mainPanelWidth, buttonHeight * 2);

        }

        int fontSize = (int) (size * Constants.fontMultiplier);
        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension buttonDimension = new Dimension(buttonWidth,buttonHeight);

        Dimension mainPanelDimension = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension buttonPanelDimension = new Dimension(mainPanelWidth, buttonHeight);

        GridBagConstraints gbc = getGridBagConstraints(GridBagConstraints.CENTER);

        JPanel mainPanel = getNewPanel(mainPanelDimension, null);
        JPanel textPanel = createTextPanel(gbc, textPanelDimension, font);
        JPanel buttonPanel = createButtonPanel(gbc, buttonPanelDimension, buttonDimension, font);

        gbc.gridy = 0;
        mainPanel.add(textPanel, gbc);

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);

        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(title);

        this.setLocation(((Constants.DIMENSION.width / 2) - ((int) (size * Constants.buttonWidthMultiplier * 1.375))),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 4)));

        this.setResizable(false);

        addListeners();

        this.setModalityType(ModalityType.APPLICATION_MODAL);
    }

    private JPanel createTextPanel(GridBagConstraints gbc, Dimension textPanelDimension, Font font)
    {
        JPanel textPanel = getNewPanel(textPanelDimension, null);

        JLabel firstPartOfInformation = getNewLabel(firstPartOfMessage, font);

        JLabel secondPartOfInformation = getNewLabel(secondPartOfMessage, font);

        gbc.gridy = 0;

        textPanel.add(firstPartOfInformation, gbc);

        gbc.gridy = 1;

        textPanel.add(secondPartOfInformation, gbc);

        if(thirdPartOfMessage != null)
        {
            JLabel thirdPartOfInformation = getNewLabel(thirdPartOfMessage, font);

            gbc.gridy = 2;

            textPanel.add(thirdPartOfInformation, gbc);
        }

        return textPanel;
    }

    private JPanel createButtonPanel(GridBagConstraints gbc, Dimension buttonPanelDimension, Dimension buttonDimension,
                                     Font font)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        okButton = getNewButton(buttonDimension, Constants.OK, font);

        gbc.gridy = 0;
        gbc.gridx = 0;

        buttonPanel.add(okButton, gbc);

        return buttonPanel;
    }

    private void addListeners()
    {
        addActionListener();

        addKeyListener();

        addWindowListener();
    }

    private void addActionListener()
    {
        okButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                exitDialog();
            }
        });
    }

    private void addKeyListener()
    {
        okButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    exitDialog();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    exitDialog();
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
                exitDialog();
            }
        });

    }

    public void exitDialog()
    {
        informationDialog.dispose();

        closeCurrentFrameAndOpenNewOne();
    }

    private void closeCurrentFrameAndOpenNewOne()
    {
        if(frame instanceof AddSoundFilesFrame)
        {
            AddSoundFilesFrame addSoundFilesFrame = (AddSoundFilesFrame) frame;
            addSoundFilesFrame.exitWordContentFrame();
            addSoundFilesFrame.dispose();

            new ListOfWordsFrame(new JsonFilesManager(null), new WindowsManager
                    (false, false, false));
        }
        else if(frame instanceof  AddOrChooseListOfWordsFrame)
        {
            AddOrChooseListOfWordsFrame addOrChooseListOfWordsFrame = (AddOrChooseListOfWordsFrame) frame;
            frame.dispose();

            if(addOrChooseListOfWordsFrame.getWindowsManager().getIfWordIsBeingEnteredFromMenu())
            {
                new MainMenuFrame(new JsonFilesManager(null));
            }
            else
            {
                new ListOfWordsFrame(new JsonFilesManager(null),
                        addOrChooseListOfWordsFrame.getWindowsManager());
            }
        }
        else if(frame instanceof  AddOrEditWordManuallyFrame)
        {
            AddOrEditWordManuallyFrame addOrEditWordManuallyFrame = (AddOrEditWordManuallyFrame) frame;
            addOrEditWordManuallyFrame.dispose();

            new AddSoundFilesFrame(
                    addOrEditWordManuallyFrame.getJsonFilesManager(),
                    addOrEditWordManuallyFrame.getWindowsManager(),
                    addOrEditWordManuallyFrame.getSoundFilesManager(),
                    addOrEditWordManuallyFrame.getWordAndItsContent());
        }
        else if(frame instanceof  SettingsFrame)
        {
            SettingsFrame settingsFrame = (SettingsFrame) frame;

            frame.dispose();

            if(settingsFrame.getWindowsManager().getIfWordIsBeingEnteredFromMenu())
            {
                new MainMenuFrame(new JsonFilesManager(null));
            }
            else
            {
                new ListOfWordsFrame(new JsonFilesManager(null), settingsFrame.getWindowsManager());
            }
        }
    }
}
