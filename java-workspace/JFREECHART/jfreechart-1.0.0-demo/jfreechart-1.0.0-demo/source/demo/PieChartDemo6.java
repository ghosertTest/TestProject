/* ------------------
 * PieChartDemo6.java
 * ------------------
 * (C) Copyright 2004, by Object Refinery Limited.
 *
 */

package demo;

import java.text.AttributedString;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A pie chart with a custom label generator.
 */
public class PieChartDemo6 extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public PieChartDemo6(String title) {
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
            "Pie Chart Demo 6",  // chart title
            dataset,             // data
            false,               // include legend
            true,
            false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(new CustomLabelGenerator());
        return chart;
        
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        PieChartDemo6 demo = new PieChartDemo6("Pie Chart Demo 6");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }
    
    /**
     * A custom label generator (returns null for one item as a test).
     */
    static class CustomLabelGenerator implements PieSectionLabelGenerator {
        
        /**
         * Generates a label for a pie section.
         * 
         * @param dataset  the dataset (<code>null</code> not permitted).
         * @param key  the section key (<code>null</code> not permitted).
         * 
         * @return the label (possibly <code>null</code>).
         */
        public String generateSectionLabel(PieDataset dataset, Comparable key) {
            String result = null;    
            if (dataset != null) {
                if (!key.equals("Two")) {
                    result = key.toString();   
                }
            }
            return result;
        }
        
        public AttributedString generateAttributedSectionLabel(
                PieDataset dataset, Comparable key) {
            return null;
        }
   
    }

}
