/*
 * Created on 2005-2-23
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;

import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.SwingConstants;



/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestApplet extends HtmlApplet
{
    // the JSObject names
    private static final String TBLABEL = "document.all.tbLabel";
    private static final int CBBOX_LIST_NUM = 2;
    private static final String[] CBBOX_LIST = initialJSObjectArray( "document.all.cbBox", CBBOX_LIST_NUM );
    
    // variables corresponding with JSObject names
    private String label = null;
    private String[] cbBoxList = new String[CBBOX_LIST_NUM];
    
    // others
    private String textAreaValue = null;
    private JLabel jlabel = null;
    
    public void rePaintApplet( String textAreaValue )
    {
        this.textAreaValue = textAreaValue;
        jlabel.setText( this.textAreaValue );
        this.update( this.getGraphics() );
    }
    
    public void writeValuesToApplet()
    {
//        label = getJSObjectValue( TBLABEL );
//        cbBoxList = getJSObjectValues( CBBOX_LIST );
        
//        label = getJSObjectAttribute( TBLABEL, JSOBJECT_ATTRIBUTE_TYPE );
//        cbBoxList = getJSObjectAttributes( CBBOX_LIST, super.JSOBJECT_ATTRIBUTE_CHECKED );
        cbBoxList = getJSObjectCheckeds( CBBOX_LIST );
        
        String cbBoxValues = "";
        for ( int i = 0; i < CBBOX_LIST_NUM; i++ )
        {
            cbBoxValues = cbBoxValues + cbBoxList[i];
        }
        rePaintApplet( cbBoxValues );
        
//        if ( label == null ) label = "null";
//        if ( label.equals("") ) label = "empty";
//        rePaintApplet( label );
    }
    
    public void readValuesFromApplet()
    {
        setJSObjectValue( TBLABEL, this.textAreaValue );
    }
    
    public void init()
    {
        super.init();
        Container container = this.getContentPane();
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
