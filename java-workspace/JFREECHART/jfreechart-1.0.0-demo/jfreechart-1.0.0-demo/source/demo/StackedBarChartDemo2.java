/* -------------------------
 * StackedBarChartDemo2.java
 * -------------------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 */

package demo;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a stacked bar chart
 * using data from a {@link CategoryDataset}.  This demo also has item labels 
 * displayed.
 */
public class StackedBarChartDemo2 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public StackedBarChartDemo2(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(10.0, "Series 1", "Category 1");
        dataset.addValue(4.0, "Series 1", "Category 2");
        dataset.addValue(15.0, "Series 1", "Category 3");
        dataset.addValue(14.0, "Series 1", "Category 4");
        dataset.addValue(-5.0, "Series 2", "Category 1");
        dataset.addValue(-7.0, "Series 2", "Category 2");
        dataset.addValue(14.0, "Series 2", "Category 3");
        dataset.addValue(-3.0, "Series 2", "Category 4");
        dataset.addValue(6.0, "Series 3", "Category 1");
        dataset.addValue(17.0, "Series 3", "Category 2");
        dataset.addValue(-12.0, "Series 3", "Category 3");
        dataset.addValue(7.0, "Series 3", "Category 4");
        dataset.addValue(7.0, "Series 4", "Category 1");
        dataset.addValue(15.0, "Series 4", "Category 2");
        dataset.addValue(11.0, "Series 4", "Category 3");
        dataset.addValue(0.0, "Series 4", "Category 4");
        dataset.addValue(-8.0, "Series 5", "Category 1");
        dataset.addValue(-6.0, "Series 5", "Category 2");
        dataset.addValue(10.0, "Series 5", "Category 3");
        dataset.addValue(-9.0, "Series 5", "Category 4");
        dataset.addValue(9.0, "Series 6", "Category 1");
        dataset.addValue(-8.0, "Series 6", "Category 2");
        dataset.addValue(0.0, "Series 6", "Category 3");
        dataset.addValue(6.0, "Series 6", "Category 4");
        return dataset;
    }
        
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a sample chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        
        JFreeChart chart = ChartFactory.createStackedBarChart(
            "Stacked Bar Chart Demo 2",
            "Category",                  // domain axis label
            "Value",                     // range axis label
            dataset,                     // data
            PlotOrientation.HORIZONTAL,  // the plot orientation
            true,                        // include legend
            true,                        // tooltips
            false                        // urls
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();
        renderer.setItemLabelsVisible(true);
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
        StackedBarChartDemo2 demo = new StackedBarChartDemo2(
                "Stacked Bar Chart Demo 2");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
