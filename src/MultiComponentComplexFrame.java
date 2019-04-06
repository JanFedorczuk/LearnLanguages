package LearnLanguages;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MultiComponentComplexFrame extends ComplexFrame
{
    public void addNavigationKeyListenersToComboBox(JComboBox comboBox)
    {
        comboBox.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyReleased(KeyEvent e)
            {
                int selectedIndex = comboBox.getSelectedIndex();

                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    if(comboBox.getItemCount() > 1)
                    {
                        if(selectedIndex == comboBox.getItemCount() - 1)
                        {
                            selectedIndex = 0;
                        }
                        else
                        {
                            selectedIndex = selectedIndex + 1;
                        }

                        comboBox.setSelectedIndex(selectedIndex);
                        comboBox.requestFocus();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    if(comboBox.getItemCount() > 1)
                    {
                        if(selectedIndex == 0)
                        {
                            selectedIndex = comboBox.getItemCount() -1;

                        }
                        else
                        {
                            selectedIndex = selectedIndex - 1;
                        }

                        comboBox.setSelectedIndex(selectedIndex);
                        comboBox.requestFocus();
                    }
                }
            }
        });
    }

    public void addMouseListenersToRadioButtons(List<JRadioButton> radioButtons)
    {
        for(JRadioButton radioButton: radioButtons)
        {
            radioButton.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseReleased(MouseEvent e)
                {
                    if(radioButton.isEnabled())
                    {
                        {

                        }
                        int index = radioButtons.indexOf(radioButton);
                        int nextIndex;
                        int previousIndex;

                        if(index == 0)
                        {
                            nextIndex = index + 1;
                            previousIndex = radioButtons.size() - 1;
                        }
                        else if(index == (radioButtons.size() - 1))
                        {
                            nextIndex = 0;
                            previousIndex = index - 1;
                        }
                        else
                        {
                            nextIndex = index + 1;
                            previousIndex = index - 1;
                        }

                        if(radioButton.isSelected())
                        {
                            radioButtons.get(nextIndex).setSelected(false);
                            radioButtons.get(previousIndex).setSelected(false);
                        }
                        else
                        {
                            radioButton.setSelected(true);
                        }
                    }
                    else
                    {
                        radioButton.setSelected(false);
                    }
                }
            });
        }
    }
}
