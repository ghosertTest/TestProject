/* ------------------
 * ImageMapDemo7.java
 * ------------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 */

package demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.imagemap.ImageMapUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;

/**
 * A demo showing how to create an HTML image map for a scatter plot.
 */
public class ImageMapDemo7 {

    /**
     * Default constructor.
     */
    public ImageMapDemo7() {
        super();
    }

    /**
     * Starting point for the demo.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        XYDataset data = new SampleXYDataset2();
        JFreeChart chart = ChartFactory.createScatterPlot(
            "Scatter Plot Demo",
            "X", "Y", 
            data, 
            PlotOrientation.VERTICAL,
            true, 
            true, 
            false
        );
        NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        chart.setBackgroundPaint(java.awt.Color.white);

        // save it to an image
        try {
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            File file1 = new File("scatter100.png");
            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);

            // write an HTML page incorporating the image with an image map
            File file2 = new File("scatter100.html");
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file2));
            PrintWriter writer = new PrintWriter(out);
            writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
            writer.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"en\" xml:lang=\"en\">");
            writer.println("<head><title>JFreeChart Image Map Demo</title></head>");
            writer.println("<body><p>");
            ImageMapUtilities.writeImageMap(writer, "chart", info);
            writer.println("<img src=\"scatter100.png\" "
                           + "width=\"600\" height=\"400\" usemap=\"#chart\" alt=\"scatter100.png\"/>");
            writer.println("</p></body>");
            writer.println("</html>");
            writer.close();

        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

    }

}
