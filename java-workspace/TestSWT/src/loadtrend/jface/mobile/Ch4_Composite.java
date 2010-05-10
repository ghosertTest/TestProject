/*
 * Created on 2005-7-27
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch4_Composite extends Ch4_MouseKey
{
    public Ch4_Composite( Composite parent )
    {
        super( parent );
        Button launch = new Button( this, SWT.PUSH );
        launch.setText( "Launch" );
        launch.setLocation( 40, 120 );
        launch.pack();

        launch.addMouseListener( new MouseAdapter()
        {
            public void mouseDown( MouseEvent e )
            {
                Ch4_Contributions sw = new Ch4_Contributions();
                sw.open();
            }
        } );
    }
}
