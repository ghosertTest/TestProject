/*
 * Created on 2005-6-1
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package wiley;

import org.apache.struts.action.ActionMapping;

// Step 1. Extend the ActionMapping class
public class WileyActionMapping extends ActionMapping
{
    // Step 2. Add the new properties
    protected boolean logResults = false;

    public WileyActionMapping()
    {
        // Step 3. Call the ActionMapping's default Constructor
        super();
    }

    // Step 4. Add matching setter/getter methods
    public void setLogResults( boolean logResults )
    {
        this.logResults = logResults;
    }

    public boolean getLogResults()
    {
        return logResults;
    }
}