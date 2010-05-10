/*
 * Created on 2005-7-24
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch3_SashForm extends Composite
{
    public Ch3_SashForm( Composite parent )
    {
        super( parent, SWT.NONE );
        
        SashForm sf = new SashForm( this, SWT.VERTICAL );
        
        Button button1 = new Button( sf, SWT.ARROW | SWT.UP );
        button1.setSize( 120, 40 );
        Button button2 = new Button( sf, SWT.ARROW | SWT.DOWN );
        button2.setBounds( 0, 40, 120, 40 );
        
        sf.setSize( 120, 80 );
        
    }
}
