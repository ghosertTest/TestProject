/* ------------------
 * DualAxisDemo3.java
 * ------------------
 * (C) Copyright 2002-2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a dual axis chart 
 * based on data from two {@link CategoryDataset} instances.
 */
public class DualAxisDemo3 extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public DualAxisDemo3(String title) {

        super(title);
        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }
    
    private static JFreeChart createChart() {
        CategoryDataset dataset1 = createDataset1();

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "Dual Axis Chart",           // chart title
            "Category",                  // domain axis label
            "Value",                     // range axis label
            dataset1,                    // dataset
            PlotOrientation.HORIZONTAL,  // orientation
            true,                        // include legend
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(new Color(0xCC, 0xFF, 0xCC));
        LegendTitle legend = (LegendTitle) chart.getSubtitle(0);
        legend.setPosition(RectangleEdge.LEFT);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        plot.getDomainAxis().setMaximumCategoryLabelWidthRatio(10.0f);
        CategoryDataset dataset2 = createDataset2();
        ValueAxis axis2 = new NumberAxis("Secondary");
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        CategoryItemRenderer renderer2 = new LineAndShapeRenderer();
        plot.setRenderer(1, renderer2);
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        // OPTIONAL CUSTOMISATION COMPLETED.
        return chart;
    }

    /**
     * Creates a sample dataset.
     *
     * @return  The dataset.
     */
    private static CategoryDataset createDataset1() {

        // row keys...
        String series1 = "First";
        String series2 = "Second";
        String series3 = "Third";

        // column keys...
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";
        String category6 = "Category 6";
        String category7 = "Category 7";
        String category8 = "Category 8";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);
        dataset.addValue(7.0, series1, category6);
        dataset.addValue(7.0, series1, category7);
        dataset.addValue(8.0, series1, category8);

        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);
        dataset.addValue(4.0, series2, category6);
        dataset.addValue(2.0, series2, category7);
        dataset.addValue(1.0, series2, category8);

        dataset.addValue(4.0, series3, category1);
        dataset.addValue(3.0, series3, category2);
        dataset.addValue(2.0, series3, category3);
        dataset.addValue(3.0, series3, category4);
        dataset.addValue(6.0, series3, category5);
        dataset.addValue(3.0, series3, category6);
        dataset.addValue(4.0, series3, category7);
        dataset.addValue(3.0, series3, category8);

        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return  The dataset.
     */
    private static CategoryDataset createDataset2() {

        // row keys...
        String series1 = "Fourth";

        // column keys...
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";
        String category6 = "Category 6";
        String category7 = "Category 7";
        String category8 = "Category 8";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(15.0, series1, category1);
        dataset.addValue(24.0, series1, category2);
        dataset.addValue(31.0, series1, category3);
        dataset.addValue(25.0, series1, category4);
        dataset.addValue(56.0, series1, category5);
        dataset.addValue(37.0, series1, category6);
        dataset.addValue(77.0, series1, category7);
        dataset.addValue(18.0, series1, category8);

        return dataset;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart();
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        DualAxisDemo3 demo = new DualAxisDemo3("Dual Axis Demo 3");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
