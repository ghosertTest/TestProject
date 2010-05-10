/*
 * Created on 2005-3-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package applet;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class BaseApplet extends JApplet implements Runnable
{
    private static final String CLASS_NAME = "BaseApplet";
    
    protected JSObject window = null;
    
    protected Thread subThread = null;
    
    protected boolean threadSign = false;
    
    protected URL servletBase = null;
    
    /**
     *  Get variable "window" and "servletBase"
     */
    public void init()
    { 
        try
        {
            window = JSObject.getWindow( this );
        }
        catch ( JSException jse )
        {
            jse.printStackTrace();
        }
        
        
	    URL codebase = this.getCodeBase();

	    // Get the host, protcol and port
	    String host = codebase.getHost();
	    String protocol = codebase.getProtocol();
        int port = codebase.getPort();
	    if ( port == -1 ) port = 80;
	
	    // Build the URL for the servlet web server
	    try
	    {
	        servletBase = new URL(protocol + "://" + host + ":" + port);
	    }
	    catch ( MalformedURLException urle )
	    {
	        urle.printStackTrace();
	    }
    } 
    
    public void start()
    {
        threadSign = false;
        if ( subThread == null )
        {
            subThread = new Thread( this );
            subThread.start();
        }
    }
    
    public void stop()
    {
        threadSign = false;
        if ( subThread != null )
        {
            subThread.stop();
        }
    }
    
    /**
     * To process muti-thread, for communicating with mobile is a slow time processing
     */
    public void run()
    {
        try
        {
            while ( true )
            {
                Thread.sleep( 100 );
                
                if ( threadSign )
                {
                    perform();
                }
            }
        }
        catch ( InterruptedException ie )
        {
            ie.printStackTrace();
        }
    }
    
    public abstract void perform();

}
