/* ---------------------------
 * MinMaxCategoryPlotDemo.java
 * ---------------------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.MinMaxCategoryRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a min/max category 
 * plot.
 */
public class MinMaxCategoryPlotDemo extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public MinMaxCategoryPlotDemo(String title) {

        super(title);

        // create a dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "First", "C1");
        dataset.addValue(4.0, "First", "C2");
        dataset.addValue(3.0, "First", "C3");
        dataset.addValue(5.0, "First", "C4");
        dataset.addValue(5.0, "First", "C5");
        dataset.addValue(7.0, "First", "C6");
        dataset.addValue(7.0, "First", "C7");
        dataset.addValue(8.0, "First", "C8");
        dataset.addValue(5.0, "Second", "C1");
        dataset.addValue(7.0, "Second", "C2");
        dataset.addValue(6.0, "Second", "C3");
        dataset.addValue(8.0, "Second", "C4");
        dataset.addValue(4.0, "Second", "C5");
        dataset.addValue(4.0, "Second", "C6");
        dataset.addValue(2.0, "Second", "C7");
        dataset.addValue(1.0, "Second", "C8");
        dataset.addValue(4.0, "Third", "C1");
        dataset.addValue(3.0, "Third", "C2");
        dataset.addValue(2.0, "Third", "C3");
        dataset.addValue(3.0, "Third", "C4");
        dataset.addValue(6.0, "Third", "C5");
        dataset.addValue(3.0, "Third", "C6");
        dataset.addValue(4.0, "Third", "C7");
        dataset.addValue(3.0, "Third", "C8");

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "Min/Max Category Plot",  // chart title
            "Category",               // domain axis label
            "Value",                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        MinMaxCategoryRenderer renderer = new MinMaxCategoryRenderer();
        plot.setRenderer(renderer);

        // add the chart to a panel...
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

        MinMaxCategoryPlotDemo demo = new MinMaxCategoryPlotDemo(
            "Min/Max Category Chart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
