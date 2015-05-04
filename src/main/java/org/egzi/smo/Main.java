package org.egzi.smo;

import org.egzi.smo.impl.airport.*;
import org.egzi.smo.ui.ConfigureMenuItem;
import org.egzi.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

public class Main {

    static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
    static ExecutorService dutyService;
    static PlaneDispatcher dispatcher;


    public static void main(String[] args) {
        executionInfo();
    }

    public static void executionInfo() {
        final MainWindow window = new MainWindow();
        window.addMenuItem(new ConfigureMenuItem());

        AnalyzerThread.init(window);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 3));

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.clearPlots();
                dispatcher = new PlaneDispatcher();
                GuardDispatcher.getInstance().init();

                dutyService = new ThreadPoolExecutor(3, 3, 0, TimeUnit.DAYS, queue);
                //start plane generator
                dutyService.submit(new ScheduledPlaneGenerator(dispatcher));
                //start process analyzer thread
                dutyService.submit(new AnalyzerThread(dispatcher));

                dutyService.submit(new UnscheduledPlaneGenerator(dispatcher));

            }
        });
        panel.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatcher.shutdown();
                dutyService.shutdownNow();
                GuardDispatcher.getInstance().shutdown();

            }
        });
        panel.add(stopButton);



        window.add(panel);

        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
