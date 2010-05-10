/* ------------------------
 * PopulationChartDemo.java
 * ------------------------
 * (C) Copyright 2003, 2004, by Object Refinery Limited.
 *
 */

package demo;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.general.KeyedValues2DDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A population pyramid demo.
 */
public class PopulationChartDemo1 extends ApplicationFrame {

    /**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public PopulationChartDemo1(String title) {

        super(title);
        CategoryDataset dataset = createDataset();

        // create the chart...
        JFreeChart chart = ChartFactory.createStackedBarChart(
            "Population Chart Demo",
            "Age Group",     // domain axis label
            "Population (millions)", // range axis label
            dataset,         // data
            PlotOrientation.HORIZONTAL,
            true,            // include legend
            true,            // tooltips
            false            // urls
        );

        // add the chart to a panel...
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    /**
     * Creates a dataset.
     * 
     * @return A dataset.
     */
    private KeyedValues2DDataset createDataset() {

        DefaultKeyedValues2DDataset data = new DefaultKeyedValues2DDataset();
        data.addValue(-6.0, "Male", "70+");
        data.addValue(-8.0, "Male", "60-69");
        data.addValue(-11.0, "Male", "50-59");
        data.addValue(-13.0, "Male", "40-49");
        data.addValue(-14.0, "Male", "30-39");
        data.addValue(-15.0, "Male", "20-29");
        data.addValue(-19.0, "Male", "10-19");
        data.addValue(-21.0, "Male", "0-9");
        data.addValue(10.0, "Female", "70+");
        data.addValue(12.0, "Female", "60-69");
        data.addValue(13.0, "Female", "50-59");
        data.addValue(14.0, "Female", "40-49");
        data.addValue(15.0, "Female", "30-39");
        data.addValue(17.0, "Female", "20-29");
        data.addValue(19.0, "Female", "10-19");
        data.addValue(20.0, "Female", "0-9");
        return data;

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        PopulationChartDemo1 demo = new PopulationChartDemo1("Population Chart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
