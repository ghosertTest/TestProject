/*
 * Created on 2005-2-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;

import java.awt.Container;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import netscape.javascript.JSObject;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewTestApplet extends JApplet
{
    // just for debug, show the value on the web
    private Container container = null;
    private JLabel jlabel = null;
    private String textAreaValue = null;
    
    /**
     * window.eval( String ) to excute javascript
     * window.call( "fn", Object[] ) to invoke javascript function fn()
     */
    private JSObject window = null;
    private static final String CLASS_NAME = "NewTestApplet";
    
    /**
     * the variable and their values are neccessary which come from javascript fnStartNewTestApplet(formName)
     */
    private String comm = null;
    private boolean blueTooth = false;
    private String num = null;
    private String array = null;
    private String selectText = null;
    
    // repaint applet for debug
    public void rePaintApplet( String textAreaValue )
    {
        this.textAreaValue = textAreaValue;
        jlabel.setText( this.textAreaValue );
        this.update( this.getGraphics() );
    }
    
    /**
     * Start applet  with initial parameters,
     * this function will always invoke by javascript fnStartNewTestApplet(formName)
     * @param comm
     * @param blueTooth
     * @param num
     * @param array
     * @param selectText
     */
    public void startWithParam( String comm, boolean blueTooth, String num, String array, String selectText )
    {
        this.comm = comm;
        this.blueTooth = blueTooth;
        this.num = num;
        this.array = array;
        this.selectText = selectText;
        
        perform();
    }
    
    public void perform()
    {
        rePaintApplet( this.selectText );
        
        String reverseComm = new StringBuffer(comm).reverse().toString();
        
        String[] result = new String[4];
        int total = 5;
        for ( int i = 0; i < total; i++ )
        {
            result[0] = reverseComm;
            result[1] = Boolean.valueOf(false).toString();
            result[2] = "Message Title" + i;
            result[3] = "Message Body" + i;
            window.call("fnRefreshFromNewTestApplet", result);
        }
        
        
    }
    
    public void init()
    {
        window = JSObject.getWindow( this );
        
        // just for debug
        container = this.getContentPane();
        jlabel = new JLabel( "", SwingConstants.CENTER );
        container.add( jlabel );
    }
    
    public void start()
    {
        
    }
    
    public void stop()
    {
        
    }
    
    public void destroy()
    {
        
    }
}


