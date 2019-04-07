package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class WordFrame extends ComplexFrame
{
    private WordFrame wordFrame = this;

    private JPanel mainPanel;

    private JButton previousWindowButton;

    private JButton getPreviousWordButton;
    private JButton getNextWordButton    ;
    private JButton editWordContentButton;
    private JButton nextWindowButton;

    private JToolBar playerToolbar;
    private JLabel fileName;
    private JSlider timeSlider;

    private JButton previousSoundFileButton;
    private JButton playOrPauseSoundButton;
    private JButton stopSoundButton;
    private JButton nextSoundFileButton;

    private boolean isActionListenerAdded = false;

    private JsonFilesManager jsonFilesManager;
    private WindowsManager windowsManager;
    private SoundFilesManager soundFilesManager;
    private List<List<String>> wordAndItsContent;

    private Thread actionThread;

    private SoundPlayer soundPlayer;

    private boolean stateValue;

    public WordFrame(JsonFilesManager jsonFilesManager, WindowsManager windowsManager,
                     SoundFilesManager soundFilesManager, List<List<String>> wordAndItsContent)
    {
        this.jsonFilesManager = jsonFilesManager;
        this.windowsManager = windowsManager;
        this.soundFilesManager = soundFilesManager;
        this.wordAndItsContent = wordAndItsContent;

        System.out.println(wordAndItsContent);

        createUI();
        setUIOptions();
        displayUI();
    }

    private void createUI()
    {
        try
        {
            this.setLayout(new GridBagLayout());

            int size = getSizeForVisualPurposes();

            buttonWidth      = (int) (size * Constants.buttonWidthMultiplier);
            int buttonHeight = (int) (size * Constants.buttonHeightMultiplier);
            int fontSize     = (int) (size * Constants.fontMultiplier);

            int mainPanelWidth  = (int) (buttonWidth * 4.632);

            int mainPanelHeight;
            int scrollWordPanelSide;
            int buttonPanelHeight;

            if(windowsManager.getIfWordIsBeingBrowsed())
            {
                mainPanelHeight = (int) (size * 2.92);
                scrollWordPanelSide = (int)Math.round(mainPanelHeight * 0.7936508);
                buttonPanelHeight = (int)(mainPanelHeight * 0.142465754325);
            }
            else
            {
                mainPanelHeight = (int) (size * 2.816);
                scrollWordPanelSide = (int)Math.round(mainPanelHeight * 0.8522727);
                buttonPanelHeight = buttonHeight;
            }

            Dimension mainPanelDimension       = new Dimension(mainPanelWidth, mainPanelHeight);
            Dimension arrowPanelDimensions           = new Dimension(mainPanelWidth, buttonHeight);
            Dimension scrollWordPanelDimension = new Dimension(scrollWordPanelSide, scrollWordPanelSide);
            Dimension buttonPanelDimension     = new Dimension(mainPanelWidth, buttonPanelHeight);

            Dimension buttonDimension          = new Dimension(buttonWidth, buttonHeight);
            Dimension toolBarButtonDimension = new Dimension((buttonWidth / 5), buttonHeight);
            Dimension playerToolbarButtonDimension = new Dimension((buttonWidth / 4), buttonHeight);

            Font buttonFont = new Font(Constants.FONT_NAME, Font.BOLD, fontSize);

            GridBagConstraints gbc = getGridBagConstraints(GridBagConstraints.CENTER);

            mainPanel                  = getNewPanel(mainPanelDimension, null);
            JPanel arrowPanel = getArrowPanel(arrowPanelDimensions, toolBarButtonDimension, buttonFont,null);
            JScrollPane scrollWordPane = getNewScrollPane(fontSize, gbc, scrollWordPanelDimension);
            JPanel buttonPanel         = createButtonPanel(gbc, buttonPanelDimension, buttonFont, buttonDimension,
                    playerToolbarButtonDimension);

            previousWindowButton = getPreviousWindowButton(arrowPanel);

            gbc.gridy = 0;
            gbc.weighty = 0;
            mainPanel.add(arrowPanel, gbc);

            gbc.gridy = 1;
            gbc.weighty = 1;
            mainPanel.add(scrollWordPane, gbc);

            gbc.gridy = 2;
            mainPanel.add(buttonPanel, gbc);

            add(mainPanel, gbc);
            pack();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

    }

    private void setUIOptions()
    {
        try
        {
            this.setTitle(Constants.PROGRAM_NAME);

            this.setLocation(((Constants.DIMENSION.width / 2) - (int)(buttonWidth * 2.25)),
                    ((Constants.DIMENSION.height / 2) - (int)(Constants.DIMENSION.height / 2.15)));

            this.setResizable(false);

            addListenersToFrame();

            setInitialStateOfWordFrame();

            displayUI();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private JPanel createWordPanel(int fontSize, GridBagConstraints gbc)
    {
        try
        {
            JPanel wordPanel = getNewPanel(null, Color.WHITE);

            JLabel wordNameLabel = getNewLabel(wordAndItsContent.get(0).get(0),
                    new Font(Constants.FONT_NAME, Font.BOLD, (int)(fontSize * 2.5)));

            wordPanel.add(wordNameLabel);

            List<List<String>> newWordAndItsContent = returnWordAndItsContentForVisualPurposes(wordAndItsContent);

            int i = 1;
            for(List<String> list: newWordAndItsContent)
            {
                gbc.gridy = i;
                gbc.gridx = 0;
                JLabel categorySeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                        new Font(Constants.FONT_NAME, Font.BOLD, (int)(fontSize * 1.5)));

                wordPanel.add(categorySeparationLabel, gbc);
                i++;

                gbc.gridy = i;

                JLabel categoryNameLabel = getNewLabel(list.get(0),
                        new Font(Constants.FONT_NAME, Font.BOLD, (int)(fontSize * 1.5)));

                wordPanel.add(categoryNameLabel, gbc);
                i++;

                JLabel categoryFromElementSeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                        new Font(Constants.FONT_NAME, Font.BOLD, fontSize / 2));

                gbc.gridy = i;
                wordPanel.add(categoryFromElementSeparationLabel, gbc);
                i++;

                for(int counter = 1; counter < list.size(); counter++)
                {
                    gbc.gridy = i;

                    JLabel elementLabel = getNewLabel(list.get(counter),
                            (new Font(Constants.FONT_NAME, Font.BOLD, fontSize)));

                    wordPanel.add(elementLabel, gbc);
                    i++;

                    gbc.gridy = i;

                    JLabel elementSeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                            new Font(Constants.FONT_NAME, Font.BOLD, fontSize / 2));

                    wordPanel.add(elementSeparationLabel, gbc);
                    i++;
                }
            }

            gbc.gridy = i;

            JLabel wordSeparationLabel = getNewLabel(Constants.EMPTY_SPACE,
                    new Font(Constants.FONT_NAME, Font.BOLD, fontSize));

            wordPanel.add(wordSeparationLabel, gbc);

            return wordPanel;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    private JPanel createButtonPanel(GridBagConstraints gbc, Dimension buttonPanelDimension, Font buttonFont,
                                     Dimension buttonDimension, Dimension playerToolbarButtonDimension)
    {
        try
        {
            List<String> filesNames = new ArrayList<>();

            if(windowsManager.getIfWordIsBeingBrowsed())
            {
                try
                {
                    if(jsonFilesManager.getFilesBulkCreationValue(jsonFilesManager.getCurrentListName() + "/" +
                            jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex()) + "/"))
                    {
                        filesNames.add(wordAndItsContent.get(0).get(0));
                    }
                    else
                    {
                        filesNames = jsonFilesManager.getFilesNamesJsonArray(jsonFilesManager.getCurrentListName() + "/" +
                                jsonFilesManager.getListOfWords().get(jsonFilesManager.getCurrentWordIndex()) + "/");
                    }

                    if(!filesNames.isEmpty())
                    {
                        soundPlayer = new SoundPlayer(filesNames,this,
                                jsonFilesManager.getCurrentListName() + "/" + jsonFilesManager.getListOfWords().get
                                        (jsonFilesManager.getCurrentWordIndex()) + "/" + Constants.SOUND_FILES + "/");
                    }
                    else
                    {
                        soundPlayer = null;
                    }
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                    filesNames = null;
                    soundPlayer = null;
                }
            }

            JPanel buttonPanel = getNewPanel(buttonPanelDimension, null);

            if(windowsManager.getIfWordIsBeingBrowsed())
            {
                gbc.gridy = 0;
                gbc.gridx = 1;
                playerToolbar = getNewToolbar(buttonDimension, false, true);
                addButtonsToPlayerToolbar(playerToolbarButtonDimension, buttonFont);
                buttonPanel.add(playerToolbar, gbc);

                gbc.gridy = 1;
                gbc.gridx = 0;
                getPreviousWordButton = getNewButton(buttonDimension, Constants.PREVIOUS, buttonFont);
                buttonPanel.add(getPreviousWordButton, gbc);

                gbc.gridy = 1;
                gbc.gridx = 1;

                if(filesNames != null && !filesNames.isEmpty())
                {
                    fileName = getNewLabel(filesNames.get(0), buttonFont);
                }
                else
                {
                    fileName = getNewLabel("", buttonFont);
                }

                buttonPanel.add(fileName, gbc);

                gbc.gridy = 1;
                gbc.gridx = 2;
                getNextWordButton = getNewButton(buttonDimension, Constants.NEXT, buttonFont);
                buttonPanel.add(getNextWordButton, gbc);

                gbc.gridy = 2;
                gbc.gridx = 1;
                timeSlider = new JSlider();

                setTimeSliderValue(0);

                if(filesNames != null && !filesNames.isEmpty())
                {
                    soundPlayer.setSliderMaxValue();
                }
                else
                {
                    timeSlider.setEnabled(false);
                }

                buttonPanel.add(timeSlider, gbc);
            }
            else
            {
                editWordContentButton = getNewButton(buttonDimension, Constants.EDIT, buttonFont);

                nextWindowButton = getNewButton(buttonDimension, Constants.TO_SAVE, buttonFont);

                gbc.weightx = 1;

                gbc.gridx = 0;
                buttonPanel.add(editWordContentButton, gbc);

                gbc.gridx = 1;
                buttonPanel.add(nextWindowButton, gbc);
            }

            return buttonPanel;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    private JScrollPane getNewScrollPane(int fontSize, GridBagConstraints gbc, Dimension dimension)
    {
        JScrollPane scrollWordPane = new JScrollPane(createWordPanel(fontSize, gbc));
        scrollWordPane.setPreferredSize(dimension);

        return scrollWordPane;
    }

    private void addButtonsToPlayerToolbar(Dimension playerToolbarButtonDimension, Font font)
    {
        try
        {
            previousSoundFileButton = getNewButton(playerToolbarButtonDimension, Constants.PREVIOUS_SOUND_FILE, font);
            playOrPauseSoundButton  = getNewButton(playerToolbarButtonDimension, Constants.PLAY_SOUND, font);
            stopSoundButton         = getNewButton(playerToolbarButtonDimension, Constants.STOP_SOUND, font);
            nextSoundFileButton     = getNewButton(playerToolbarButtonDimension, Constants.NEXT_SOUND_FILE, font);

            previousSoundFileButton.setToolTipText(Constants.PREVIOUS_SOUND_FILE_TIP);
            stopSoundButton.setToolTipText(Constants.STOP_SOUND_TIP);
            playOrPauseSoundButton.setToolTipText(Constants.PLAY_SOUND_TIP);
            nextSoundFileButton.setToolTipText(Constants.NEXT_SOUND_FILE_TIP);

            playerToolbar.add(previousSoundFileButton);
            playerToolbar.add(stopSoundButton);
            playerToolbar.add(playOrPauseSoundButton);
            playerToolbar.add(nextSoundFileButton);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void addActionListenersToPlayer()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
          previousSoundFileButton.addActionListener(new ActionListener()
          {
              public void actionPerformed(ActionEvent e)
              {
                  try
                  {
                      if(soundPlayer != null)
                      {
                          soundPlayer.choosePreviousFile();
                      }
                  }
                  catch (Exception exception)
                  {
                      exception.printStackTrace();
                  }
              }
          });

          playOrPauseSoundButton.addActionListener(new ActionListener()
          {
              public void actionPerformed(ActionEvent e)
              {
                  try
                  {
                      if(soundPlayer != null)
                      {
                          soundPlayer.action();
                      }
                  }
                  catch (Exception exception)
                  {
                      exception.printStackTrace();
                  }
              }
          });

          stopSoundButton.addActionListener(new ActionListener()
          {
              public void actionPerformed(ActionEvent e)
              {
                  try
                  {
                      if(soundPlayer != null)
                      {
                          soundPlayer.stop();
                      }
                  }
                  catch (Exception exception)
                  {
                      exception.printStackTrace();
                  }
              }
          });

          nextSoundFileButton.addActionListener(new ActionListener()
          {
              public void actionPerformed(ActionEvent e)
              {
                  try
                  {
                      if(soundPlayer != null)
                      {
                          soundPlayer.chooseNextFile();

                      }
                  }
                  catch (Exception exception)
                  {
                      exception.printStackTrace();
                  }
              }
          });

          timeSlider.addMouseListener(new MouseAdapter()
          {
              @Override
              public void mousePressed(MouseEvent e)
              {
                  try
                  {
                      if(soundPlayer != null)
                      {
                          soundPlayer.stop();
                      }

                  }
                  catch (Exception exception)
                  {
                      exception.printStackTrace();
                  }
              }

              @Override
              public void mouseReleased(MouseEvent e)
              {
                  try
                  {
                      if(soundPlayer != null)
                      {
                          actionThread = null;
                          actionThread = new Thread(new ActionRunnable());
                          actionThread.start();
                      }
                  }
                  catch (Exception exception)
                  {
                      exception.printStackTrace();
                  }
              }
          });
        }
    }

    private void addKeyListenersToPlayer()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            previousSoundFileButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        try
                        {
                            if(soundPlayer != null)
                            {
                                soundPlayer.choosePreviousFile();
                            }
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }
            });

            playOrPauseSoundButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        try
                        {
                            if(soundPlayer != null)
                            {
                                soundPlayer.action();
                            }
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }
            });

            stopSoundButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        try
                        {
                            if(soundPlayer != null)
                            {
                                soundPlayer.stop();
                            }
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }
            });

            nextSoundFileButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        try
                        {
                            if(soundPlayer != null)
                            {
                                soundPlayer.chooseNextFile();
                            }
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }
            });

            timeSlider.addKeyListener(new KeyAdapter()
            {
                public void keyPressed(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)
                    {
                        try
                        {
                            if(soundPlayer != null)
                            {
                                if(soundPlayer.getState() == Constants.PLAYING)
                                {
                                    soundPlayer.pause();
                                }
                            }
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)
                    {
                        try
                        {
                            if(soundPlayer != null)
                            {
                                actionThread = null;
                                actionThread = new Thread(new ActionRunnable());
                                actionThread.start();
                            }
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void addListenersToFrame()
    {
        try
        {
            if(!isActionListenerAdded)
            {
                addWindowListener();

                isActionListenerAdded = true;
            }

            addActionListeners();

            addKeyListeners();

            addNavigationKeyListeners();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void addActionListeners()
    {
        try
        {
            addActionListenersToArrowPanel();

            if(windowsManager.getIfWordIsBeingBrowsed())
            {
                getPreviousWordButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        getPreviousWord();
                    }
                });

                getNextWordButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        getNextWord();
                    }
                });

                addActionListenersToPlayer();
            }
            else
            {
                editWordContentButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        editWordContent();
                    }
                });

                nextWindowButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        getNextWindow();
                    }
                });
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
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

    private void addKeyListeners()
    {
        addKeyListenerToArrowPanel();

        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            getPreviousWordButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        getPreviousWord();
                    }
                }
            });

            getNextWordButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        getNextWord();
                    }
                }
            });

            class passiveAction extends AbstractAction
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                }
            }

            KeyStroke upKeyStroke = KeyStroke.getKeyStroke(Constants.UP);
            KeyStroke downKeyStroke = KeyStroke.getKeyStroke(Constants.UP);

            timeSlider.getInputMap().put(upKeyStroke, upKeyStroke);
            timeSlider.getActionMap().put(upKeyStroke, new passiveAction());

            timeSlider.getInputMap().put(downKeyStroke, downKeyStroke);
            timeSlider.getActionMap().put(downKeyStroke, new passiveAction());

            addKeyListenersToPlayer();
        }
        else
        {
            editWordContentButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        editWordContent();
                    }
                }
            });

            nextWindowButton.addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        getNextWindow();
                    }
                }
            });
        }
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

    private void addNavigationKeyListeners()
    {
        try
        {
            List<Component> components = new ArrayList<>();

            previousWindowButton.addKeyListener(new KeyAdapter()
            {
                public void keyReleased(KeyEvent e)
                {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                        exitFrame();
                    }
                }
            });

            if(windowsManager.getIfWordIsBeingBrowsed())
            {
                components.add(getPreviousWordButton);
                components.add(previousSoundFileButton);
                components.add(stopSoundButton);
                components.add(playOrPauseSoundButton);
                components.add(nextSoundFileButton);
                components.add(getNextWordButton);

                for(Component component: components)
                {
                    component.addKeyListener(new KeyAdapter()
                    {
                        @Override
                        public void keyReleased(KeyEvent e)
                        {
                            if(e.getKeyCode() == KeyEvent.VK_DOWN)
                            {
                                if(timeSlider.isEnabled())
                                {
                                    timeSlider.requestFocus();
                                }
                            }
                        }
                    });
                }

                timeSlider.addKeyListener(new KeyAdapter()
                {
                    @Override
                    public void keyReleased(KeyEvent e)
                    {
                        if(e.getKeyCode() == KeyEvent.VK_UP)
                        {
                            playOrPauseSoundButton.requestFocus();
                        }
                    }
                });

                removeNativeListener(components);

                addNavigationKeyListenersToMainComponents(components, true, false);
            }
            else
            {
                components.add(editWordContentButton);
                components.add(nextWindowButton);

                addNavigationKeyListenersToMainComponents(components, true, false);
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
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

    private void editWordContent()
    {
        if(soundPlayer != null)
        {
            soundPlayer.stop();
            soundPlayer.closeClipAndAudioInputStream();
        }

        new AddOrEditWordManuallyFrame(jsonFilesManager, windowsManager, soundFilesManager, wordAndItsContent).
                setVisible(true);

        wordFrame.dispose();
    }

    private void getNextWindow()
    {
        wordFrame.dispose();

        new AddOrChooseListOfWordsFrame(jsonFilesManager, windowsManager, soundFilesManager, wordAndItsContent);
    }

    public void exitFrame()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            if(soundPlayer != null)
            {
                soundPlayer.stop();
                soundPlayer.closeClipAndAudioInputStream();
            }

            wordFrame.dispose();

            new ListOfWordsFrame(jsonFilesManager, windowsManager);
        }
        else
        {
            wordFrame.dispose();

            new AddOrEditWordManuallyFrame(jsonFilesManager, windowsManager, soundFilesManager, wordAndItsContent);
        }
    }

    private List<List<String>> returnWordAndItsContentForVisualPurposes(List<List<String>> wordAndItsContent)
    {
        try
        {
            List<List<String>> copyOfWordAndItsContent = new ArrayList<>(wordAndItsContent);
            copyOfWordAndItsContent.remove(0);
            List<List<String>> newListOfStringLists = new ArrayList<>();
            for(List<String> List: copyOfWordAndItsContent)
            {
                List<String> newStringList = new ArrayList<>();
                for (String string: List)
                {
                    if((string.length() >= Constants.NUMBER_OF_CHARACTERS_IN_LINE) && (string.contains(" ")))
                    {
                        while(string.length() >= Constants.NUMBER_OF_CHARACTERS_IN_LINE)
                        {
                            for(int counter = (Constants.NUMBER_OF_CHARACTERS_IN_LINE - 1); counter > 0; counter--)
                            {
                                char charAt = string.charAt(counter);
                                if(Character.isWhitespace(charAt))
                                {
                                    String firstPartOfTheNewString = string.substring(0, counter);
                                    string = string.substring(counter, string.length());
                                    newStringList.add(firstPartOfTheNewString);
                                    if(string.length() < Constants.NUMBER_OF_CHARACTERS_IN_LINE)
                                    {
                                        newStringList.add(string);
                                    }
                                    counter = 0;
                                }
                            }
                        }
                    }
                    else
                    {
                        newStringList.add(string);
                    }
                }
                newListOfStringLists.add(newStringList);
            }
            return newListOfStringLists;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    private void getNextWord()
    {
        try
        {
            if(soundPlayer != null)
            {
                soundPlayer.stop();
            }

            int newWordIndex;

            if(jsonFilesManager.getCurrentWordIndex() != jsonFilesManager.getListOfWords().size() - 1)
            {
                newWordIndex = jsonFilesManager.getCurrentWordIndex() + 1;
            }
            else
            {
                newWordIndex = 0;
            }

            jsonFilesManager.setCurrentWordIndex(newWordIndex);
            jsonFilesManager.setContentOfGivenWord();
            wordAndItsContent = jsonFilesManager.getContentOfGivenWord();

            wordFrame.remove(mainPanel);

            createUI();
            setUIOptions();
            repaint();

            getNextWordButton.requestFocus();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void getPreviousWord()
    {
        try
        {
            if(soundPlayer != null)
            {
                soundPlayer.stop();
            }

            int newWordIndex;

            if(jsonFilesManager.getCurrentWordIndex() != 0)
            {
                newWordIndex = jsonFilesManager.getCurrentWordIndex() - 1;
            }
            else
            {
                newWordIndex = jsonFilesManager.getListOfWords().size() - 1;
            }

            jsonFilesManager.setCurrentWordIndex(newWordIndex);
            jsonFilesManager.setContentOfGivenWord();
            wordAndItsContent = jsonFilesManager.getContentOfGivenWord();

            wordFrame.remove(mainPanel);

            createUI();
            setUIOptions();
            repaint();

            getPreviousWordButton.requestFocus();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void setInitialStateOfWordFrame()
    {
        if(windowsManager.getIfWordIsBeingBrowsed())
        {
            playOrPauseSoundButton.requestFocus();
        }
        else
        {
            nextWindowButton.requestFocus();
        }
    }

    public void setFileName(String text)
    {
        fileName.setText(text);
    }

    public void setPlayOrPauseButtonText(String text)
    {
        try
        {
            playOrPauseSoundButton.setText(text);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void setMaxTimeSliderValue(int maximumValue)
    {
        try
        {
            timeSlider.setMaximum(maximumValue);
            timeSlider.repaint();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void setTimeSliderValue(int value)
    {
        try
        {
            timeSlider.setValue(value);
            timeSlider.repaint();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    class ActionRunnable implements Runnable
    {

        @Override
        public void run()
        {
            try
            {
                long newDuration = timeSlider.getValue() * 1000000;
                soundPlayer.stopClipFromSlider();
                soundPlayer.start(newDuration);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
}
