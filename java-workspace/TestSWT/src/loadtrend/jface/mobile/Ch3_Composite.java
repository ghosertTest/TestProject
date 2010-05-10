/*
 * Created on 2005-7-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch3_Composite extends Composite
{
    public Ch3_Composite( Composite parent )
    {
        super( parent, SWT.NONE );
        Ch3_Group mg = new Ch3_Group( this );
        mg.setLocation( 0, 0 );
        mg.pack();
        
        Ch3_SashForm msf = new Ch3_SashForm( this );
        msf.setLocation( 125, 25 );
        msf.pack();
        
        pack();
    }
}
