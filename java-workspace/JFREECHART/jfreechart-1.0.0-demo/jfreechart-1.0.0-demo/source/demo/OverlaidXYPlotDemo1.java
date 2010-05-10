/* -----------------------
 * OverlaidXYPlotDemo.java
 * -----------------------
 * (C) Copyright 2002-2004, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.date.MonthConstants;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demonstration application showing a time series chart overlaid with a vertical XY bar chart.
 */
public class OverlaidXYPlotDemo1 extends ApplicationFrame {

    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public OverlaidXYPlotDemo1(String title) {

        super(title);
        JFreeChart chart = createOverlaidChart();
        ChartPanel panel = new ChartPanel(chart, true, true, true, true, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(panel);

    }

    /**
     * Creates an overlaid chart.
     *
     * @return The chart.
     */
    private JFreeChart createOverlaidChart() {

        // create plot ...
        IntervalXYDataset data1 = createDataset1();
        XYItemRenderer renderer1 = new XYBarRenderer(0.20);
        renderer1.setToolTipGenerator(
            new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")
            )
        );
        DateAxis domainAxis = new DateAxis("Date");
        domainAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        ValueAxis rangeAxis = new NumberAxis("Value");
        XYPlot plot = new XYPlot(data1, domainAxis, rangeAxis, renderer1);
        double x = new Day(9, MonthConstants.MARCH, 2002).getMiddleMillisecond();
        XYTextAnnotation annotation = new XYTextAnnotation("Hello!", x, 10000.0);
        annotation.setFont(new Font("SansSerif", Font.PLAIN, 9));
        plot.addAnnotation(annotation);

        // add a second dataset and renderer...
        XYDataset data2 = createDataset2();
        XYItemRenderer renderer2 = new StandardXYItemRenderer();
        renderer2.setToolTipGenerator(
            new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")
            )
        );
        plot.setDataset(1, data2);
        plot.setRenderer(1, renderer2);
        
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // return a new chart containing the overlaid plot...
        return new JFreeChart("Overlaid Plot Example", JFreeChart.DEFAULT_TITLE_FONT, plot, true);

    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private IntervalXYDataset createDataset1() {

        // create dataset 1...
        TimeSeries series1 = new TimeSeries("Series 1", Day.class);
        series1.add(new Day(1, MonthConstants.MARCH, 2002), 12353.3);
        series1.add(new Day(2, MonthConstants.MARCH, 2002), 13734.4);
        series1.add(new Day(3, MonthConstants.MARCH, 2002), 14525.3);
        series1.add(new Day(4, MonthConstants.MARCH, 2002), 13984.3);
        series1.add(new Day(5, MonthConstants.MARCH, 2002), 12999.4);
        series1.add(new Day(6, MonthConstants.MARCH, 2002), 14274.3);
        series1.add(new Day(7, MonthConstants.MARCH, 2002), 15943.5);
        series1.add(new Day(8, MonthConstants.MARCH, 2002), 14845.3);
        series1.add(new Day(9, MonthConstants.MARCH, 2002), 14645.4);
        series1.add(new Day(10, MonthConstants.MARCH, 2002), 16234.6);
        series1.add(new Day(11, MonthConstants.MARCH, 2002), 17232.3);
        series1.add(new Day(12, MonthConstants.MARCH, 2002), 14232.2);
        series1.add(new Day(13, MonthConstants.MARCH, 2002), 13102.2);
        series1.add(new Day(14, MonthConstants.MARCH, 2002), 14230.2);
        series1.add(new Day(15, MonthConstants.MARCH, 2002), 11235.2);

        return new TimeSeriesCollection(series1);

    }

    /**
     * Creates a sample dataset.
     *
     * @return The dataset.
     */
    private XYDataset createDataset2() {

        // create dataset 2...
        TimeSeries series2 = new TimeSeries("Series 2", Day.class);

        series2.add(new Day(3, MonthConstants.MARCH, 2002), 16853.2);
        series2.add(new Day(4, MonthConstants.MARCH, 2002), 19642.3);
        series2.add(new Day(5, MonthConstants.MARCH, 2002), 18253.5);
        series2.add(new Day(6, MonthConstants.MARCH, 2002), 15352.3);
        series2.add(new Day(7, MonthConstants.MARCH, 2002), 13532.0);
        series2.add(new Day(8, MonthConstants.MARCH, 2002), 12635.3);
        series2.add(new Day(9, MonthConstants.MARCH, 2002), 13998.2);
        series2.add(new Day(10, MonthConstants.MARCH, 2002), 11943.2);
        series2.add(new Day(11, MonthConstants.MARCH, 2002), 16943.9);
        series2.add(new Day(12, MonthConstants.MARCH, 2002), 17843.2);
        series2.add(new Day(13, MonthConstants.MARCH, 2002), 16495.3);
        series2.add(new Day(14, MonthConstants.MARCH, 2002), 17943.6);
        series2.add(new Day(15, MonthConstants.MARCH, 2002), 18500.7);
        series2.add(new Day(16, MonthConstants.MARCH, 2002), 19595.9);

        TimeSeriesCollection tsc = new TimeSeriesCollection(series2);
        return tsc;

    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        OverlaidXYPlotDemo1 demo = new OverlaidXYPlotDemo1("Overlaid XYPlot Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
