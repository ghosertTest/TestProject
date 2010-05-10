package wiley;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class LookupForm extends ActionForm
{
    private String symbol = null;

    public String getSymbol()
    {
        return ( symbol );
    }

    public void setSymbol( String symbol )
    {
        this.symbol = symbol;
    }

    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        this.symbol = null;
    }

    public ActionErrors validate( ActionMapping mapping,
            HttpServletRequest request )
    {
        ActionErrors errors = new ActionErrors();
        
        if ( this.symbol == null || this.symbol.equals( "" ) )
        {
            errors.add ( "symbol", new ActionError( "errors.lookup.symbol.required" ) );
        }
        
        return errors;
    }
}