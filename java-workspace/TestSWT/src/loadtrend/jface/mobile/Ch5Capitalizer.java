/*
 * Created on 2005-7-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

/**
 * @author Jiawei_zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class Ch5Capitalizer extends Composite
{
    public Ch5Capitalizer( Composite parent )
    {
        super( parent, SWT.NONE );
        buildControls();
    }

    private void buildControls()
    {
        this.setLayout( new FillLayout() );
        Text text = new Text( this, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        text.addVerifyListener( new VerifyListener()
        {
            public void verifyText( VerifyEvent e )
            {
                if ( e.text.startsWith( "1" ) )
                {
                    e.doit = false;
                }
                else
                {
                    e.text = e.text.toUpperCase();
                }
            }
        } );
    }
}
