/*
 * Created on 2005-2-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package src;


//import java.util.ArrayList;
//import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.JApplet;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HtmlApplet extends JApplet
{

    private static final String CLASS_NAME = "HtmlApplet";
    
    // the JSObject attribute
    protected static final String JSOBJECT_ATTRIBUTE_TYPE = "type";
    protected static final String JSOBJECT_ATTRIBUTE_VALUE = "value";
    protected static final String JSOBJECT_ATTRIBUTE_ID = "id";
    protected static final String JSOBJECT_ATTRIBUTE_NAME = "name";
    protected static final String JSOBJECT_ATTRIBUTE_CHECKED = "checked";
    
    // window.document.form.textbox
    // window.eval(...)
    // window.call(...)
    protected JSObject window = null;
    
    public void init()
    {
        window = JSObject.getWindow( this );
    }
    
    /**
     * Initial the Names of JSObject array with the specified Name;
     * @param fullJSObjectName the specified name act on JSObject array
     * @param numOfArray the number of the JSObject array
     * @return JSObject array names
     */
    public static String[] initialJSObjectArray( String fullJSObjectName, int numOfArray )
    {
        String[] array = new String[numOfArray];
        for ( int i = 0; i < numOfArray; i++ )
        {
            array[i] = fullJSObjectName + i;
        }
        return array;
    }
    /**
     * Get the Javascript Object
     * @param fullJSObjectName eg. "document.all.tbName"(if there is no form) or "document.formName.tbName"
     * the function does not support JSObject which is "one NAME - muti-VALUES" now, so it also doesn't support
     * "radio" JSObject
     * @return JSObject if the object exist, or null
     */
    public JSObject getJSObject( String fullJSObjectName )
    {
        String subJSObjectName = null;
        JSObject object = window;
        
        try
        {
            StringTokenizer st = new StringTokenizer( fullJSObjectName, "." );
        
            while ( st.hasMoreTokens() )
            {
                subJSObjectName = st.nextToken();
                object = (JSObject) object.getMember( subJSObjectName );
            }
        }
        catch ( JSException jse )
        {
            // fullJSObjectName does not exist.
            return null;
        }
        
        return object;
    }
    
    /**
     * Get the value of the JSObject's attribute specified
     * @param fullJSObjectName eg. "document.all.tbName"(if there is no form) or "document.formName.tbName"
     * the function does not support JSObject which is "one NAME - muti-VALUES" now, so it also doesn't support
     * "radio" JSObject
     * @param attribute the attribute of the JSObject such as "value", "type", "id", "name", "checked", etc.
     * but you should have a try and see whether other attributes defined in HTML works.
     * @return null if the attribute is an unknown name or fullJSObjectName is not right. The String "" if the 
     * attribute is not set with any value.
     */
    public String getJSObjectAttribute( String fullJSObjectName, String attribute )
    {
        JSObject object = getJSObject( fullJSObjectName );
        
        if ( object != null )
        {
            Object subObject = null;
            try
            {
                subObject = object.getMember( attribute );
                return subObject.toString();
            }
            catch ( JSException jse )
            {
                // fullJSObjectName is a "one NAME - muti-VALUES" JSObject or
                // the attribute is not supported
                return null;
            }
            catch ( NullPointerException npe )
            {
                // attribute is not set with any value
                return "";
            }
        }
        else
        {
            // fullJSObjectName doesn't exist
            return null;
        }
    }
    
    /**
     * Get the values of the JSObjectArray's attribute specified
     * @see HtmlApplet.getJSObjectAttribute( String, String )
     */
    public String[] getJSObjectAttributes( String[] fullJSObjectNames, String attribute )
    {
        int numOfArray = fullJSObjectNames.length;
        String[] values = new String[numOfArray];
        for ( int i = 0; i < numOfArray; i++ )
        {
            values[i] = getJSObjectAttribute( fullJSObjectNames[i], attribute );
        }
        return values;
    }
    
    /**
     * Get the value of the JSObject specified
     * @param fullJSObjectName eg. "document.all.tbName"(if there is no form) or "document.formName.tbName"
     * the function does not support JSObject which is "one NAME - muti-VALUES" now, so it also doesn't support
     * "radio" JSObject
     * @return the value of the JSObject. Or return null if the JSObject doesn't exist or which is 
     * "one NAME - muti-VALUES"
     */
    public String getJSObjectValue( String fullJSObjectName )
    {
        String value = getJSObjectAttribute( fullJSObjectName, JSOBJECT_ATTRIBUTE_VALUE );
        
        if ( value != null )
        {
            return value;
        }
        else
        {
            return null;
        }
    }
    
    public String[] getJSObjectCheckeds( String[] fullJSObjectNames )
    {
        return getJSObjectAttributes( fullJSObjectNames, JSOBJECT_ATTRIBUTE_CHECKED );
    }
    
    public String getJSObjectChecked( String fullJSObjectName )
    {
        String value = getJSObjectAttribute( fullJSObjectName, JSOBJECT_ATTRIBUTE_CHECKED );
        
        if ( value != null )
        {
            return value;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Get the values of the JSObjectArray specified
     * @see HtmlApplet.getJSObjectValue( String )
     */
    public String[] getJSObjectValues( String[] fullJSObjectNames )
    {
        return getJSObjectAttributes( fullJSObjectNames, JSOBJECT_ATTRIBUTE_VALUE );
    }
    
    
// thinking the more at "set" value from applet to html
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Set the value of the JSObject specified
     * @param fullJSObjectName eg. "document.all.tbName"(if there is no form) or "document.formName.tbName"
     * the function does not support JSObject which is "one NAME - muti-VALUES" now, so it also doesn't support
     * "radio" JSObject
     * @param value the value you want to set to JSObject
     */
    public void setJSObjectValue( String fullJSObjectName, String value )
    {
        if ( getJSObject( fullJSObjectName ) != null && value != null )
        {
            window.eval( fullJSObjectName + ".value=" + "\"" + value + "\"" );
        }
    }
}
