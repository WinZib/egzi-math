package org.egzi.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class PlotContainerImpl implements PlotContainer {
    XYSeries series = new XYSeries("Error");

    JFreeChart chart;
    public PlotContainerImpl(String plotName, String xAxisName, String yAxisName) {
        XYDataset xyDataset = new XYSeriesCollection(series);
        chart = ChartFactory.createXYAreaChart
                (       plotName,            // Title
                        xAxisName,           // X-Axis label
                        yAxisName,           // Y-Axis label
                        xyDataset,           // Dataset
                        PlotOrientation.VERTICAL,
                        true,
                        true,
                        false
                );
        //series.setMaximumItemCount(1000);
    }

    @Override
    public JFreeChart getChart() {
        return chart;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void clear() {
        series.clear();
    }

    @Override
    public void add(Double x, Double y) {
        series.add(x, y);
    }
}
