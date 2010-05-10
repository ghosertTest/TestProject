/*
 * Created on 2005-7-20
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MobileApplicaiton extends ApplicationWindow
{
    public MobileApplicaiton()
    {
        super( null );
    }
    
    // the appearance for the application
    protected Control createContents( Composite parent )
    {
        Text helloText = new Text( parent, SWT.CENTER );
        helloText.setText( "Jiawei Zhang!!!" );
        parent.pack();
        return parent;
    }
    
    // the operation for the application
    public static void main( String[] args )
    {
        MobileApplicaiton ma = new MobileApplicaiton();
        ma.setBlockOnOpen( true );
        ma.open();
        // excute below code after closing the application
        Display.getCurrent().dispose();
    }
}
