/*
 * Created on 2005-7-18
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.swt.mobile;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MobileApplication
{
    private static int iheight = 0;
    
    private static int iY = 35;
    
    public static void main( String[] args )
    {
        Display display = new Display();
        // Shell shell = new Shell( display ); equals
        // Shell shell = new Shell( display, SWT.SHELL_TRIM ); equals
        // Shell shell = new Shell( display, SWT.TITLE|SWT.MIN|SWT.MAX|SWT.RESIZE|SWT.CLOSE );
        final Shell shell = new Shell( display, SWT.TOOL|SWT.CLOSE );
        
        final Rectangle bounds = Display.getDefault().getBounds();
        shell.setLocation(bounds.width - 220, bounds.height - iY);
        shell.setSize(200, iheight);
        final Timer timer = new Timer(true);
        timer.schedule(new TimerTask(){
            public void run() {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        shell.setLocation(bounds.width - 220, bounds.height - iY);
                        shell.setSize(200, iheight);
                        iheight = iheight + 10;
                        iY = iY + 10;
                        if (iheight == 150) timer.cancel();
                    }
                });
            }
        }, 0, 30);
        shell.setText("New SMS");
        setBackGroundWhite(shell);
        shell.setLayout(new FormLayout());
        
        // The first row: product name label
        FormData formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( 0, 0 );
        Label lbProductName = new Label( shell, SWT.LEFT );
        lbProductName.setLayoutData( formData );
        lbProductName.setText("13916939847");
        setBackGroundWhite( lbProductName );
        
        // The first row: product name label
        formData = new FormData();
        formData.left = new FormAttachment( 0, 0 );
        formData.right = new FormAttachment( 0, 350 );
        formData.top = new FormAttachment( lbProductName, 10 );
        Link lkAuthor = new Link( shell, SWT.NONE );
        lkAuthor.setLayoutData( formData );
        lkAuthor.setText("<A HREF=\"\">倪静莉是一个大猪头啊大猪头！</A>");
        lkAuthor.addSelectionListener( new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // e.text;
            }
        });
        setBackGroundWhite( lkAuthor );
        
        shell.open();
        
        while ( !shell.isDisposed() )
        {
            if ( !display.readAndDispatch() )
            {
                display.sleep();
            }
        }
        
        display.dispose();
    }
    
    private static void setBackGroundWhite( Control control )
    {
        control.setBackground( Display.getCurrent().getSystemColor( SWT.COLOR_WHITE ) );
    }
}
