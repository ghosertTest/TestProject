/*
 * Created on 2005-5-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package wiley;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TranslationAction extends Action
{
    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws IOException, ServletException
    {
        String localeString = request.getParameter( "locale" );
        
        if ( localeString != null )
        {
            Locale locale = this.parseLocaleString( localeString ); 
            super.setLocale( request, locale );
        }

        // Forward to the appropriate View
        return ( mapping.findForward( "index" ) );
    }
    
    /**
     * parse locale string and create Locale class
     * @param localeString "zh_CN", "en_US" is standard acceptable
     * <p> paramaters, but you can ignore the case sensetive in using
     * <p> this method.
     * @return Locale null if failure to parse
     */
    protected Locale parseLocaleString( String localeString )
    {
        Locale locale = null;
        int index = localeString.indexOf( "_" );
        
        if ( index == -1 )
        {
            if ( localeString.length() == 2 )
            {
                locale = new Locale( localeString.toLowerCase() );
            }
        }
        else
        {  
            String language = localeString.substring( 0, index );
            String country = localeString.substring( index + 1 );
            
            if ( language.length() == 2 && country.length() == 2 )
            {
                locale = new Locale( language.toLowerCase(), country.toUpperCase() );
            }
        }
        return locale;
    }
}
