/*
 * Created on 2005-8-7
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch10CustomDialogComposite extends Composite
{
    public Ch10CustomDialogComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        buildControls();
    }

    protected void buildControls()
    {
        FillLayout layout = new FillLayout();
        setLayout( layout );
        Button dialogBtn = new Button( this, SWT.PUSH );
        dialogBtn.setText( "Password Dialog..." );
        dialogBtn.addSelectionListener( new SelectionListener()
        {
            public void widgetSelected( SelectionEvent e )
            {
                UsernamePasswordDialog dialog = new UsernamePasswordDialog(
                        getShell() );
                dialog.open();
            }

            public void widgetDefaultSelected( SelectionEvent e )
            {
            }
        } );
    }
}
