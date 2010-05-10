/* ---------------------
 * ScatterPlotDemo4.java
 * ---------------------
 * (C) Copyright 2002-2004, by Object Refinery Limited.
 *
 */

package demo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo scatter plot showing the use of the {@link XYDotRenderer} class.
 */
public class ScatterPlotDemo4 extends ApplicationFrame {

    /**
     * A demonstration application showing a scatter plot.
     *
     * @param title  the frame title.
     */
    public ScatterPlotDemo4(String title) {

        super(title);
        XYDataset data = new SampleXYDataset2();
        JFreeChart chart = ChartFactory.createScatterPlot(
            "Scatter Plot Demo",
            "X", 
            "Y", 
            data, 
            PlotOrientation.VERTICAL,
            true, 
            true, 
            false
        );
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new XYDotRenderer());
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        ScatterPlotDemo4 demo = new ScatterPlotDemo4("Scatter Plot Demo 4");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
