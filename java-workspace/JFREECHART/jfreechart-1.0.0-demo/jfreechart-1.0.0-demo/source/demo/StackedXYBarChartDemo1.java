/* ---------------------------
 * StackedXYBarChartDemo1.java
 * ---------------------------
 * (C) Copyright 2004, by Object Refinery Limited.
 *
 */

package demo;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple stacked bar chart on an XYPlot.
 */
public class StackedXYBarChartDemo1 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public StackedXYBarChartDemo1(String title) {
        super(title);
        TableXYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private TableXYDataset createDataset() {
        
        DefaultTableXYDataset dataset = new DefaultTableXYDataset();
        
        XYSeries s1 = new XYSeries("Series 1", true, false);
        s1.add(1.0, 5.0);
        s1.add(2.0, 15.5);
        s1.add(3.0, 9.5);
        s1.add(4.0, 7.5);
        dataset.addSeries(s1);
        
        XYSeries s2 = new XYSeries("Series 2", true, false);
        s2.add(1, 5.0);
        s2.add(2, 15.5);
        s2.add(3, 9.5);
        s2.add(4, 3.5);
        dataset.addSeries(s2);
        
        return dataset;
        
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset for the chart.
     * 
     * @return a sample chart.
     */
    private JFreeChart createChart(TableXYDataset dataset) {

        NumberAxis domainAxis = new NumberAxis("X");
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        NumberAxis rangeAxis = new NumberAxis("Y");
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.10);
        renderer.setDrawBarOutline(false);

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        JFreeChart chart = new JFreeChart("Stacked XY Bar Chart Demo 1", plot);
        return chart;
        
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        StackedXYBarChartDemo1 demo = new StackedXYBarChartDemo1(
            "Stacked XY Bar Chart Demo 1"
        );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
