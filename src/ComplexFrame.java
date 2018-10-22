package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ComplexFrame extends JFrame
{
    int size = Constants.SIZE;

    public int buttonWidth;

    public int getSizeForVisualPurposes()
    {

        int size = Constants.SIZE;

        if ((int) (size / 19.2) < 12)
        {
            size = (int) (1480 / 6.4);
        }
        return size;
    }

    public void displayUI()
    {

        this.setVisible(true);
    }

    public JPanel getNewPanel(Dimension dimension, Color color)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(dimension);
        if(color != null)
        {
            panel.setBackground(color);
        }
        return panel;
    }

    public JLabel getNewLabel(String text, Font font)
    {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    public JButton getNewButton(Dimension dimension, String text, Font font)
    {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setPreferredSize(dimension);
        return button;
    }

    public JToolBar getNewToolbar(Dimension dimension, Boolean floatable, Boolean rollover)
    {
        JToolBar toolBar = new JToolBar();
        toolBar.setPreferredSize(dimension);
        toolBar.setFloatable(floatable);
        toolBar.setRollover(rollover);
        return toolBar;
    }

    public JComboBox getNewComboBox(Dimension dimension)
    {
        JComboBox comboBox = new JComboBox();
        comboBox.setPreferredSize(dimension);
        return comboBox;
    }

    public JComboBox getNewComboBox(Dimension dimension, Font font)
    {
        JComboBox comboBox = new JComboBox();
        comboBox.setPreferredSize(dimension);
        comboBox.setFont(font);
        return comboBox;
    }

    public JTextField getNewTextField(Dimension dimension, Font font, int horizontalAlignment)
    {
        JTextField textField = new JTextField();
        textField.setPreferredSize(dimension);
        textField.setFont(font);
        textField.setHorizontalAlignment(horizontalAlignment);
        return textField;
    }

    public JScrollPane getNewScrollPane(Dimension dimension, JList list)
    {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(dimension);
        return scrollPane;
    }

    public JList getNewList(Font font, int selectionModel, int layoutOrientation)
    {
        JList jList = new JList();
        jList.setFont(font);
        jList.setSelectionMode(selectionModel);
        jList.setLayoutOrientation(layoutOrientation);
        return jList;
    }

    public JScrollPane getNewScrollPane(Dimension dimension, JTextArea textArea, int vsbPolicy, int hsbPolicy)
    {
        JScrollPane scrollPane = new JScrollPane(textArea, vsbPolicy, hsbPolicy);
        scrollPane.setPreferredSize(dimension);
        return scrollPane;
    }

    public JTextArea getNewJTextArea(Font font)
    {
        JTextArea textArea = new JTextArea();
        textArea.setFont(font);
        return textArea;
    }

    public JMenu getNewMenu(String text, Font font)
    {
        JMenu menu = new JMenu(text);
        menu.setFont(font);
        return menu;
    }

    public JMenuItem getNewMenuItem(String text, Font font)
    {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setFont(font);
        return menuItem;
    }

    public JRadioButton getNewRadioButton(Dimension dimension, String text, Font font)
    {
        JRadioButton radioButton = new JRadioButton();

        radioButton.setText(text);
        radioButton.setPreferredSize(dimension);
        radioButton.setFont(font);

        return radioButton;
    }

    public GridBagConstraints getGridBagConstraints(int anchor)
    {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = anchor;
        gbc.weighty = 1;
        gbc.weightx = 1;

        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        gbc.gridy = 0;
        gbc.gridx = 0;

        return gbc;
    }

    public int getElementIndex(Boolean ifNext, int index, List<Component> list, boolean areAllComponentsEnabled)
    {
        if(ifNext)
        {
            if(index < list.size() - 1)
            {
                index = index + 1;
            }
            else
            {
                index = 0;
            }

            if(!areAllComponentsEnabled)
            {
                while(!list.get(index).isEnabled())
                {
                    if(index < list.size() - 1)
                    {
                        index = index + 1;
                    }
                    else
                    {
                        index = 0;
                    }
                }
            }
        }
        else
        {
            if(index == 0)
            {
                index = (list.size() - 1);
            }
            else
            {
                index = index - 1;
            }

            if(!areAllComponentsEnabled)
            {
                while(!list.get(index).isEnabled())
                {
                    if(index == 0)
                    {
                        index = (list.size() - 1);
                    }
                    else
                    {
                        index = index - 1;
                    }
                }
            }
        }

        return index;
    }

    public int getIndexForHorizontalComponents(List<Component> components, int index, boolean ifNext)
    {
        if(ifNext)
        {
            if(index < components.size() - 1)
            {
                index = index + 1;
            }
            else
            {
                index = 0;
            }
        }
        else
        {
            if(index == 0)
            {
                index = (components.size() - 1);
            }
            else
            {
                index = index - 1;
            }
        }

        return index;
    }

    public void addNavigationKeyListenersToMainComponents(List<Component> components, boolean areAllComponentsEnabled,
                                                          boolean navigationVertical)
    {
        for(int index = 0; index <= components.size() - 1; index++)
        {

            int indexValue = index;

            components.get(index).addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyReleased(KeyEvent e)
                {
                    int keyForMovingBack;
                    int keyForMovingNext;

                    if(navigationVertical)
                    {
                        keyForMovingBack = KeyEvent.VK_UP;
                        keyForMovingNext = KeyEvent.VK_DOWN;
                    }
                    else
                    {
                        keyForMovingBack = KeyEvent.VK_LEFT;
                        keyForMovingNext = KeyEvent.VK_RIGHT;
                    }


                    if (e.getKeyCode() == keyForMovingBack)
                    {
                        int newIndex = getElementIndex(false, indexValue, components, areAllComponentsEnabled);

                        components.get(newIndex).requestFocus();
                    }
                    if (e.getKeyCode() == keyForMovingNext)
                    {
                        int newIndex = getElementIndex(true, indexValue, components, areAllComponentsEnabled);
                        components.get(newIndex).requestFocus();
                    }
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    {
                        exitFrame();
                    }
                }
            });
        }
    }

    public void addHorizontalNavigationKeyBindingsToToolbarButtons(List<Component> buttons)
    {
        KeyStroke upKeyStroke = KeyStroke.getKeyStroke("UP");
        KeyStroke downKeyStroke = KeyStroke.getKeyStroke("DOWN");

        if(buttons.get(0) instanceof JButton)
        {
            List<JButton> jbuttons = new ArrayList<>();
            for(Component button: buttons)
            {
                jbuttons.add((JButton) button);
            }

            for(JButton button: jbuttons)
            {
                List<Component> components = new ArrayList<>(buttons);

                class navigationRightAction extends AbstractAction
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        int newIndex = getIndexForHorizontalComponents(components, buttons.indexOf(button), true);

                        buttons.get(newIndex).requestFocus();
                    }
                }

                class navigationLeftAction extends AbstractAction
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {

                        int newIndex = getIndexForHorizontalComponents(components, buttons.indexOf(button), false);

                        buttons.get(newIndex).requestFocus();
                    }
                }

                class passiveAction extends AbstractAction
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                    }
                }

                String leftActionMapKey = Constants.LEFT + button.getName();

                button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true),
                        leftActionMapKey);
                button.getActionMap().put(leftActionMapKey, new navigationLeftAction());

                String rightActionMapKey = Constants.RIGHT + button.getName();

                button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true),
                        rightActionMapKey);
                button.getActionMap().put(rightActionMapKey, new navigationRightAction());

                KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(Constants.RIGHT);
                KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(Constants.LEFT);

                button.getInputMap().put(leftKeyStroke, leftKeyStroke);
                button.getActionMap().put(leftKeyStroke, new passiveAction());

                button.getInputMap().put(rightKeyStroke, rightKeyStroke);
                button.getActionMap().put(rightKeyStroke, new passiveAction());

                button.getInputMap().put(upKeyStroke, upKeyStroke);
                button.getActionMap().put(upKeyStroke, new passiveAction());

                button.getInputMap().put(downKeyStroke, downKeyStroke);
                button.getActionMap().put(downKeyStroke, new passiveAction());
            }
        }
        else if (buttons.get(0) instanceof JRadioButton)
        {
            List<JRadioButton> radioButtons = new ArrayList<>();
            for(Component button: buttons)
            {
                radioButtons.add((JRadioButton) button);
            }

            for(JRadioButton button: radioButtons)
            {
                List<Component> components = new ArrayList<>(buttons);

                class navigationRightAction extends AbstractAction
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        int newIndex = getIndexForHorizontalComponents(components, buttons.indexOf(button), true);

                        buttons.get(newIndex).requestFocus();
                    }
                }

                class navigationLeftAction extends AbstractAction
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {

                        int newIndex = getIndexForHorizontalComponents(components, buttons.indexOf(button), false);

                        buttons.get(newIndex).requestFocus();
                    }
                }

                class passiveAction extends AbstractAction
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                    }
                }

                String leftActionMapKey = Constants.LEFT + button.getName();

                button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true),
                        leftActionMapKey);
                button.getActionMap().put(leftActionMapKey, new navigationLeftAction());

                String rightActionMapKey = Constants.RIGHT + button.getName();

                button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true),
                        rightActionMapKey);
                button.getActionMap().put(rightActionMapKey, new navigationRightAction());

                KeyStroke rightKeyStroke = KeyStroke.getKeyStroke(Constants.RIGHT);
                KeyStroke leftKeyStroke = KeyStroke.getKeyStroke(Constants.LEFT);

                button.getInputMap().put(leftKeyStroke, leftKeyStroke);
                button.getActionMap().put(leftKeyStroke, new passiveAction());

                button.getInputMap().put(rightKeyStroke, rightKeyStroke);
                button.getActionMap().put(rightKeyStroke, new passiveAction());

                button.getInputMap().put(upKeyStroke, upKeyStroke);
                button.getActionMap().put(upKeyStroke, new passiveAction());

                button.getInputMap().put(downKeyStroke, downKeyStroke);
                button.getActionMap().put(downKeyStroke, new passiveAction());
            }
        }


    }

    public void addVerticalNavigationKeyBindingsToToolbarButtons(List<Component> buttons, List<Component> components,
                                                                 int elementNumber, boolean areAllComponentsEnabled)
    {
        KeyStroke upKeyStroke = KeyStroke.getKeyStroke(Constants.UP);
        KeyStroke downKeyStroke = KeyStroke.getKeyStroke(Constants.DOWN);

            if (buttons.get(0) instanceof JButton)
            {
                List<JButton> jbuttons = new ArrayList<>();
                for(Component button: buttons)
                {
                    jbuttons.add((JButton) button);
                }

                for(JButton button: jbuttons)
                {
                    class navigationUpAction extends AbstractAction
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            int newIndex = getElementIndex(false, elementNumber, components, areAllComponentsEnabled);
                            components.get(newIndex).requestFocus();

                            components.get(newIndex).requestFocus();
                        }
                    }

                    class navigationDownAction extends AbstractAction
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {

                            int newIndex = getElementIndex(true, elementNumber, components, areAllComponentsEnabled);
                            components.get(newIndex).requestFocus();

                            components.get(newIndex).requestFocus();
                        }
                    }

                    class passiveAction extends AbstractAction
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                        }
                    }

                    String upActionMapKey = Constants.UP + button.getName();

                    button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true),
                            upActionMapKey);
                    button.getActionMap().put(upActionMapKey, new navigationUpAction());

                    String downActionMapKey = Constants.DOWN + button.getName();

                    button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true),
                            downActionMapKey);
                    button.getActionMap().put(downActionMapKey, new navigationDownAction());

                    button.getInputMap().put(upKeyStroke, upKeyStroke);
                    button.getActionMap().put(upKeyStroke, new passiveAction());

                    button.getInputMap().put(downKeyStroke, downKeyStroke);
                    button.getActionMap().put(downKeyStroke, new passiveAction());
                }
            }
            else if (buttons.get(0) instanceof JRadioButton)
            {
                List<JRadioButton> radioButtons = new ArrayList<>();
                for(Component button: buttons)
                {
                    radioButtons.add((JRadioButton) button);
                }

                for(JRadioButton button: radioButtons)
                {
                    class navigationUpAction extends AbstractAction
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                            int newIndex = getElementIndex(false, elementNumber, components, areAllComponentsEnabled);
                            components.get(newIndex).requestFocus();

                            components.get(newIndex).requestFocus();
                        }
                    }

                    class navigationDownAction extends AbstractAction
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {

                            int newIndex = getElementIndex(true, elementNumber, components, areAllComponentsEnabled);
                            components.get(newIndex).requestFocus();

                            components.get(newIndex).requestFocus();
                        }
                    }

                    class passiveAction extends AbstractAction
                    {
                        @Override
                        public void actionPerformed(ActionEvent e)
                        {
                        }
                    }

                    String upActionMapKey = Constants.UP + button.getName();

                    button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true),
                            upActionMapKey);
                    button.getActionMap().put(upActionMapKey, new navigationUpAction());

                    String downActionMapKey = Constants.DOWN + button.getName();

                    button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true),
                            downActionMapKey);
                    button.getActionMap().put(downActionMapKey, new navigationDownAction());

                    button.getInputMap().put(upKeyStroke, upKeyStroke);
                    button.getActionMap().put(upKeyStroke, new passiveAction());

                    button.getInputMap().put(downKeyStroke, downKeyStroke);
                    button.getActionMap().put(downKeyStroke, new passiveAction());
                }
            }
    }

    public void exitFrame()
    {

    }
}
