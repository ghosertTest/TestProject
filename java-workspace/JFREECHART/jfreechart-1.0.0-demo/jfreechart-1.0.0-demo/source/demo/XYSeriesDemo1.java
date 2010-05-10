/* ------------------
 * XYSeriesDemo1.java
 * ------------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 */

package demo;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demo showing a dataset created using the {@link XYSeriesCollection} 
 * class.
 */
public class XYSeriesDemo1 extends ApplicationFrame {

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title  the frame title.
     */
    public XYSeriesDemo1(String title) {

        super(title);
        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
    
    private static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart(
            "XY Series Demo",
            "X", 
            "Y", 
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        return chart;
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private static XYDataset createDataset() {
        XYSeries series = new XYSeries("Random Data");
        series.add(1.0, 500.2);
        series.add(5.0, 694.1);
        series.add(4.0, 100.0);
        series.add(12.5, 734.4);
        series.add(17.3, 453.2);
        series.add(21.2, 500.2);
        series.add(21.9, null);
        series.add(25.6, 734.4);
        series.add(30.0, 453.2);
        return new XYSeriesCollection(series);       
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        XYSeriesDemo1 demo = new XYSeriesDemo1("XY Series Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
