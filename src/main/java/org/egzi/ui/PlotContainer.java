package org.egzi.ui;

import org.jfree.chart.JFreeChart;

public interface PlotContainer {
    JFreeChart getChart();
    void clear();
    void add(Double x, Double y);
}
