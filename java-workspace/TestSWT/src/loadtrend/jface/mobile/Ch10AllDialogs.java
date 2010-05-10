/*
 * Created on 2005-8-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package loadtrend.jface.mobile;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Zhang Jiawei
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Ch10AllDialogs extends Composite
{
    private Shell shell = null;
    private Text text = null;
    
    public Ch10AllDialogs( Composite parent )
    {
        super( parent, SWT.NONE );
        this.setLayout( new FillLayout( SWT.VERTICAL ) );
        shell = parent.getShell();
        text = new Text( this, SWT.H_SCROLL | SWT.V_SCROLL );
        this.buildDialogs();
    }
    
    private void buildDialogs()
    {
        Button btColor = new Button( this, SWT.PUSH );
        btColor.setText( "ColorDialog" );
        btColor.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                ColorDialog cd = new ColorDialog( shell );
                cd.setRGB( new RGB( 255, 255, 0 ) );
                Object object = cd.open();
                if ( object != null )
                {
                    text.setText( object.toString() );
                }
                else
                {
                    text.setText( "Nothing Selected." );
                }
            }
        });
        
        Button btDirectory = new Button( this, SWT.PUSH );
        btDirectory.setText( "DirectoryDialog" );
        btDirectory.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                DirectoryDialog dd = new DirectoryDialog( shell );
                dd.setFilterPath( "C:\\" );
                dd.setMessage( "Gege is choosing directory..." );
                Object object = dd.open();
                if ( object != null )
                {
                    text.setText( object.toString() );
                }
                else
                {
                    text.setText( "Nothing Selected." );
                }
            }
        });
        
        Button btFile = new Button( this, SWT.PUSH );
        btFile.setText( "FileDialog" );
        btFile.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                FileDialog fd = new FileDialog( shell, SWT.OPEN | SWT.MULTI );
                fd.setFilterPath( "F:\\" );
                fd.open();
                String[] fileNames = fd.getFileNames();
                if ( fileNames != null )
                {
                    String string = "";
                    for ( int i = 0; i < fileNames.length; i++ )
                    {
                        string = string + fileNames[i] + "\r\n";
                    }
                    text.setText( string );
                }
                else
                {
                    text.setText( "Nothing Selected." );
                }
            }
        });
        
        Button btMessageBox = new Button( this, SWT.PUSH );
        btMessageBox.setText( "MessageBox" );
        btMessageBox.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                MessageBox mb = new MessageBox( shell, SWT.OK | SWT.CANCEL );
                mb.setMessage( "Do you wish to continue?" );
                mb.setText( "I'm title." );
                int result = mb.open();
                
                switch ( result )
                {
                    case SWT.OK:
                        text.setText( result + ": OK selected." );
                        break;
                    case SWT.CANCEL:
                        text.setText( result + ": CANCEL selected." );
                        break;
                    default:
                        text.setText( result + ": Nothing Selected." );
                }
            }
        });
        
        
        Button btMessageDialog = new Button( this, SWT.PUSH );
        btMessageDialog.setText( "MessageDialog" );
        btMessageDialog.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                MessageDialog dialog =
                    new MessageDialog(
                    shell,
                    "Greeting Dialog", //the dialog title
                    null,
                    "Hello! How are you today?", //text to be displayed
                    MessageDialog.QUESTION, //dialog type
                    new String[] { "Good",
                    "Been better",
                    "Excited about SWT!" }, //button labels
                    0);
                    text.setText( "" + dialog.open() );
            }
        });        
        
        Button btInputDialog = new Button( this, SWT.PUSH );
        btInputDialog.setText( "InputDialog" );
        btInputDialog.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                IInputValidator validator = new IInputValidator() {
                    public String isValid(String text) { //return an error message,
                    if(text.length() < 5) //or null for no error
                    return "You must enter at least 5 characters";
                    else if(text.length() > 12)
                    return "You may not enter more than 12 characters";
                    else
                    return null;
                    }
                    };
                    InputDialog inputDialog = new InputDialog( shell,
                    "Please input a String", //dialog title
                    "Enter a String:", //dialog prompt
                    "default text", //default text
                    validator ); //validator to use
                    inputDialog.open();
            }
        }); 
            
        Button btErrorDialog = new Button( this, SWT.PUSH );
        btErrorDialog.setText( "ErrorDialog" );
        btErrorDialog.addSelectionListener( new SelectionAdapter()
        {
            public void widgetSelected( SelectionEvent e )
            {
                ErrorDialog errorDialog = new ErrorDialog( shell,
                        "Test Error Dialog",
                        "This is a test error dialog",
                        createStatus(),
                        IStatus.ERROR | IStatus.INFO );
                text.setText( "" + errorDialog.open() );
            }
        });
    }
    
    public IStatus createStatus()
    {
        final String dummyPlugin = "some plugin";
        IStatus[] statuses = new IStatus[2];
        statuses[0] = new Status( IStatus.ERROR, dummyPlugin, IStatus.OK,
                "Oh no! An error occurred!", new Exception() );
        statuses[1] = new Status( IStatus.INFO, dummyPlugin, IStatus.OK,
                "More errors!?!?", new Exception() );
        MultiStatus multiStatus = new MultiStatus( dummyPlugin, IStatus.OK,
                statuses, "Several errors have occurred.", new Exception() );
        return multiStatus;
    }
    
}
