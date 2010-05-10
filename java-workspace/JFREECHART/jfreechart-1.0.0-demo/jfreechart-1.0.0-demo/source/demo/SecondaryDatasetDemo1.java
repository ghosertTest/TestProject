/* --------------------------
 * SecondaryDatasetDemo1.java
 * --------------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo showing the addition and removal of secondary datasets / renderers.
 */
public class SecondaryDatasetDemo1 extends ApplicationFrame 
                                   implements ActionListener {

    /** The plot. */
    private CategoryPlot plot;
 
    /** The index of the last dataset added. */
    private int secondaryDatasetIndex = 0;
    
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public SecondaryDatasetDemo1(String title) {

        super(title);
        CategoryDataset dataset1 = createRandomDataset("Series 1");
        JFreeChart chart = ChartFactory.createLineChart(
            "Secondary Dataset Demo 2", "Category", "Value", 
            dataset1, PlotOrientation.VERTICAL, true, true, false
        );
        chart.setBackgroundPaint(Color.white);
        
        this.plot = chart.getCategoryPlot();
        this.plot.setBackgroundPaint(Color.lightGray);
        this.plot.setDomainGridlinePaint(Color.white);
        this.plot.setRangeGridlinePaint(Color.white);
        this.plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        
        NumberAxis rangeAxis = (NumberAxis) this.plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);
        
        JPanel content = new JPanel(new BorderLayout());

        ChartPanel chartPanel = new ChartPanel(chart);
        content.add(chartPanel);
        
        JButton button1 = new JButton("Add Dataset");
        button1.setActionCommand("ADD_DATASET");
        button1.addActionListener(this);
        
        JButton button2 = new JButton("Remove Dataset");
        button2.setActionCommand("REMOVE_DATASET");
        button2.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        
        content.add(buttonPanel, BorderLayout.SOUTH);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);

    }

    /**
     * Creates a random dataset.
     * 
     * @param name  the series name.
     * 
     * @return A random dataset.
     */
    private CategoryDataset createRandomDataset(String name) {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        double value = 100.0;
        for (int i = 0; i < 10; i++) {
            String key = "T" + i;
            value = value * (1.0 + Math.random() / 100);
            result.addValue(value, name, key);
        }
        return result;
    }
    
    /**
     * Handles a click on the button by adding new (random) data.
     *
     * @param e  the action event.
     */
    public void actionPerformed(ActionEvent e) {
       
        if (e.getActionCommand().equals("ADD_DATASET")) {
            if (this.secondaryDatasetIndex < 20) {
                this.secondaryDatasetIndex++;
                this.plot.setDataset(
                    this.secondaryDatasetIndex, 
                    createRandomDataset("S" + this.secondaryDatasetIndex)
                );
                this.plot.setRenderer(
                    this.secondaryDatasetIndex, 
                    new LineAndShapeRenderer(true, false)
                );
            }
        }
        else if (e.getActionCommand().equals("REMOVE_DATASET")) {
            if (this.secondaryDatasetIndex > 0) {
                this.plot.setDataset(this.secondaryDatasetIndex, null);
                this.plot.setRenderer(this.secondaryDatasetIndex, null);
                this.secondaryDatasetIndex--;
            }
        }
        
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        SecondaryDatasetDemo1 demo = new SecondaryDatasetDemo1(
            "Secondary Dataset Demo 2"
        );
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
