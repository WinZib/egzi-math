package org.egzi.smo.ui;

import org.egzi.nn.ui.EducationalConfigurator;
import org.egzi.smo.GlobalParameters;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: WinZib
 * Date: 5/19/13
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigureMenuItem extends JMenuItem {
    public ConfigureMenuItem() {
        setText("Configurate Parameters");

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame newFrame = new GlobalParameters();

                newFrame.pack();
                newFrame.setVisible(true);
            }
        });


    }
}
