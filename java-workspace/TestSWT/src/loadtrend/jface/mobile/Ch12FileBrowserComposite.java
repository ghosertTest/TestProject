/*
 * Created on 2005-8-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Jiawei_Zhang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch12FileBrowserComposite extends Composite
{
    private FileBrowser browser;

    public Ch12FileBrowserComposite( Composite parent )
    {
        super( parent, SWT.NONE );
        RowLayout layout = new RowLayout( SWT.HORIZONTAL );
        setLayout( layout );
        Button copyButton = new Button( this, SWT.PUSH );
        copyButton.setText( "Copy" );
        copyButton.addSelectionListener( new SelectionListener()
        {
            public void widgetSelected( SelectionEvent e )
            {
                Clipboard clipboard = new Clipboard( getDisplay() );
                FileTransfer transfer = FileTransfer.getInstance();
                clipboard.setContents( new Object[] { browser
                        .getSelectedFiles() }, new Transfer[] { transfer } );
                clipboard.dispose();
            }

            public void widgetDefaultSelected( SelectionEvent e )
            {
            }
        } );
        Button pasteButton = new Button( this, SWT.PUSH );
        pasteButton.setText( "Paste" );
        pasteButton.addSelectionListener( new SelectionListener()
        {
            public void widgetSelected( SelectionEvent e )
            {
                Clipboard clipboard = new Clipboard( getDisplay() );
                FileTransfer transfer = FileTransfer.getInstance();
                Object data = clipboard.getContents( transfer );
                if ( data != null )
                {
                    browser.copyFiles( (String[]) data );
                }
                clipboard.dispose();
            }

            public void widgetDefaultSelected( SelectionEvent e )
            {
            }
        } );
        browser = new FileBrowser( this );
        new FileBrowser( this );
    }
}
