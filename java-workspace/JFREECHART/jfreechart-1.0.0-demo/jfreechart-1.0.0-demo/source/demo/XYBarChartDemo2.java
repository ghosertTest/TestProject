/* --------------------
 * XYBarChartDemo2.java
 * --------------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited.
 *
 */

package demo;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a vertical bar chart.
 */
public class XYBarChartDemo2 extends ApplicationFrame {

    /**
     * Constructs the demo application.
     *
     * @param title  the frame title.
     */
    public XYBarChartDemo2(String title) {

        super(title);
        IntervalXYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        setContentPane(chartPanel);

    }

    private static IntervalXYDataset createDataset() {
        TimeSeries series1 = new TimeSeries("Series 1", Day.class);
        series1.add(new Day(1, 1, 2003), 54.3);
        series1.add(new Day(2, 1, 2003), 20.3);
        series1.add(new Day(3, 1, 2003), 43.4);
        series1.add(new Day(4, 1, 2003), -12.0);

        TimeSeries series2 = new TimeSeries("Series 2", Day.class);
        series2.add(new Day(1, 1, 2003), 8.0);
        series2.add(new Day(2, 1, 2003), 16.0);
        series2.add(new Day(3, 1, 2003), 21.0);
        series2.add(new Day(4, 1, 2003), 5.0);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.setDomainIsPointsInTime(false);
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }
    
    /** 
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private static JFreeChart createChart(IntervalXYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYBarChart(
            "XY Bar Chart Demo 2",      // chart title
            "Date",                     // domain axis label
            true,
            "Y",                        // range axis label
            dataset,                    // data
            PlotOrientation.HORIZONTAL,
            true,                       // include legend
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        XYPlot plot = chart.getXYPlot();
        plot.setRenderer(new ClusteredXYBarRenderer());
        // OPTIONAL CUSTOMISATION COMPLETED.
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

        XYBarChartDemo2 demo = new XYBarChartDemo2("XY Bar Chart Demo 2");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
