/* --------------------
 * ParetoChartDemo.java
 * --------------------
 * (C) Copyright 2003-2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Color;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.DataUtilities;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.SortOrder;

/**
 * A demo showing the creation of a pareto chart.
 */
public class ParetoChartDemo extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public ParetoChartDemo(String title) {

        super(title);

        DefaultKeyedValues data = new DefaultKeyedValues();
        data.addValue("C", new Integer(4843));
        data.addValue("C++", new Integer(2098));
        data.addValue("C#", new Integer(26));
        data.addValue("Java", new Integer(1901));
        data.addValue("Perl", new Integer(2507));
        data.addValue("PHP", new Integer(1689));
        data.addValue("Python", new Integer(948));
        data.addValue("Ruby", new Integer(100));
        data.addValue("SQL", new Integer(263));
        data.addValue("Unix Shell", new Integer(485));

        data.sortByValues(SortOrder.DESCENDING);
        KeyedValues cumulative = DataUtilities.getCumulativePercentages(data);
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
            "Languages", data
        );

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "Freshmeat Software Projects",  // chart title
            "Language",                     // domain axis label
            "Projects",                     // range axis label
            dataset,                        // data
            PlotOrientation.VERTICAL,
            true,                           // include legend
            true,
            false
        );

        chart.addSubtitle(new TextTitle("By Programming Language"));
        chart.addSubtitle(new TextTitle("As at 5 March 2003"));
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        LineAndShapeRenderer renderer2 = new LineAndShapeRenderer();

        CategoryDataset dataset2 = DatasetUtilities.createCategoryDataset(
            "Cumulative", cumulative
        );
        NumberAxis axis2 = new NumberAxis("Percent");
        axis2.setNumberFormatOverride(NumberFormat.getPercentInstance());
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, renderer2);
        plot.mapDatasetToRangeAxis(1, 1);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);
        // OPTIONAL CUSTOMISATION COMPLETED.

        // add the chart to a panel...
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(550, 270));
        setContentPane(chartPanel);

    }
    
    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {

        ParetoChartDemo demo = new ParetoChartDemo("Pareto Chart Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
