/*
 * Created on 2005-7-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Slider;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch5Slider extends Composite
{
    private Label lb = null;
    private Slider sl = null;
    private ProgressBar bar = null;
    private int index = 0;
    private ProgressIndicator indicator = null;
    private ProgressIndicator indicator2 = null;
    
    
    public Ch5Slider( Composite parent )
    {
        super( parent, SWT.NONE );
        setLayout( new RowLayout() );
        sl = new Slider( this, SWT.HORIZONTAL );
        lb = new Label( this, SWT.SHADOW_IN );
        
        sl.setValues( 1000, 400, 1600, 200, 10, 100 );
        showSlider();
        
        sl.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                showSlider();
            }
        });
        
//      Style can be SMOOTH, HORIZONTAL, or VERTICAL
        bar = new ProgressBar( this, SWT.SMOOTH );
        bar.setMaximum( 100 );
        Button bt = new Button( this, SWT.PUSH );
        bt.setText( "Launch Progress Bar..." );
        bt.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                for ( index = 0; index < 10; index++ )
                {
                    // Take care to only update the display from its
                    // own thread
                    Display.getCurrent().asyncExec( new Runnable()
                    {
                        public void run()
                        {
                            // Update how much of the bar should be filled in
                            bar.setSelection( (int) ( bar.getMaximum() * ( index + 1 ) / 10 ) );
                        }
                    } );
                }
            }
        });
        
        
        indicator = new ProgressIndicator( this );
        indicator.beginTask( 10 );
        Button bt2 = new Button( this, SWT.PUSH );
        bt2.setText( "Launch Progress Bar2..." );
        bt2.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                Display.getCurrent().asyncExec( new Runnable()
                {
                    public void run()
                    {
                        indicator.worked( 1 );
                    }
                } );
            }
        } );
        
        indicator2 = new ProgressIndicator( this );
        indicator2.beginAnimatedTask();
        Button bt3 = new Button( this, SWT.PUSH );
        bt3.setText( "Launch Progress Bar3..." );
        bt3.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                indicator2.done();
            }
        } );
    }
    
    private void showSlider()
    {
        lb.setText( "minimum: " + sl.getMinimum() + " maximum: " + sl.getMaximum()
                + " thumb: " + sl.getThumb() + " increment: " + sl.getIncrement()
                + " pageincrement: " + sl.getPageIncrement() 
                + " selection: " + sl.getSelection() );
        lb.pack();
    }
}
