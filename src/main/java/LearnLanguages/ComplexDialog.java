package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ComplexDialog extends JDialog
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

    public JTextField getNewTextField(Dimension dimension, Font font, int horizontalAlignment)
    {
        JTextField textField = new JTextField();
        textField.setPreferredSize(dimension);
        textField.setFont(font);
        textField.setHorizontalAlignment(horizontalAlignment);
        return textField;
    }

    public JProgressBar getNewProgressBar(Dimension dimension, Font font)
    {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setPreferredSize(dimension);
        progressBar.setFont(font);

        return progressBar;
    }

    public int getElementIndex(Boolean ifNext, int index, java.util.List<Component> list, boolean areAllComponentsEnabled)
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

    public void addNavigationKeyListenersToMainElements(java.util.List<Component> components, boolean areAllComponentsEnabled,
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
                        exitDialog();
                    }
                }
            });
        }
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

    public void exitDialog()
    {

    }
}
