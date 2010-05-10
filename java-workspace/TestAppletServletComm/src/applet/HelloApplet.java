/*
 * Created on 2005-2-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package applet;

import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HelloApplet extends BaseApplet
{
    // just for debug, show the value on the web
    private String textAreaValue = "blank";
    
    private static final String CLASS_NAME = "HelloApplet";
    private static final String DISPATCH_SERVLET = "TestAppletServletComm/HelloServlet";
    
    /**
     * the variable and their values are neccessary which come from javascript fnStartNewTestApplet(formName)
     */
    private String comm = null;
    private boolean blueTooth = false;
    private String array = null;
    private String selectText = null;
    private String evalStatement = null;
    
    /**
     * Start applet  with initial parameters,
     * this function will always invoke by javascript fnStartNewTestApplet(formName)
     * @param comm
     * @param blueTooth
     * @param num
     * @param array
     * @param selectText
     * @param evalStatement the statement that should be excute by javascript to dispatch jsp to servlet
     */
    public void startWithParam( String comm, boolean blueTooth, String array, String selectText, String evalStatement )
    {
        this.comm = comm;
        this.blueTooth = blueTooth;
        this.array = array;
        this.selectText = selectText;
        this.evalStatement = evalStatement;
        
		//
		this.textAreaValue = "Applet running in 10s...";
		this.update( this.getGraphics() );
		
		super.threadSign = true;
    }
    
    /**
     * perform() invoked by run() multi-thread
     *
     */
    public void perform()
    {
        try
        {
            URL servlet = new URL( super.servletBase, DISPATCH_SERVLET);

            Thread.sleep(10000);
  		    Serializable objs[] = { comm, String.valueOf( blueTooth ), array, selectText };
		    ObjectInputStream in = ServletWriter.postObjects( servlet, objs );
		    this.textAreaValue = (String) in.readObject();
			in.close();
			
			this.update( this.getGraphics() );

		    super.threadSign = false;
		    
		    if ( evalStatement != null && evalStatement.equals("") == false )
		    {
		        super.window.eval( evalStatement );
		    }
        }
        catch ( MalformedURLException urle )
        {
            urle.printStackTrace();
        }
        catch ( IOException ioe )
        {
            ioe.printStackTrace();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Do something on interface
     */
    public void paint(Graphics g) {
        g.clearRect( 0, 0, this.getWidth(), this.getHeight());
	    g.drawString(this.textAreaValue, 25, 50);
    } 
    
//    public void init()
//    {
//        super.init();
//    }
//    
//    public void start()
//    {
//        super.start();
//    }
//    
//    public void stop()
//    {
//        super.stop();
//    }
}


