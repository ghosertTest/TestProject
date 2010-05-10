/*
 * Created on 2005-8-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch12WebBrowserComposite extends Composite
{
    private Browser browser;

    public Ch12WebBrowserComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        GridLayout layout = new GridLayout( 2, true );
        setLayout( layout );
        browser = new Browser( this, SWT.NONE );
        GridData layoutData = new GridData( GridData.FILL_BOTH );
        layoutData.horizontalSpan = 2;
        layoutData.verticalSpan = 2;
        browser.setLayoutData( layoutData );
        browser.setUrl( "http://www.google.com" );
        final Text text = new Text( this, SWT.SINGLE );
        layoutData = new GridData( GridData.FILL_HORIZONTAL );
        text.setLayoutData( layoutData );
        Button openButton = new Button( this, SWT.PUSH );
        openButton.setText( "Open" );
        openButton.addSelectionListener( new SelectionListener()
        {
            public void widgetSelected( SelectionEvent e )
            {
                browser.setUrl( text.getText() );
            }

            public void widgetDefaultSelected( SelectionEvent e )
            {
            }
        } );
        Button backButton = new Button( this, SWT.PUSH );
        backButton.setText( "Back" );
        backButton.addSelectionListener( new SelectionListener()
        {
            public void widgetSelected( SelectionEvent e )
            {
                browser.back();
            }

            public void widgetDefaultSelected( SelectionEvent e )
            {
            }
        } );
        Button forwardButton = new Button( this, SWT.PUSH );
        forwardButton.setText( "Forward" );
        forwardButton.addSelectionListener( new SelectionListener()
        {
            public void widgetSelected( SelectionEvent e )
            {
                browser.forward();
            }

            public void widgetDefaultSelected( SelectionEvent e )
            {
            }
        } );
    }
}
