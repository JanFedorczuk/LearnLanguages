package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProgressDialog extends ComplexDialog
{
    private JPanel mainPanel = new JPanel();
    private JButton cancelButton = new JButton();
    private JProgressBar progressBar;
    private JLabel progressLabel;

    private SoundFilesManager soundFilesManager;
    private WindowsManager windowsManager;

    public ProgressDialog(JsonFilesManager jsonFilesManager, SoundFilesManager soundFilesManager,
                          WindowsManager windowsManager)
    {
        this.soundFilesManager = soundFilesManager;
        this.windowsManager = windowsManager;

        createUI();
        setUIOptions();
    }

    private void createUI()
    {
        this.setLayout(new GridBagLayout());

        size = getSizeForVisualPurposes();

        buttonWidth      = (int) (size * Constants.buttonWidthMultiplier);
        int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);

        int mainPanelWidth  = (int) (buttonWidth * 2);

        int mainPanelHeight = (int) (size * 0.872);

        int fontSize = (int) (size * Constants.fontMultiplier);
        Font font = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

        Dimension buttonDimension = new Dimension(buttonWidth,buttonHeight);

        Dimension mainPanelDimension        = new Dimension(mainPanelWidth, mainPanelHeight);
        Dimension informationPanelDimension = new Dimension(mainPanelWidth, (int)(size * 0.456));
        Dimension buttonPanelDimension      = new Dimension(mainPanelWidth, buttonHeight);

        GridBagConstraints componentGbc = getGridBagConstraints(GridBagConstraints.NORTH);

        mainPanel = getNewPanel(mainPanelDimension, null);
        JPanel informationPanel = createInformationPanel(componentGbc, informationPanelDimension, buttonDimension, font);
        JPanel buttonPanel = createButtonPanel(componentGbc, buttonPanelDimension, buttonDimension, font);

        GridBagConstraints panelGbc = getGridBagConstraints(GridBagConstraints.CENTER);

        panelGbc.gridy = 0;
        mainPanel.add(informationPanel, panelGbc);

        panelGbc.gridy = 1;
        mainPanel.add(buttonPanel, panelGbc);

        add(mainPanel);

        pack();
    }

    private void setUIOptions()
    {
        this.setTitle(Constants.INFORMATION);

        this.setLocation(((Constants.DIMENSION.width / 2) - ((int) (size * Constants.buttonWidthMultiplier))),
                (Constants.DIMENSION.height / 2) - ((int)(Constants.DIMENSION.height / 4)));

        this.setResizable(false);

        addListeners();
    }

    private JPanel createInformationPanel(GridBagConstraints gbc, Dimension textPanelDimension,
                                          Dimension buttonDimension, Font font)
    {
        JPanel textPanel = getNewPanel(textPanelDimension, null);

        JLabel informationLabel = getNewLabel(Constants.SAVING_FILES_PLEASE_WAIT, font);

        progressBar = getNewProgressBar(buttonDimension, font);

        progressLabel = getNewLabel("", font);

        gbc.gridy = 0;

        textPanel.add(informationLabel, gbc);

        gbc.gridy = 1;

        textPanel.add(progressBar, gbc);

        gbc.gridy = 2;
        gbc.weighty = 0;
        textPanel.add(progressLabel, gbc);

        return textPanel;
    }

    private JPanel createButtonPanel(GridBagConstraints gbc, Dimension buttonPanelDimension, Dimension buttonDimension,
                                     Font font)
    {
        JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

        cancelButton = getNewButton(buttonDimension, Constants.CANCEL, font);

        gbc.gridy = 0;
        gbc.gridx = 0;

        buttonPanel.add(cancelButton, gbc);

        return buttonPanel;
    }

    private void addListeners()
    {
        addActionListener();

        addKeyListener();

        addWindowListener();
    }

    private void addKeyListener()
    {
        cancelButton.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    soundFilesManager.interruptThread();
                }
            }
        });
    }

    private void addActionListener()
    {
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                soundFilesManager.interruptThread();
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
        soundFilesManager.interruptThread();
    }

    public void setProgressBarMaximum(int maximum)
    {

        progressBar.setMaximum(maximum);
    }

    public void setProgressBarValue(int value)
    {
        progressBar.setValue(value);

    }

    public void setProgressLabelText(String text)
    {
        progressLabel.setText(text);

    }

    public void startNewThread(JFrame frame)
    {
        soundFilesManager.startANewThread(this, frame);

        this.setModalityType(ModalityType.TOOLKIT_MODAL);

        displayUI();
    }
}
