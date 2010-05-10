/* ------------------------
 * FastScatterPlotDemo.java
 * ------------------------
 * (C) Copyright 2002-2004, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.RenderingHints;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo of the fast scatter plot.
 */
public class FastScatterPlotDemo extends ApplicationFrame {

    /** A constant for the number of items in the sample dataset. */
    private static final int COUNT = 500000;

    /** The data. */
    private float[][] data = new float[2][COUNT];

    /**
     * Creates a new fast scatter plot demo.
     *
     * @param title  the frame title.
     */
    public FastScatterPlotDemo(final String title) {

        super(title);
        populateData();
        NumberAxis domainAxis = new NumberAxis("X");
        domainAxis.setAutoRangeIncludesZero(false);
        NumberAxis rangeAxis = new NumberAxis("Y");
        rangeAxis.setAutoRangeIncludesZero(false);
        FastScatterPlot plot = new FastScatterPlot(this.data, domainAxis, rangeAxis);
        JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);

        // force aliasing of the rendered content..
        chart.getRenderingHints().put
            (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ChartPanel panel = new ChartPanel(chart, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
        panel.setDomainZoomable(true);
        panel.setRangeZoomable(true);
        panel.setMinimumDrawHeight(10);
        panel.setMaximumDrawHeight(2000);
        panel.setMinimumDrawWidth(20);
        panel.setMaximumDrawWidth(2000);
        
        setContentPane(panel);

    }
    
    /**
     * Populates the data array with random values.
     */
    private void populateData() {

        for (int i = 0; i < this.data[0].length; i++) {
            float x = (float) i + 100000;
            this.data[0][i] = x;
            this.data[1][i] = 100000 + (float) Math.random() * COUNT;
        }

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        FastScatterPlotDemo demo = new FastScatterPlotDemo("Fast Scatter Plot Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
