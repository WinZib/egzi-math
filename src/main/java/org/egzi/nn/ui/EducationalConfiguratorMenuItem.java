package org.egzi.nn.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EducationalConfiguratorMenuItem extends JMenuItem {
    public EducationalConfiguratorMenuItem() {
        setText("Configuration Educational Parameters");

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame newFrame = new EducationalConfigurator();

                newFrame.pack();
                newFrame.setVisible(true);
            }
        });


    }
}
