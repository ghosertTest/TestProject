/* ---------------------
 * ThermometerDemo1.java
 * ---------------------
 * (C) Copyright 2002-2004, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

/**
 * A simple demonstration application showing how to create a thermometer.
 */
public class ThermometerDemo1 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public ThermometerDemo1(String title) {
        super(title);
        JPanel chartPanel = createDemoPanel();
        setContentPane(chartPanel);
    }
    
    private static JFreeChart createChart(ValueDataset dataset) {
        ThermometerPlot plot = new ThermometerPlot(dataset);
        JFreeChart chart = new JFreeChart(
            "Thermometer Demo 1",  // chart title
            JFreeChart.DEFAULT_TITLE_FONT,
            plot,                  // plot
            false                  // no legend
        );               

        plot.setInsets(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setThermometerStroke(new BasicStroke(2.0f));
        plot.setThermometerPaint(Color.lightGray);
        plot.setUnits(ThermometerPlot.UNITS_FAHRENHEIT);
        return chart;       
    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     * 
     * @return A panel.
     */
    public static JPanel createDemoPanel() {
        JFreeChart chart = createChart(new DefaultValueDataset(new Double(43.0)));
        return new ChartPanel(chart);
    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        ThermometerDemo1 demo = new ThermometerDemo1("Thermometer Demo 1");
        demo.pack();
        demo.setVisible(true);

    }

}
