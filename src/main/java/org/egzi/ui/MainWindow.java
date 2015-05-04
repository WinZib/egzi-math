package org.egzi.ui;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    JPanel panel = new JPanel();
    PlotContainer container;
    JMenu configMenu = new JMenu("Configuration");

    ArrayList<PlotContainer> plots = new ArrayList<PlotContainer>();

    public MainWindow() {
        panel.setLayout(new GridLayout(2, 2));
        setContentPane(panel);

        //config menu

        JMenuBar bar = new JMenuBar();
        bar.add(configMenu);
        setJMenuBar(bar);

    }

    public void addPlot(PlotContainer plotContainer) {
        plots.add(plotContainer);

        ChartPanel chartPanel = new ChartPanel(plotContainer.getChart());
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        // add it to our application
        panel.add(chartPanel);
    }

    public void addPlot(PlotContainer plotContainer, int width, int height) {
        this.container = plotContainer;
        ChartPanel chartPanel = new ChartPanel(plotContainer.getChart());
        // default size
        chartPanel.setPreferredSize(new Dimension(width, height));
        // add it to our application
        panel.add(chartPanel);
    }

    public void addMenuItem(JMenuItem menuItem) {
        configMenu.add(menuItem);
    }

    public void clearPlots() {
        for (PlotContainer plot : plots) {
            plot.clear();
        }
    }
}
