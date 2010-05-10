/* --------------------
 * XYBarChartDemo4.java
 * --------------------
 * (C) Copyright 2004, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;
import java.awt.GradientPaint;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demonstration of the {@link XYBarDataset} wrapper class.
 */
public class XYBarChartDemo4 extends ApplicationFrame {

    /**
     * Constructs the demo application.
     *
     * @param title  the frame title.
     */
    public XYBarChartDemo4(String title) {

        super(title);
        IntervalXYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        setContentPane(chartPanel);

    }

    private static JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart(
            "XYBarChartDemo4",
            "X",
            false,
            "Y",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        );

        // then customise it a little...
        chart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 1000, 0, Color.blue));
        XYPlot plot = (XYPlot) chart.getPlot();
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart; 
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private static IntervalXYDataset createDataset() {
        XYSeries series = new XYSeries("Series 1");
        series.add(1.0, 5.0);
        series.add(2.0, 7.8);
        series.add(3.0, 9.3);
        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(series);
        return new XYBarDataset(collection, 0.9);
    }
    
    /**
     * Creates a panel for the demo.
     *  
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        return new ChartPanel(createChart(new SimpleIntervalXYDataset()));
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        XYBarChartDemo4 demo = new XYBarChartDemo4("XY Bar Chart Demo 4");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
