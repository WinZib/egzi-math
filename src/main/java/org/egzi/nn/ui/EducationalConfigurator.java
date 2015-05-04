package org.egzi.nn.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EducationalConfigurator extends JFrame {
    public static int COUNT_OF_NEURON_IN_HIDDEN_LAYER = 10;


    public EducationalConfigurator() {
        setLayout(new FlowLayout(0, 2, 10));

        JLabel countOfNeuronInHiddenLayerLabel = new JLabel("Count Of Neurons in Hidden Layer");
        add(countOfNeuronInHiddenLayerLabel);

        JTextField countOfNeuronInHiddenLayer = new JTextField(Integer.toString(COUNT_OF_NEURON_IN_HIDDEN_LAYER), 5);
        add(countOfNeuronInHiddenLayer);


        JButton button = new JButton("Save");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeFrame();
            }
        });
        add(button);
    }

    public void closeFrame() {
        setVisible(false);
    }
}
