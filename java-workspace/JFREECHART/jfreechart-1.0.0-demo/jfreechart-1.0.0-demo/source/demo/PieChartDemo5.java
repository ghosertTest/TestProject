/* ------------------
 * PieChartDemo5.java
 * ------------------
 * (C) Copyright 2004, by Object Refinery Limited.
 *
 */

package demo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A pie chart with no section labels.
 */
public class PieChartDemo5 extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public PieChartDemo5(String title) {
        super(title);
        PieDataset dataset = createDataset();
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
    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("One", new Double(43.2));
        dataset.setValue("Two", new Double(10.0));
        dataset.setValue("Three", new Double(27.5));
        dataset.setValue("Four", new Double(17.5));
        dataset.setValue("Five", new Double(11.0));
        dataset.setValue("Six", new Double(19.4));
        return dataset;        
    }
       
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(PieDataset dataset) {
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Pie Chart Demo 5",  // chart title
            dataset,             // data
            false,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setInteriorGap(0.0);
        plot.setLabelGenerator(null);
        return chart;
        
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        PieChartDemo5 demo = new PieChartDemo5("Pie Chart Demo 5");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
