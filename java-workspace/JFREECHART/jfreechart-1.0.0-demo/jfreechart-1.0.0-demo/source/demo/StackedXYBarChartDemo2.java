/* ---------------------------
 * StackedXYBarChartDemo2.java
 * ---------------------------
 * (C) Copyright 2005, by Object Refinery Limited.
 *
 */

package demo;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeTableXYDataset;
import org.jfree.data.xy.TableXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A simple stacked bar chart using time series data.
 */
public class StackedXYBarChartDemo2 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public StackedXYBarChartDemo2(String title) {
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
        
        TimeTableXYDataset dataset = new TimeTableXYDataset();
        
        Day d1 = new Day(1, 3, 2005);
        Day d2 = new Day(2, 3, 2005);
        Day d3 = new Day(3, 3, 2005);
        Day d4 = new Day(4, 3, 2005);
        Day d5 = new Day(5, 3, 2005);
        
        dataset.add(d1, 1.0, "Series 1");
        dataset.add(d2, 1.7, "Series 1");
        dataset.add(d3, 2.3, "Series 1");
        dataset.add(d4, 3.7, "Series 1");
        dataset.add(d5, 2.6, "Series 1");

        dataset.add(d1, 3.2, "Series 2");
        dataset.add(d2, 1.1, "Series 2");
        dataset.add(d3, 1.4, "Series 2");
        dataset.add(d4, 2.9, "Series 2");
        dataset.add(d5, 0.6, "Series 2");
        
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

        DateAxis domainAxis = new DateAxis("Date");
        domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        NumberAxis rangeAxis = new NumberAxis("Y");
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.10);
        renderer.setDrawBarOutline(false);

        XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        JFreeChart chart = new JFreeChart("Stacked XY Bar Chart Demo 2", plot);
        return chart;
        
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        StackedXYBarChartDemo2 demo = new StackedXYBarChartDemo2(
            "Stacked XY Bar Chart Demo 2"
        );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
