/* ---------------------------
 * CategoryStepChartDemo1.java
 * ---------------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryStepRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a step chart 
 * using data from a {@link CategoryDataset}.
 */
public class CategoryStepChartDemo1 extends ApplicationFrame {

    /**
     * Creates a new demo application.
     *
     * @param title  the frame title.
     */
    public CategoryStepChartDemo1(String title) {

        super(title);
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        chartPanel.setEnforceFileExtensions(false);
        setContentPane(chartPanel);

    }

    /**
     * Creates a sample dataset.
     * 
     * @return A dataset.
     */
    private static CategoryDataset createDataset() {
        double[][] data = new double[][] {
            {1.0, 4.0, 3.0, 5.0, 5.0, 7.0, 7.0, 8.0},
            {5.0, 7.0, 6.0, 8.0, 4.0, 4.0, 2.0, 1.0},
            {4.0, 3.0, 2.0, 3.0, 6.0, 3.0, 4.0, 3.0}
        };
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
            "Series ", "Type ", data
        );
        return dataset; 
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
            
        CategoryItemRenderer renderer = new CategoryStepRenderer(true);
        CategoryAxis domainAxis = new CategoryAxis("Category");
        NumberAxis rangeAxis = new NumberAxis("Value");
        CategoryPlot plot = new CategoryPlot(
            dataset, domainAxis, rangeAxis, renderer
        );
        JFreeChart chart = new JFreeChart("Category Step Chart", plot);
 
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        domainAxis.setLowerMargin(0.0);
        domainAxis.setUpperMargin(0.0);
        domainAxis.addCategoryLabelToolTip("Type 1", "The first type.");
        domainAxis.addCategoryLabelToolTip("Type 2", "The second type.");
        domainAxis.addCategoryLabelToolTip("Type 3", "The third type.");
        
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setLabelAngle(0 * Math.PI / 2.0);
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

        CategoryStepChartDemo1 demo = new CategoryStepChartDemo1(
            "Category Step Renderer Demo"
        );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
