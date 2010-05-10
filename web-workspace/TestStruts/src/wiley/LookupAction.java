package wiley;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class LookupAction extends Action
{
    protected Double getQuote( String symbol )
    {
        if ( symbol.equalsIgnoreCase( "SUNW" ) )
        {
            return new Double( 25.00 );
        }
        return null;
    }
        
    public ActionForward execute( ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response )
            throws IOException, ServletException
    {
        WileyActionMapping wileyActionMapping = (WileyActionMapping) mapping;

        Double price = null;

        // Default target to success
        String target = new String( "success" );
        String symbol = null;

        if ( form != null )
        {
            // Use the LookupForm to get the request parameters
            LookupForm lookupForm = (LookupForm) form;
            symbol = lookupForm.getSymbol();
            price = getQuote( symbol );
        }

        // Set the target to failure
        if ( price == null )
        {
            target = new String( "failure" );
            ActionErrors errors = new ActionErrors();
            errors.add( ActionErrors.GLOBAL_ERROR, new ActionError(
                    "errors.lookup.symbol.unknown", symbol ) );

            // Report any errors we have discovered
            if ( !errors.empty() )
            {
                super.saveErrors( request, errors );
            }
        }
        else
        {
            if ( wileyActionMapping.getLogResults() )
            {
                System.err.println( "SYMBOL:" + symbol + " PRICE:" + price );
            }
            request.setAttribute( "PRICE", price );
        }
        // Forward to the appropriate View
        return ( mapping.findForward( target ) );
    }
}