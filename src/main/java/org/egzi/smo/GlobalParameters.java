package org.egzi.smo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class GlobalParameters extends JFrame {
    public static int COUNT_OF_GATES = 20;
    public static int LOW_COUNT_OF_PASSENGERS = 100;
    public static int HIGH_COUNT_OF_PASSENGERS = 200;
    public static long MAXIMUM_TIME_OF_WAITING = 20;
    public static int GATE_CAPACITY = 20;
    public static int COUNT_OF_GUARDS = 1000;
    public static long TIME_OF_PROCESSING_CITIZENS = 1;
    public static long TIME_OF_PROCESSING_FOREIGNER = 2;
    public static int PROBABILITY_OF_TRANSIT_PASSENGER = 1;
    public static int PROBABILITY_OF_CITIZEN_PASSENGER = 60;
    public static int MAXIMUM_QUEUE_SIZE = 60;
    public static TimeUnit TIME_UNIT = TimeUnit.SECONDS;

    public GlobalParameters(){
        setPreferredSize(new Dimension(500, 500));
        setLayout(new GridLayout(10, 1));

        JPanel panel1 = new JPanel(new GridLayout(1, 4));

        //COUNT_OF_GATES
        JLabel label1 = new JLabel("Count of Gates");
        panel1.add(label1);

        final JTextField textField1 = new JTextField(Integer.toString(COUNT_OF_GATES), 10);
        panel1.add(textField1);

        add(panel1);

        //LOW_COUNT_OF_PASSENGERS
        JPanel panel2 =  new JPanel(new GridLayout(1, 4));

        JLabel label2 = new JLabel("Low Count of Passenger on the Board:");
        panel2.add(label2);

        final JTextField textField2 = new JTextField(Integer.toString(LOW_COUNT_OF_PASSENGERS), 100);
        panel2.add(textField2);
        add(panel2);

        //HIGH_COUNT_OF_PASSENGER
        JPanel panel3 = new JPanel(new GridLayout(1, 4));

        JLabel label3 = new JLabel("High Count of Passenger on the Board:");
        panel3.add(label3);

        final JTextField textField3 = new JTextField(Integer.toString(HIGH_COUNT_OF_PASSENGERS), 100);
        panel3.add(textField3);
        add(panel3);

        //MAXIMUM_TIME_OF_WAITING
        JPanel panel4 = new JPanel(new GridLayout(1, 4));

        JLabel label4 = new JLabel("Max Time Of Gate Waiting:");
        panel4.add(label4);

        final JTextField textField4 = new JTextField(Long.toString(MAXIMUM_TIME_OF_WAITING), 100);
        panel4.add(textField4);
        add(panel4);

        //COUNT_OF_GUARDS
        JPanel panel5 = new JPanel(new GridLayout(1, 4));

        JLabel label5 = new JLabel("Count Of Guards:");
        panel5.add(label5);

        final JTextField textField5 = new JTextField(Long.toString(COUNT_OF_GUARDS), 100);
        panel5.add(textField5);
        add(panel5);

        //MAXIMUM_QUEUE_SIZE
        JPanel panel6 = new JPanel(new GridLayout(1, 4));

        JLabel label6 = new JLabel("Maximum Plane Queue Size:");
        panel6.add(label6);

        final JTextField textField6 = new JTextField(Long.toString(MAXIMUM_QUEUE_SIZE), 100);
        panel6.add(textField6);
        add(panel6);

        //TIME_UNIT
        JPanel panel7 = new JPanel(new GridLayout(1, 4));

        JLabel label7 = new JLabel("Type of TimeUnit:");
        panel7.add(label7);

        final JComboBox<TimeUnit> comboBox = new JComboBox<TimeUnit>();

        for (TimeUnit timeUnit : TimeUnit.values())
            comboBox.addItem(timeUnit);

        comboBox.setSelectedItem(TIME_UNIT);

        panel7.add(comboBox);
        add(panel7);


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                COUNT_OF_GATES = Integer.valueOf(textField1.getText());
                LOW_COUNT_OF_PASSENGERS = Integer.valueOf(textField2.getText());
                HIGH_COUNT_OF_PASSENGERS = Integer.valueOf(textField3.getText());
                MAXIMUM_TIME_OF_WAITING = Long.valueOf(textField4.getText());
                COUNT_OF_GUARDS = Integer.valueOf(textField5.getText());
                MAXIMUM_QUEUE_SIZE = Integer.valueOf(textField6.getText());
                TIME_UNIT = (TimeUnit)comboBox.getSelectedItem();

                //save
                setVisible(false);
            }
        });
        add(saveButton);
    }


}
