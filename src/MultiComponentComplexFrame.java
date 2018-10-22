package LearnLanguages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
}
