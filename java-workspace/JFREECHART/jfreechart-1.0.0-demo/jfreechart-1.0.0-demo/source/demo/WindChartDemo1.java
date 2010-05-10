/* -------------------
 * WindChartDemo1.java
 * -------------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;
import java.awt.GradientPaint;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultWindDataset;
import org.jfree.data.xy.WindDataset;
import org.jfree.date.DateUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a wind chart.
 *
 */
public class WindChartDemo1 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public WindChartDemo1(String title) {

        super(title);
        WindDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
    
    /**
     * Creates a sample wind dataset.
     *
     * @return a sample wind dataset.
     */
    public static WindDataset createDataset() {

        int jan = 1;
        Object[][][] data = new Object[][][] {{
            {DateUtilities.createDate(1999, jan, 3), new Double(0.0), new Double(10.0)},
            {DateUtilities.createDate(1999, jan, 4), new Double(1.0), new Double(8.5)},
            {DateUtilities.createDate(1999, jan, 5), new Double(2.0), new Double(10.0)},
            {DateUtilities.createDate(1999, jan, 6), new Double(3.0), new Double(10.0)},
            {DateUtilities.createDate(1999, jan, 7), new Double(4.0), new Double(7.0)},
            {DateUtilities.createDate(1999, jan, 8), new Double(5.0), new Double(10.0)},
            {DateUtilities.createDate(1999, jan, 9), new Double(6.0), new Double(8.0)},
            {DateUtilities.createDate(1999, jan, 10), new Double(7.0), new Double(11.0)},
            {DateUtilities.createDate(1999, jan, 11), new Double(8.0), new Double(10.0)},
            {DateUtilities.createDate(1999, jan, 12), new Double(9.0), new Double(11.0)},
            {DateUtilities.createDate(1999, jan, 13), new Double(10.0), new Double(3.0)},
            {DateUtilities.createDate(1999, jan, 14), new Double(11.0), new Double(9.0)},
            {DateUtilities.createDate(1999, jan, 15), new Double(12.0), new Double(11.0)},
            {DateUtilities.createDate(1999, jan, 16), new Double(0.0), new Double(0.0)} } };

        return new DefaultWindDataset(new String[] {"Wind!!"}, data);
    }

    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private static JFreeChart createChart(WindDataset dataset) {
        
        JFreeChart chart = ChartFactory.createWindPlot(
            "Wind Chart Demo", 
            "Date", 
            "Direction / Force", 
            dataset,
            true,
            false,
            false
        );

        // then customise it a little...
        chart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 1000, 0, Color.green));
        return chart;
        
    }

    /**
     * Creates a panel for the demo.
     *  
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        return new ChartPanel(createChart(createDataset()));
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        WindChartDemo1 demo = new WindChartDemo1("Wind Chart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
