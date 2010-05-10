/*
 * Created on 2005-7-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch5Contributions extends ApplicationWindow
{
    public Ch5Contributions()
    {
        super( null );
        addToolBar( SWT.FLAT | SWT.WRAP );
    }
    
    protected ToolBarManager createToolBarManager( int style )
    {
        ToolBarManager tbm = new ToolBarManager( style );
        tbm.add( new ControlContribution( "Custom" )
        {
            protected Control createControl( Composite parent )
            {
                SashForm sf = new SashForm( parent, SWT.NONE );
                Label lb = new Label( sf, SWT.SHADOW_IN );
                lb.setText( "tool bar!" );
                lb.pack();
                Button b1 = new Button( sf, SWT.PUSH );
                b1.setText( "Hello" );
                b1.pack();
                Button b2 = new Button( sf, SWT.PUSH );
                b2.setText( "World" );
                b2.pack();
                b2.addSelectionListener( new SelectionAdapter()
                {
                    public void widgetSelected( SelectionEvent e )
                    {
                        System.out.println( "Selected:" + e );
                    }
                } );
                return sf;
            }
        } );
        return tbm;
    }
    
    public static void main( String[] args )
    {
        Ch5Contributions win = new Ch5Contributions();
        win.setBlockOnOpen( true );
        win.open();
        Display.getCurrent().dispose();
    }
}
