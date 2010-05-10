/* ----------------------------
 * BoxAndWhiskerChartDemo1.java
 * ----------------------------
 * (C) Copyright 2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a box-and-whisker 
 * chart.
 */
public class BoxAndWhiskerChartDemo1 extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public BoxAndWhiskerChartDemo1(String title) {

        super(title);
        BoxAndWhiskerCategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);

    }

    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    private static BoxAndWhiskerCategoryDataset createDataset() {
        final int SERIES_COUNT = 3;
        final int CATEGORY_COUNT = 5;
        final int VALUE_COUNT = 20;
        DefaultBoxAndWhiskerCategoryDataset result 
            = new DefaultBoxAndWhiskerCategoryDataset();
        for (int s = 0; s < SERIES_COUNT; s++) {
            for (int c = 0; c < CATEGORY_COUNT; c++) {
                List values = createValueList(0, 20.0, VALUE_COUNT);
                result.add(values, "Series " + s, "Category " + c);
            }
        }
        return result;
    }
    
    private static List createValueList(double lowerBound, double upperBound, 
                                        int count) {
        List result = new java.util.ArrayList();
        for (int i = 0; i < count; i++) {
            double v = lowerBound + (Math.random() * (upperBound - lowerBound));
            result.add(new Double(v));   
        }
        return result;
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private static JFreeChart createChart(BoxAndWhiskerCategoryDataset dataset) {
        
        CategoryAxis domainAxis = new CategoryAxis(null);
        NumberAxis rangeAxis = new NumberAxis("Value");
        CategoryItemRenderer renderer = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(
            dataset, domainAxis, rangeAxis, renderer
        );
        JFreeChart chart = new JFreeChart("Box-and-Whisker Chart Demo 1", plot);
        
        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
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

        BoxAndWhiskerChartDemo1 demo = new BoxAndWhiskerChartDemo1(
            "Box-and-Whisker Chart Demo 1"
        );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
