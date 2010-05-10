/* ------------------
 * BarChartDemo5.java
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple demonstration application showing how to create a horizontal bar 
 * chart.
 */
public class BarChartDemo5 extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public BarChartDemo5(String title) {

        super(title);
        CategoryDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    /**
     * Creates a sample dataset.
     * 
     * @return A sample dataset.
     */
    private static CategoryDataset createDataset() {
        double[][] data = new double[][] {
                {1.0, 43.0, 35.0, 58.0, 54.0, 77.0, 71.0, 89.0},
                {54.0, 75.0, 63.0, 83.0, 43.0, 46.0, 27.0, 13.0},
                {41.0, 33.0, 22.0, 34.0, 62.0, 32.0, 42.0, 34.0}
        };
        return DatasetUtilities.createCategoryDataset("Series ", "Factor ", data);
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(final CategoryDataset dataset) {
        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "Bar Chart",                 // chart title
            "Category",                  // domain axis label
            "Score (%)",                 // range axis label
            dataset,                     // data
            PlotOrientation.HORIZONTAL,  // orientation
            true,                        // include legend
            true,
            false
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        CategoryPlot plot = chart.getCategoryPlot();

        plot.getRenderer().setSeriesPaint(0, new Color(0, 0, 255));
        plot.getRenderer().setSeriesPaint(1, new Color(75, 75, 255));
        plot.getRenderer().setSeriesPaint(2, new Color(150, 150, 255));
        
        // change the auto tick unit selection to integer units only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

       // NumberAxis hna = rangeAxis;
       // MarkerAxisBand band = new MarkerAxisBand(hna, 2.0, 2.0, 2.0, 2.0,
       //     new Font("SansSerif", Font.PLAIN, 9));

//        IntervalMarker m1 = new IntervalMarker(0.0, 33.0, "Low", Color.gray,
//            new BasicStroke(0.5f), Color.green, 0.75f);
//        IntervalMarker m2 = new IntervalMarker(33.0, 66.0, "Medium", Color.gray,
//            new BasicStroke(0.5f), Color.orange, 0.75f);
//        IntervalMarker m3 = new IntervalMarker(66.0, 100.0, "High", Color.gray,
//            new BasicStroke(0.5f), Color.red, 0.75f);
//        band.addMarker(m1);
//        band.addMarker(m2);
//        band.addMarker(m3);
//        hna.setMarkerBand(band);
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

        BarChartDemo5 demo = new BarChartDemo5("Bar Chart Demo 5");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
