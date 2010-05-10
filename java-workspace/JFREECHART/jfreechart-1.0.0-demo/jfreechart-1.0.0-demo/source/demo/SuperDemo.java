/* --------------
 * SuperDemo.java
 * --------------
 * (C) Copyright 2004, 2005, by Object Refinery Limited.
 *
 */

package demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo for JFreeChart.
 */
public class SuperDemo extends ApplicationFrame 
                       implements ActionListener, TreeSelectionListener {

    /** Exit action command. */
    public static final String EXIT_COMMAND = "EXIT";

    private JPanel displayPanel;
    
    private JPanel chartContainer;
    
    private JPanel descriptionContainer;
    
    private JTextPane descriptionPane;
    
    /**
     * Creates a new demo instance.
     * 
     * @param title  the frame title.
     */
    public SuperDemo(String title) {
        super(title);
        setContentPane(createContent());
        setJMenuBar(createMenuBar());
    }

    /**
     * Creates a panel for the main window.
     * 
     * @return A panel.
     */
    private JComponent createContent() {
        JPanel content = new JPanel(new BorderLayout());
        
        JTabbedPane tabs = new JTabbedPane();
        JPanel content1 = new JPanel(new BorderLayout());
        content1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        JTree tree = new JTree(createTreeModel());
        tree.addTreeSelectionListener(this);
        splitter.setLeftComponent(new JScrollPane(tree));
        splitter.setRightComponent(createChartDisplayPanel());
        content1.add(splitter);
        tabs.add("Demos", content1);
        MemoryUsageDemo memUse = new MemoryUsageDemo(300000);
        memUse.new DataGenerator(1000).start();
        tabs.add("Memory Usage", memUse);
        tabs.add("Source Code", createSourceCodePanel());
        tabs.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        content.add(tabs);
        return content;
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // first the file menu
        JMenu fileMenu = new JMenu("File", true);
        fileMenu.setMnemonic('F');

        JMenuItem exitItem = new JMenuItem("Exit", 'x');
        exitItem.setActionCommand(EXIT_COMMAND);
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);

        // finally, glue together the menu and return it
        menuBar.add(fileMenu);

        return menuBar;
    }
    
    private JPanel createSourceCodePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        java.net.URL sourceURL = SuperDemo.class.getResource("source.html");
        if (sourceURL != null) {
            try {
                editorPane.setPage(sourceURL);
            } 
            catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + sourceURL);
            }
        } 
        else {
            System.err.println("Couldn't find file: source.html");
        }

        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));
        
        panel.add(editorScrollPane);
        return panel;
    }
    
    /**
     * Handles menu selections by passing control to an appropriate method.
     *
     * @param event  the event.
     */
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();
        if (command.equals(EXIT_COMMAND)) {
            attemptExit();
        }
    }
    
    /**
     * Exits the application, but only if the user agrees.
     */
    private void attemptExit() {

        String title = "Confirm";
        String message = "Are you sure you want to exit the demo?";
        int result = JOptionPane.showConfirmDialog(
            this, message, title, JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }


    private JPanel createChartDisplayPanel() {
         
        this.displayPanel = new JPanel(new BorderLayout());
        this.chartContainer = new JPanel(new BorderLayout());
        this.chartContainer.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createLineBorder(Color.black)
            )
        );
        this.chartContainer.add(createNoDemoSelectedPanel());
        this.descriptionContainer = new JPanel(new BorderLayout());
        this.descriptionContainer.setBorder(
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        );
        this.descriptionContainer.setPreferredSize(new Dimension(600, 140));
        this.descriptionPane = new JTextPane();
        this.descriptionPane.setEditable(false);
        JScrollPane scroller = new JScrollPane(
            this.descriptionPane, 
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
        );
        this.descriptionContainer.add(scroller);
        displayDescription("select.html");
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitter.setTopComponent(this.chartContainer);
        splitter.setBottomComponent(this.descriptionContainer);
        this.displayPanel.add(splitter);
        splitter.setDividerLocation(0.75);
        return this.displayPanel;
    }
    
    /**
     * Creates a <code>TreeModel</code> with references to all the individual 
     * demo applications.  This is an ugly piece of hard-coding but, hey, it's
     * just a demo!
     * 
     * @return A TreeModel.
     */
    private TreeModel createTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("JFreeChart");
        root.add(createPieChartsNode());
        root.add(createBarChartsNode());
        root.add(createLineChartsNode());
        root.add(createAreaChartsNode());
        root.add(createTimeSeriesChartsNode());
        root.add(createFinancialChartsNode());
        root.add(createXYChartsNode());
        root.add(createMeterChartsNode());
        root.add(createMultipleAxisChartsNode());
        root.add(createCombinedAxisChartsNode());
        root.add(createGanttChartsNode());
        root.add(createMiscellaneousChartsNode());
        return new DefaultTreeModel(root);
    }
    
    /**
     * Creates the tree node for the pie chart demos.
     * 
     * @return A populated tree node.
     */
    private MutableTreeNode createPieChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Pie Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PieChartDemo1", "PieChartDemo1.java")
        );  
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PieChartDemo2", "PieChartDemo2.java")
        );            
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PieChartDemo3", "PieChartDemo3.java")
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PieChart3DDemo1", "PieChart3DDemo1.java")
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
                new DemoDescription("demo.PieChart3DDemo2", "PieChart3DDemo2.java")
            );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PieChart3DDemo3", "PieChart3DDemo3.java")
        );  
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.MultiplePieChartDemo1", "MultiplePieChartDemo1.java"
            )
        );
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.RingChartDemo1", "RingChartDemo1.java"
            )
        );
                
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        root.add(n8);
        return root;
    }
    
    /**
     * Creates a tree node containing sample bar charts.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createBarChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Bar Charts");            
        root.add(createCategoryBarChartsNode());
        root.add(createXYBarChartsNode());
        return root;        
    }
    
    /**
     * Creates a tree node containing bar charts based on the 
     * {@link CategoryPlot} class.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createCategoryBarChartsNode() {
        DefaultMutableTreeNode root 
            = new DefaultMutableTreeNode("CategoryPlot");
        
        MutableTreeNode n1 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo1", "BarChartDemo1.java"));                
        MutableTreeNode n2 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo2", "BarChartDemo2.java"));                
        MutableTreeNode n3 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo3", "BarChartDemo3.java"));                
        MutableTreeNode n4 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo4", "BarChartDemo4.java"));                
        MutableTreeNode n5 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo5", "BarChartDemo5.java"));                
        MutableTreeNode n6 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo6", "BarChartDemo6.java"));                
        MutableTreeNode n7 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo7", "BarChartDemo7.java"));                
        MutableTreeNode n8 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo8", "BarChartDemo8.java"));                
        MutableTreeNode n9 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo9", "BarChartDemo9.java"));                
        MutableTreeNode n10 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChartDemo10", "BarChartDemo10.java"));                
        MutableTreeNode n11 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChart3DDemo1", "BarChart3DDemo1.java"));                
        MutableTreeNode n12 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChart3DDemo2", "BarChart3DDemo2.java"));                
        MutableTreeNode n13 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.BarChart3DDemo3", "BarChart3DDemo3.java"));
        MutableTreeNode n14 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.IntervalBarChartDemo1", "IntervalBarChartDemo1.java"));
        MutableTreeNode n15 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.LayeredBarChartDemo1", "LayeredBarChartDemo1.java"));
        MutableTreeNode n16 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.LayeredBarChartDemo2", "LayeredBarChartDemo2.java"));
        MutableTreeNode n17 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.StackedBarChartDemo1", "StackedBarChartDemo1.java"));
        MutableTreeNode n18 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.StackedBarChartDemo2", "StackedBarChartDemo2.java"));
        MutableTreeNode n19 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.StackedBarChartDemo3", "StackedBarChartDemo3.java"));
        MutableTreeNode n20 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.StackedBarChartDemo4", "StackedBarChartDemo4.java"));
        MutableTreeNode n21 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.StatisticalBarChartDemo1", 
                "StatisticalBarChartDemo1.java"));
        MutableTreeNode n22 = new DefaultMutableTreeNode(new DemoDescription(
                "demo.WaterfallChartDemo1", "WaterfallChartDemo1.java"));
            
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        root.add(n8);
        root.add(n9);
        root.add(n10);
        root.add(n11);
        root.add(n12);
        root.add(n13);
        root.add(n14);
        root.add(n15);
        root.add(n16);
        root.add(n17);
        root.add(n18);
        root.add(n19);
        root.add(n20);
        root.add(n21);
        root.add(n22);
        
        return root;        
    }
    
    private MutableTreeNode createXYBarChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("XYPlot");
        
        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYBarChartDemo1", "XYBarChartDemo1.java")
        );                
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYBarChartDemo2", "XYBarChartDemo2.java")
        );                
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYBarChartDemo3", "XYBarChartDemo3.java")
        );                
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYBarChartDemo4", "XYBarChartDemo4.java")
        );                
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        
        return root;
    }
    
    /**
     * Creates a tree node containing line chart items.
     * 
     * @return A tree node.
     */
    private MutableTreeNode createLineChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Line Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.AnnotationDemo1", "AnnotationDemo1.java"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.LineChartDemo1", "LineChartDemo1.java"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.LineChartDemo2", "LineChartDemo2.java"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.LineChartDemo3", "LineChartDemo3.java"));
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("demo.LineChartDemo4", "LineChartDemo4.java"));
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription("demo.LineChartDemo5", "LineChartDemo5.java"));
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription("demo.LineChartDemo6", "LineChartDemo6.java"));
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription("demo.NormalDistributionDemo", 
                "NormalDistributionDemo.java"));
        DefaultMutableTreeNode n9 = new DefaultMutableTreeNode(
            new DemoDescription("demo.StatisticalLineChartDemo1", 
                "StatisticalLineChartDemo1.java"));
        DefaultMutableTreeNode n10 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYStepRendererDemo1", 
                "XYStepRendererDemo1.java"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        root.add(n8);
        root.add(n9);
        root.add(n10);
        
        return root;
    }
    
    /**
     * A node for various area charts.
     * 
     * @return The node.
     */
    private MutableTreeNode createAreaChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Area Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.AreaChartDemo1", "AreaChartDemo1.java"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.StackedXYAreaChartDemo1", 
                "StackedXYAreaChartDemo1.java"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.StackedXYAreaChartDemo2", 
                "StackedXYAreaChartDemo2.java"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYAreaChartDemo1", 
                "XYAreaChartDemo1.java"));
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYAreaChartDemo2", 
                "XYAreaChartDemo2.java"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        
        return root;
    }
    
    /**
     * Creates a sub-tree for the time series charts.
     * 
     * @return The root node for the subtree.
     */
    private MutableTreeNode createTimeSeriesChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Time Series Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo1", "TimeSeriesDemo1.java"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo2", "TimeSeriesDemo2.java"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo3", "TimeSeriesDemo3.java"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo4", "TimeSeriesDemo4.java"));
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo5", "TimeSeriesDemo5.java"));
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo6", "TimeSeriesDemo6.java"));
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo7", "TimeSeriesDemo7.java"));
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo8", "TimeSeriesDemo8.java"));
        DefaultMutableTreeNode n9 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo9", "TimeSeriesDemo9.java"));
        DefaultMutableTreeNode n10 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo10", 
                    "TimeSeriesDemo10.java"));
        DefaultMutableTreeNode n11 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo11", 
                    "TimeSeriesDemo11.java"));
        DefaultMutableTreeNode n12 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo12", 
                    "TimeSeriesDemo12.java"));
        DefaultMutableTreeNode n13 = new DefaultMutableTreeNode(
            new DemoDescription("demo.TimeSeriesDemo13", 
                    "TimeSeriesDemo13.java"));
        DefaultMutableTreeNode n14 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PeriodAxisDemo1", "PeriodAxisDemo1.java"));
        DefaultMutableTreeNode n15 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PeriodAxisDemo2", "PeriodAxisDemo2.java"));
        DefaultMutableTreeNode n16 = new DefaultMutableTreeNode(
                new DemoDescription("demo.DynamicDataDemo1", 
                "DynamicDataDemo1.java"));
        DefaultMutableTreeNode n17 = new DefaultMutableTreeNode(
                new DemoDescription("demo.DynamicDataDemo2", 
                "DynamicDataDemo2.java"));
        DefaultMutableTreeNode n18 = new DefaultMutableTreeNode(
                new DemoDescription("demo.DynamicDataDemo3", 
                "DynamicDataDemo3.java"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        root.add(n8);
        root.add(n9);
        root.add(n10);
        root.add(n11);
        root.add(n12);
        root.add(n13);
        root.add(n14);
        root.add(n15);
        root.add(n16);
        root.add(n17);
        root.add(n18);
        
        return root;
    }
    
    /**
     * Creates a node for the tree model that contains financial charts.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createFinancialChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                "Financial Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("demo.CandlestickChartDemo1", 
                "CandlestickChartDemo1.java"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("demo.HighLowChartDemo1", 
                "HighLowChartDemo1.java"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("demo.HighLowChartDemo2", 
                "HighLowChartDemo2.java"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
                new DemoDescription("demo.PriceVolumeDemo1", 
                "PriceVolumeDemo1.java"));
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
                new DemoDescription("demo.YieldCurveDemo", 
                "YieldCurveDemo.java"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        return root;
    }

    private MutableTreeNode createXYChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("XY Charts");
        
        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.XYLineAndShapeRendererDemo1", 
                "XYLineAndShapeRendererDemo1.java"
            )
        );                
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYSeriesDemo1", "XYSeriesDemo1.java")
        );                
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYSeriesDemo2", "XYSeriesDemo2.java")
        );                
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.XYSeriesDemo3", "XYSeriesDemo3.java")
        );                
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("demo.WindChartDemo1", "WindChartDemo1.java")
        );                
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        
        return root;
    }

    /**
     * Creates a node for the tree model that contains "meter" charts.
     * 
     * @return The tree node.
     */
    private MutableTreeNode createMeterChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
                "Meter Charts");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("demo.MeterChartDemo1", 
                "MeterChartDemo1.java"));
        
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("demo.MeterChartDemo2", 
                "MeterChartDemo2.java"));
        
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("demo.MeterChartDemo4", 
                "MeterChartDemo4.java"));
        
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
                new DemoDescription("demo.ThermometerDemo1", 
                "ThermometerDemo1.java"));
       
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        return root;
    }

    private MutableTreeNode createMultipleAxisChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Multiple Axis Charts"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.DualAxisDemo1", "DualAxisDemo1.java")
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.DualAxisDemo2", "DualAxisDemo2.java")
        );
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.DualAxisDemo3", "DualAxisDemo3.java")
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.DualAxisDemo4", "DualAxisDemo4.java")
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription("demo.DualAxisDemo5", "DualAxisDemo5.java")
        );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.MultipleAxisDemo1", "MultipleAxisDemo1.java"
            )
        );
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        
        return root;
    }
    
    private MutableTreeNode createCombinedAxisChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Combined Axis Charts"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CombinedCategoryPlotDemo1", 
                "CombinedCategoryPlotDemo1.java"
            )
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CombinedCategoryPlotDemo2", 
                "CombinedCategoryPlotDemo2.java"
            )
        );
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CombinedTimeSeriesDemo1", 
                "CombinedTimeSeriesDemo1.java"
            )
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CombinedXYPlotDemo1", 
                "CombinedXYPlotDemo1.java"
            )
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CombinedXYPlotDemo2", 
                "CombinedXYPlotDemo2.java"
            )
        );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CombinedXYPlotDemo3", 
                "CombinedXYPlotDemo3.java"
            )
        );
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CombinedXYPlotDemo4", 
                "CombinedXYPlotDemo4.java"
            )
        );

        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        
        return root;
    }

    private MutableTreeNode createGanttChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Gantt Charts"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.GanttDemo1", "GanttDemo1.java")
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.GanttDemo2", "GanttDemo2.java")
        );
        
        root.add(n1);
        root.add(n2);
        
        return root;
    }
    
    /**
     * Creates the subtree containing the miscellaneous chart types.
     * 
     * @return A subtree.
     */
    private MutableTreeNode createMiscellaneousChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Miscellaneous");

        DefaultMutableTreeNode n0 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.BoxAndWhiskerChartDemo1", "BoxAndWhiskerChartDemo1.java"
            )
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.BubbleChartDemo1", "BubbleChartDemo1.java"
            )
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CategoryStepChartDemo1", "CategoryStepChartDemo1.java"
            )
        );
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.CompassDemo1", "CompassDemo1.java")
        );
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CompassFormatDemo1", "CompassFormatDemo1.java"
            )
        );
        DefaultMutableTreeNode n5 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.CompassFormatDemo2", "CompassFormatDemo2.java"
            )
        );
        DefaultMutableTreeNode n6 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.DifferenceChartDemo1", "DifferenceChartDemo1.java"
            )
        );
        DefaultMutableTreeNode n7 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.DifferenceChartDemo2", "DifferenceChartDemo2.java"
            )
        );
        DefaultMutableTreeNode n8 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.EventFrequencyDemo1", "EventFrequencyDemo1.java"
            )
        );

        DefaultMutableTreeNode n9 = new DefaultMutableTreeNode(
            new DemoDescription("demo.HideSeriesDemo1", "HideSeriesDemo1.java")
        );
        
        DefaultMutableTreeNode n10 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.OverlaidBarChartDemo1", "OverlaidBarChartDemo1.java"
            )
        );
        
        DefaultMutableTreeNode n11 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.OverlaidBarChartDemo2", "OverlaidBarChartDemo2.java"
            )
        );
        
        DefaultMutableTreeNode n12 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.SpiderWebChartDemo1", "SpiderWebChartDemo1.java"
            )
        );
        DefaultMutableTreeNode n13 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.SymbolAxisDemo1", "SymbolAxisDemo1.java"
            )
        );
        DefaultMutableTreeNode n14 = new DefaultMutableTreeNode(
            new DemoDescription("demo.PolarChartDemo1", "PolarChartDemo1.java")
        );
        DefaultMutableTreeNode n15 = new DefaultMutableTreeNode(
                new DemoDescription("demo.YIntervalChartDemo1", 
                "YIntervalChartDemo1.java"));
        
        root.add(createCrosshairChartsNode());
        root.add(createItemLabelsNode());
        root.add(createLegendNode());
        root.add(createMarkersNode());
        root.add(createOrientationNode());
        root.add(n0);
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        root.add(n5);
        root.add(n6);
        root.add(n7);
        root.add(n8);
        root.add(n9);
        root.add(n10);
        root.add(n11);
        root.add(n12);
        root.add(n13);
        root.add(n14);
        root.add(n15);
        
        return root;
    }
    
    private MutableTreeNode createCrosshairChartsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Crosshairs");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("demo.CrosshairDemo1", 
                "CrosshairDemo1.java"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("demo.CrosshairDemo2", 
                "CrosshairDemo2.java"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("demo.CrosshairDemo3", 
                "CrosshairDemo3.java"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
                new DemoDescription("demo.CrosshairDemo4", 
                "CrosshairDemo4.java"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        
        return root;
    }
    
    private MutableTreeNode createItemLabelsNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Item Labels");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("demo.ItemLabelDemo1", 
                "ItemLabelDemo1.java"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
                new DemoDescription("demo.ItemLabelDemo2", 
                "ItemLabelDemo2.java"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
                new DemoDescription("demo.ItemLabelDemo3", 
                "ItemLabelDemo3.java"));
        
        root.add(n1);
        root.add(n2);
        root.add(n3);
        
        return root;
    }
    
    private MutableTreeNode createLegendNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Legends");

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
                new DemoDescription("demo.LegendWrapperDemo1", 
                        "LegendWrapperDemo1.java"));
        
        root.add(n1);
        
        return root;
    }
    
    private MutableTreeNode createMarkersNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Markers");
        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription("demo.CategoryMarkerDemo1", 
            "CategoryMarkerDemo1.java"));
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription("demo.CategoryMarkerDemo2", 
            "CategoryMarkerDemo2.java"));
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode(
            new DemoDescription("demo.MarkerDemo1", "MarkerDemo1.java"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode(
            new DemoDescription("demo.MarkerDemo2", "MarkerDemo2.java"));
        root.add(n1);
        root.add(n2);
        root.add(n3);
        root.add(n4);
        return root;
    }
    
    private MutableTreeNode createOrientationNode() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            "Plot Orientation"
        );

        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.PlotOrientationDemo1", "PlotOrientationDemo1.java"
            )
        );
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode(
            new DemoDescription(
                "demo.PlotOrientationDemo2", "PlotOrientationDemo2.java"
            )
        );
       
        root.add(n1);
        root.add(n2);
        
        return root;
    }
    
    private void displayDescription(String fileName) {
        java.net.URL descriptionURL = SuperDemo.class.getResource(fileName);
        if (descriptionURL != null) {
            try {
                this.descriptionPane.setPage(descriptionURL);
            } 
            catch (IOException e) {
                System.err.println(
                    "Attempted to read a bad URL: " + descriptionURL
                );
            }
        } 
        else {
            System.err.println("Couldn't find file: " + fileName);
        }
         
    }
    
    /**
     * Receives notification of tree selection events and updates the demo 
     * display accordingly.
     * 
     * @param event  the event.
     */
    public void valueChanged(TreeSelectionEvent event) {
        TreePath path = event.getPath();
        Object obj = path.getLastPathComponent();
        if (obj != null) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) obj;
            Object userObj = n.getUserObject();
            if (userObj instanceof DemoDescription) {
                DemoDescription dd = (DemoDescription) userObj;
                SwingUtilities.invokeLater(new DisplayDemo(this, dd));
            }
            else {
                this.chartContainer.removeAll();
                this.chartContainer.add(createNoDemoSelectedPanel());
                this.displayPanel.validate();
                displayDescription("select.html");
            }
        }
        System.out.println(obj);
    }
    
    private JPanel createNoDemoSelectedPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("No demo selected"));
        panel.setPreferredSize(new Dimension(600, 400));
        return panel;
    }
    
    /**
     * Starting point for the JFreeChart Demo Collection.
     * 
     * @param args  ignored.
     */
    public static void main(String[] args) {
        SuperDemo demo = new SuperDemo("JFreeChart Demo Collection");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
    
    static class DisplayDemo implements Runnable {
        
        private SuperDemo app;
        
        private DemoDescription demoDescription;
        
        /**
         * Creates a new runnable.
         * 
         * @param app  the application.
         * @param d  the demo description.
         */
        public DisplayDemo(SuperDemo app, DemoDescription d) {
            this.app = app;
            this.demoDescription = d;    
        }
        
        /**
         * Runs the task.
         */
        public void run() {
            try {
                Class c = Class.forName(this.demoDescription.getClassName());
                Method m = c.getDeclaredMethod("createDemoPanel", null);
                JPanel panel = (JPanel) m.invoke(null, null);
                this.app.chartContainer.removeAll();
                this.app.chartContainer.add(panel);
                this.app.displayPanel.validate();
                String className = c.getName();
                String fileName = className;
                int i = className.lastIndexOf('.');
                if (i > 0) {
                    fileName = className.substring(i + 1);
                }
                fileName = fileName + ".html";
                this.app.displayDescription(fileName);
                
            }
            catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }
            catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
            catch (IllegalAccessException e4) {
                e4.printStackTrace();
            }
               
        }
        
    }
}
