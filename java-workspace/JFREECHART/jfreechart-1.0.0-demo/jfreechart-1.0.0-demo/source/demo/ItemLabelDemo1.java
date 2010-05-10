/* -------------------
 * ItemLabelDemo1.java
 * -------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.AbstractCategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demo showing a label generator that only displays labels for items 
 * with a value that is greater than some threshold.
 */
public class ItemLabelDemo1 extends ApplicationFrame {

    /**
     * A custom label generator.
     */
    static class LabelGenerator extends AbstractCategoryItemLabelGenerator 
                                implements CategoryItemLabelGenerator {
                                    
        /** The threshold. */
        private double threshold;
        
        /**
         * Creates a new generator that only displays labels that are greater 
         * than or equal to the threshold value.
         * 
         * @param threshold  the threshold value.
         */
        public LabelGenerator(double threshold) {
            super("", NumberFormat.getInstance());
            this.threshold = threshold;
        }

        /**
         * Generates a label for the specified item. The label is typically a 
         * formatted version of the data value, but any text can be used.
         *
         * @param dataset  the dataset (<code>null</code> not permitted).
         * @param series  the series index (zero-based).
         * @param category  the category index (zero-based).
         *
         * @return the label (possibly <code>null</code>).
         */
        public String generateLabel(CategoryDataset dataset,
                                    int series,
                                    int category) {
            
            String result = null;
            Number value = dataset.getValue(series, category);
            if (value != null) {
                double v = value.doubleValue();
                if (v > this.threshold) {
                    result = value.toString();  // could apply formatting here
                }
            }
            return result;
            
        }
        
    }
    
    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public ItemLabelDemo1(String title) {
        
        super(title);
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
        
    }

    /**
     * Returns a sample dataset.
     * 
     * @return The dataset.
     */
    private static CategoryDataset createDataset() {
       
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(11.0, "S1", "C1");
        dataset.addValue(44.3, "S1", "C2");
        dataset.addValue(93.0, "S1", "C3");
        dataset.addValue(35.6, "S1", "C4");
        dataset.addValue(75.1, "S1", "C5");
        return dataset;
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return the chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "Item Label Demo 1",      // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            false,                    // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setUpperMargin(0.15);
        
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setItemLabelGenerator(new LabelGenerator(50.0));
        renderer.setItemLabelFont(new Font("Serif", Font.PLAIN, 20));
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

        ItemLabelDemo1 demo = new ItemLabelDemo1("Item Label Demo 1");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
