/*
 * Created on 2005-7-25
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch4_StatusAction extends Action
{

    StatusLineManager statman;
    short triggercount = 0;

    public Ch4_StatusAction( StatusLineManager sm )
    {
        super( "&Trigger@Ctrl+T", AS_PUSH_BUTTON );
        statman = sm;
        setToolTipText( "Trigger the Action" );
        setImageDescriptor( ImageDescriptor.createFromFile( this.getClass(),
                "eclipse.gif" ) );
    }

    public void run()
    {
        triggercount++;
        statman.setMessage( "The status action has fired. Count: "
        + triggercount );
    }
    
}


