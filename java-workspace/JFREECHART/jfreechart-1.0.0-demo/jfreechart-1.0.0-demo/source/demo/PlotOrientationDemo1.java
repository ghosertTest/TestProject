/* -------------------------
 * PlotOrientationDemo1.java
 * -------------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 * Changes
 * -------
 * 06-May-2004 : Version 1 (DG);
 * 20-Jul-2004 : Moved to premium demo collection (DG);
 *
 */

package demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYShapeAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo showing eight plots with various inverted axis and plot orientation 
 * combinations.
 */
public class PlotOrientationDemo1 extends ApplicationFrame {

    /** The number of charts. */
    private static int CHART_COUNT = 8;
    
    static class DemoPanel extends JPanel {

        /** The datasets. */
        private XYDataset[] datasets = new XYDataset[CHART_COUNT];
        
        /** The charts. */
        private JFreeChart[] charts = new JFreeChart[CHART_COUNT];
        
        /** The chart panels. */
        private ChartPanel[] panels = new ChartPanel[CHART_COUNT];
        
        /**
         * Creates a new self-contained demo panel.
         */
        public DemoPanel() {
            super(new GridLayout(2, 4));
            for (int i = 0; i < CHART_COUNT; i++) {
                this.datasets[i] = createDataset(i);
                this.charts[i] = createChart(i, this.datasets[i]);
                XYPlot plot = this.charts[i].getXYPlot();
                XYShapeAnnotation a1 = new XYShapeAnnotation(
                    new Rectangle2D.Double(1.0, 2.0, 2.0, 3.0), new BasicStroke(1.0f), Color.blue
                );
                XYLineAnnotation a2 = new XYLineAnnotation(0.0, -5.0, 10.0, -5.0);
                XYImageAnnotation a3 = new XYImageAnnotation(5.0, 2.0, JFreeChart.INFO.getLogo());
                plot.addAnnotation(a1);
                plot.addAnnotation(a2);
                plot.addAnnotation(a3);
                plot.setQuadrantPaint(0, new Color(230, 230, 255));
                plot.setQuadrantPaint(1, new Color(230, 255, 230));
                plot.setQuadrantPaint(2, new Color(255, 230, 230));
                plot.setQuadrantPaint(3, new Color(255, 230, 255));
                this.panels[i] = new ChartPanel(this.charts[i]);
            }
            this.charts[1].getXYPlot().getDomainAxis().setInverted(true);
            this.charts[2].getXYPlot().getRangeAxis().setInverted(true);
            this.charts[3].getXYPlot().getDomainAxis().setInverted(true);
            this.charts[3].getXYPlot().getRangeAxis().setInverted(true);

            this.charts[5].getXYPlot().getDomainAxis().setInverted(true);
            this.charts[6].getXYPlot().getRangeAxis().setInverted(true);
            this.charts[4].getXYPlot().getDomainAxis().setInverted(true);
            this.charts[4].getXYPlot().getRangeAxis().setInverted(true);
            
            this.charts[4].getXYPlot().setOrientation(PlotOrientation.HORIZONTAL);
            this.charts[5].getXYPlot().setOrientation(PlotOrientation.HORIZONTAL);
            this.charts[6].getXYPlot().setOrientation(PlotOrientation.HORIZONTAL);
            this.charts[7].getXYPlot().setOrientation(PlotOrientation.HORIZONTAL);

            add(this.panels[0]);
            add(this.panels[1]);
            add(this.panels[4]);
            add(this.panels[5]);
            add(this.panels[2]);
            add(this.panels[3]);
            add(this.panels[6]);
            add(this.panels[7]);
            setPreferredSize(new Dimension(800, 600));
        }
    }
    
    /**
     * Creates a new demo instance.
     * 
     * @param title  the frame title.
     */
    public PlotOrientationDemo1(String title) {

        super(title);
        setContentPane(createDemoPanel());

    }
    
    /**
     * Creates a sample dataset.
     * 
     * @param index  the dataset index.
     * 
     * @return A dataset.
     */
    private static XYDataset createDataset(int index) {
        XYSeries series1 = new XYSeries("Series " + (index + 1));
        series1.add(-10.0, -5.0);
        series1.add(10.0, 5.0);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        return dataset;
    }
    
    /**
     * Creates a sample chart.
     * 
     * @param index  the chart index.
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(int index, XYDataset dataset) {
        
        // create the chart...
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Chart " + (index + 1),   // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            false,                    // include legend
            false,                    // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
       
        XYLineAndShapeRenderer renderer 
            = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(true);
        renderer.setShapesFilled(true);
        // change the auto tick unit selection to integer units only...
        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
        
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        return new DemoPanel();
    }
    
    /**
     * The starting point for the demo.
     * 
     * @param args  ignored.
     */
    public static void main(String[] args) {
        PlotOrientationDemo1 demo = new PlotOrientationDemo1(
            "Plot Orientation Demo"
        );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
